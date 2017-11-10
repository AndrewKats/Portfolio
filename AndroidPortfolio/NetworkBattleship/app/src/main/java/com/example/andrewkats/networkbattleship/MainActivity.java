package com.example.andrewkats.networkbattleship;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.gson.Gson;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/*
 * Andrew Katsanevas
 * CS 4530
 * 11/18/2016
 * Tested on Galaxy S7, Nexus 6P, Nexus 7 (2012), Nexus 9
 */
public class MainActivity extends AppCompatActivity implements GameListFragment.OnGameChangedListener, GameGridFragment.OnChooseLaunchListener, PreGameFragment.OnEnterGameListener, GameGridFragment.OnSaveIdListener, GameGridFragment.OnSaveIdJoinListener, PreGameFragment.OnSaveStateListener
{
    public static int MASTER_FRAME_ID = 10;
    public static int DETAIL_FRAME_ID = 20;
    public static String GAME_LIST_FRAGMENT_TAG = "GameListFragment";
    public static String GAME_GRID_FRAGMENT_TAG = "GameGridFragment";
    public static String PREGAME_FRAGMENT_TAG = "PreGameFragment";
    public static String BEGIN_TABLET_TAG = "BeginFragmentTag";
    GameCollection _games;

    boolean isTablet = false;

    FrameLayout detailLayout;
    FrameLayout masterLayout;

    public static Activity main = null;

    Timer timer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        main = this;

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpHeight = displayMetrics.heightPixels / displayMetrics.density;
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        float realWidth = 0;
        if(dpHeight>dpWidth)
        {
            realWidth = dpWidth;
        }
        else
        {
            realWidth = dpHeight;
        }

        System.out.println("HERE'S THE WIDTH ------------------------> " + realWidth);

        if (realWidth>=450)
        {
            isTablet=true;
        }
        else
        {
            isTablet=false;
        }







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
                        if(_games.myGameHolder != null)
                        {
                            synchronized (_games.myGameHolder.myTurns)
                            {
                                for (String gameIdString : _games.myGameHolder.myGames)
                                {

                                    final int index = _games.myGameHolder.myGames.indexOf(gameIdString);
                                    String playerIdString = _games.myGameHolder.myPlayerIds.get(index);
                                    try
                                    {
                                        NetworkManager.getInstance().getTurn(playerIdString, gameIdString, new NetworkManager.getTurnListener()
                                        {
                                            @Override
                                            public void turnReceived(boolean success, boolean isYourTurn, String winner)
                                            {
                                                if (index < _games.myGameHolder.myTurns.size())
                                                {
                                                    if (success)
                                                    {
                                                        if (winner.equals("IN PROGRESS"))
                                                        {
                                                            if (isYourTurn)
                                                            {
                                                                _games.myGameHolder.myTurns.set(index, "Your Turn");
                                                            }
                                                            else
                                                            {
                                                                _games.myGameHolder.myTurns.set(index, "Not Your Turn");
                                                            }
                                                        }
                                                        else
                                                        {
                                                            _games.myGameHolder.myTurns.set(index, winner + " Won!");
                                                        }
                                                    }
                                                    else
                                                    {

                                                    }
                                                }
                                            }
                                        });
                                    }
                                    catch (IOException e)
                                    {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                });
            }
        };
        timer.schedule(doAsyncTask, 0, 500);








