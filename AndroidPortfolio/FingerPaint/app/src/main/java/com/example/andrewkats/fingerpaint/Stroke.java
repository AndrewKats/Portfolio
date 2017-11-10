package com.example.andrewkats.fingerpaint;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by AndrewKats on 10/2/2016.
 */
public class Stroke implements Serializable
{
    ArrayList<PaintPoint> _points = new ArrayList<PaintPoint>();
    int _color = 0xFF000000;

    public int getColor()
    {
        return _color;
    }

    public void setColor(int _color)
    {
        this._color = _color;
    }

    int getPointCount()
    {
        return 0;
    }

    PaintPoint getPoint(int pointIndex)
    {
        return _points.get(pointIndex);
    }

    void addPoint(PaintPoint p)
    {
        _points.add(p);
    }
}
