/*
 * Andrew Katsanevas
 * CS4400
 * Assignment 4
 * 3/3/2017
 * NOTE: If anything goes wrong with a completion level, it can be changed to a lower level before the call.
 */

#include <stdlib.h>
#include <stdio.h>
#include <sys/mman.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>
#include <elf.h>

/* Given the in-memory ELF header pointer as `ehdr` and a section
   header pointer as `shdr`, returns a pointer to the memory that
   contains the in-memory content of the section */
#define AT_SEC(ehdr, shdr) ((void *)(ehdr) + (shdr)->sh_offset)

void print_section_names(Elf64_Ehdr* ehdr);
static void check_for_shared_object(Elf64_Ehdr *ehdr);
static void fail(char *reason, int err_code);
void iteratePrint(Elf64_Ehdr* ehdr, int completionLevel);
Elf64_Shdr* section_by_name(Elf64_Ehdr *ehdr, char* name);
void getVariable(char* j, Elf64_Shdr *text_shdr, Elf64_Sym *syms, char* offsetAddress, Elf64_Rela *rela, char* strs, int i, Elf64_Shdr *rela_dyn, int index, long shift, char* jumpOffset);

int main(int argc, char **argv)
{
  int fd;
  size_t len;
  void *p;
  Elf64_Ehdr *ehdr;

  if (argc != 2)
    fail("expected one file on the command line", 0);

  /* Open the shared-library file */
  fd = open(argv[1], O_RDONLY);
  if (fd == -1)
    fail("could not open file", errno);

  /* Find out how big the file is: */
  len = lseek(fd, 0, SEEK_END);

  /* Map the whole file into memory: */
  p = mmap(NULL, len, PROT_READ, MAP_PRIVATE, fd, 0);
  if (p == (void*)-1)
    fail("mmap failed", errno);

  /* Since the ELF file starts with an ELF header, the in-memory image
     can be cast to a `Elf64_Ehdr *` to inspect it: */
  ehdr = (Elf64_Ehdr *)p;

  /* Check that we have the right kind of file: */
  check_for_shared_object(ehdr);

  
  /* Choose which completion level to use (1, 2, or 3) */
  int completionLevel = 3;
  /* My call */
  iteratePrint(ehdr, completionLevel);

  return 0;
}