        if(isTablet)
        {
            LinearLayout tabletLayout = new LinearLayout(this);

            masterLayout = new FrameLayout(this);
            masterLayout.setId(MASTER_FRAME_ID);

            tabletLayout.addView(masterLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

            detailLayout = new FrameLayout(this);
            detailLayout.setId(DETAIL_FRAME_ID);

            tabletLayout.addView(detailLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 2));

            setContentView(tabletLayout);


            // FRAGMENT CODE
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            GameListFragment gameListFragment = (GameListFragment) getSupportFragmentManager().findFragmentByTag(GAME_LIST_FRAGMENT_TAG);
            GameGridFragment gameGridFragment = (GameGridFragment) getSupportFragmentManager().findFragmentByTag(GAME_GRID_FRAGMENT_TAG);
            PreGameFragment preGameFragment = (PreGameFragment) getSupportFragmentManager().findFragmentByTag(PREGAME_FRAGMENT_TAG);
            BeginTabletFragment beginTabletFragment = (BeginTabletFragment) getSupportFragmentManager().findFragmentByTag(BEGIN_TABLET_TAG);
            if (gameListFragment == null)
            {
                gameListFragment = GameListFragment.newInstance();
                gameGridFragment = GameGridFragment.newInstance();
                preGameFragment = PreGameFragment.newInstance();
                beginTabletFragment = BeginTabletFragment.newInstance();
                transaction.add(masterLayout.getId(), gameListFragment, GAME_LIST_FRAGMENT_TAG);
                if(_games.gameState==2)
                {
                    transaction.add(detailLayout.getId(), gameGridFragment, GAME_GRID_FRAGMENT_TAG);
                }
                else if(_games.gameState==1)
                {
                    transaction.add(detailLayout.getId(), preGameFragment, PREGAME_FRAGMENT_TAG);
                }
                else
                {
                    transaction.add(detailLayout.getId(), beginTabletFragment, BEGIN_TABLET_TAG);
                }
            }
            else
            {
                transaction.replace(masterLayout.getId(), gameListFragment);
                if(_games.gameState==2)
                {
                    if(gameGridFragment ==null)
                    {
                        gameGridFragment = GameGridFragment.newInstance();
                    }
                    transaction.replace(detailLayout.getId(), gameGridFragment);
                }
                else if(_games.gameState==1)
                {
                    if(preGameFragment ==null)
                    {
                        preGameFragment = PreGameFragment.newInstance();
                    }
                    transaction.replace(detailLayout.getId(), preGameFragment);
                }
                else
                {
                    if(beginTabletFragment == null)
                    {
                        beginTabletFragment = BeginTabletFragment.newInstance();
                    }
                    transaction.replace(detailLayout.getId(), beginTabletFragment);
                }
            }
            transaction.commit();
        }
        else
        {
            detailLayout = new FrameLayout(this);
            detailLayout.setId(DETAIL_FRAME_ID);

            setContentView(detailLayout);


            // FRAGMENT CODE
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            GameListFragment gameListFragment = (GameListFragment) getSupportFragmentManager().findFragmentByTag(GAME_LIST_FRAGMENT_TAG);
            GameGridFragment gameGridFragment = (GameGridFragment) getSupportFragmentManager().findFragmentByTag(GAME_GRID_FRAGMENT_TAG);
            PreGameFragment preGameFragment = (PreGameFragment) getSupportFragmentManager().findFragmentByTag(PREGAME_FRAGMENT_TAG);


            if(_games.gameState==0)
            {
                if(gameListFragment==null)
                {
                    gameListFragment = GameListFragment.newInstance();
                    transaction.add(detailLayout.getId(), gameListFragment, GAME_LIST_FRAGMENT_TAG);
                }
                else
                {
                    transaction.replace(detailLayout.getId(), gameListFragment);
                }
            }
            if(_games.gameState==1)
            {
                if(preGameFragment ==null)
                {
                    preGameFragment = PreGameFragment.newInstance();
                    transaction.replace(detailLayout.getId(), preGameFragment, PREGAME_FRAGMENT_TAG);
                }
                else
                {
                    transaction.replace(detailLayout.getId(), preGameFragment);
                }
            }
            if(_games.gameState==2)
            {
                if(gameGridFragment==null)
                {
                    gameGridFragment = GameGridFragment.newInstance();
                    transaction.replace(detailLayout.getId(), gameGridFragment, GAME_GRID_FRAGMENT_TAG);
                }
                else
                {
                    transaction.replace(detailLayout.getId(), gameGridFragment);
                }
            }

            transaction.commit();
        }
    }


    @Override
    public void onGameChanged(String gameId, String status) throws IOException
    {
        _games.playing = true;
        saveGames();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        GameListFragment gameListFragment = (GameListFragment) getSupportFragmentManager().findFragmentByTag(GAME_LIST_FRAGMENT_TAG);
        PreGameFragment preGameFragment = (PreGameFragment) getSupportFragmentManager().findFragmentByTag(PREGAME_FRAGMENT_TAG);
        GameGridFragment gameGridFragment = (GameGridFragment) getSupportFragmentManager().findFragmentByTag(GAME_GRID_FRAGMENT_TAG);
        if(isTablet)
        {
            if (!gameId.equals(_games.currentGame))
            {
                //transaction.detach(gameListFragment);
                //transaction.attach(gameListFragment);
                gameListFragment._gamesListView.invalidateViews();
                if (gameId.equals("new"))
                {
                    _games.currentGameStatus = "new";
                    //_games.addNewGame();
                    //_games.currentGame = _games.getGameCount()-1;
                    //_games.getGame(_games.currentGame).bothRandomShips();
                    saveGames();
                }
                else
                {
                    _games.currentGameStatus = status;
                    _games.currentGameId = gameId;
                    saveGames();
                }


                if(preGameFragment ==null)
                {
                    preGameFragment = PreGameFragment.newInstance();
                   // _games.getGame(_games.currentGame).launching = false;
                    saveGames();
                    transaction.replace(detailLayout.getId(), preGameFragment, PREGAME_FRAGMENT_TAG);
                }
                else
                {
                    transaction.detach(preGameFragment);
                    transaction.attach(preGameFragment);
                    //_games.getGame(_games.currentGame).launching = false;
                    saveGames();
                    transaction.replace(detailLayout.getId(), preGameFragment);
                }
                transaction.commit();
            }
        }
        else
        {
            System.out.println("HERE'S THE STATUS WE'RE USING: " + status + " <---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------");
            if (gameId.equals("new"))
            {
                _games.currentGameStatus = "new";
                //_games.addNewGame();
                //_games.currentGame = _games.getGameCount()-1;
                //_games.getGame(_games.currentGame).bothRandomShips();
                saveGames();
            }
            else
            {
                _games.currentGameStatus = status;
                _games.currentGameId = gameId;
                saveGames();
            }


            if(preGameFragment ==null)
            {
                preGameFragment = PreGameFragment.newInstance();
                //_games.getGame(_games.currentGame).launching = false;
                //saveGames();
                transaction.replace(detailLayout.getId(), preGameFragment, PREGAME_FRAGMENT_TAG);
            }
            else
            {
                transaction.detach(preGameFragment);
                transaction.attach(preGameFragment);
                //_games.getGame(_games.currentGame).launching = false;
                //saveGames();
                transaction.replace(detailLayout.getId(), preGameFragment);
            }

            transaction.commit();
        }

    }


    @Override
    public void onChooseLaunch(GridButton[][] buttons, GridButton button) throws IOException
    {

        if (button == null)
        {
            _games.playing = false;
            saveGames();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            GameListFragment gameListFragment = (GameListFragment) getSupportFragmentManager().findFragmentByTag(GAME_LIST_FRAGMENT_TAG);
            if(gameListFragment == null)
            {
                //_games.getGame(_games.currentGame).launching = false;
                saveGames();
                gameListFragment = GameListFragment.newInstance();
                transaction.replace(detailLayout.getId(), gameListFragment, GAME_LIST_FRAGMENT_TAG);
            }
            else
            {
                //_games.getGame(_games.currentGame).launching = false;
                saveGames();
                transaction.replace(detailLayout.getId(), gameListFragment);
            }
            transaction.commit();
        }
        else
        {
            for (int x = 0; x < 10; x++)
            {
                for (int y = 0; y < 10; y++)
                {
                    if (buttons[x][y] == button)
                    {
                        // LAUNCH!!!
                        int index = _games.myGameHolder.myGames.indexOf(_games.currentGameId);
                        if(index != -1)
                        {
                            String playerId = _games.myGameHolder.myPlayerIds.get(index);

                            NetworkManager.getInstance().launchMissile(_games.currentGameId, playerId, x, y, new NetworkManager.LaunchListener()
                            {
                                @Override
                                public void missileLaunched(boolean success, boolean hit)
                                {
                                    // Might not need to do anything
                                }
                            });



                            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                            //PreGameFragment preGameFragment = (PreGameFragment) getSupportFragmentManager().findFragmentByTag(PREGAME_FRAGMENT_TAG);
                            GameListFragment gameListFragment = (GameListFragment) getSupportFragmentManager().findFragmentByTag(GAME_LIST_FRAGMENT_TAG);
                            GameGridFragment gameGridFragment = (GameGridFragment) getSupportFragmentManager().findFragmentByTag(GAME_GRID_FRAGMENT_TAG);
                            if(isTablet)
                            {
                                transaction.detach(gameListFragment);
                                transaction.attach(gameListFragment);
                            }
                            transaction.commit();
                            gameGridFragment.vertLayout.invalidate();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onEnterGame(String gameName, String playerName) throws IOException
    {
        boolean alphanumeric = true;
        if(playerName != null)
        {
            if(playerName.length()==0)
            {
                alphanumeric = false;
            }
            for (char c : playerName.toCharArray())
            {
                if (!Character.isLetterOrDigit(c) && c != ' ')
                {
                    alphanumeric = false;
                }
            }
        }

        if(alphanumeric)
        {
            if (_games.currentGameStatus.equals("new"))
            {
                boolean alphanumericGame = true;
                for(char c : gameName.toCharArray())
                {
                    if(!Character.isLetterOrDigit(c) && c != ' ')
                    {
                        alphanumericGame = false;
                    }
                }
                if(alphanumericGame && gameName.length()!=0 && gameName.trim().length()!=0 && gameName!=null)
                {
                    _games.currentGameName = gameName;
                    _games.currentPlayerName = playerName;
                    saveGames();
                }
                else
                {
                    Toast toast = Toast.makeText(this, "Game names may only contain alphanumeric characters", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
            }
            else if (gameName == null && playerName != null)
            {
                _games.currentPlayerName = playerName;
                saveGames();
            }

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            GameGridFragment gameGridFragment = (GameGridFragment) getSupportFragmentManager().findFragmentByTag(GAME_GRID_FRAGMENT_TAG);
            if (gameGridFragment == null)
            {
                gameGridFragment = GameGridFragment.newInstance();
                //_games.getGame(_games.currentGame).launching=true;
                saveGames();
                transaction.replace(detailLayout.getId(), gameGridFragment, GAME_GRID_FRAGMENT_TAG);
            }
            else
            {
                gameGridFragment.vertLayout.invalidate();
                //_games.getGame(_games.currentGame).launching=true;
                saveGames();
                transaction.replace(detailLayout.getId(), gameGridFragment);
            }
            transaction.commit();
        }
        else
        {
            Toast toast = Toast.makeText(this, "Player names may only contain alphanumeric characters", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    public void saveGames() throws IOException
    {
        Gson gson = new Gson();
        String jsonString = gson.toJson(_games.myGameHolder);

        SharedPreferences.Editor editor = getSharedPreferences("MY_GAMES", MODE_PRIVATE).edit();
        editor.putString("MyGames", jsonString);
        editor.commit();

        System.out.println("Here's the JSON String: " + jsonString + " <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

        /*
        String fileName = "collection.dat";
        ObjectOutput out = null;
        try
        {
            out = new ObjectOutputStream(new FileOutputStream(new File(this.getFilesDir(), "")+ File.separator+fileName));
            System.out.println("HERE'S WHAT WE'RE SAVING: " + _games.currentGameStatus + " <=======================================================================================================================================================");
            out.writeObject(_games.myGameHolder);
            out.close();

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

        _games = GameCollection.getInstance();

        SharedPreferences preferences = getSharedPreferences("MY_GAMES", MODE_PRIVATE);
        String retrieved = preferences.getString("MyGames", null);

        System.out.println("Retrieved it: " + retrieved + "  mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm");

        Gson gson = new Gson();
        MyGameHolder holder = gson.fromJson(retrieved, MyGameHolder.class);

        if(holder != null)
        {
            _games.myGameHolder = holder;
        }

        if(_games.myGameHolder.myGames == null)
        {
            String printThis = _games.myGameHolder.myGames.toString();
            System.out.println("Print this: " + printThis);
        }
        else
        {
            System.out.println("IT WAS NULL");
        }


        /*
        ObjectInputStream input;
        String filename = "collection.dat";

        try
        {
            input = new ObjectInputStream(new FileInputStream(new File(new File(this.getFilesDir(), "")+File.separator+filename)));
            _games.myGameHolder = (MyGameHolder) input.readObject();
            input.close();
        }
        catch(Exception e)
        {
            Log.i("File", "Ok we failed to read <***************************************************");
            throw e;
        }
        */
    }

    @Override
    public void onBackPressed()
    {
        if(_games.playing && !isTablet)
        {
            _games.playing = false;
            try
            {
                saveGames();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            GameListFragment gameListFragment = (GameListFragment) getSupportFragmentManager().findFragmentByTag(GAME_LIST_FRAGMENT_TAG);
            if(gameListFragment == null)
            {
                //_games.getGame(_games.currentGame).launching = false;
                try {
                    saveGames();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                gameListFragment = GameListFragment.newInstance();
                transaction.replace(detailLayout.getId(), gameListFragment, GAME_LIST_FRAGMENT_TAG);
            }
            else
            {
                //_games.getGame(_games.currentGame).launching = false;
                try
                {
                    saveGames();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                transaction.replace(detailLayout.getId(), gameListFragment);
            }
            transaction.commit();
        }
        else
        {
            super.onBackPressed();
        }

    }

    @Override
    public void OnSaveId(String gameId, String playerId) throws IOException
    {
        if(!_games.myGameHolder.myGames.contains(gameId))
        {
            synchronized (_games.myGameHolder.myTurns)
            {
                _games.myGameHolder.myTurns.add("new");
            }
            synchronized (_games.myGameHolder.myGames)
            {
                _games.myGameHolder.myGames.add(gameId);
            }
            synchronized (_games.myGameHolder.myPlayerIds)
            {
                _games.myGameHolder.myPlayerIds.add(playerId);
            }
        }
        _games.currentGameId = gameId;
        _games.currentGameStatus = "WAITING";
        saveGames();
    }

    @Override
    public void OnSaveIdJoin(String gameId, String playerId) throws IOException
    {
        if(!_games.myGameHolder.myGames.contains(gameId))
        {
            synchronized (_games.myGameHolder.myTurns)
            {
                _games.myGameHolder.myTurns.add("new");
            }
            synchronized (_games.myGameHolder.myGames)
            {
                _games.myGameHolder.myGames.add(gameId);
            }
            synchronized (_games.myGameHolder.myPlayerIds)
            {
                _games.myGameHolder.myPlayerIds.add(playerId);
            }
        }
        _games.currentGameId = gameId;
        _games.currentGameStatus = "PLAYING";
        saveGames();
    }

    @Override
    public void onSaveState(int state) throws IOException
    {
        _games.gameState=state;
        saveGames();
    }


    @Override
    protected void onDestroy()
    {
        if(timer != null)
        {
            timer.cancel();
        }
        super.onDestroy();
    }

}
