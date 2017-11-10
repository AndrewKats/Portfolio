package com.example.andrewkats.fingerpaint;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener, PaintAreaView.OnPolylineEndedListener, CurrentSplotchView.OnChangeColorListener, ValueAnimator.AnimatorUpdateListener
{
    /*
    PaletteView _paletteLayout = null;
    SplotchView _selectedSplotch = null;
    KnobColorSelectorView _knobLayout = null;
    PaintAreaView _paintArea = null;

    Button _plusButton = null;
    Button _minusButton = null;

    int _redValue = 0;
    int _greenValue = 0;
    int _blueValue = 0;
    */

    int _currentColor = Color.RED;

    PaintAreaView _paintView = null;
    LinearLayout _menuLayout = null;
    Button _backButton = null;
    Button _forwardButton = null;
    Button _playButton = null;
    CurrentSplotchView _currentSplotch = null;

    boolean playing = false;

    ValueAnimator animator = new ValueAnimator();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            retrieveGallery();
            insertDrawing(Gallery.getInstance().galleryCurrent);
            _paintView.invalidate();
        }
        catch (Exception e)
        {
            System.out.println("Gallery file not found");
            e.printStackTrace();
        }

        float density = getResources().getDisplayMetrics().density;
        //float padWidth = .075f * 160.0f * density;

        float childWidth = 0.6f * 160.0f * density;
        float childHeight = 0.4f * 160.0f * density;

        LinearLayout masterLayout = new LinearLayout(this);
        masterLayout.setOrientation(LinearLayout.VERTICAL);



        if(getIntent().getExtras() == null)
        {
            Gallery.getInstance().addNewDrawing();
        }

        _paintView = new PaintAreaView(this);
        _paintView.setPolylineEndedListener(this);
        masterLayout.addView(_paintView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 5));

        if(getIntent().getExtras() != null)
        {
            //_currentColor = getIntent().getIntExtra("Return Color", Color.RED);
            //_paintView.setColor(_currentColor);

            String intentString = getIntent().getExtras().getString("Return Color");
            if (intentString != null)
            {
                String[] intentSplit = intentString.split(" ");
                _currentColor = Integer.parseInt(intentSplit[0]);
                _paintView.setColor(_currentColor);
                //_currentDrawingIndex = Integer.parseInt(intentSplit[1]);
                if(Gallery.getInstance().galleryCurrent < Gallery.getInstance().getDrawingCount())
                {
                    insertDrawing(Gallery.getInstance().galleryCurrent);
                }
                else if(Gallery.getInstance().galleryCurrent == Gallery.getInstance().getDrawingCount())
                {
                    Gallery.getInstance().addNewDrawing();
                    insertDrawing(Gallery.getInstance().galleryCurrent);
                }
            }

            String newDrawString = getIntent().getExtras().getString("New Painting");
            if (newDrawString != null)
            {
                String[] newSplit = newDrawString.split(" ");

                Gallery.getInstance().galleryCurrent = Integer.parseInt(newSplit[1]);
            }

            String editString = getIntent().getExtras().getString("Return Edit");
            if (editString != null)
            {
                String[] editSplit = editString.split(" ");
                Gallery.getInstance().galleryCurrent = Integer.parseInt(editSplit[1]);
                if (Gallery.getInstance().galleryCurrent < Gallery.getInstance().getDrawingCount())
                {
                    //insertDrawing(_currentDrawingIndex);
                }
                else if (Gallery.getInstance().galleryCurrent == Gallery.getInstance().getDrawingCount())
                {
                    Gallery.getInstance().addNewDrawing();
                    //insertDrawing(_currentDrawingIndex);
                }

            }

            getIntent().removeExtra("Return Color");
            getIntent().removeExtra("New Painting");
            getIntent().removeExtra("Return Edit");
        }

        _menuLayout = new LinearLayout(this);
        _menuLayout.setBackgroundResource(R.drawable.wood);

        _backButton = new Button(this);
        _backButton.setBackgroundResource(R.drawable.back);
        _backButton.setOnClickListener(this);
        _menuLayout.addView(_backButton, new LinearLayout.LayoutParams((int) childWidth, ViewGroup.LayoutParams.MATCH_PARENT, 5));

        _forwardButton = new Button(this);
        _forwardButton.setBackgroundResource(R.drawable.forward);
        _forwardButton.setOnClickListener(this);
        _menuLayout.addView(_forwardButton, new LinearLayout.LayoutParams((int) childWidth, ViewGroup.LayoutParams.MATCH_PARENT, 5));

        _playButton = new Button(this);
        //_playButton.setText("PLAY");
        _playButton.setBackgroundResource(R.drawable.play);
        _playButton.setOnClickListener(this);
        _menuLayout.addView(_playButton, new LinearLayout.LayoutParams((int) childWidth, ViewGroup.LayoutParams.MATCH_PARENT, 5));




        _currentSplotch = new CurrentSplotchView(this);
        _currentSplotch.setSplotchColor(_currentColor);
        _currentSplotch.setChangeColorListener(this);
        LinearLayout.LayoutParams splotchParams = new LinearLayout.LayoutParams((int) childWidth, ViewGroup.LayoutParams.MATCH_PARENT, 3);
        splotchParams.gravity = Gravity.CENTER_VERTICAL;
        _menuLayout.addView(_currentSplotch, splotchParams);

        masterLayout.addView(_menuLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1));

        setContentView(masterLayout);

        if(Gallery.getInstance().galleryCurrent < Gallery.getInstance().getDrawingCount())
        {
            insertDrawing(Gallery.getInstance().galleryCurrent);
        }
        else if(Gallery.getInstance().galleryCurrent == Gallery.getInstance().getDrawingCount())
        {
            Gallery.getInstance().addNewDrawing();
            insertDrawing(Gallery.getInstance().galleryCurrent);
        }
        _paintView.invalidate();

        System.out.println("HERE'S CURRENT INDEX: " + Gallery.getInstance().galleryCurrent + "<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
    }

    @Override
    public void onClick(View v)
    {
        if(!playing)
        {
            if (v == _backButton)
            {
                if (Gallery.getInstance().galleryCurrent > 0)
                {
                    Gallery.getInstance().galleryCurrent--;
                    insertDrawing(Gallery.getInstance().galleryCurrent);
                    _paintView.invalidate();
                }
            }
            else if (v == _forwardButton)
            {

                if (Gallery.getInstance().galleryCurrent == Gallery.getInstance().getDrawingCount() - 1)
                {
                    if (Gallery.getInstance().getDrawing(Gallery.getInstance().galleryCurrent).getStrokeCount() > 0)
                    {
                        Gallery.getInstance().addNewDrawing();
                        Gallery.getInstance().galleryCurrent++;
                        insertDrawing(Gallery.getInstance().galleryCurrent);
                        _paintView.invalidate();
                    }
                }
                else if (Gallery.getInstance().galleryCurrent < Gallery.getInstance().getDrawingCount() - 1)
                {
                    Gallery.getInstance().galleryCurrent++;
                    insertDrawing(Gallery.getInstance().galleryCurrent);
                    _paintView.invalidate();
                }


            }
        }
        if(v == _playButton)
        {
            if(playing)
            {
                playing = false;
                _playButton.setBackgroundResource(R.drawable.play);
                _paintView.setPlaying(false);
                insertDrawing(Gallery.getInstance().galleryCurrent);
                _paintView.invalidate();
            }
            else
            {
                playing = true;
                _playButton.setBackgroundResource(R.drawable.stop);
                insertDrawing(Gallery.getInstance().galleryCurrent);
                _paintView.setPlaying(true);

                AnimateDrawing();

            }
        }
    }

    public void AnimateDrawing()
    {
        if(Gallery.getInstance().getDrawing(Gallery.getInstance().galleryCurrent)._strokes.size() == 0)
        {
            playing = false;
            _playButton.setBackgroundResource(R.drawable.play);
            _paintView.setPlaying(false);
        }
        else
        {
            _paintView.empty();
            animator.setIntValues(0, Gallery.getInstance().getDrawing(Gallery.getInstance().galleryCurrent)._strokes.size()+1);
            animator.setDuration(5000);
            animator.addUpdateListener(this);
            animator.start();
        }
    }


    @Override
    public void onAnimationUpdate(ValueAnimator animation)
    {

        if(playing)
        {
            int currentValue = (int) animation.getAnimatedValue();
            Log.i("Animation", "" + currentValue);

            if (currentValue > _paintView.polylines.size())
            {
                try
                {
                    _paintView.addPolyLine(Gallery.getInstance().getDrawing(Gallery.getInstance().galleryCurrent)._strokes.get(currentValue - 1)._points);
                }
                catch(Exception e)
                {

                }
            }

            _paintView.invalidate();

            if (currentValue == Gallery.getInstance().getDrawing(Gallery.getInstance().galleryCurrent).getStrokeCount())
            {
                //animator.end();
                playing = false;
                _playButton.setBackgroundResource(R.drawable.play);
                _paintView.setPlaying(false);
            }
        }

    }


    @Override
    public void onPolylineEnded(ArrayList<PaintPoint> lastPoints) throws IOException
    {
        Stroke stroke = new Stroke();
        for(PaintPoint p : lastPoints)
        {
            PaintPoint strokePoint = new PaintPoint(p.getUnitX(), p.getUnitY(), p.getColor());
            stroke.addPoint(strokePoint);
        }

        Gallery.getInstance().addStrokeToDrawing(Gallery.getInstance().galleryCurrent, stroke);
        saveGallery();
    }

    public void insertDrawing(int drawingIndex)
    {
        _paintView.empty();
        for(Stroke s : Gallery.getInstance().getDrawing(drawingIndex)._strokes)
        {
            _paintView.addPolyLine(s._points);
        }
    }

    @Override
    public void OnChangeColor(CurrentSplotchView splotch)
    {
        if(!playing)
        {
            Intent getColorIntent = new Intent();
            getColorIntent.setClass(this, ColorActivity.class);
            getColorIntent.putExtra("Current Color", "" + _currentColor + " " + Gallery.getInstance().galleryCurrent);
            startActivity(getColorIntent);
        }
    }


    public void saveGallery() throws IOException
    {
        try
        {
            FileOutputStream os = openFileOutput("gallery.dat", MODE_PRIVATE);
            ObjectOutputStream objout = new ObjectOutputStream(os);
            objout.writeObject(Gallery.getInstance());
            objout.close();
        }
        catch(Exception e)
        {
            Log.i("File", "Alright we failed to write <++++++++++++++++++++++++++++++++++++++");
            throw e;
        }
    }

    public void retrieveGallery() throws IOException, ClassNotFoundException
    {
        try
        {
            FileInputStream in = openFileInput("gallery.dat");
            ObjectInputStream objin = new ObjectInputStream(in);
            Gallery g = (Gallery)objin.readObject();
            Gallery.getInstance()._drawings = g._drawings;
        }
        catch(Exception e)
        {
            Log.i("File", "Ok we failed to read <***************************************************");
            throw e;
        }
    }


    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        _currentColor = data.getIntExtra("Return Color", Color.MAGENTA);
        _currentSplotch.setSplotchColor(_currentColor);
        _currentSplotch.invalidate();

        _paintView.setColor(_currentColor);
        System.out.println("MADE IT BACK BABY");
    }
    */
}
