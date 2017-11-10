package com.example.andrewkats.fingerpaint;

import java.io.Serializable;

/**
 * Created by AndrewKats on 9/19/2016.
 */
public class PaintPoint implements Serializable
{
    float _unitX;
    float _unitY;
    int _color;

    public int getColor()
    {
        return _color;
    }

    public void setColor(int _color)
    {
        this._color = _color;
    }

    public float getUnitY()
    {
        return _unitY;
    }

    public void setUnitY(float _unitY)
    {
        this._unitY = _unitY;
    }

    public float getUnitX()
    {
        return _unitX;
    }

    public void setUnitX(float _unitX)
    {
        this._unitX = _unitX;
    }

    public PaintPoint(float x, float y, int color)
    {
        _unitX = x;
        _unitY = y;
        _color = color;
    }

}
