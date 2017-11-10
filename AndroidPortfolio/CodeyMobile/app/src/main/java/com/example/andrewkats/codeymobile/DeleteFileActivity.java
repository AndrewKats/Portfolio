package com.example.andrewkats.codeymobile;

import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by AndrewKats on 12/19/2016.
 */
public class DeleteFileActivity  extends AppCompatActivity implements ListAdapter, View.OnClickListener, AdapterView.OnItemClickListener
{
    Button backButton = null;
    DataModel dataModel = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        dataModel = DataModel.getInstance();

        try
        {
            dataModel = DataModel.retrieveData(this);
            DataModel._Instance = dataModel;
            System.out.println("MADE IT TO THIS SPECIFIC POINT");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        LinearLayout masterLayout = new LinearLayout(this);
        masterLayout.setOrientation(LinearLayout.VERTICAL);

        TextView selectText = new TextView(this);
        selectText.setText("Select a file to delete");
        selectText.setTextSize(30);
        selectText.setGravity(Gravity.CENTER);
        masterLayout.addView(selectText);

        backButton = new Button(this);
        backButton.setText("Back to options");
        backButton.setOnClickListener(this);
        masterLayout.addView(backButton);

        ListView fileList = new ListView(this);
        fileList.setAdapter(this);
        fileList.setOnItemClickListener(this);
        masterLayout.addView(fileList);

        setContentView(masterLayout);
    }

    @Override
    public void onClick(View v)
    {
        if(v == backButton)
        {
            Intent optionsIntent = new Intent();
            optionsIntent.setClass(this, OptionsActivity.class);
            startActivity(optionsIntent);
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
    public void registerDataSetObserver(DataSetObserver observer) {}

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {}

    @Override
    public int getCount()
    {
        System.out.println("HERE'S THE SIZE OF THIS THING: " + DataModel.getInstance().playerStats.size());
        return DataModel.getInstance().playerStats.size();
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
    public View getView(int position, View convertView, ViewGroup parent)
    {
        String completion = "";
        if(DataModel.getInstance().getLevelOn(position) == -1)
        {
            completion = "COMPLETE";
        }
        else
        {
            completion = "" + DataModel.getInstance().getLevelOn(position);
        }

        System.out.println();
        TextView playerSummary = new TextView(this);
        playerSummary.setTextSize(16);
        playerSummary.setText("File " + position + " (" + DataModel.getInstance().playerStats.get(position).name + ")"
                + "\n" + "Level " + completion
                + "\n" + "Score: " + DataModel.getInstance().getScore(position));

        return playerSummary;
    }

    @Override
    public int getItemViewType(int position)
    {
        return 0;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public boolean isEmpty()
    {
        return false;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        dataModel.playerStats.remove(position);
        dataModel.saveData(this);


        Intent levelIntent = new Intent();
        levelIntent.setClass(this, FileDeletedActivity.class);
        startActivity(levelIntent);
    }
}
