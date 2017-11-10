package com.example.andrewkats.codeymobile;

import android.content.Context;
import android.widget.Button;

public class SquareButton extends Button
{
    public SquareButton(Context context)
    {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

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
