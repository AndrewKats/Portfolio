package com.example.andrewkats.networkbattleship;

import java.io.IOException;

/**
 * Created by AndrewKats on 11/13/2016.
 */
public class GameSummary
{
    public String id;
    public String name;
    public String status;

    @Override
    public String toString()
    {
        GameCollection _games = GameCollection.getInstance();
        String returnString = "\nName: " + name + "\n\n" + "Status: " + status + "\n";

        if(_games.myGameHolder.myGames.contains(id))
        {
            int index = _games.myGameHolder.myGames.indexOf(id);
            if(index<_games.myGameHolder.myTurns.size())
            {
                returnString += "\n" + _games.myGameHolder.myTurns.get(index) + "\n";
            }
        }

        return returnString;
    }
}
