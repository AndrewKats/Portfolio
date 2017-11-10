package com.example.andrewkats.fingerpaint;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by AndrewKats on 9/18/2016.
 */
public class PaintAreaView extends View
{
    public interface OnPolylineStartedListener
    {
        void onPolylineStarted();
    }

    public interface OnPolylineEndedListener
    {
        void onPolylineEnded(ArrayList<PaintPoint> lastPoints) throws IOException;
    }



    public interface OnPaintListener
    {
        void onPaint(float theta, int color);
    }


    boolean playing = false;

    public boolean isPlaying()
    {
        return playing;
    }

    public void setPlaying(boolean playing)
    {
        this.playing = playing;
    }

    int _color = Color.RED;
    ArrayList<PaintPoint> currentPoints = null;
    ArrayList<ArrayList<PaintPoint>> polylines = new ArrayList<ArrayList<PaintPoint>> ();

    OnPolylineStartedListener _polylineStartedListener = null;
    OnPolylineEndedListener _polylineEndedListener = null;

    public OnPolylineEndedListener getPolylineEndedListener()
    {
        return _polylineEndedListener;
    }

    public void setPolylineEndedListener(OnPolylineEndedListener _polylineEndedListener)
    {
        this._polylineEndedListener = _polylineEndedListener;
    }

    public OnPolylineStartedListener getPolylineStartedListener()
    {
        return _polylineStartedListener;
    }

    public void setPolylineStartedListener(OnPolylineStartedListener _polylineStartedListener)
    {
        this._polylineStartedListener = _polylineStartedListener;
    }

    public void empty()
    {
        polylines = new ArrayList<ArrayList<PaintPoint>>();
        currentPoints = null;
        invalidate();
    }

    public void addPolyLine(ArrayList<PaintPoint> polyline)
    {
        polylines.add(polyline);
        invalidate();
    }



    public void setColor(int color)
    {
        _color = color;
    }


    public PaintAreaView(Context context)
    {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        Paint pointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //pointPaint.setStyle(Paint.Style.STROKE);
        //pointPaint.setStrokeJoin(Paint.Join.MITER);
        //pointPaint.setStrokeWidth(16f);

        //float strokeWidth = getWidth()*0.01f;
        float density = getResources().getDisplayMetrics().density;

        float strokeWidth = .03f * 160.0f * density;
        float circleRadius = strokeWidth/2;
        pointPaint.setStrokeWidth(strokeWidth);


        for(ArrayList<PaintPoint> l : polylines)
        {
            pointPaint.setColor(l.get(0).getColor());

            canvas.drawCircle(l.get(0).getUnitX()*getWidth(), l.get(0).getUnitY()*getHeight(),circleRadius,pointPaint);
            canvas.drawCircle(l.get(l.size()-1).getUnitX()*getWidth(), l.get(l.size()-1).getUnitY()*getHeight(),circleRadius,pointPaint);

            for(int i = 0; i < l.size()-1; i++)
            {
                PaintPoint first = l.get(i);
                PaintPoint second = l.get(i+1);
                canvas.drawLine(first.getUnitX() * getWidth(), first.getUnitY() * getHeight(), second.getUnitX() * getWidth(), second.getUnitY() * getHeight(), pointPaint);
            }
        }

        if(currentPoints!=null)
        {
            pointPaint.setColor(currentPoints.get(0).getColor());

            canvas.drawCircle(currentPoints.get(0).getUnitX()*getWidth(), currentPoints.get(0).getUnitY()*getHeight(),circleRadius,pointPaint);
            canvas.drawCircle(currentPoints.get(currentPoints.size()-1).getUnitX()*getWidth(), currentPoints.get(currentPoints.size()-1).getUnitY()*getHeight(),circleRadius,pointPaint);

            for (int i = 0; i < currentPoints.size() - 1; i++) {
                PaintPoint first = currentPoints.get(i);
                PaintPoint second = currentPoints.get(i + 1);
                canvas.drawLine(first.getUnitX() * getWidth(), first.getUnitY() * getHeight(), second.getUnitX() * getWidth(), second.getUnitY() * getHeight(), pointPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if(playing)
        {
            return true;
        }
        float x = event.getX();
        float y = event.getY();

        float unitX = x/getWidth();
        float unitY = y/getHeight();

        /*
        PaintPoint newPoint = new PaintPoint(unitX, unitY, _color);
        points.add(newPoint);
        */

        PaintPoint newPoint = new PaintPoint(unitX, unitY, _color);
        if(event.getActionMasked() == MotionEvent.ACTION_DOWN)
        {
            Log.i("PaintView", "STARTED <--------------------------------------------------------------------");
            currentPoints = new ArrayList<PaintPoint>();
            currentPoints.add(newPoint);
            if(_polylineStartedListener != null)
            {
                _polylineStartedListener.onPolylineStarted();
            }
        }
        if(event.getActionMasked() == MotionEvent.ACTION_MOVE)
        {
            currentPoints.add(newPoint);
        }
        if(event.getActionMasked() == MotionEvent.ACTION_UP)
        {
            Log.i("PaintView", "FINISHED <--------------------------------------------------------------------");
            currentPoints.add(newPoint);
            polylines.add(currentPoints);
            if(_polylineEndedListener != null)
            {
                try
                {
                    _polylineEndedListener.onPolylineEnded(currentPoints);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        invalidate();

        return true;
    }

}

