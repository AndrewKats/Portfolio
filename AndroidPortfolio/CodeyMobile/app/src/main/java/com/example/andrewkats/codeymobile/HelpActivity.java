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
public class HelpActivity  extends AppCompatActivity implements View.OnClickListener
{
    Button backButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        LinearLayout masterLayout = new LinearLayout(this);
        masterLayout.setOrientation(LinearLayout.VERTICAL);

        TextView helpText = new TextView(this);
        helpText.setText("Codey is an educational game that helps teach you simple coding concepts. The goal is to get Codey the code-bot to the finish flag."
            + " To accomplish this, you must add movement commands like walk and jump to your queue, then run it to see what happens. But if you want more points, "
            + "you may want to learn how a loop works. You can add up to 3 commands to a loop, and also tell the loop how many times to run, then click the + button to queue it. Some"
            + " levels will even make you use conditional statements to make it out alive. Add a conditional to help Codey prepare for rain or fire (due to budget constraints,"
            + " Codey is neither fireproof nor waterproof, and has thus been equipped with an umbrella and a welder's mask). if(raining){useUmbrella}else{useWeldersMask}. These items last 2 moves. "
            + "Hopefully this makes coding pretty fun!"
            + "\n\n Made by Andrew Katsanevas\n");
        helpText.setTextSize(16);
        masterLayout.addView(helpText);

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
