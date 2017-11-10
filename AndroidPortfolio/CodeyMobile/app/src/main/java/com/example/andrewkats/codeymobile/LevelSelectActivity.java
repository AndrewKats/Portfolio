package com.example.andrewkats.codeymobile;

import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by AndrewKats on 12/16/2016.
 */
public class LevelSelectActivity extends AppCompatActivity implements ListAdapter, AdapterView.OnItemClickListener
{
    int _playerId = -1;

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
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            if (getIntent().getExtras() != null)
            {
                _playerId = getIntent().getExtras().getInt("PlayerId");
            }
        }
        catch(Exception e)
        {
            System.out.println("failed to read intent");
        }


        _playerId = dataModel.currentPlayerId;

        GridView levelGrid = new GridView(this);
        levelGrid.setNumColumns(2);
        levelGrid.setHorizontalSpacing(100);
        levelGrid.setVerticalSpacing(100);
        levelGrid.setAdapter(this);
        levelGrid.setOnItemClickListener(this);

        setContentView(levelGrid);

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
        return 8;
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
        RelativeLayout thumbLayout = new RelativeLayout(this);
        //thumbLayout.setOrientation(LinearLayout.VERTICAL);

        TextView summaryText = new TextView(this);
        int levelNumber = position+1;
        summaryText.setText("Level " + levelNumber + "     " + "High Score: " + DataModel.getInstance().getLevelScore(_playerId, position));
        summaryText.setTextSize(20);
        //summaryText.setBackgroundColor(Color.BLUE);

        ImageView preview = new ImageView(this);
        if(levelNumber==1)
        {
            preview.setImageResource(R.drawable.level1);
        }
        else if(levelNumber==2)
        {
            preview.setImageResource(R.drawable.level2);
        }
        else if(levelNumber==3)
        {
            preview.setImageResource(R.drawable.level3);
        }
        else if(levelNumber==4)
        {
            preview.setImageResource(R.drawable.level4);
        }
        else if(levelNumber==5)
        {
            preview.setImageResource(R.drawable.level5);
        }
        else if(levelNumber==6)
        {
            preview.setImageResource(R.drawable.level6);
        }
        else if(levelNumber==7)
        {
            preview.setImageResource(R.drawable.level7);
        }
        else if(levelNumber==8)
        {
            preview.setImageResource(R.drawable.level8);
        }
       // preview.setBackgroundColor(Color.MAGENTA);


       // thumbLayout.addView(preview, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2));

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        summaryText.setId(1);
        preview.setId(2);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 1);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, 2);
        thumbLayout.addView(preview, params);
        thumbLayout.addView(summaryText, params);

       // thumbLayout.setBackgroundColor(Color.RED);


        thumbLayout.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 620));
        return thumbLayout;
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
        if(DataModel.getInstance().getLevelOn(_playerId) < position+1)
        {
            Toast.makeText(this, "Complete previous levels to unlock this level", Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent chooseLevelIntent = new Intent();
            chooseLevelIntent.setClass(this, PlayGameActivity.class);
            chooseLevelIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            chooseLevelIntent.putExtra("Level", position + 1);
            chooseLevelIntent.putExtra("Player", _playerId);
            startActivity(chooseLevelIntent);
        }
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();

        Intent winIntent = new Intent();
        winIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        winIntent.setClass(this, FileSelectActivity.class);
        startActivity(winIntent);
    }
}
