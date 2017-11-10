package com.example.andrewkats.networkbattleship;

/**
 * Created by AndrewKats on 11/15/2016.
 */
public class CellResponse
{
    int xPos;
    int yPos;
    String status;

    public CellResponse(int theXPos, int theYPos, String theStatus)
    {
        xPos = theXPos;
        yPos = theYPos;
        status = theStatus;
    }

}
