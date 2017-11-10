package com.example.andrewkats.fingerpaint;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

/**
 * Created by AndrewKats on 10/2/2016.
 */
public class Gallery implements Serializable
{

    private static Gallery _Instance = null;

    ArrayList<Drawing> _drawings = new ArrayList<Drawing>();

    int galleryCurrent;

    public static Gallery getInstance()
    {
        if(_Instance == null)
        {

            synchronized (new Object())
            {
                _Instance = new Gallery();
            }

        }
        return _Instance;
    }

    private Gallery()
    {
        // TODO: Construct gallery
    }

    int getDrawingCount()
    {
        return _drawings.size();
    }

    Drawing getDrawing(int drawingIndex)
    {
        return _drawings.get(drawingIndex);
    }

    void addNewDrawing()
    {
        synchronized (new Object())
        {
            _drawings.add(new Drawing());
        }
    }

    void addStrokeToDrawing(int drawingIndex, Stroke stroke)
    {
        _drawings.get(drawingIndex).addStroke(stroke);
    }


}

