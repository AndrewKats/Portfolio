package com.example.andrewkats.networkbattleship;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Timer;
import java.util.TimerTask;


public class PreGameFragment extends Fragment
{

    public interface OnEnterGameListener
    {
        void onEnterGame(String gameName, String playerName) throws IOException;
    }

    public interface OnSaveStateListener
    {
        void onSaveState(int state) throws IOException;
    }

    OnSaveStateListener _saveStateListener = null;
    OnEnterGameListener _enterGameListener = null;
    GameGridFragment.OnChooseLaunchListener _chooseLaunchListener = null;

    GameCollection _games = null;

    GameDetails _details = null;

    Button backButton = null;

    EditText gameNameEdit = null;
    EditText playerNameEdit = null;

    Timer timer = null;

    TextView infoText = null;

    LinearLayout vertLayout = null;



    public static PreGameFragment newInstance()
    {
        return new PreGameFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        try
        {
            _saveStateListener.onSaveState(1);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        _games = GameCollection.getInstance();

        vertLayout = new LinearLayout(getActivity());
        vertLayout.setOrientation(LinearLayout.VERTICAL);


        if(!((MainActivity)getActivity()).isTablet)
        {
            backButton = new Button(getActivity());
            backButton.setText("Back to Game List");
            backButton.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    try
                    {
                        _chooseLaunchListener.onChooseLaunch(null, null);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            vertLayout.addView(backButton);
        }



        infoText = new TextView(getActivity());
        infoText.setTextSize(20);
        infoText.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);

        if(_games.currentGameStatus.equals("new"))
        {
            infoText.setText("Create a new game");
        }
        else if(_games.myGameHolder.myGames.contains(_games.currentGameId))
        {
            if(_games.currentGameStatus.equals("WAITING"))
            {
                infoText.setText("Waiting for someone to join your game");
            }
            else if(_games.currentGameStatus.equals("PLAYING"))
            {
                getDetails();
            }
            else if(_games.currentGameStatus.equals("DONE"))
            {
                getDetails();
            }
        }
        else if(_games.currentGameStatus.equals("WAITING"))
        {
            infoText.setText("Join game");
        }
        // Someone else's in progress game
        else if(_games.currentGameStatus.equals("PLAYING"))
        {
            getDetails();
        }
        // Someone else's done game
        else if(_games.currentGameStatus.equals("DONE"))
        {
            getDetails();
        }
        else if(_games.currentGameStatus.equals(""))
        {
            infoText.setText("SOMETHING WENT WRONG");
        }

        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2);
        textParams.gravity = Gravity.CENTER_HORIZONTAL;
        vertLayout.addView(infoText, textParams);



        if(_games.currentGameStatus.equals("new"))
        {
            TextView gameNameLabel = new TextView(getActivity());
            gameNameLabel.setText("Game Name:");
            vertLayout.addView(gameNameLabel);

            gameNameEdit = new EditText(getActivity());
            gameNameEdit.setHint("Game Name");
            vertLayout.addView(gameNameEdit);


            TextView playerNameLabel = new TextView(getActivity());
            playerNameLabel.setText("Player Name:");
            vertLayout.addView(playerNameLabel);

            playerNameEdit = new EditText(getActivity());
            playerNameEdit.setHint("Player Name");
            vertLayout.addView(playerNameEdit);
        }
        else if(_games.currentGameStatus.equals("WAITING"))
        {
            if(!_games.myGameHolder.myGames.contains(_games.currentGameId))
            {
                TextView playerNameLabel = new TextView(getActivity());
                playerNameLabel.setText("Player Name:");
                vertLayout.addView(playerNameLabel);

                playerNameEdit = new EditText(getActivity());
                playerNameEdit.setHint("Player Name");
                vertLayout.addView(playerNameEdit);
            }
        }




        Button continueButton = new Button(getActivity());

        /*
        if(_games.getGame(_games.currentGame).isGameOver())
        {
            continueButton.setText("Review Game");
        }
        else
        {
            continueButton.setText("Player " + _games.getGame(_games.currentGame)._currentTurn + "'s Turn");
        }
        */
        continueButton.setText("Continue");

        continueButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    // New game
                    if(gameNameEdit != null && playerNameEdit != null)
                    {
                        _enterGameListener.onEnterGame(gameNameEdit.getText().toString(), playerNameEdit.getText().toString());
                    }
                    else
                    {
                        // Join
                        if(gameNameEdit == null && playerNameEdit != null)
                        {
                            _enterGameListener.onEnterGame(null, playerNameEdit.getText().toString());
                        }
                        // Other cases
                        else
                        {
                            _enterGameListener.onEnterGame(null, null);
                        }
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);

        if((_games.myGameHolder.myGames.contains(_games.currentGameId) && (_games.currentGameStatus.equals("PLAYING") || _games.currentGameStatus.equals("DONE")))
                || (_games.currentGameStatus.equals("WAITING"))
                || _games.currentGameStatus.equals("new"))
        {
            vertLayout.addView(continueButton, buttonParams);
        }


        return vertLayout;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        _enterGameListener = (OnEnterGameListener) activity;
        _chooseLaunchListener = (GameGridFragment.OnChooseLaunchListener) activity;
        _saveStateListener = (OnSaveStateListener) activity;
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
    }


    public void getDetails()
    {
        final Handler handler = new Handler();
        timer = new Timer();
        TimerTask doAsyncTask = new TimerTask()
        {
            @Override
            public void run()
            {
                handler.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        NetworkManager.getInstance().getGameDetail(_games.currentGameId, new NetworkManager.GameDetailsListener()
                        {
                            @Override
                            public void gameReceived(boolean success, GameDetails gameDetails)
                            {
                                if(success)
                                {
                                    String infoString = "Game: " + gameDetails.name + "\n\n"
                                            + gameDetails.player1 + " vs. " + gameDetails.player2 + "\n\n"
                                            + "Missiles launched: " + gameDetails.missilesLaunched;
                                    if(gameDetails.winner.equals("IN PROGRESS"))
                                    {
                                        infoString += "\n\n\n" + "In Progress";
                                    }
                                    else
                                    {
                                        infoString += "\n\n\n" + gameDetails.winner + " won!";
                                    }
                                    infoText.setText(infoString);
                                }
                            }
                        });
                    }
                });
               // vertLayout.invalidate();
            }
        };
        timer.schedule(doAsyncTask, 0, 500);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        if(timer != null)
        {
            timer.cancel();
        }
    }
}
