package com.example.andrewkats.codeymobile;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by AndrewKats on 12/16/2016.
 */
public class CreateFileActivity extends AppCompatActivity implements View.OnClickListener
{
    Button _enterButton = null;
    EditText _editName = null;
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

        LinearLayout masterLayout = new LinearLayout(this);
        masterLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout vertLayout = new LinearLayout(this);
        vertLayout.setOrientation(LinearLayout.VERTICAL);

        TextView enterText = new TextView(this);
        enterText.setText("Enter your name");
        enterText.setTextSize(40);
        vertLayout.addView(enterText,  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        _editName = new EditText(this);
        _editName.setHint("Name");
        _editName.setTextSize(40);
        vertLayout.addView(_editName,  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        _enterButton = new Button(this);
        _enterButton.setText("Create");
        _enterButton.setOnClickListener(this);
        vertLayout.addView(_enterButton,  new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        masterLayout.addView(vertLayout, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1));

        setContentView(masterLayout);
    }

    @Override
    public void onClick(View v)
    {
        if(v == _enterButton)
        {
            String nameText = _editName.getText().toString();
            System.out.println("Name: " + nameText + " Length: " + nameText.length());
            if(nameText.trim().equals(""))
            {
                Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
                return;
            }
            PlayerStats newPlayer = new PlayerStats(nameText);
            DataModel.getInstance().playerStats.add(newPlayer);
            dataModel.currentPlayerId =  DataModel.getInstance().playerStats.size()-1;
            DataModel.saveData(this);



            Intent levelIntent = new Intent();
            levelIntent.setClass(this, LevelSelectActivity.class);
            levelIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            levelIntent.putExtra("PlayerId", DataModel.getInstance().playerStats.size()-1);
            startActivity(levelIntent);
        }
    }
}
