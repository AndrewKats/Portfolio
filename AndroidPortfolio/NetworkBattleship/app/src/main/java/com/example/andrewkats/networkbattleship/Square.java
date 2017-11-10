package com.example.andrewkats.networkbattleship;

import java.io.Serializable;

/**
 * Created by AndrewKats on 10/22/2016.
 */
public class Square implements Serializable
{
    // 1 = mystery, 2 = hit, 3 = miss
    int _hitStatus = 1;
    boolean _isShip = false;

    public Square()
    {
        _isShip = false;
        _hitStatus = 1;
    }

    public Square(boolean ship)
    {
        _isShip = ship;
        _hitStatus = 1;
    }

    boolean isShip()
    {
        return _isShip;
    }

    void setShip(boolean isShip)
    {
        _isShip = isShip;
    }

    void setHitStatus(int status)
    {
        _hitStatus = status;
    }

    boolean fire()
    {
        if(_isShip)
        {
            _hitStatus = 2;
        }
        else
        {
            _hitStatus = 3;
        }

        return _isShip;
    }


}
