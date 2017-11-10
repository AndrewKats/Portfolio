package com.example.andrewkats.codeymobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by AndrewKats on 12/19/2016.
 */
public class FileDeletedActivity extends AppCompatActivity implements View.OnClickListener
{
    Button backButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LinearLayout masterLayout = new LinearLayout(this);
        masterLayout.setOrientation(LinearLayout.VERTICAL);

        TextView deleteText = new TextView(this);
        deleteText.setText("File Deleted");
        deleteText.setTextSize(30);
        masterLayout.addView(deleteText);

        backButton = new Button(this);
        backButton.setText("Back to options");
        backButton.setOnClickListener(this);
        masterLayout.addView(backButton);


        setContentView(masterLayout);
    }

    @Override
    public void onClick(View v)
    {
        Intent optionsIntent = new Intent();
        optionsIntent.setClass(this, OptionsActivity.class);
        startActivity(optionsIntent);
    }
}
