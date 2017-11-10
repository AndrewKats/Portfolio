package com.example.andrewkats.networkbattleship;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by AndrewKats on 11/18/2016.
 */
public class MyGameHolder implements Serializable
{
    ArrayList<String> myGames = new ArrayList<String>();
    ArrayList<String> myPlayerIds = new ArrayList<String>();
    ArrayList<String> myTurns = new ArrayList<String>();
}
