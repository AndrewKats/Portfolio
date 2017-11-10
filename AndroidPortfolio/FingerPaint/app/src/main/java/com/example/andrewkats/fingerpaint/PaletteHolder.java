package com.example.andrewkats.fingerpaint;


import android.graphics.Color;
import android.widget.LinearLayout;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by AndrewKats on 10/3/2016.
 */
public class PaletteHolder implements Serializable
{
    private static PaletteHolder _Instance = null;

    ArrayList<Integer> colors = new ArrayList<Integer>();
    int selectedColor = Color.RED;

    public static PaletteHolder getInstance()
    {
        if(_Instance == null)
        {
            synchronized (new Object())
            {
                _Instance = new PaletteHolder();
                _Instance.colors.add(Color.RED);
                _Instance.colors.add(Color.GREEN);
                _Instance.colors.add(Color.BLUE);
                _Instance.colors.add(Color.YELLOW);
                _Instance.colors.add(Color.rgb(148,0,211));
            }

        }
        return _Instance;
    }


    void setColors(ArrayList<Integer> colorList)
    {
        colors = colorList;
    }

    void setSelectedColor(int color)
    {
        selectedColor = color;
    }


}
