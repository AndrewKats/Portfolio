package com.example.andrewkats.codeymobile;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/*
 * Andrew Katsanevas
 * 12/19/2016
 * 4530 FINAL PROJECT
 * CodeyMobile
 * The Educational Coding Game
 *
 * TESTED ON: Samsung Galaxy S7 (hardware), Nexus 6P (emulated), Nexus 7 (2012) (emulated), Nexus 10 (emulated)
 *
 * Check out "Help and Credits" in the options menu if there is any confusion.
 */
public class TitleActivity extends AppCompatActivity implements View.OnClickListener
{

    Button _playButton = null;
    Button _scoresButton = null;
    Button _optionsButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LinearLayout masterLayout = new LinearLayout(this);
        masterLayout.setOrientation(LinearLayout.HORIZONTAL);

        ImageView codeyImage = new ImageView(this);
        codeyImage.setImageResource(R.drawable.codeybig);
        masterLayout.addView(codeyImage, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        LinearLayout menuLayout = new LinearLayout(this);
        menuLayout.setOrientation(LinearLayout.VERTICAL);

        TextView codeyTitle = new TextView(this);
        codeyTitle.setText("CODEY");
        codeyTitle.setTextSize(40);
        codeyTitle.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams menuParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);

        menuLayout.addView(codeyTitle, menuParams);

        _playButton = new Button(this);
        _playButton.setText("Play");
        _playButton.setOnClickListener(this);
        menuLayout.addView(_playButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        _scoresButton = new Button(this);
        _scoresButton.setText("Scores");
        _scoresButton.setOnClickListener(this);
        menuLayout.addView(_scoresButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        _optionsButton = new Button(this);
        _optionsButton.setText("Options");
        _optionsButton.setOnClickListener(this);
        menuLayout.addView(_optionsButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        //menuLayout.setBackgroundColor(Color.BLUE);
        masterLayout.addView(menuLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        setContentView(masterLayout);


    }

    @Override
    public void onClick(View v)
    {
        System.out.println("MADE IT <---------------------------------------");

        if(v == _playButton)
        {
            Intent playIntent = new Intent();
            playIntent.setClass(this, FileSelectActivity.class);
            startActivity(playIntent);
        }

        if(v == _scoresButton)
        {
            Intent scoreIntent = new Intent();
            scoreIntent.setClass(this, ScoreSelectActivity.class);
            startActivity(scoreIntent);
        }

        if(v == _optionsButton)
        {
            Intent optionsIntent = new Intent();
            optionsIntent.setClass(this, OptionsActivity.class);
            startActivity(optionsIntent);
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
