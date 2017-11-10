package com.example.andrewkats.codeymobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by AndrewKats on 12/16/2016.
 */
public class ScoreSelectActivity extends AppCompatActivity implements View.OnClickListener
{
    Button overallButton = null;
    SquareButton button1 = null;
    SquareButton button2 = null;
    SquareButton button3 = null;
    SquareButton button4 = null;
    SquareButton button5 = null;
    SquareButton button6 = null;
    SquareButton button7 = null;
    SquareButton button8 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        LinearLayout masterLayout = new LinearLayout(this);
        masterLayout.setOrientation(LinearLayout.VERTICAL);


        overallButton = new Button(this);
        overallButton.setText("Overall");
        overallButton.setOnClickListener(this);
        masterLayout.addView(overallButton, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));


        LinearLayout firstRow = new LinearLayout(this);
        firstRow.setOrientation(LinearLayout.HORIZONTAL);

        button1 = new SquareButton(this);
        button1.setText("Lvl 1");
        button1.setOnClickListener(this);
        firstRow.addView(button1, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        button2 = new SquareButton(this);
        button2.setText("Lvl 2");
        button2.setOnClickListener(this);
        firstRow.addView(button2, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        button3 = new SquareButton(this);
        button3.setText("Lvl 3");
        button3.setOnClickListener(this);
        firstRow.addView(button3, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        button4 = new SquareButton(this);
        button4.setText("Lvl 4");
        button4.setOnClickListener(this);
        firstRow.addView(button4, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        firstRow.setGravity(Gravity.CENTER_HORIZONTAL);
        masterLayout.addView(firstRow, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        LinearLayout secondRow = new LinearLayout(this);
        secondRow.setOrientation(LinearLayout.HORIZONTAL);

        button5 = new SquareButton(this);
        button5.setText("Lvl 5");
        button5.setOnClickListener(this);
        secondRow.addView(button5, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        button6 = new SquareButton(this);
        button6.setText("Lvl 6");
        button6.setOnClickListener(this);
        secondRow.addView(button6, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        button7 = new SquareButton(this);
        button7.setText("Lvl 7");
        button7.setOnClickListener(this);
        secondRow.addView(button7, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        button8 = new SquareButton(this);
        button8.setText("Lvl 8");
        button8.setOnClickListener(this);
        secondRow.addView(button8, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        secondRow.setGravity(Gravity.CENTER_HORIZONTAL);
        masterLayout.addView(secondRow, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));


        setContentView(masterLayout);

    }

    @Override
    public void onClick(View v)
    {
        if(v == overallButton)
        {
            goToScores(0);
        }
        else if(v == button1)
        {
            goToScores(1);
        }
        else if(v == button2)
        {
            goToScores(2);
        }
        else if(v == button3)
        {
            goToScores(3);
        }
        else if(v == button4)
        {
            goToScores(4);
        }
        else if(v == button5)
        {
            goToScores(5);
        }
        else if(v == button6)
        {
            goToScores(6);
        }
        else if(v == button7)
        {
            goToScores(7);
        }
        else if(v == button8)
        {
            goToScores(8);
        }
    }

    public void goToScores(int level)
    {
        Intent scoreIntent = new Intent();
        scoreIntent.setClass(this, HighScoresActivity.class);
        scoreIntent.putExtra("Level", level);
        startActivity(scoreIntent);
    }
}
