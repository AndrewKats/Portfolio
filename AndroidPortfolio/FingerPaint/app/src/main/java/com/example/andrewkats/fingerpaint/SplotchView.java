package com.example.andrewkats.fingerpaint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Andrew on 9/18/2016.
 */
public class SplotchView extends View
{
    public interface OnHighlightListener
    {
        void OnHighlightChanged(SplotchView splotch);
    }

    OnHighlightListener _highlightListener = null;

    int _splotchColor = Color.WHITE;
    boolean _highlighted = false;

    public boolean isHighlighted() {
        return _highlighted;
    }

    public void setHighlighted(boolean _highlighted)
    {
        this._highlighted = _highlighted;
        invalidate();
    }

    public SplotchView(Context context)
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

        if(_highlighted)
        {
            Paint highlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            highlightPaint.setColor(Color.rgb(255 - Color.red(_splotchColor), 255 - Color.green(_splotchColor), 255 - Color.blue(_splotchColor)));
            highlightPaint.setStyle(Paint.Style.STROKE);
            highlightPaint.setStrokeWidth(splotchRect.height() * 0.1f);

            RectF highlightRect = new RectF(splotchRect);
            highlightRect.left += splotchRect.height() * 0.05f;
            highlightRect.top += splotchRect.height() * 0.05f;
            highlightRect.right -= splotchRect.height() * 0.05f;
            highlightRect.bottom -= splotchRect.height() * 0.05f;

            canvas.drawOval(highlightRect, highlightPaint);
        }
    }

    public void setHighlightListener(OnHighlightListener listener)
    {
        _highlightListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        _highlightListener.OnHighlightChanged(this);

        return true;
        // return super.onTouchEvent(event);
    }
}

