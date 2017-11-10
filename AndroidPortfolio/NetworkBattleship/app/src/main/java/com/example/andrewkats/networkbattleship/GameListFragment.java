package com.example.andrewkats.networkbattleship;

import android.app.Activity;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by AndrewKats on 10/22/2016.
 */
public class GameListFragment extends Fragment implements ListAdapter
{
    ArrayList<GameSummary> _summaries = new ArrayList<GameSummary>();
    ArrayList<GameSummary> _waitingSummaries = new ArrayList<GameSummary>();
    ArrayList<GameSummary> _playingSummaries = new ArrayList<GameSummary>();
    ArrayList<GameSummary> _doneSummaries = new ArrayList<GameSummary>();
    ArrayList<GameSummary> _mySummaries = new ArrayList<GameSummary>();
    Timer timer = null;
    ArrayAdapter<GameSummary> _adapter;

    public interface OnGameChangedListener
    {
        void onGameChanged(String gameId, String status) throws IOException;
    }

    OnGameChangedListener _gameChangedListener = null;

    GameCollection _games = null;

    Button _newButton = null;

    Spinner _statusSpinner = null;

    ListView _gamesListView = null;

    public static GameListFragment newInstance()
    {
        return new GameListFragment();
    }
    public GameListFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
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
                        NetworkManager.getInstance().getGameList(new NetworkManager.GameListListener()
                        {
                            @Override
                            public void gameListReceived(boolean succeeded, Set<GameSummary> games)
                            {
                                synchronized (_gamesListView)
                                {

                                    if(games != null)
                                    {
                                        _summaries.clear();
                                        _summaries.addAll(games);
                                    }

                                    Collections.sort(_summaries, new Comparator<GameSummary>()
                                    {
                                        @Override
                                        public int compare(GameSummary lhs, GameSummary rhs)
                                        {
                                            if(lhs.name.compareToIgnoreCase(rhs.name) == 0)
                                            {
                                                return lhs.id.compareToIgnoreCase(rhs.id);
                                            }
                                            else
                                            {
                                                return lhs.name.compareToIgnoreCase(rhs.name);
                                            }
                                        }
                                    });

                                    _waitingSummaries.clear();
                                    _playingSummaries.clear();
                                    _doneSummaries.clear();
                                    _mySummaries.clear();

                                    for(GameSummary summary : _summaries)
                                    {
                                        if(_games.myGameHolder.myGames.contains(summary.id))
                                        {
                                            _mySummaries.add(summary);
                                        }

                                        if(summary.status.equals("WAITING"))
                                        {
                                            _waitingSummaries.add(summary);
                                        }
                                        else if(summary.status.equals("PLAYING"))
                                        {
                                            _playingSummaries.add(summary);
                                        }
                                        else if(summary.status.equals("DONE"))
                                        {
                                            _doneSummaries.add(summary);
                                        }
                                    }

                                    if(_statusSpinner.getSelectedItem().equals("Waiting"))
                                    {
                                        _summaries.clear();
                                        _summaries.addAll(_waitingSummaries);
                                    }
                                    else if(_statusSpinner.getSelectedItem().equals("Playing"))
                                    {
                                        _summaries.clear();
                                        _summaries.addAll(_playingSummaries);
                                    }
                                    else if(_statusSpinner.getSelectedItem().equals("Done"))
                                    {
                                        _summaries.clear();
                                        _summaries.addAll(_doneSummaries);
                                    }
                                    else if(_statusSpinner.getSelectedItem().equals("My Games"))
                                    {
                                        _summaries.clear();
                                        _summaries.addAll(_mySummaries);
                                    }

                                }
                            }
                        });
                        _adapter.notifyDataSetChanged();
                    }
                });
            }
        };
        timer.schedule(doAsyncTask, 0, 500);







        _games = GameCollection.getInstance();

        if(!((MainActivity)getActivity()).isTablet)
        {
            _games.gameState = 0;
        }


        LinearLayout vertLayout = new LinearLayout(getActivity());
        vertLayout.setOrientation(LinearLayout.VERTICAL);


        _newButton = new Button(getActivity());
        _newButton.setText("Start a new game");
        _newButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    _gameChangedListener.onGameChanged("new", "new");
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });
        vertLayout.addView(_newButton);



        ArrayList<String> spinnerStrings = new ArrayList<String>();
        spinnerStrings.add("All");
        spinnerStrings.add("Waiting");
        spinnerStrings.add("Playing");
        spinnerStrings.add("Done");
        spinnerStrings.add("My Games");

        _statusSpinner = new Spinner(getActivity());
        final ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerStrings);
        _statusSpinner.setAdapter(spinnerAdapter);
        _statusSpinner.setSelection(_games.filterState);
        _statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                if(_statusSpinner.getSelectedItem().equals("All"))
                {
                    _games.filterState=0;
                }
                if(_statusSpinner.getSelectedItem().equals("Waiting"))
                {
                    _games.filterState=1;
                }
                if(_statusSpinner.getSelectedItem().equals("Playing"))
                {
                    _games.filterState=2;
                }
                if(_statusSpinner.getSelectedItem().equals("Done"))
                {
                    _games.filterState=3;
                }
                if(_statusSpinner.getSelectedItem().equals("My Games"))
                {
                    _games.filterState=4;
                }


                _adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
                _adapter.notifyDataSetChanged();
            }
        });
        vertLayout.addView(_statusSpinner);


        _gamesListView = new ListView(getActivity());
        _adapter = new ArrayAdapter<GameSummary>(getActivity(), android.R.layout.simple_list_item_1, _summaries)
        {
            @Override
            public View getView(int position, View convertView, ViewGroup parent)
            {
                TextView colorView = (TextView) convertView;
                if(colorView != null)
                {
                    if(_statusSpinner.getSelectedItem().equals("Waiting"))
                    {
                        if(position < _waitingSummaries.size())
                        {
                            if (_waitingSummaries.get(position).id.equals(_games.currentGameId))
                            {
                                colorView.setTextColor(Color.BLUE);
                            }
                            else
                            {
                                colorView.setTextColor(Color.BLACK);
                            }
                        }
                    }
                    else if(_statusSpinner.getSelectedItem().equals("Playing"))
                    {
                        if(position < _playingSummaries.size())
                        {
                            if (_playingSummaries.get(position).id.equals(_games.currentGameId))
                            {
                                colorView.setTextColor(Color.BLUE);
                            }
                            else
                            {
                                colorView.setTextColor(Color.BLACK);
                            }
                        }
                    }
                    else if(_statusSpinner.getSelectedItem().equals("Done"))
                    {
                        if(position < _doneSummaries.size())
                        {
                            if (_doneSummaries.get(position).id.equals(_games.currentGameId))
                            {
                                colorView.setTextColor(Color.BLUE);
                            }
                            else
                            {
                                colorView.setTextColor(Color.BLACK);
                            }
                        }
                    }
                    else if(_statusSpinner.getSelectedItem().equals("My Games"))
                    {
                        if(position < _mySummaries.size())
                        {
                            if (_mySummaries.get(position).id.equals(_games.currentGameId))
                            {
                                colorView.setTextColor(Color.BLUE);
                            }
                            else
                            {
                                colorView.setTextColor(Color.BLACK);
                            }
                        }
                    }
                    else
                    {
                        if(position < _summaries.size())
                        {
                            if (_summaries.get(position).id.equals(_games.currentGameId))
                            {
                                colorView.setTextColor(Color.BLUE);
                            }
                            else
                            {
                                colorView.setTextColor(Color.BLACK);
                            }
                        }
                    }
                }
                return super.getView(position, colorView, parent);
            }
        };
        _gamesListView.setAdapter(_adapter);
        _gamesListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                try
                {
                    if(_statusSpinner.getSelectedItem().equals("Waiting"))
                    {
                        _gameChangedListener.onGameChanged(_waitingSummaries.get(position).id, _waitingSummaries.get(position).status);
                        Log.i("Item Clicked: ", _waitingSummaries.get(position).name + " " + _waitingSummaries.get(position).status);
                    }
                    else if(_statusSpinner.getSelectedItem().equals("Playing"))
                    {
                        _gameChangedListener.onGameChanged(_playingSummaries.get(position).id, _playingSummaries.get(position).status);
                        Log.i("Item Clicked: ", _playingSummaries.get(position).name + " " + _playingSummaries.get(position).status);

                    }
                    else if(_statusSpinner.getSelectedItem().equals("Done"))
                    {
                        _gameChangedListener.onGameChanged(_doneSummaries.get(position).id, _doneSummaries.get(position).status);
                        Log.i("Item Clicked: ", _doneSummaries.get(position).name + " " + _doneSummaries.get(position).status);

                    }
                    else if(_statusSpinner.getSelectedItem().equals("My Games"))
                    {
                        _gameChangedListener.onGameChanged(_mySummaries.get(position).id, _mySummaries.get(position).status);
                        Log.i("Item Clicked: ", _mySummaries.get(position).name + " " + _mySummaries.get(position).status);
                    }
                    else
                    {
                        _gameChangedListener.onGameChanged(_summaries.get(position).id, _summaries.get(position).status);
                        Log.i("Item Clicked: ", _summaries.get(position).name + " " + _summaries.get(position).status);

                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        });



        vertLayout.addView(_gamesListView);

        return vertLayout;
    }


    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        _gameChangedListener = (OnGameChangedListener) activity;
    }


    @Override
    public boolean areAllItemsEnabled()
    {
        return true;
    }

    @Override
    public boolean isEnabled(int position)
    {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {}

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {}

    @Override
    public int getCount()
    {
        if(_statusSpinner.getSelectedItem().equals("Waiting"))
        {
            return _waitingSummaries.size();
        }
        else if(_statusSpinner.getSelectedItem().equals("Playing"))
        {
            return _playingSummaries.size();
        }
        else if(_statusSpinner.getSelectedItem().equals("Done"))
        {
            return _doneSummaries.size();
        }
        else if(_statusSpinner.getSelectedItem().equals("My Games"))
        {
            return _mySummaries.size();
        }
        else
        {
            return _summaries.size();
        }
    }

    @Override
    public Object getItem(int position)
    {
        if(_statusSpinner.getSelectedItem().equals("Waiting"))
        {
            return _waitingSummaries.get(position);
        }
        else if(_statusSpinner.getSelectedItem().equals("Playing"))
        {
            return _playingSummaries.get(position);
        }
        else if(_statusSpinner.getSelectedItem().equals("Done"))
        {
            return _doneSummaries.get(position);
        }
        else if(_statusSpinner.getSelectedItem().equals("My Games"))
        {
            return _mySummaries.get(position);
        }
        else
        {
            return _summaries.get(position);
        }
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public boolean hasStableIds()
    {
        return true;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        GameSummary game = (GameSummary) getItem(position);

        TextView descriptionText = new TextView(getActivity());

        descriptionText.setText("\nName: " + game.name + "\n\n" + "Status: " + game.status + "\n");

        return descriptionText;

    }

    @Override
    public int getItemViewType(int position)
    {
        return 0;
    }

    @Override
    public int getViewTypeCount()
    {
        return 1;
    }

    @Override
    public boolean isEmpty()
    {
        if(_statusSpinner.getSelectedItem().equals("Waiting"))
        {
            return _waitingSummaries.isEmpty();
        }
        else if(_statusSpinner.getSelectedItem().equals("Playing"))
        {
            return _playingSummaries.isEmpty();
        }
        else if(_statusSpinner.getSelectedItem().equals("Done"))
        {
            return _doneSummaries.isEmpty();
        }
        else if(_statusSpinner.getSelectedItem().equals("My Games"))
        {
            return _mySummaries.isEmpty();
        }
        else
        {
            return _summaries.isEmpty();
        }
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
