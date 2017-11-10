package com.example.andrewkats.fingerpaint;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Created by AndrewKats on 10/3/2016.
 */
public class GalleryActivity extends AppCompatActivity implements ListAdapter, View.OnClickListener, AdapterView.OnItemClickListener
{

    Button _newButton = null;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        try
        {
            retrieveGallery();
        }
        catch (Exception e)
        {
            System.out.println("Gallery file not found");
            e.printStackTrace();
        }

        ListView galleryListView = new ListView(this);
        galleryListView.setAdapter(this);
        galleryListView.setOnItemClickListener(this);

        LinearLayout vertLayout = new LinearLayout(this);
        vertLayout.setOrientation(LinearLayout.VERTICAL);

        _newButton = new Button(this);
        _newButton.setText("Create a new drawing");
        _newButton.setOnClickListener(this);

        vertLayout.addView(_newButton);
        vertLayout.addView(galleryListView);

        setContentView(vertLayout);
    }

    public void retrieveGallery() throws IOException, ClassNotFoundException
    {
        try
        {
            FileInputStream in = openFileInput("gallery.dat");
            ObjectInputStream objin = new ObjectInputStream(in);
            Gallery g = (Gallery)objin.readObject();
            Gallery.getInstance()._drawings = g._drawings;
            System.out.println("Amount of drawings: " + Gallery.getInstance().getDrawingCount() + " <,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,");
        }
        catch(Exception e)
        {
            Log.i("File", "Ok we failed to read <***************************************************");
            throw e;
        }
    }

    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer)
    {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer)
    {

    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public int getCount()
    {
        return Gallery.getInstance().getDrawingCount();
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        PaintAreaView paintView = new PaintAreaView(this);
        paintView.setPlaying(true);
        for(Stroke s : Gallery.getInstance().getDrawing(position)._strokes)
        {
            paintView.addPolyLine(s._points);
        }

        float density = getResources().getDisplayMetrics().density;
        int rowHeight = (int)(density * 100);

        paintView.setMinimumHeight(rowHeight);

        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setMinimumHeight(rowHeight);
        rowLayout.addView(paintView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1));



        TextView summaryView = new TextView(this);
        summaryView.setText("Drawing #" + position + "\nStrokes: " + Gallery.getInstance().getDrawing(position)._strokes.size() + "\n\n[Tap here to edit]");

        rowLayout.addView(summaryView, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        paintView.invalidate();

        return rowLayout;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }



    @Override
    public int getItemViewType(int position)
    {
        return 0;
    }


    @Override
    public void onClick(View v)
    {
        if(v == _newButton)
        {
            System.out.println("MADE IT TO HERE //////////////////////////////////////////////////////////////////////////////////////");
            Intent mainIntent = new Intent();
            mainIntent.setClass(this, MainActivity.class);
            mainIntent.putExtra("Return Edit", "" + Color.RED + " " + Gallery.getInstance().getDrawingCount());
            startActivity(mainIntent);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Intent mainIntent = new Intent();
        mainIntent.setClass(this, MainActivity.class);
        mainIntent.putExtra("New Painting", "" + Color.RED + " " + position);
        startActivity(mainIntent);
    }
}
