package com.example.andrewkats.codeymobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by AndrewKats on 12/16/2016.
 */
public class OptionsActivity extends AppCompatActivity implements View.OnClickListener
{
    Button changeFileNameButton = null;
    Button deleteFileButton = null;
    Button deleteAllFilesButton = null;
    Button backButton = null;
    Button helpButton = null;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LinearLayout masterLayout = new LinearLayout(this);
        masterLayout.setOrientation(LinearLayout.VERTICAL);

        TextView optionsText = new TextView(this);
        optionsText.setText("OPTIONS");
        optionsText.setTextSize(20);
        optionsText.setGravity(Gravity.CENTER_HORIZONTAL);
        masterLayout.addView(optionsText);

        changeFileNameButton = new Button(this);
        changeFileNameButton.setText("Change file name");
        changeFileNameButton.setOnClickListener(this);
        //masterLayout.addView(changeFileNameButton);

        deleteFileButton = new Button(this);
        deleteFileButton.setText("Delete a file");
        deleteFileButton.setOnClickListener(this);
        masterLayout.addView(deleteFileButton);

        deleteAllFilesButton = new Button(this);
        deleteAllFilesButton.setText("Delete all files");
        deleteAllFilesButton.setOnClickListener(this);
        masterLayout.addView(deleteAllFilesButton);

        helpButton = new Button(this);
        helpButton.setText("Help and Credits");
        helpButton.setOnClickListener(this);
        masterLayout.addView(helpButton);

        backButton = new Button(this);
        backButton.setText("Back to title screen");
        backButton.setOnClickListener(this);
        masterLayout.addView(backButton);



        setContentView(masterLayout);
    }

    @Override
    public void onClick(View v)
    {
        if(v == deleteFileButton)
        {
            Intent deleteIntent = new Intent();
            deleteIntent.setClass(this, DeleteFileActivity.class);
            startActivity(deleteIntent);
        }
        else if(v == deleteAllFilesButton)
        {
            Intent deleteIntent = new Intent();
            deleteIntent.setClass(this, DeleteAllFilesActivity.class);
            startActivity(deleteIntent);
        }
        else if(v == backButton)
        {
            Intent backIntent = new Intent();
            backIntent.setClass(this, TitleActivity.class);
            startActivity(backIntent);
        }
        else if(v == helpButton)
        {
            Intent helpIntent = new Intent();
            helpIntent.setClass(this, HelpActivity.class);
            startActivity(helpIntent);
        }
    }

    @Override
    public void onBackPressed()
    {
        Intent backIntent = new Intent();
        backIntent.setClass(this, TitleActivity.class);
        startActivity(backIntent);
    }
}
