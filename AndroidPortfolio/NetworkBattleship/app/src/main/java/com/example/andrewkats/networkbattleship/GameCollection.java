package com.example.andrewkats.networkbattleship;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by AndrewKats on 10/22/2016.
 */
public class GameCollection implements Serializable
{
    private static GameCollection _Instance = null;

    boolean playing = false;

    int currentGame=0;

    String currentGameId="";

    String currentGameStatus="";

    // 0=list, 1=pregame, 2=game
    int gameState = 0;

    // 0=all, 1=waiting, 2=playing, 3=done
    int filterState=0;

    String currentGameName = "";
    String currentPlayerName = "";

    MyGameHolder myGameHolder = new MyGameHolder();

    public static GameCollection getInstance()
    {
        if(_Instance == null)
        {

            synchronized (new Object())
            {
                _Instance = new GameCollection();
            }

        }
        return _Instance;
    }

    private GameCollection() { }

}
