package com.example.andrewkats.codeymobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by AndrewKats on 12/17/2016.
 */
public class WinLevelActivity extends AppCompatActivity implements View.OnClickListener
{
    int _levelId = -1;
    int _score = -1;
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

        if(getIntent().getExtras() != null)
        {
            _levelId = getIntent().getExtras().getInt("Level");
            _score = getIntent().getExtras().getInt("Score");
            _playerId = getIntent().getExtras().getInt("Player");
        }

        _playerId = dataModel.currentPlayerId;

        System.out.println("THIS IS THAT PLAYER ID: " + _playerId);

        int oldScore = DataModel.getInstance().getLevelScore(_playerId, _levelId-1);
        DataModel.getInstance().playerStats.get(_playerId).levelScores[_levelId-1] = Math.max(_score, oldScore);

        DataModel.saveData(this);

        LinearLayout masterLayout = new LinearLayout(this);
        masterLayout.setOrientation(LinearLayout.HORIZONTAL);

        ImageView codeyImage = new ImageView(this);
        codeyImage.setImageResource(R.drawable.victoryhd);
        masterLayout.addView(codeyImage, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));


        LinearLayout textLayout = new LinearLayout(this);
        textLayout.setOrientation(LinearLayout.VERTICAL);

        TextView victoryText = new TextView(this);
        victoryText.setText("VICTORY!");
        victoryText.setTextSize(60);
        victoryText.setGravity(Gravity.CENTER_HORIZONTAL);
        textLayout.addView(victoryText, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));



        TextView infoText = new TextView(this);
        infoText.setText("Level " + _levelId
                + "\n" + "Score: " + _score);
        infoText.setTextSize(30);
        infoText.setGravity(Gravity.CENTER_HORIZONTAL);
        textLayout.addView(infoText, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));


        Button backButton = new Button(this);
        backButton.setText("LEVEL SELECT");
        backButton.setOnClickListener(this);
        textLayout.addView(backButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));


        masterLayout.addView(textLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));


        setContentView(masterLayout);
    }

    @Override
    public void onClick(View v)
    {
        Intent winIntent = new Intent();
        winIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        winIntent.setClass(this, LevelSelectActivity.class);
        winIntent.putExtra("PlayerId", "" + _playerId);
        startActivity(winIntent);
    }

    @Override
    public void onBackPressed()
    {
        Intent winIntent = new Intent();
        winIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        winIntent.setClass(this, LevelSelectActivity.class);
        winIntent.putExtra("PlayerId", "" + _playerId);
        startActivity(winIntent);
    }
}
