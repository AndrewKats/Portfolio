package com.example.andrewkats.networkbattleship;

import android.content.Context;
import android.widget.Button;

/**
 * Created by AndrewKats on 10/23/2016.
 */
public class GridButton extends Button
{
    public GridButton(Context context)
    {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec)/10;
        int height = MeasureSpec.getSize(heightMeasureSpec)/10;

        if(width>height)
        {
            width=height;
        }
        else
        {
            height=width;
        }

        setMeasuredDimension(width, height);
    }
}
