package com.example.andrewkats.networkbattleship;

import android.os.AsyncTask;
import android.util.Log;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Created by AndrewKats on 11/13/2016.
 */
public class NetworkManager
{

    private static NetworkManager _Instance = null;

    public static NetworkManager getInstance()
    {
        // TODO: multithreaded

        if(_Instance == null)
        {
            _Instance = new NetworkManager();
        }
        return _Instance;
    }

    private NetworkManager() {}



    public interface GameListListener
    {
        void gameListReceived(boolean succeeded, Set<GameSummary> games);
    }

    public void getGameList(final GameListListener listener)
    {
        // Obtain URL to the server endpoint for game lobby
        URL lobbyURL = null;
        try
        {
            lobbyURL = new URL("http://battleship.pixio.com/api/v2/lobby");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            listener.gameListReceived(false, null);
        }

        // Create an asynchronous task to retrieve the list
        AsyncTask<URL, Double, Set<GameSummary>> gameListRequestTask = new AsyncTask<URL, Double, Set<GameSummary>>()
        {
            @Override
            protected Set<GameSummary> doInBackground(URL... urls)
            {
                // Request list
                URL url = urls[0];
                String gameListString = null;
                try
                {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    Scanner scanner = new Scanner(inputStream);
                    StringBuilder stringBuilder = new StringBuilder();
                    while(scanner.hasNext())
                    {
                        stringBuilder.append(scanner.nextLine());
                    }
                    gameListString = stringBuilder.toString();
                    Log.i("Lobby", gameListString);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    listener.gameListReceived(false, null);
                }

                //Process list
                Gson gson = new Gson();
                GameSummary[] summaries = gson.fromJson(gameListString, GameSummary[].class);

                Set<GameSummary> gameIdentifiers = new HashSet<GameSummary>();
                if(summaries != null)
                {
                    for (GameSummary gameSummary : summaries)
                    {
                        gameIdentifiers.add(gameSummary);
                    }
                }
                else
                {
                    listener.gameListReceived(false, null);
                }


                return gameIdentifiers;
            }

            @Override
            protected void onPostExecute(Set<GameSummary> games)
            {
                super.onPostExecute(games);
                listener.gameListReceived(true, games);
            }
        };

        // Execute the task
        gameListRequestTask.execute(lobbyURL);

    }


    public interface GameDetailsListener
    {
        void gameReceived(boolean success, GameDetails gameDetails);
    }

    public void getGameDetail(String gameId, final GameDetailsListener listener)
    {
        // Obtain URL to the server endpoint for game lobby
        URL gameURL = null;
        try
        {
            gameURL = new URL("http://battleship.pixio.com/api/v2/lobby/" + gameId);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            listener.gameReceived(false, null);
        }

        // Create an asynchronous task to retrieve the list
        AsyncTask<URL, Double, GameDetails> gameDetailRequestTask = new AsyncTask<URL, Double, GameDetails>()
        {
            @Override
            protected GameDetails doInBackground(URL... urls)
            {
                // Request list
                URL url = urls[0];
                String gameDetailsString = null;
                try
                {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = connection.getInputStream();
                    Scanner scanner = new Scanner(inputStream);
                    StringBuilder stringBuilder = new StringBuilder();
                    while(scanner.hasNext())
                    {
                        stringBuilder.append(scanner.nextLine());
                    }
                    gameDetailsString = stringBuilder.toString();
                    Log.i("Game", gameDetailsString);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    listener.gameReceived(false, null);
                }

                //Process list
                Gson gson = new Gson();
                GameDetails details = gson.fromJson(gameDetailsString, GameDetails.class);

                return details;
            }

            @Override
            protected void onPostExecute(GameDetails details)
            {
                super.onPostExecute(details);
                listener.gameReceived(true, details);
            }
        };

        // Execute the task
        gameDetailRequestTask.execute(gameURL);
    }


    public interface CreateGameListener
    {
        void gameCreated(boolean success, String gameId, String playerId) throws IOException;
    }

