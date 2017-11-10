package com.example.andrewkats.networkbattleship;

import java.io.Serializable;

/**
 * Created by AndrewKats on 10/22/2016.
 */
public class Game implements Serializable
{
    int _currentTurn = 1;

    GameGrid _oneGrid = new GameGrid();
    GameGrid _twoGrid = new GameGrid();

    boolean lastHit = false;
    int lastX = -1;
    int lastY = -1;

    boolean launching = false;

    void setTurn(int player)
    {
        _currentTurn = player;
    }

    void changeTurn()
    {
        if(_currentTurn==1)
        {
            _currentTurn=2;
        }
        else
        {
            _currentTurn=1;
        }
    }


    void bothRandomShips()
    {
        _oneGrid.randomShips();
        _twoGrid.randomShips();
    }

    GameGrid getGrid(int player)
    {
        if(player == 1)
        {
            return _oneGrid;
        }
        else
        {
            return _twoGrid;
        }
    }

    String getState()
    {
        if(_oneGrid._missilesLaunched==0 && _twoGrid._missilesLaunched==0)
        {
            return "Start";
        }
        if(_oneGrid._hits==17 || _twoGrid._hits==17)
        {
            if(_oneGrid._hits > _twoGrid._hits)
            {
                return "Player One Won";
            }
            if(_oneGrid._hits < _twoGrid._hits)
            {
                return "Player Two Won";
            }
        }
        if(_oneGrid._hits<17 && _twoGrid._hits<17)
        {
            return "In Progress";
        }

        return "Something went wrong: " + _oneGrid._hits + " " + _twoGrid._hits;
    }

    boolean isGameOver()
    {
        if(_oneGrid._hits==17 || _twoGrid._hits==17)
        {
            return true;
        }
        return false;
    }

}
