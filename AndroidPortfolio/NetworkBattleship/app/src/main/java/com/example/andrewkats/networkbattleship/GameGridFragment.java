package com.example.andrewkats.networkbattleship;



import android.app.Activity;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.PorterDuff;
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
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by AndrewKats on 10/23/2016.
 */
public class GameGridFragment extends Fragment
{
    public interface OnChooseLaunchListener
    {
        void onChooseLaunch(GridButton[][] buttons, GridButton clicked) throws IOException;
    }

    public interface OnSaveIdListener
    {
        void OnSaveId(String gameId, String playerId) throws IOException;
    }

    public interface OnSaveIdJoinListener
    {
        void OnSaveIdJoin(String gameId, String playerId) throws IOException;
    }

    OnSaveIdListener _saveIdListener = null;

    OnSaveIdJoinListener _saveIdJoinListener = null;

    OnChooseLaunchListener _chooseLaunchListener = null;

    PreGameFragment.OnSaveStateListener _saveStateListener = null;

    GameCollection _games = null;

    public static GameGridFragment newInstance()
    {
        return new GameGridFragment();
    }

    GridButton[][] _playerButtons = new GridButton[10][10];

    GridButton[][] _enemyButtons = new GridButton[10][10];

    Button backButton = null;

    LinearLayout vertLayout;
    LinearLayout horizLayout;
    LinearLayout vertEnemyLayout;
    LinearLayout vertPlayerLayout;

    BoardsResponse _boards = new BoardsResponse();

    Timer timer = null;

    TextView playerText = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        _games = GameCollection.getInstance();
        try
        {
            _saveStateListener.onSaveState(2);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        if(_games.currentGameStatus.equals("new"))
        {
            try
            {
                NetworkManager.getInstance().createGame(_games.currentGameName, _games.currentPlayerName, new NetworkManager.CreateGameListener()
                {
                    @Override
                    public void gameCreated(boolean success, String gameId, String playerId) throws IOException
                    {
                        if(success)
                        {
                            for (int i = 0; i < 10; i++)
                            {
                                for (int j = 0; j < 10; j++)
                                {
                                    if(_boards.playerBoard != null && _boards.opponentBoard != null)
                                    {
                                        if (_boards.playerBoard[i][j] != null && _boards.opponentBoard[i][j] != null)
                                        {
                                            _boards.playerBoard[i][j].status = "NONE";
                                            _boards.opponentBoard[i][j].status = "NONE";
                                        }
                                        resetPlayerButtonColors();
                                        resetEnemyButtonColors();
                                        resetPlayerText();
                                        vertLayout.invalidate();
                                    }
                                }
                            }
                            _saveIdListener.OnSaveId(gameId, playerId);
                            //_games.myGameHolder.myGames.add(gameId);
                            //_games.myGameHolder.myPlayerIds.add(playerId);
                            //_games.currentGameId = gameId;
                            _games.currentGameStatus = "WAITING";
                        }
                    }
                });
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            if(vertLayout != null)
            {
                for (int i = 0; i < 10; i++)
                {
                    for (int j = 0; j < 10; j++)
                    {
                        if(_boards.playerBoard[i][j] != null && _boards.opponentBoard[i][j] != null)
                        {
                            _boards.playerBoard[i][j].status = "NONE";
                            _boards.opponentBoard[i][j].status = "NONE";
                        }

                    }
                }
                resetPlayerButtonColors();
                resetEnemyButtonColors();
                vertLayout.invalidate();
            }
        }

        if(_games.currentGameStatus.equals("WAITING") && !_games.myGameHolder.myGames.contains(_games.currentGameId))
        {
            try
            {
                NetworkManager.getInstance().joinGame(_games.currentGameId, _games.currentPlayerName, new NetworkManager.JoinGameListener()
                {
                    @Override
                    public void gameJoined(boolean success, String playerId) throws IOException
                    {
                        _saveIdJoinListener.OnSaveIdJoin(_games.currentGameId, playerId);
                    }
                });
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }




        getTheBoards();


        vertLayout = new LinearLayout(getActivity());
        vertLayout.setOrientation(LinearLayout.VERTICAL);
        vertLayout.setBackgroundColor(Color.rgb(232,246,255));

        horizLayout = new LinearLayout(getActivity());

        vertEnemyLayout = new LinearLayout(getActivity());
        vertEnemyLayout.setOrientation(LinearLayout.VERTICAL);
        //vertEnemyLayout.setBackgroundColor(Color.MAGENTA);

        vertPlayerLayout = new LinearLayout(getActivity());
        vertPlayerLayout.setOrientation(LinearLayout.VERTICAL);
        //vertPlayerLayout.setBackgroundColor(Color.GREEN);

        float density = getResources().getDisplayMetrics().density;
        float padSize = 0.1f * 160.0f * density;



        if(!((MainActivity)getActivity()).isTablet)
        {
            if(_games.currentGameStatus.equals("DONE"))
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
            else if(getActivity().getResources().getConfiguration().orientation != Configuration.ORIENTATION_PORTRAIT)
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
                vertPlayerLayout.addView(backButton);
            }
            else
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
        }


        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            TextView yourShipsText = new TextView(getActivity());
            yourShipsText.setText("Your Ships");
            yourShipsText.setTextSize(18);
            yourShipsText.setGravity(Gravity.CENTER_HORIZONTAL);
            vertLayout.addView(yourShipsText);
        }

        GridLayout playerGrid = new GridLayout(getActivity());
        //playerGrid.setUseDefaultMargins(false);
        //playerGrid.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        //playerGrid.setRowOrderPreserved(false);
        playerGrid.setColumnCount(10);
        playerGrid.setRowCount(10);

        setPlayerButtonColors();


        for(GridButton[] bArray : _playerButtons)
        {
            for(GridButton b : bArray)
            {
                playerGrid.addView(b, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            }
        }



        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            LinearLayout.LayoutParams playerParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 2);
            playerParams.gravity = Gravity.CENTER_HORIZONTAL;
            //playerGrid.setPadding(0,0,0,(int)padSize);
            vertLayout.addView(playerGrid, playerParams);
        }
        else
        {
            TextView leftText = new TextView(getActivity());
            int index = _games.myGameHolder.myGames.indexOf(_games.currentGameId);
            if(index < 0)
            {
                leftText.setText("Your Ships");
            }
            else
            {
                leftText.setText("Your Ships");
            }

            leftText.setTextSize(18);
            leftText.setGravity(Gravity.CENTER_HORIZONTAL);

            vertPlayerLayout.addView(leftText);



            LinearLayout.LayoutParams playerParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            //playerParams.gravity = Gravity.CENTER_HORIZONTAL;
            //playerGrid.setPadding(0,(int)padSize,0,0);
            //vertPlayerLayout.addView(playerGrid, playerParams);


            LinearLayout.LayoutParams playerGridParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            playerGridParams.gravity = Gravity.CENTER_HORIZONTAL;
            vertPlayerLayout.addView(playerGrid, playerGridParams);

            vertPlayerLayout.setPadding((int)padSize,0,0,0);
            LinearLayout.LayoutParams vertPlayerParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            horizLayout.addView(vertPlayerLayout, vertPlayerParams);
        }


