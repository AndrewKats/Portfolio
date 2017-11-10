package com.example.andrewkats.networkbattleship;

import java.io.Serializable;
import java.util.Random;

/**
 * Created by AndrewKats on 10/22/2016.
 */
public class GameGrid implements Serializable
{
    Square[][] _gridSquares = new Square[10][10];

    int _missilesLaunched = 0;
    int _hits = 0;

    public GameGrid()
    {
        for(int i=0; i<10; i++)
        {
            for(int j=0; j<10; j++)
            {
                _gridSquares[i][j] = new Square();
            }
        }
    }



    boolean fireAt(int x, int y)
    {
        if(x < 10 && y < 10)
        {
            boolean isHit = _gridSquares[x][y].fire();
            _missilesLaunched++;
            if(isHit)
            {
                _hits++;
                return true;
            }
        }
        return false;
    }

    void randomShips()
    {
        placeShip(5);
        placeShip(4);
        placeShip(3);
        placeShip(3);
        placeShip(2);
    }

    private void placeShip(int shipSize)
    {
        Random rand = new Random();
        int VerticalOrHorizontal = rand.nextInt(2);

        // Vertical
        if(VerticalOrHorizontal==0)
        {
            int x = rand.nextInt(10);
            int y = rand.nextInt(10-shipSize);

            boolean valid = true;
            for(int i=0; i<shipSize; i++)
            {
                if(_gridSquares[x][y+i]._isShip)
                {
                    valid = false;
                }
            }

            if(valid)
            {
                for(int i=0; i<shipSize; i++)
                {
                    _gridSquares[x][y+i].setShip(true);
                }
            }
            else
            {
                placeShip(shipSize);
            }
        }
        // Horizontal
        else
        {

            int x = rand.nextInt(10-shipSize);
            int y = rand.nextInt(10);

            boolean valid = true;
            for(int i=0; i<shipSize; i++)
            {
                if(_gridSquares[x+i][y]._isShip)
                {
                    valid = false;
                }
            }

            if(valid)
            {
                for(int i=0; i<shipSize; i++)
                {
                    _gridSquares[x+i][y].setShip(true);
                }
            }
            else
            {
                placeShip(shipSize);
            }
        }
    }


}
