package com.example.andrewkats.fingerpaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by AndrewKats on 9/16/2016.
 */
public class KnobView extends View
{
    public interface OnAngleChangedListener
    {
        void onAngleChanged(float theta, int color);
    }


    int _color = 0;
    int _knobColor = Color.BLACK;
    float _theta = 0.0f;
    RectF _knobRect = new RectF();
    OnAngleChangedListener _angleChangedListener = null;

    public KnobView(Context context)
    {
        super(context);
    }

    public void setKnobColor(int color)
    {
        _knobColor = color;
        invalidate();
    }

    public float getTheta()
    {
        return _theta;
    }

    public void setColor(int color)
    {
        _color = color;
    }

    public int getColor()
    {
        return _color;
    }


    public void setTheta(float theta)
    {
        _theta = theta;
        invalidate();
    }

    public void setOnAngleChangedListener(OnAngleChangedListener listener)
    {
        _angleChangedListener = listener;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        PointF touchPoint = new PointF();
        touchPoint.x = event.getX();
        touchPoint.y = event.getY();

        float theta = (float)Math.atan2(touchPoint.y - _knobRect.centerY(), touchPoint.x - _knobRect.centerX());

        setTheta(theta);
        _angleChangedListener.onAngleChanged(theta, _color);

        return true;
        // return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        _knobRect.left = getPaddingLeft();
        _knobRect.top = getPaddingTop();
        _knobRect.right = getWidth() - getPaddingRight();
        _knobRect.bottom = _knobRect.width();

        float offset = (getHeight() - _knobRect.height()) * .5f;
        _knobRect.top += offset;
        _knobRect.bottom += offset;

        float radius = _knobRect.width() * 0.35f;

        PointF nibCenter = new PointF();
        nibCenter.x = _knobRect.centerX() + radius * (float)Math.cos((double)_theta);
        nibCenter.y = _knobRect.centerY() + radius * (float)Math.sin((double)_theta);

        float nibRadius = radius * 0.2f;

        RectF nibRect = new RectF();
        nibRect.left = nibCenter.x - nibRadius;
        nibRect.top = nibCenter.y - nibRadius;
        nibRect.right = nibCenter.x + nibRadius;
        nibRect.bottom = nibCenter.y + nibRadius;

        Paint knobPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        knobPaint.setColor(_knobColor);

        Paint nibPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        nibPaint.setColor(Color.YELLOW);
        canvas.drawOval(_knobRect, knobPaint);
        canvas.drawOval(nibRect, nibPaint);


        RectF shineRect = new RectF(_knobRect);
        shineRect.left += _knobRect.height() * .03;
        shineRect.top += _knobRect.height() * .03;
        shineRect.right -= _knobRect.height() * .03;
        shineRect.bottom -= _knobRect.height() * .03;

        Paint shinePaint = new Paint();
        shinePaint.setColor(Color.argb(50,255,255,255));
        shinePaint.setStyle(Paint.Style.STROKE);
        shinePaint.setStrokeWidth(_knobRect.height() * 0.05f);
        canvas.drawOval(shineRect, shinePaint);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpec = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpec = MeasureSpec.getSize(heightMeasureSpec);

        int width = 300;
        int height = 300;

        if(widthMode == MeasureSpec.EXACTLY)
        {
            width = widthSpec;
        }
        if(heightMode == MeasureSpec.EXACTLY)
        {
            height = heightSpec;
        }

        if(widthMode != MeasureSpec.EXACTLY && heightMode != MeasureSpec.EXACTLY)
        {
            if(width < height)
            {
                width = height;
            }
            if(height < width)
            {
                height = width;
            }
        }


        width = Math.max(width, getSuggestedMinimumWidth());
        height = Math.max(height, getSuggestedMinimumHeight());

        if(width < height)
        {
            width = height;
        }
        if(height < width)
        {
            height = width;
        }

        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }

}




