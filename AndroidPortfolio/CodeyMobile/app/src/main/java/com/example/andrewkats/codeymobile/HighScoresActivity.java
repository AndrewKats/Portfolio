package com.example.andrewkats.codeymobile;

import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by AndrewKats on 12/19/2016.
 */
public class HighScoresActivity extends AppCompatActivity implements ListAdapter
{

    int _levelId = -1;

    DataModel dataModel = null;

    ArrayList<PlayerStats> _stats = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getIntent().getExtras() != null)
        {
            _levelId = getIntent().getExtras().getInt("Level");
        }

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

        _stats = new ArrayList<PlayerStats>();

        for(PlayerStats player : dataModel.playerStats)
        {
            if(_levelId>0)
            {
                if (player.levelScores[_levelId - 1] != 0)
                {
                    _stats.add(player);
                }
            }
        }

        if(_levelId <= 0)
        {
            for(int i=0; i<dataModel.playerStats.size(); i++)
            {
                if (dataModel.getScore(i) > 0)
                {
                    _stats.add(dataModel.playerStats.get(i));
                }
            }
        }

        System.out.println("SCORE SIZE: " + _stats.size());


        if(_levelId > 0)
        {
            Collections.sort(_stats, new Comparator<PlayerStats>()
            {
                @Override
                public int compare(PlayerStats lhs, PlayerStats rhs)
                {
                    if (lhs.levelScores[_levelId-1] > rhs.levelScores[_levelId - 1])
                    {
                        return -1;
                    }
                    else if (lhs.levelScores[_levelId-1] < rhs.levelScores[_levelId - 1])
                    {
                        return 1;
                    }
                    else
                    {
                        return 0;
                    }
                }
            });
        }
        else
        {
            Collections.sort(_stats, new Comparator<PlayerStats>()
            {
                @Override
                public int compare(PlayerStats lhs, PlayerStats rhs)
                {
                    int lhsSum=0;
                    for(int l : lhs.levelScores)
                    {
                        lhsSum += l;
                    }

                    int rhsSum=0;
                    for(int r : rhs.levelScores)
                    {
                        rhsSum += r;
                    }

                    if (lhsSum > rhsSum)
                    {
                        return -1;
                    }
                    else if (lhsSum < rhsSum)
                    {
                        return 1;
                    }
                    else
                    {
                        return 0;
                    }
                }
            });
        }

        LinearLayout masterLayout = new LinearLayout(this);
        masterLayout.setOrientation(LinearLayout.VERTICAL);

        TextView titleText = new TextView(this);
        titleText.setTextSize(24);
        titleText.setGravity(Gravity.CENTER_HORIZONTAL);

        if(_levelId == 0)
        {
            titleText.setText("Overall Scores");
        }
        else
        {
            titleText.setText("Level " + _levelId + " Scores");
        }

        masterLayout.addView(titleText);

        ListView scoreList = new ListView(this);
        scoreList.setAdapter(this);


        masterLayout.addView(scoreList);

        setContentView(masterLayout);
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
    public int getCount()
    {
        return _stats.size();
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
        int place = position + 1;
        TextView infoText = new TextView(this);
        infoText.setTextSize(20);

        if(_levelId==0)
        {
            int overall = 0;
            for(int s : _stats.get(position).levelScores)
            {
                overall += s;
            }
            infoText.setText(place + " " + _stats.get(position).name + " " + overall);
        }
        else
        {
            infoText.setText(place + " " + _stats.get(position).name + " " + _stats.get(position).levelScores[_levelId-1]);
        }

        return infoText;
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
        return _stats.isEmpty();
    }
}
