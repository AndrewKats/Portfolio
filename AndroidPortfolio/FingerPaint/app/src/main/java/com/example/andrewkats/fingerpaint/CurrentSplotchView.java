package com.example.andrewkats.fingerpaint;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by AndrewKats on 10/3/2016.
 */
public class CurrentSplotchView extends View
{

    public interface OnChangeColorListener
    {
        void OnChangeColor(CurrentSplotchView splotch);
    }

    OnChangeColorListener _changeColorListener = null;

    int _splotchColor = Color.WHITE;



    public CurrentSplotchView(Context context)
    {
        super(context);
    }

    public int getSplotchColor()
    {
        return _splotchColor;
    }

    public void setSplotchColor(int _splotchColor)
    {
        this._splotchColor = _splotchColor;
        invalidate();
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
        shadowPaint.setColor(Color.argb(100, 0, 0, 0));

        canvas.drawOval(shadowRect, shadowPaint);

        RectF splotchRect = new RectF();
        splotchRect.left = getPaddingLeft();
        splotchRect.top = getPaddingTop();
        splotchRect.right = getWidth() - getPaddingRight();
        splotchRect.bottom = getHeight()*.90f - getPaddingBottom();

        Paint splotchPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        splotchPaint.setColor(_splotchColor);

        canvas.drawOval(splotchRect, splotchPaint);


        RectF shineRect = new RectF();
        shineRect.left = splotchRect.left + splotchRect.width()*.2f;
        shineRect.top = splotchRect.top + splotchRect.height()*.2f;
        shineRect.right = shineRect.left + splotchRect.width()*.2f;
        shineRect.bottom = shineRect.top + splotchRect.height()*.2f;;

        Paint shinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shinePaint.setColor(Color.argb(150,255,255,255));

        canvas.drawOval(shineRect, shinePaint);
    }

    public void setChangeColorListener(OnChangeColorListener listener)
    {
        _changeColorListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        _changeColorListener.OnChangeColor(this);

        return true;
    }
}
