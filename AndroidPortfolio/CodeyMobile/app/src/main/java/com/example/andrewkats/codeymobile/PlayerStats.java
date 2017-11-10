package com.example.andrewkats.codeymobile;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by AndrewKats on 12/16/2016.
 */
public class PlayerStats implements Serializable
{
    String name = "";
    int[] levelScores = {0,0,0,0,0,0,0,0};

    public PlayerStats(String newName)
    {
        name = newName;
    }

}
