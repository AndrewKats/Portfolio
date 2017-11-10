package com.example.andrewkats.fingerpaint;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
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

/**
 * Created by AndrewKats on 10/3/2016.
 */
public class ColorActivity extends AppCompatActivity implements KnobView.OnAngleChangedListener, SplotchView.OnHighlightListener, View.OnClickListener
{
    PaletteView _paletteLayout = null;
    SplotchView _selectedSplotch = null;
    KnobColorSelectorView _knobLayout = null;

    Button _doneButton = null;

    Button _plusButton = null;
    Button _minusButton = null;

    int _selectedColor = 0;
    int _currentDrawingIndex = 0;

    int _redValue = 0;
    int _greenValue = 0;
    int _blueValue = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            retrievePalette();
        }
        catch (Exception e)
        {
            System.out.println("Palette file not found");
            e.printStackTrace();
        }

        String intentString = getIntent().getExtras().getString("Current Color");
        String[] intentSplit = intentString.split(" ");
        _selectedColor = Integer.parseInt(intentSplit[0]);
        _currentDrawingIndex = Integer.parseInt(intentSplit[1]);


        float density = getResources().getDisplayMetrics().density;
        float padWidth = .075f * 160.0f * density;

        LinearLayout masterLayout = new LinearLayout(this);
        masterLayout.setOrientation(LinearLayout.VERTICAL);
        //masterLayout.setBackgroundColor(Color.rgb(210, 180, 140));
        masterLayout.setBackgroundResource(R.drawable.wood);
        //masterLayout.setBackgroundColor(Color.rgb(210, 180, 140));

        _doneButton = new Button(this);
        _doneButton.setText("<-   Back to Painting!");
        _doneButton.setOnClickListener(this);
        LinearLayout.LayoutParams doneParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 1);
        doneParams.gravity = Gravity.CENTER_HORIZONTAL;
        masterLayout.addView(_doneButton, doneParams);


        _paletteLayout = new PaletteView(this);

        for(int c : PaletteHolder.getInstance().colors)
        {
            SplotchView newPaintSplotchView = new SplotchView(this);
            newPaintSplotchView.setHighlightListener(this);
            _paletteLayout.addColor(newPaintSplotchView, c);
        }

        SplotchView setSplotch = new SplotchView(this);
        for(int i=0; i<_paletteLayout.getChildCount() ; i++)
        {
            SplotchView lookAtSplotch = (SplotchView) _paletteLayout.getChildAt(i);
            if(lookAtSplotch.getSplotchColor() == PaletteHolder.getInstance().selectedColor)
            {
                setSplotch = lookAtSplotch;
            }
        }
        _paletteLayout.setSelection(setSplotch);
        _selectedSplotch = setSplotch;

        masterLayout.addView(_paletteLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 3));

        _knobLayout = new KnobColorSelectorView(this);
        //_knobLayout.setBackgroundColor(Color.rgb(139, 119, 101));

        LinearLayout.LayoutParams individualKnobsParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        individualKnobsParams.gravity = Gravity.CENTER_VERTICAL;

        KnobView redKnob = new KnobView(this);
        redKnob.setColor(Color.RED);
        //redKnob.setBackgroundColor(Color.RED);
        redKnob.setOnAngleChangedListener(this);
        redKnob.setPadding((int)padWidth/3,0,0,0);
        _knobLayout.addView(redKnob, individualKnobsParams);

        KnobView greenKnob = new KnobView(this);
        greenKnob.setColor(Color.GREEN);
        //greenKnob.setBackgroundColor(Color.GREEN);
        greenKnob.setOnAngleChangedListener(this);
        greenKnob.setPadding((int)padWidth/3,0,0,0);
        _knobLayout.addView(greenKnob, individualKnobsParams);

        KnobView blueKnob = new KnobView(this);
        blueKnob.setColor(Color.BLUE);
        //blueKnob.setBackgroundColor(Color.BLUE);
        blueKnob.setOnAngleChangedListener(this);
        blueKnob.setPadding((int)padWidth/3,0,0,0);
        _knobLayout.addView(blueKnob, individualKnobsParams);

        LinearLayout plusMinusLayout = new LinearLayout(this);
        plusMinusLayout.setOrientation(LinearLayout.VERTICAL);
        //plusMinusLayout.setBackgroundColor(Color.BLUE);

        _plusButton = new Button(this);
        // _plusButton.setBackgroundColor(Color.LTGRAY);
        _plusButton.setText("+");
        plusMinusLayout.addView(_plusButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        _plusButton.setOnClickListener(this);


        _minusButton = new Button(this);
        //_minusButton.setBackgroundColor(Color.BLACK);
        //_minusButton.setTextColor(Color.WHITE);
        _minusButton.setText("-");
        plusMinusLayout.addView(_minusButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        _minusButton.setOnClickListener(this);


        plusMinusLayout.setPadding((int)padWidth,0,0,0);

        LinearLayout.LayoutParams plusMinusParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        plusMinusParams.gravity = Gravity.CENTER_VERTICAL;
        _knobLayout.addView(plusMinusLayout, plusMinusParams);


        LinearLayout.LayoutParams knobParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        knobParams.gravity = Gravity.CENTER_HORIZONTAL;
        //_knobLayout.setBackgroundColor(Color.MAGENTA);
        masterLayout.addView(_knobLayout, knobParams);

        setContentView(masterLayout);
    }

    void finishTheIntent() throws IOException
    {
        /*
        Intent returnIntent = new Intent();
        returnIntent.putExtra("Return Color", _selectedColor);
        setResult(Activity.RESULT_OK, returnIntent);
        System.out.println("ABOUT TO GO BACK <========================================================================================");
        finish();
        */

        ArrayList<Integer> newColors = new ArrayList<Integer>();
        for(int i=0; i< _paletteLayout.getChildCount(); i++)
        {
            int colorToAdd = ((SplotchView)_paletteLayout.getChildAt(i)).getSplotchColor();
            newColors.add(colorToAdd);
        }

        PaletteHolder.getInstance().setColors(newColors);
        PaletteHolder.getInstance().setSelectedColor(_selectedColor);

        savePalette();

        Intent getColorIntent = new Intent();
        getColorIntent.setClass(this, MainActivity.class);
        getColorIntent.putExtra("Return Color", "" + _selectedColor + " " + _currentDrawingIndex);
        startActivity(getColorIntent);
    }

    public void savePalette() throws IOException
    {
        try
        {
            FileOutputStream os = openFileOutput("palette.dat", MODE_PRIVATE);
            ObjectOutputStream objout = new ObjectOutputStream(os);
            objout.writeObject(PaletteHolder.getInstance());
            objout.close();
        }
        catch(Exception e)
        {
            Log.i("File", "Alright we failed to write <++++++++++++++++++++++++++++++++++++++");
            throw e;
        }
    }

    public void retrievePalette() throws IOException, ClassNotFoundException
    {
        try
        {
            FileInputStream in = openFileInput("palette.dat");
            ObjectInputStream objin = new ObjectInputStream(in);
            PaletteHolder p = (PaletteHolder)objin.readObject();
            PaletteHolder.getInstance().colors = p.colors;
        }
        catch(Exception e)
        {
            Log.i("File", "Ok we failed to read <***************************************************");
            throw e;
        }
    }

    @Override
    public void onAngleChanged(float theta, int color)
    {
        float twoPi = ((float)(2 * Math.PI));

        if(theta < 0)
        {
            theta = twoPi + theta;
        }

        float thetaMod = theta%twoPi;
        float portion = thetaMod/twoPi;
        float rgbValue = 255.0f*portion;
        if(color==Color.RED)
        {
            ((KnobView)_knobLayout.getChildAt(0)).setKnobColor(Color.rgb((int)rgbValue,0,0));
            _redValue=(int)rgbValue;
        }
        if(color==Color.GREEN)
        {
            ((KnobView)_knobLayout.getChildAt(1)).setKnobColor(Color.rgb(0,(int)rgbValue,0));
            _greenValue=(int)rgbValue;
        }
        if(color==Color.BLUE)
        {
            ((KnobView)_knobLayout.getChildAt(2)).setKnobColor(Color.rgb(0,0,(int)rgbValue));
            _blueValue=(int)rgbValue;
        }
    }

    @Override
    public void OnHighlightChanged(SplotchView splotch)
    {
        _selectedSplotch = splotch;
        _selectedColor = splotch.getSplotchColor();
        _paletteLayout.setSelection(splotch);
    }

    @Override
    public void onClick(View v)
    {
        if(v == _plusButton)
        {
            SplotchView newPaintSplotchView = new SplotchView(this);
            newPaintSplotchView.setHighlightListener(this);
            _paletteLayout.addColor(newPaintSplotchView, Color.rgb(_redValue, _greenValue, _blueValue));
        }
        else if(v == _minusButton)
        {
            if(_paletteLayout.getChildCount() > 1)
            {
                if(((SplotchView)(_paletteLayout.getChildAt(_paletteLayout.getChildCount() - 1))).isHighlighted())
                {
                    SplotchView toSelect = (SplotchView)(_paletteLayout.getChildAt(_paletteLayout.getChildCount() - 2));
                    _paletteLayout.setSelection(toSelect);
                    _selectedSplotch = toSelect;
                }

                _paletteLayout.removeColor();
            }
        }
        else if(v == _doneButton)
        {
            try
            {
                finishTheIntent();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


}

