package com.example.andrewkats.codeymobile;

/**
 * Created by AndrewKats on 12/17/2016.
 */
public class CodeyLoop extends CodeyAction
{
    int times=0;
    int timesDone=0;
    public CodeyAction action1=null;
    public CodeyAction action2=null;
    public CodeyAction action3=null;

    public CodeyLoop(int theTimes, CodeyAction first, CodeyAction second, CodeyAction third)
    {
        times = theTimes;
        action1 = first;
        action2 = second;
        action3 = third;
    }


    @Override
    String actionString()
    {
        return "loop";
    }
}
