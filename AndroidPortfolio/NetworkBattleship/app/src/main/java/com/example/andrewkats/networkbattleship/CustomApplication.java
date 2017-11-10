package com.example.andrewkats.networkbattleship;

import android.app.Application;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * Created by AndrewKats on 10/24/2016.
 */
public class CustomApplication extends Application
{
    GameCollection _games = null;

    @Override
    public void onCreate()
    {
        /*
        try
        {
            retrieveGames();
        }
        catch (Exception e)
        {
            _games=GameCollection.getInstance();
            System.out.println("Games file not found");
            e.printStackTrace();
        }

        //_games.playing = false;
        try
        {
            saveTheGames();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        */

        //_games=GameCollection.getInstance();
        //_games.gameState=0;
        super.onCreate();
    }

    @Override
    public void onTerminate()
    {
        /*
        _games = GameCollection.getInstance();
        _games.playing = false;
        _games.gameState=0;
        try
        {
            saveTheGames();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("FAAAAAAAAAAAIIIIIIIIIIIIIIIIIIIILLLLLLLLLLLLLEEEEEDDDDDDDDDDDDDDDDDDDDDD TOOOOOOOOOOOOOOOOOOOOOOOOOOOOO WRITEEEEEEEEEEEEEEEEEEEEE");
        }
        */

        super.onTerminate();
    }


    public void saveTheGames() throws IOException
    {
        String fileName = "collection.dat";
        ObjectOutput out = null;
        try
        {
            out = new ObjectOutputStream(new FileOutputStream(new File(MainActivity.main.getFilesDir(), "")+ File.separator+fileName));


            /*
            FileOutputStream os = openFileOutput("collection.dat", MODE_PRIVATE);
            ObjectOutputStream objout = new ObjectOutputStream(os);
            objout.writeObject(GameCollection.getInstance());
            objout.close();
            */
        }
        catch(Exception e)
        {
            Log.i("File", "Alright we failed to write <++++++++++++++++++++++++++++++++++++++");
            throw e;
        }
        /*
        try
        {
            FileOutputStream os = openFileOutput("collection.dat", MODE_PRIVATE);
            ObjectOutputStream objout = new ObjectOutputStream(os);
            objout.writeObject(GameCollection.getInstance());
            objout.close();
        }
        catch(Exception e)
        {
            Log.i("File", "Alright we failed to write <++++++++++++++++++++++++++++++++++++++");
            throw e;
        }
        */
    }

    public void retrieveGames() throws IOException, ClassNotFoundException
    {
        ObjectInputStream input;
        String filename = "collection.dat";

        try
        {
            input = new ObjectInputStream(new FileInputStream(new File(new File(MainActivity.main.getFilesDir(), "")+File.separator+filename)));
            _games = (GameCollection) input.readObject();
            input.close();

            /*
            FileInputStream in = openFileInput("collection.dat");
            ObjectInputStream objin = new ObjectInputStream(in);
            GameCollection g = (GameCollection)objin.readObject();
            _games = GameCollection.getInstance();
            _games.games = g.games;
            _games.playing = g.playing;
            _games.currentGame=g.currentGame;
            */
        }
        catch(Exception e)
        {
            Log.i("File", "Ok we failed to read <***************************************************");
            throw e;
        }
        /*
        try
        {
            FileInputStream in = openFileInput("collection.dat");
            ObjectInputStream objin = new ObjectInputStream(in);
            GameCollection g = (GameCollection)objin.readObject();
            _games = GameCollection.getInstance();
            //_games.games = g.games;
            _games.playing = g.playing;
            _games.currentGame=g.currentGame;
        }
        catch(Exception e)
        {
            Log.i("File", "Ok we failed to read <***************************************************");
            throw e;
        }
        */
    }

}
