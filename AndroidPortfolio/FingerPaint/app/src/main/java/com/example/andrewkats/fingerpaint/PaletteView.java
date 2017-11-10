package com.example.andrewkats.fingerpaint;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Andrew on 9/18/2016.
 */
public class PaletteView extends ViewGroup
{
    public PaletteView(Context context)
    {
        super(context);
        setWillNotDraw(false);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b)
    {
        for(int childIndex = 0; childIndex < getChildCount(); childIndex++)
        {
            float theta = (float)(2.0 * Math.PI) / (float)getChildCount() * (float)childIndex;

            float density = getResources().getDisplayMetrics().density;
            float childWidth = 0.3f * 160.0f * density;
            float childHeight = 0.2f * 160.0f * density;

            PointF childCenter = new PointF();
            childCenter.x = getWidth() * 0.5f + (getWidth() * 0.5f - childWidth * 0.7f) * (float)Math.cos(theta);
            childCenter.y = getHeight()*.95f * 0.5f + (getHeight()*.95f* 0.5f - childHeight * 0.7f) * (float)Math.sin(theta);


            Rect childRect = new Rect();
            childRect.left = (int)(childCenter.x - childWidth * 0.5f);
            childRect.right = (int)(childCenter.x + childWidth * 0.5f);
            childRect.top = (int)(childCenter.y - childHeight * 0.5f);
            childRect.bottom = (int)(childCenter.y + childHeight * 0.5f);

            View childView = getChildAt(childIndex);
            childView.layout(childRect.left, childRect.top, childRect.right, childRect.bottom);
        }
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        RectF shadowRect = new RectF();
        shadowRect.left = getPaddingLeft();
        shadowRect.top = getPaddingTop();
        shadowRect.right = getWidth() - getPaddingRight();
        shadowRect.bottom = getHeight() - getPaddingBottom();

        Paint shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint.setColor(Color.argb(150, 0, 0, 0));

        canvas.drawOval(shadowRect, shadowPaint);

        RectF paletteRect = new RectF();
        paletteRect.left = getPaddingLeft();
        paletteRect.top = getPaddingTop();
        paletteRect.right = getWidth() - getPaddingRight();
        paletteRect.bottom = getHeight()*.95f - getPaddingBottom();

        Paint palettePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        palettePaint.setColor(Color.rgb(238, 223, 204));

        canvas.drawOval(paletteRect, palettePaint);
    }

    public void setSelection(SplotchView splotch)
    {
        for(int childIndex = 0; childIndex < getChildCount(); childIndex++)
        {
            if(getChildAt(childIndex) == splotch)
            {
                splotch.setHighlighted(true);
            }
            else if(((SplotchView)(getChildAt(childIndex))).isHighlighted())
            {
                ((SplotchView)(getChildAt(childIndex))).setHighlighted(false);
            }
        }
    }

    public void removeColor()
    {
        this.removeView(this.getChildAt(this.getChildCount() - 1));
        this.requestLayout();
    }

    public void addColor(SplotchView splotch, int color)
    {
        splotch.setSplotchColor(color);
        this.addView(splotch);
        this.requestLayout();
    }
}

