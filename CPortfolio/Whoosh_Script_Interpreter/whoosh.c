/* This is the main file for the `whoosh` interpreter and the part
   that you modify. */

/*
 * Andrew Katsanevas
 * 3/27/2017
 * CS 4400
 * Assignment 5
 */

#include <stdlib.h>
#include <stdio.h>
#include "csapp.h"
#include "ast.h"
#include "fail.h"

static void run_script(script *scr);
static void run_group(script_group *group);
static void run_single(script_group *group);
static void run_and(script_group *group);
static void run_or(script_group *group);
static void run_command(script_command *command);
static void set_var(script_var *var, int new_value);


volatile int suspendedCount = 0;
static volatile int ctrl_c = 0;
sigset_t sigs;
int status;

// Handle control-c by setting a global variable 
void ctrl_c_handler(int sig){ ctrl_c = 1; }

// Unignore child signal
void sigchld_handler(int sig){}

/* You probably shouldn't change main at all. */
int main(int argc, char **argv) 
{
  script *scr;
  
  if ((argc != 1) && (argc != 2)) 
  {
    fprintf(stderr, "usage: %s [<script-file>]\n", argv[0]);
    exit(1);
  }

  scr = parse_script_file((argc > 1) ? argv[1] : NULL);

  run_script(scr);

  return 0;
}

/*
 * Run groups and handle background tasks
 */
static void run_script(script *scr) 
{
  // Run the groups
  int i;
  for (i = 0; i < scr->num_groups; ++i)
  {
    run_group(&scr->groups[i]);
  }

  Sigemptyset(&sigs);

  // Background tasks
  while(suspendedCount != 0)
  {
    Sigsuspend(&sigs);

    if (ctrl_c)
    {
      ctrl_c = 0;
      break;
    }

    int wait = wait = waitpid(-1, &status, WUNTRACED | WNOHANG);
    while(wait != 0)
    {
      wait = waitpid(-1, &status, WUNTRACED | WNOHANG);
      if(wait == -1)
      {
        return;
      }
      suspendedCount--;
    }
  }
}

/*
 * Run a group of tasks
 */
static void run_group(script_group *group) 
{
  Signal(SIGINT, ctrl_c_handler);
  Signal(SIGCHLD, sigchld_handler);

  // Repeat
  int r;
  for(r = 0; r < group->repeats; r++)
  {
    // Single mode
    if(group->mode == GROUP_SINGLE)
    {
      run_single(group);
    }
    // And mode
    else if(group->mode == GROUP_AND)
    {
      run_and(group);
    }
    // Or mode
    else if(group->mode == GROUP_OR)
    {
      run_or(group);
    }
  }

  // Handle @
  if(group->result_to != NULL)
  {
    if(WIFEXITED(status))
    {
      set_var(group->result_to, WEXITSTATUS(status));
    }
    else
    {
      set_var(group->result_to, -WTERMSIG(status));
    }
  }
}

/*
 * Single mode case
 */
static void run_single(script_group *group)
{
  int pid = 0;

  Sigemptyset(&sigs);
  Sigaddset(&sigs, SIGINT);
  Sigprocmask(SIG_BLOCK, &sigs, NULL);

  // Create a fork for the process
  pid = fork();
  if (pid == 0)
  {
    setpgid(0, getpid());
    run_command(&group->commands[0]);
  }

  Sigemptyset(&sigs);
  Sigsuspend(&sigs);

  // Set variable
  if (group->commands->pid_to != NULL)
  {
    set_var(group->commands->pid_to, pid);
  }

  // Kill the process on ctrl-c
  if (ctrl_c == 1)
  {
    kill(-pid, SIGTERM);
    ctrl_c = 0;
  }

  // Wait for process to complete
  wait(&status);
}


/*
 * And mode case
 */