// Iterate through the Elf file to find functions and variables
void iteratePrint(Elf64_Ehdr* ehdr, int completionLevel) 
{
  Elf64_Shdr *rela_plt = section_by_name(ehdr, ".rela.plt");
  Elf64_Rela *plt = AT_SEC(ehdr, rela_plt);

  Elf64_Shdr *rela_dyn = section_by_name(ehdr, ".rela.dyn");
  Elf64_Rela *rela = AT_SEC(ehdr, rela_dyn);

  Elf64_Shdr *text_shdr = section_by_name(ehdr, ".text");
  //char *text = AT_SEC(ehdr, text_shdr);

  Elf64_Shdr *dynsym_shdr = section_by_name(ehdr, ".dynsym");
  Elf64_Sym *syms = AT_SEC(ehdr, dynsym_shdr);

  char *strs = AT_SEC(ehdr, section_by_name(ehdr, ".dynstr"));

  int i, count = dynsym_shdr->sh_size / sizeof(Elf64_Sym);
  for (i = 0; i < count; i++) 
  {
    // Check if it's a function
    if (ELF64_ST_TYPE(syms[i].st_info) == STT_FUNC) 
    {
      // Check that it isn't a function with size 0
      if (syms[i].st_size != 0) 
      {
        // Print the function name
        printf("%s\n", strs + syms[i].st_name);
        Elf64_Addr funcAddress = syms[i].st_value;
        char* machineCode = AT_SEC(ehdr, text_shdr);
        char* offsetAddress = machineCode + (funcAddress - text_shdr->sh_addr);
        char* j= offsetAddress;

        // Start iterating through the function
        while(1)
        {
          //printf("j: %p\n", j);

          // return case
          if((0xff & *j) == 0xc3)
          {
            //printf("RETURNED\n");
            break;
          }
          // jump case
          else if((0xff & *j) == 0xe9)
          {
            // Only do it on level 3
            if(completionLevel == 1 || completionLevel == 2)
            {
              break;
            }

            j += 5;

            // Get the bytes of the instruction
            long bytes[4];
            bytes[0] = (0xff & (*(j-1)));
            bytes[1] = (0xff & (*(j-2)));
            bytes[2] = (0xff & (*(j-3)));
            bytes[3] = (0xff & (*(j-4)));
            
            int first = bytes[0] << 24;
            int second = bytes[1] << 16;
            int third = bytes[2] << 8;
            int fourth = bytes[3];

            int total = first + second + third + fourth;

            

            char* funcTextAddress = (char*)text_shdr->sh_offset + syms[i].st_value - text_shdr->sh_addr;
            int instOffset = j - offsetAddress;
            char* instAddress = funcTextAddress + instOffset;
            //char* theAddress = j + total;
            char* jumpOffset = total + instAddress;
            char* jumpAddress = j + total;

            //printf("j: %x\n", j);
            //printf("here total: %x\n", total);
            //printf("joff: %x\n", jumpOffset);
            //printf("address: %p\n", jumpAddress);
            //printf("thing: %x\n", 0xff & *jumpAddress);

            bytes[0] = (0xff & (*(jumpAddress+5)));
            bytes[1] = (0xff & (*(jumpAddress+4)));
            bytes[2] = (0xff & (*(jumpAddress+3)));
            bytes[3] = (0xff & (*(jumpAddress+2)));

            //printf("%x, %x, %x, %x\n", bytes[0], bytes[1], bytes[2], bytes[3]);

            first = bytes[0] << 24;
            second = bytes[1] << 16;
            third = bytes[2] << 8;
            fourth = bytes[3];

            total = first + second + third + fourth;

            char* pltTarget = total+jumpOffset+6;

            //printf("target: %x\n", pltTarget);
            //printf("total: %x\n", total);
            //printf("j: %x\n", j);

            //printf("checkit: %x\n", text_shdr->sh_addr);


            int relaSize = rela_plt->sh_size / sizeof(Elf64_Rela);

            int off;
            for(off=0; off<relaSize; off++)
            {
              long nextOff = plt[off].r_offset;

              if((char*)nextOff == pltTarget)
              {
                // Print the called function's name
                printf("  (%s)\n", strs + syms[ELF64_R_SYM(plt[off].r_info)].st_name);

                // Get the variables of the called function
                if(completionLevel == 3)
                {
                  getVariable(jumpAddress+7, text_shdr, syms, jumpAddress, rela, strs, i, rela_dyn, ELF64_R_SYM(plt[off].r_info), total, 0);
                }

              }

              //printf("offset: %x\n", nextOff);
            }
            break;
          }
          // jump with signed char displacement
          // replace this conditional and the one above with a break for only part 2 completion
          else if((0xff & *j) == 0xeb)
          {
            // Only do if level 3
            if(completionLevel == 1 || completionLevel == 2)
            {
              break;
            }

            j += 2;

            signed long total = 0x0 + (*(j-1));

            
            char* funcTextAddress = (char*)text_shdr->sh_offset + syms[i].st_value - text_shdr->sh_addr;
            int instOffset = j - offsetAddress;
            char* instAddress = funcTextAddress + instOffset;
            char* jumpOffset = instAddress + total;
            char* jumpAddress = j + total;

            // Lots of good old prints
            //printf("funcTextAddress: %x\n", funcTextAddress);
            //printf("offsetAddress: %x\n", offsetAddress);
            //printf("instOffset: %x\n", instOffset);
            //printf("instAddress: %x\n", instAddress);
            //printf("jump j: %x\n", j);
            //printf("at j: %x\n", *j);
            //printf("jump total: %d\n", total);
            //printf("jump offset: %x\n", jumpOffset);
            //printf("jump address: %x\n", jumpAddress);
            //printf("at jump address: %x %x %x %x\n", *jumpAddress, *(jumpAddress+1), *(jumpAddress+2), *(jumpAddress+3));
            

            // If level 3, get the variables from the call
            if(completionLevel == 3)
            {
              getVariable(jumpAddress+7, text_shdr, syms, jumpAddress, rela, strs, i, rela_dyn, -1, total, jumpOffset);
            }

            break;
          }
          // mov case
          // 7, 3, 7, 4 are the possible sizes
          else if((0xff & *j) == 0x48)
          {
            // Look here for variables
            // 7 size
            if((0xff & (*(j+1))) == 0x8b)
            {
              j += 7;

              // Get the variables if level 2 or 3
              if(completionLevel == 2 || completionLevel == 3)
              {
                getVariable(j, text_shdr, syms, offsetAddress, rela, strs, i, rela_dyn, i, 0, 0);
              }
            }
            // 3, 7, 4 size
            else if((0xff & (*(j+1))) == 0x63 || (0xff & (*(j+1))) == 0x89 || (0xff & (*(j+1))) == 0x8b)
            {
              // top two bits 0, bottom three 5
              if((0xff & (*(j+2))) <= 0x3f && (0xff & ((*(j+2)))<<5) == 0xA0)
              {
                j += 7;
              }
              // top two bits 0, bottom three 4
              else if((0xff & (*(j+2))) <= 0x3f && (0xff & ((*(j+2)))<<5) == 0x80)
              {
                j += 4;
              }
              // other cases are size 3
              else
              {
                j += 3;
              }
            }
          }
          // more mov
          // 2, 6, 3, 2 are the possible sizes
          else if((0xff & *j) == 0x63 || (0xff & *j) == 0x89 || (0xff & *j) == 0x8b)
          {
              // top two bits 0, bottom three 5
              if((0xff & (*(j+1))) <= 0x3f && (0xff & ((*(j+1)))<<5) == 0xA0)
              {
                j += 6;
              }
              // top two bits 0, bottom three 4
              else if((0xff & (*(j+1))) <= 0x3f && (0xff & (((*(j+1))))<<5) == 0x80)
              {
                j += 3;
              }
              else
              {
                j += 2;
              }
          }
          else
          {
            //printf("oops\n");
          }
        }
      }
    }
  }
}