    public void createGame(final String gameName, final String playerName, final CreateGameListener listener) throws IOException
    {
        // Obtain URL to the server endpoint for game lobby
        URL gameURL = null;
        try
        {
            gameURL = new URL("http://battleship.pixio.com/api/v2/lobby/");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            listener.gameCreated(false, null, null);
        }

        // Create an asynchronous task to retrieve the list
        AsyncTask<URL, Double, NewGameResponse> createGameRequestTask = new AsyncTask<URL, Double, NewGameResponse>()
        {
            @Override
            protected NewGameResponse doInBackground(URL... urls)
            {
                // Request list
                URL url = urls[0];
                String responseString = null;
                try
                {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestMethod("POST");

                    JSONObject post = new JSONObject();
                    post.put("gameName", gameName);
                    post.put("playerName", playerName);

                    System.out.println("Post: " + post);


                    OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                    out.write(post.toString());
                    out.close();


                    InputStream inputStream = connection.getInputStream();
                    Scanner scanner = new Scanner(inputStream);
                    StringBuilder stringBuilder = new StringBuilder();
                    while(scanner.hasNext())
                    {
                        stringBuilder.append(scanner.nextLine());
                    }
                    responseString = stringBuilder.toString();
                    Log.i("Response", responseString);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    try
                    {
                        listener.gameCreated(false, null, null);
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                //Process list
                Gson gson = new Gson();
                NewGameResponse response = gson.fromJson(responseString, NewGameResponse.class);

                return response;
            }

            @Override
            protected void onPostExecute(NewGameResponse response)
            {
                super.onPostExecute(response);
                try
                {
                    if(response != null)
                    {
                        listener.gameCreated(true, response.gameId, response.playerId);
                    }
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        };

        // Execute the task
        createGameRequestTask.execute(gameURL);
    }


    public interface GetBoardListener
    {
        void boardReceived(boolean success, BoardsResponse boards);
    }

    public void getBoards(final String playerId, final String gameId, final GetBoardListener listener) throws IOException
    {
        // Obtain URL to the server endpoint for game lobby
        URL gameURL = null;
        try
        {
            gameURL = new URL("http://battleship.pixio.com/api/v2/games/" + gameId + "/boards" + "?playerId=" + playerId);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            listener.boardReceived(false, null);
        }

        // Create an asynchronous task to retrieve the list
        AsyncTask<URL, Double, BoardsResponse> getBoardsRequestTask = new AsyncTask<URL, Double, BoardsResponse>()
        {
            @Override
            protected BoardsResponse doInBackground(URL... urls)
            {
                // Request list
                URL url = urls[0];
                String responseString = null;
                try
                {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //connection.setDoOutput(true);
                    //connection.setDoInput(true);
                    //connection.setRequestProperty("Content-Type", "application/json");
                    //connection.setRequestProperty("Accept", "application/json");
                    //connection.setRequestProperty("playerId", playerId);
                    //connection.setRequestMethod("GET");

                    InputStream inputStream = connection.getInputStream();
                    Scanner scanner = new Scanner(inputStream);
                    StringBuilder stringBuilder = new StringBuilder();
                    while(scanner.hasNext())
                    {
                        stringBuilder.append(scanner.nextLine());
                    }
                    responseString = stringBuilder.toString();
                    Log.i("Response", responseString);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    listener.boardReceived(false, null);

                }

                //Process list
                Gson gson = new Gson();

                JSONObject jsonObj = null;
                try
                {
                    jsonObj = new JSONObject(responseString);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    listener.boardReceived(false, null);
                }
                JSONArray playerBoard;
                JSONArray opponentBoard;
                CellResponse[][] playerArray = new CellResponse[10][10];
                CellResponse[][] opponentArray = new CellResponse[10][10];



                try
                {
                    playerBoard = jsonObj.getJSONArray("playerBoard");
                    opponentBoard = jsonObj.getJSONArray("opponentBoard");
                    for(int i=0; i<playerBoard.length(); i++)
                    {
                        JSONObject currentObj = playerBoard.getJSONObject(i);
                        int xPos = currentObj.getInt("xPos");
                        int yPos = currentObj.getInt("yPos");
                        String status = currentObj.getString("status");
                        playerArray[xPos][yPos] = new CellResponse(xPos, yPos, status);

                        currentObj = opponentBoard.getJSONObject(i);
                        xPos = currentObj.getInt("xPos");
                        yPos = currentObj.getInt("yPos");
                        status = currentObj.getString("status");
                        opponentArray[xPos][yPos] = new CellResponse(xPos, yPos, status);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    listener.boardReceived(false, null);
                }




                BoardsResponse boards = new BoardsResponse();
                boards.playerBoard = playerArray;
                boards.opponentBoard = opponentArray;

                return boards;
            }

            @Override
            protected void onPostExecute(BoardsResponse boards)
            {
                super.onPostExecute(boards);
                listener.boardReceived(true, boards);
            }
        };

        // Execute the task
        getBoardsRequestTask.execute(gameURL);
    }


    public interface JoinGameListener
    {
        void gameJoined(boolean success, String playerId) throws IOException;
    }

    public void joinGame(final String gameId, final String playerName, final JoinGameListener listener) throws IOException
    {
        // Obtain URL to the server endpoint for game lobby
        URL gameURL = null;
        try
        {
            gameURL = new URL("http://battleship.pixio.com/api/v2/lobby/" + gameId);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            listener.gameJoined(false, null);
        }

        // Create an asynchronous task to retrieve the list
        AsyncTask<URL, Double, String> joinGameRequestTask = new AsyncTask<URL, Double, String>()
        {
            @Override
            protected String doInBackground(URL... urls)
            {
                // Request list
                URL url = urls[0];
                String responseString = null;
                try
                {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestMethod("PUT");

                    JSONObject post = new JSONObject();
                    post.put("gameId", gameId);
                    post.put("playerName", playerName);

                    System.out.println("Put: " + post);


                    OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                    out.write(post.toString());
                    out.close();


                    InputStream inputStream = connection.getInputStream();
                    Scanner scanner = new Scanner(inputStream);
                    StringBuilder stringBuilder = new StringBuilder();
                    while(scanner.hasNext())
                    {
                        stringBuilder.append(scanner.nextLine());
                    }
                    responseString = stringBuilder.toString();
                    Log.i("Join Response", responseString);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    try
                    {
                        listener.gameJoined(false, null);
                    }
                    catch (IOException e1)
                    {
                        e1.printStackTrace();
                    }
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                //Process list
                Gson gson = new Gson();
                JoinResponse response = gson.fromJson(responseString, JoinResponse.class);

                return response.playerId;
            }

            @Override
            protected void onPostExecute(String playerId)
            {
                super.onPostExecute(playerId);
                try
                {
                    listener.gameJoined(true, playerId);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        };

        // Execute the task
        joinGameRequestTask.execute(gameURL);
    }


    public interface LaunchListener
    {
        void missileLaunched(boolean success, boolean hit);
    }

    public void launchMissile(final String gameId, final String playerId, final int xPos, final int yPos, final LaunchListener listener) throws IOException
    {
        // Obtain URL to the server endpoint for game lobby
        URL gameURL = null;
        try
        {
            gameURL = new URL("http://battleship.pixio.com/api/v2/games/" + gameId);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            listener.missileLaunched(false, false);
        }

        // Create an asynchronous task to retrieve the list
        AsyncTask<URL, Double, Boolean> launchRequestTask = new AsyncTask<URL, Double, Boolean>()
        {
            @Override
            protected Boolean doInBackground(URL... urls)
            {
                // Request list
                URL url = urls[0];
                String responseString = null;
                try
                {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestProperty("Content-Type", "application/json");
                    connection.setRequestProperty("Accept", "application/json");
                    connection.setRequestMethod("POST");

                    JSONObject post = new JSONObject();
                    post.put("playerId", playerId);
                    post.put("xPos", xPos);
                    post.put("yPos", yPos);

                    System.out.println("Launch Post: " + post);


                    OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream());
                    out.write(post.toString());
                    out.close();


                    InputStream inputStream = connection.getInputStream();
                    Scanner scanner = new Scanner(inputStream);
                    StringBuilder stringBuilder = new StringBuilder();
                    while(scanner.hasNext())
                    {
                        stringBuilder.append(scanner.nextLine());
                    }
                    responseString = stringBuilder.toString();
                    Log.i("Response", responseString);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    listener.missileLaunched(false, false);
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                //Process list
                Gson gson = new Gson();
                LaunchResponse response = gson.fromJson(responseString, LaunchResponse.class);


                if(response == null)
                {
                    return false;
                }
                else
                {
                    return response.hit;
                }
            }

            @Override
            protected void onPostExecute(Boolean response)
            {
                super.onPostExecute(response);
                listener.missileLaunched(true, response);
            }
        };

        // Execute the task
        launchRequestTask.execute(gameURL);
    }

    public interface getTurnListener
    {
        void turnReceived(boolean success, boolean isYourTurn, String winner);
    }

    public void getTurn(final String playerId, final String gameId, final getTurnListener listener) throws IOException
    {
        // Obtain URL to the server endpoint for game lobby
        URL gameURL = null;
        try
        {
            gameURL = new URL("http://battleship.pixio.com/api/v2/games/" + gameId + "?playerId=" + playerId);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
            listener.turnReceived(false, false, null);
        }

        // Create an asynchronous task to retrieve the list
        AsyncTask<URL, Double, TurnResponse> getTurnRequestTask = new AsyncTask<URL, Double, TurnResponse>()
        {
            @Override
            protected TurnResponse doInBackground(URL... urls)
            {
                // Request list
                URL url = urls[0];
                String responseString = null;
                try
                {
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //connection.setDoOutput(true);
                    //connection.setDoInput(true);
                    //connection.setRequestProperty("Content-Type", "application/json");
                    //connection.setRequestProperty("Accept", "application/json");
                    //connection.setRequestProperty("playerId", playerId);
                    //connection.setRequestMethod("GET");

                    InputStream inputStream = connection.getInputStream();
                    Scanner scanner = new Scanner(inputStream);
                    StringBuilder stringBuilder = new StringBuilder();
                    while(scanner.hasNext())
                    {
                        stringBuilder.append(scanner.nextLine());
                    }
                    responseString = stringBuilder.toString();
                    Log.i("Response", responseString);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    listener.turnReceived(false, false, null);

                }

                //Process list
                Gson gson = new Gson();

                TurnResponse response = gson.fromJson(responseString, TurnResponse.class);

                return response;
            }

            @Override
            protected void onPostExecute(TurnResponse response)
            {
                super.onPostExecute(response);
                if(response != null)
                {
                    listener.turnReceived(true, response.isYourTurn, response.winner);
                }
            }
        };

        // Execute the task
        getTurnRequestTask.execute(gameURL);
    }
}