static void run_and(script_group *group)
{
  Sigemptyset(&sigs);
  Sigaddset(&sigs, SIGINT);
  Sigprocmask(SIG_BLOCK, &sigs, NULL);

  // Set up pipelines
  int fd[2];
  int fd2[2];
  fd2[0] = -1;

  int pid = 0;

  int i;
  for (i = 0; i < group->num_commands; i++)
  {
    // Pipe all commands other than the last
    if (i < group->num_commands - 1)
    {
      pipe(fd);
      fd2[1] = fd[1];
    }
    // Last command
    else
    {
      fd2[1] = -1;
    }

    // First command
    if (i == 0)
    {
      // Pipe
      pid = fork();
      if(pid == 0)
      {
        setpgid(0, getpid());

        if (fd2[0] != 0 && fd2[0] != -1)
        {
          dup2(fd2[0], 0);
          close(fd2[0]);
        }

        if(fd2[0] != 1 && fd2[1] != -1)
        {
          dup2(fd2[1], 1);
          close(fd2[1]);
        }

        run_command(&group->commands[i]);
      }
    }
    // All but the first command
    else
    {
      // Pipe
      if (fork() == 0)
      {
        setpgid(0, pid);
        if (fd2[0] != 0 && fd2[0] != -1)
        {
          dup2(fd2[0], 0);
          close(fd2[0]);
        }

        if (fd2[0] != 1 && fd2[1] != -1)
        {
          dup2(fd2[1], 1);
          close(fd2[1]);
        }
        run_command(&group->commands[i]);
      }
    }

    close(fd2[0]);
    close(fd2[1]);

    fd2[0] = fd[0];

    // Set variable
    if (group->commands->pid_to != NULL)
    {
      set_var(group->commands->pid_to, pid);
    }
  }

  Sigemptyset(&sigs);

  // Wait for child processes to end
  int done = 0;
  while(done != group->num_commands)
  {
    Sigsuspend(&sigs);

    // Kill the process on ctrl-c
    if (ctrl_c)
    {
      ctrl_c = 0;
      kill(-pid, SIGTERM);
      return;
    }

    // Suspend a process
    int wait = 0;
    while((wait=waitpid(-1, &status, WNOHANG | WUNTRACED)) != 0)
    {
      if(wait == -1)
      {
        return;
      }
      if(WIFSTOPPED(status))
      {
        suspendedCount++;
      }
      done++;
    }

    if(wait == -1)
    {
      return;
    }
  }
}


/*
 * Or mode case
 */
static void run_or(script_group *group)
{
  Sigemptyset(&sigs);
  Sigaddset(&sigs, SIGINT);
  Sigprocmask(SIG_BLOCK, &sigs, NULL);

  int pid = 0;

  int i;
  for(i = 0; i < group->num_commands; i++)
  {
    // First command
    if(i == 0)
    {
      pid = fork();
      if(pid == 0)
      {
        setpgid(0, getpid());
        run_command(&group->commands[i]);
      }
    }
    // Not first command
    else
    {
      if(fork() == 0)
      {
        setpgid(0, pid);
        run_command(&group->commands[i]);
      }
    }
  }

  Sigemptyset(&sigs);
  Sigsuspend(&sigs);

  // Set variable
  if(group->commands->pid_to != NULL)
  {
    set_var(group->commands->pid_to, pid);
  }

  // Kill on ctrl-c
  if(ctrl_c)
  {
    ctrl_c = 0;
    kill(-pid, SIGTERM);
  }

  // Wait for the first child
  wait(&status);

  // Terminate children
  kill(-pid, SIGTERM);
}

/* This run_command function is a good start, but note that it runs
   the command as a replacement for the `whoosh` script, instead of
   creating a new process. */
static void run_command(script_command *command) 
{
  const char **argv;
  int i;

  argv = malloc(sizeof(char *) * (command->num_arguments + 2));
  argv[0] = command->program;
  
  for (i = 0; i < command->num_arguments; i++) 
  {
    if (command->arguments[i].kind == ARGUMENT_LITERAL)
      argv[i+1] = command->arguments[i].u.literal;
    else
      argv[i+1] = command->arguments[i].u.var->value;
  }
  
  argv[command->num_arguments + 1] = NULL;

  Execve(argv[0], (char * const *)argv, environ);

  free(argv);
}

/* You'll likely want to use this set_var function for converting a
   numeric value to a string and installing it as a variable's
   value: */
static void set_var(script_var *var, int new_value) 
{
  char buffer[32];
  free((void *)var->value);
  snprintf(buffer, sizeof(buffer), "%d", new_value);
  var->value = strdup(buffer);
}