// For comparing name strings in section_by_name
int nameComp(char* firstString, char* secondString) 
{
    while (*firstString != '\0') 
    {
        if (*secondString == '\0') 
          return  1;
        if (*secondString > *firstString)   
          return -1;
        if (*firstString > *secondString)   
          return  1;

        firstString++;
        secondString++;
    }

    if (*secondString != '\0') 
    {
      return -1;
    }

    return 0;
}

// Find the address of a specified Elf section
Elf64_Shdr* section_by_name(Elf64_Ehdr *ehdr, char* name)
{
  int i;

  unsigned int sh_offset = ehdr->e_shoff;

  Elf64_Shdr* shdrs = (void*)ehdr + sh_offset;

  unsigned long shstrtab_offset = shdrs[ehdr->e_shstrndx].sh_offset;

  char* strings = (void*)ehdr + shstrtab_offset;

  for (i = 0; i < ehdr->e_shnum; i++) 
  {
    if (nameComp(strings + shdrs[i].sh_name, name) == 0)
    {
      break;
    }
  }

  return &shdrs[i];
}



// Just do a little bit of error-checking.
// Make sure we're dealing with an ELF file.
static void check_for_shared_object(Elf64_Ehdr *ehdr)
{
  if ((ehdr->e_ident[EI_MAG0] != ELFMAG0)
      || (ehdr->e_ident[EI_MAG1] != ELFMAG1)
      || (ehdr->e_ident[EI_MAG2] != ELFMAG2)
      || (ehdr->e_ident[EI_MAG3] != ELFMAG3))
    fail("not an ELF file", 0);

  if (ehdr->e_ident[EI_CLASS] != ELFCLASS64)
    fail("not a 64-bit ELF file", 0);

  if (ehdr->e_type != ET_DYN)
    fail("not a shared-object file", 0);
}

static void fail(char *reason, int err_code)
{
  fprintf(stderr, "%s (%d)\n", reason, err_code);
  exit(1);
}

// Print all the variables of a function
void getVariable(char* j, Elf64_Shdr *text_shdr, Elf64_Sym *syms, char* offsetAddress, Elf64_Rela *rela, char* strs, int i, Elf64_Shdr *rela_dyn, int index, long shift, char* jumpOffset)
{
  // Get the bytes from the instruction
  long bytes[4];
  bytes[0] = (0xff & (*(j-1)));
  bytes[1] = (0xff & (*(j-2)));
  bytes[2] = (0xff & (*(j-3)));
  bytes[3] = (0xff & (*(j-4)));

  
  long first = bytes[0] << 24;
  long second = bytes[1] << 16;
  long third = bytes[2] << 8;
  long fourth = bytes[3];

  long total = first + second + third + fourth;


  // The address of the start of the function
  char* funcTextAddress = (char*)text_shdr->sh_offset + syms[i].st_value - text_shdr->sh_addr; 
  // The offset of the variable instruction from the beginning of the funcion
  int instOffset = j - offsetAddress;
  // The address of the next instruction
  char* ripAddress = funcTextAddress + instOffset;
  // The address in .rela.dyn that we want to look at
  char* reladynTarget = total + ripAddress;

  // If we're in a jump case
  if(index == -1)
  {
    reladynTarget = total + jumpOffset + 7;
  
    // Here be the Land of Lost Prints
    // A moment of silence for those lost in the Great Debugging War 2017-2017
    //printf("funcAddress: %x\n", funcTextAddress);
    //printf("at funcTextAddress: %x\n", *funcTextAddress);
    //printf("shift: %d\n", shift);
    //printf("offsetAddress: %x\n", offsetAddress);
    //printf("instOffset: %x\n", instOffset);
    //printf("j: %x\n", j);
    //printf("Next Instr: %p\n", j);
    //printf("total: %x\n", total);
    //printf("rip + offset: %p\n", theAddress);
    //printf("text: %p\n", text_shdr);
    //printf("rip: %p\n", ripAddress);
    //printf("thing: %p\n", thing);
    //printf("thing+total: %p\n", thing+total);
    //printf("at thing+total: %p\n", *(thing+total));
    //printf("rela target: %p\n", reladynTarget);
    //printf("at rela target: %p\n", *(total+0x847));
  }

  // The size of the rela table
  int relaSize = rela_dyn->sh_size / sizeof(Elf64_Rela);

  // The offset into the rela table
  int off;
  for(off=0; off<relaSize; off++)
  {
    long nextOff = rela[off].r_offset;

    // If we find the target, print the variable
    if((char*)nextOff == reladynTarget)
    {
      printf("  %s\n", strs + syms[ELF64_R_SYM(rela[off].r_info)].st_name);
    }
  }
}



