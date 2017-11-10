package com.example.andrewkats.fingerpaint;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by AndrewKats on 10/2/2016.
 */
public class Drawing implements Serializable
{
    ArrayList<Stroke> _strokes = new ArrayList<Stroke>();

    int getStrokeCount()
    {
        return _strokes.size();
    }

    Stroke getStroke(int strokeIndex)
    {
        return _strokes.get(strokeIndex);
    }

    void addStroke(Stroke stroke)
    {
        _strokes.add(stroke);
    }

}

