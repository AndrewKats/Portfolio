package com.example.andrewkats.codeymobile;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by AndrewKats on 12/16/2016.
 */
public class DataModel implements Serializable
{
    public static DataModel _Instance = null;

    ArrayList<PlayerStats> playerStats = new ArrayList<PlayerStats>();

    int currentPlayerId=-1;

    public static DataModel getInstance()
    {
        if(_Instance == null)
        {

            synchronized (new Object())
            {
                _Instance = new DataModel();
            }

        }
        return _Instance;
    }

    private DataModel(){}

    int getLevelOn(int playerPosition)
    {
        PlayerStats stats = playerStats.get(playerPosition);
        for(int i=0; i<stats.levelScores.length; i++)
        {
            if(stats.levelScores[i] == 0)
            {
                return i+1;
            }
        }
        return -1;
    }

    int getScore(int playerPosition)
    {
        PlayerStats stats = playerStats.get(playerPosition);

        int sum = 0;
        for(int levelScore : stats.levelScores)
        {
            sum += levelScore;
        }

        return sum;
    }

    int getLevelScore(int playerPosition, int levelId)
    {
        PlayerStats stats = playerStats.get(playerPosition);

        return stats.levelScores[levelId];
    }

    static void saveData(Context context)
    {
        Gson gson = new Gson();
        String jsonString = gson.toJson(DataModel.getInstance());

        SharedPreferences.Editor editor = context.getSharedPreferences("MY_DATA", context.MODE_PRIVATE).edit();
        editor.putString("MyData", jsonString);
        editor.commit();

        System.out.println("Here's the JSON String: " + jsonString + " <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

    }

    static DataModel retrieveData(Context context) throws IOException, ClassNotFoundException
    {

        SharedPreferences preferences = context.getSharedPreferences("MY_DATA", context.MODE_PRIVATE);
        String retrieved = preferences.getString("MyData", null);

        System.out.println("Retrieved it: " + retrieved + "  mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");

        Gson gson = new Gson();
        DataModel retrievedModel = gson.fromJson(retrieved, DataModel.class);

        System.out.println("HERE'S THE FUCKIN SIZE: " + retrievedModel.playerStats.size());

        return retrievedModel;

    }
}