        playerText = new TextView(getActivity());
        int index = _games.myGameHolder.myGames.indexOf(_games.currentGameId);
        if(index <0)
        {
            playerText.setText("...");
        }
        else
        {
            if(index < _games.myGameHolder.myTurns.size())
            {
                playerText.setText(_games.myGameHolder.myTurns.get(index));
            }
        }
        playerText.setTextSize(18);



        playerText.setGravity(Gravity.CENTER_HORIZONTAL);

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            vertLayout.addView(playerText);
        }
        else
        {
            vertEnemyLayout.addView(playerText);
        }

        GridLayout enemyGrid = new GridLayout(getActivity());
        enemyGrid.setColumnCount(10);
        enemyGrid.setRowCount(10);

        setEnemyButtonColors();

        for(GridButton[] bArray : _enemyButtons)
        {
            for(GridButton b : bArray)
            {
                enemyGrid.addView(b,new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            }
        }


        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            LinearLayout.LayoutParams enemyParams = null;
            if(_games.currentGameStatus.equals("DONE"))
            {
                enemyParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 2);
            }
            else
            {
                enemyGrid.setPadding(0,0,0,(int)padSize);
                enemyParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0, 3);
            }
            enemyParams.gravity = Gravity.CENTER_HORIZONTAL;

            vertLayout.addView(enemyGrid, enemyParams);
        }
        else
        {
            LinearLayout.LayoutParams enemyGridParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            enemyGridParams.gravity = Gravity.CENTER_HORIZONTAL;
            vertEnemyLayout.addView(enemyGrid, enemyGridParams);
            vertEnemyLayout.setPadding((int)padSize,0,0,0);
            horizLayout.addView(vertEnemyLayout, new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
            vertLayout.addView(horizLayout);
        }

        return vertLayout;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        _chooseLaunchListener = (OnChooseLaunchListener) activity;
        _saveIdListener = (OnSaveIdListener) activity;
        _saveIdJoinListener = (OnSaveIdJoinListener) activity;
        _saveStateListener = (PreGameFragment.OnSaveStateListener) activity;
    }


    public void setPlayerButtonColors()
    {
        for(int i=0; i<10; i++)
        {
            for(int j=0; j<10; j++)
            {
                GridButton newButton = new GridButton(getActivity());
                newButton.setMinimumWidth(0);
                newButton.setMinimumHeight(0);
                newButton.setMinWidth(0);
                newButton.setMinHeight(0);
                newButton.setHeight(10);
                newButton.setWidth(10);
                _playerButtons[i][j] = newButton;

                newButton.getBackground().setColorFilter(Color.rgb(0, 191, 255), PorterDuff.Mode.MULTIPLY);

                if(_boards.playerBoard != null )
                {
                    if(_boards.playerBoard[i][j] != null)
                    {
                        if (_boards.playerBoard[i][j].status.equals("NONE") || _games.currentGameStatus.equals("WAITING"))
                        {
                            newButton.getBackground().setColorFilter(Color.rgb(0, 191, 255), PorterDuff.Mode.MULTIPLY);
                        }
                        if (_boards.playerBoard[i][j].status.equals("SHIP"))
                        {
                            newButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                        }
                        if (_boards.playerBoard[i][j].status.equals("HIT"))
                        {
                            newButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        }
                        if (_boards.playerBoard[i][j].status.equals("MISS"))
                        {
                            newButton.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                        }
                    }
                }

            }
        }
    }

    public void resetPlayerButtonColors()
    {
        for(int i=0; i<10; i++)
        {
            for(int j=0; j<10; j++)
            {

                if(_games.currentGameStatus.equals("new") || _games.currentGameStatus.equals("WAITING"))
                {
                    _playerButtons[i][j].getBackground().setColorFilter(Color.rgb(0, 191, 255), PorterDuff.Mode.MULTIPLY);
                }
                else
                {
                    if (_boards.playerBoard != null)
                    {
                        if (_boards.playerBoard[i][j] != null)
                        {
                            if (_boards.playerBoard[i][j].status.equals("NONE") || _games.currentGameStatus.equals("WAITING"))
                            {
                                _playerButtons[i][j].getBackground().setColorFilter(Color.rgb(0, 191, 255), PorterDuff.Mode.MULTIPLY);
                            }
                            if (_boards.playerBoard[i][j].status.equals("SHIP"))
                            {
                                _playerButtons[i][j].getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                            }
                            if (_boards.playerBoard[i][j].status.equals("HIT"))
                            {
                                _playerButtons[i][j].getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                            }
                            if (_boards.playerBoard[i][j].status.equals("MISS"))
                            {
                                _playerButtons[i][j].getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                            }
                        }
                        else
                        {
                            _playerButtons[i][j].getBackground().setColorFilter(Color.rgb(0, 191, 255), PorterDuff.Mode.MULTIPLY);
                        }
                    }
                    else
                    {
                        _playerButtons[i][j].getBackground().setColorFilter(Color.rgb(0, 191, 255), PorterDuff.Mode.MULTIPLY);
                    }
                }
            }
        }
    }

    public void setEnemyButtonColors()
    {
        for(int i=0; i<10; i++)
        {
            for(int j=0; j<10; j++)
            {
                GridButton newButton = new GridButton(getActivity());
                //newButton.setText("enemy");
                newButton.setMinimumWidth(0);
                newButton.setMinimumHeight(0);
                newButton.setMinWidth(0);
                newButton.setMinHeight(0);
                newButton.setHeight(10);
                newButton.setWidth(10);
                _enemyButtons[i][j] = newButton;

                newButton.getBackground().setColorFilter(Color.rgb(0, 191, 255), PorterDuff.Mode.MULTIPLY);

                if(_boards.opponentBoard != null)
                {
                    if(_boards.opponentBoard[i][j] != null)
                    {
                        if (_boards.opponentBoard[i][j].status.equals("NONE") || _games.currentGameStatus.equals("WAITING"))
                        {
                            newButton.getBackground().setColorFilter(Color.rgb(0, 191, 255), PorterDuff.Mode.MULTIPLY);
                        }
                        if (_boards.opponentBoard[i][j].status.equals("SHIP") && _games.currentGameStatus.equals("DONE"))
                        {
                            newButton.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                        }
                        if (_boards.opponentBoard[i][j].status.equals("HIT"))
                        {
                            newButton.getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        }
                        if (_boards.opponentBoard[i][j].status.equals("MISS"))
                        {
                            newButton.getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                        }
                    }
                }

                newButton.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        try
                        {
                            _chooseLaunchListener.onChooseLaunch(_enemyButtons, (GridButton) v);
                        } catch (IOException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });
            }
        }
    }

    public void resetEnemyButtonColors()
    {
        for(int i=0; i<10; i++)
        {
            for(int j=0; j<10; j++)
            {
                if(_games.currentGameStatus.equals("new") || _games.currentGameStatus.equals("WAITING"))
                {
                    _enemyButtons[i][j].getBackground().setColorFilter(Color.rgb(0, 191, 255), PorterDuff.Mode.MULTIPLY);
                }
                else
                {
                    if (_boards.opponentBoard != null)
                    {
                        if (_boards.opponentBoard[i][j] != null)
                        {
                            if (_boards.opponentBoard[i][j].status.equals("NONE") || _games.currentGameStatus.equals("WAITING"))
                            {
                                _enemyButtons[i][j].getBackground().setColorFilter(Color.rgb(0, 191, 255), PorterDuff.Mode.MULTIPLY);
                            }
                            if (_boards.opponentBoard[i][j].status.equals("SHIP"))
                            {
                                _enemyButtons[i][j].getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
                            }
                            if (_boards.opponentBoard[i][j].status.equals("HIT"))
                            {
                                _enemyButtons[i][j].getBackground().setColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                            }
                            if (_boards.opponentBoard[i][j].status.equals("MISS"))
                            {
                                _enemyButtons[i][j].getBackground().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
                            }
                        }
                        else
                        {
                            _enemyButtons[i][j].getBackground().setColorFilter(Color.rgb(0, 191, 255), PorterDuff.Mode.MULTIPLY);
                        }
                    }
                    else
                    {
                        _enemyButtons[i][j].getBackground().setColorFilter(Color.rgb(0, 191, 255), PorterDuff.Mode.MULTIPLY);
                    }
                }

            }
        }
    }

    public void resetPlayerText()
    {
        int index = _games.myGameHolder.myGames.indexOf(_games.currentGameId);
        if(index < 0)
        {
            playerText.setText("...");
        }
        else
        {
            if(index < _games.myGameHolder.myTurns.size())
            {
                playerText.setText(_games.myGameHolder.myTurns.get(index));
            }
        }
    }


    public void getTheBoards()
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
                        synchronized (_boards)
                        {
                            try
                            {
                                int index = _games.myGameHolder.myGames.indexOf(_games.currentGameId);
                                if (index != -1)
                                {
                                    final String playerId = _games.myGameHolder.myPlayerIds.get(index);
                                    NetworkManager.getInstance().getBoards(playerId, _games.currentGameId, new NetworkManager.GetBoardListener()
                                    {
                                        @Override
                                        public void boardReceived(boolean success, BoardsResponse boards)
                                        {
                                            if (success)
                                            {
                                                if (boards != null)
                                                {
                                                    _boards.playerBoard = boards.playerBoard;
                                                    _boards.opponentBoard = boards.opponentBoard;
                                                    _games.currentGameStatus= "PLAYING";
                                                }
                                            }
                                            else
                                            {
                                                if(_boards.playerBoard != null && _boards.opponentBoard != null)
                                                {
                                                    for (int i = 0; i < 10; i++)
                                                    {
                                                        for (int j = 0; j < 10; j++)
                                                        {
                                                            if(_boards.playerBoard[i][j] != null && _boards.opponentBoard[i][j] != null)
                                                            {
                                                                _boards.playerBoard[i][j].status = "NONE";
                                                                _boards.opponentBoard[i][j].status = "NONE";
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    });
                                }
                                else
                                {
                                    for (int i = 0; i < 10; i++)
                                    {
                                        for (int j = 0; j < 10; j++)
                                        {
                                            if (_boards.playerBoard != null && _boards.opponentBoard != null)
                                            {
                                                if (_boards.playerBoard[i][j] != null && _boards.opponentBoard[i][j] != null)
                                                {
                                                    _boards.playerBoard[i][j].status = "NONE";
                                                    _boards.opponentBoard[i][j].status = "NONE";
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            catch (IOException e)
                            {
                                for (int i = 0; i < 10; i++)
                                {
                                    for (int j = 0; j < 10; j++)
                                    {
                                        if (_boards.playerBoard != null && _boards.opponentBoard != null)
                                        {
                                            if (_boards.playerBoard[i][j] != null && _boards.opponentBoard[i][j] != null)
                                            {
                                                _boards.playerBoard[i][j].status = "NONE";
                                                _boards.opponentBoard[i][j].status = "NONE";
                                            }
                                        }
                                    }
                                }
                                e.printStackTrace();
                            }

                            resetPlayerButtonColors();
                            resetEnemyButtonColors();
                            resetPlayerText();
                            vertLayout.invalidate();

                        }
                    }
                });
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
