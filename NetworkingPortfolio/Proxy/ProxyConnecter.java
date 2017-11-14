package cs4480;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

public class ProxyConnecter extends Thread
{
	BufferedReader inFromClient;
	DataOutputStream outToClient;
	
	public ProxyConnecter(Socket connectionSocket) throws IOException
	{
		inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		outToClient = new DataOutputStream(connectionSocket.getOutputStream());
	}
	
	public void run()
	{
		String requestString = "";
		try 
		{
			requestString = inFromClient.readLine() + "\n";
		} 
		catch (IOException e) 
		{
			//e.printStackTrace();
		}
		String nextInputLine = "";
		try 
		{
			while((nextInputLine = inFromClient.readLine()) != null && nextInputLine.length()!=0 )
			{			
				//nextInputLine = inFromClient.readLine();
				//System.out.println("Next input: " + nextInputLine + "\n");
				requestString += nextInputLine + "\n";
			}
		} 
		catch (IOException e) 
		{
			//e.printStackTrace();
		}
		
		
		System.out.println("This is the request: " + requestString);
		
		String host = "";
		
		String[] requestArray = requestString.split("\n");
		String firstLine = requestArray[0];
		
		ArrayList<String> headers = new ArrayList<String>();
		
		for(int i=1; i<requestArray.length; i++)
		{
			String[] splitLine = requestArray[i].split(" ");
			if(splitLine[0].equals("Connection:"))
			{
				requestArray[i] = "Connection: close";
			}
			else if(splitLine[0].equals("Host:"))
			{
				host = splitLine[1];
			}
			else
			{
				headers.add(requestArray[i]);
			}
		}
		
		//System.out.println(requestArray[1]);
			
		
		String[] firstArray = firstLine.split(" ");
		
		if(firstArray.length != 3)
		{
			try 
			{
				outToClient.writeBytes("Bad Request\n");
			} 
			catch (IOException e) 
			{
				//e.printStackTrace();
			}
		}
		
		String method = "";
		String url = "";
		String version = "";
		
		try
		{
			method = firstArray[0];
			url = firstArray[1];
			version = firstArray[2];
		}
		catch(Exception e)
		{
			
		}
		
		//System.out.println("Method: " + method);
		//System.out.println("URL: " + url);
		//System.out.println("Version: " + version);
		
		
		if(method.equals("POST") || method.equals("PUT") || method.equals("DELETE") || method.equals("OPTIONS") || method.equals("CONNECT"))
		{
			try 
			{
				outToClient.writeBytes("Not implemented\n");
			} 
			catch (IOException e) 
			{
				//e.printStackTrace();
			}
			return;
		}
		else if(method.equals("GET"))
		{
			
		}
		else
		{
			try 
			{
				outToClient.writeBytes("Bad Request\n");
			} 
			catch (IOException e) 
			{
				//e.printStackTrace();
			}
			return;
		}
		
		boolean isUrl = false;
		if(url.startsWith("http"))
		{
			isUrl = true;
		}
		
		String urlProtocol = "";
		String urlHost = "";
		int urlPort = 80;
		String urlFile = "";
		
		
		if(isUrl)
		{
			URL theUrl = null;
			try 
			{
				theUrl = new URL(url);
			} 
			catch (MalformedURLException e) 
			{
				//e.printStackTrace();
			}
			
			urlProtocol = theUrl.getProtocol();
			urlHost = theUrl.getHost();
			if(theUrl.getPort() != -1)
			{
				urlPort = theUrl.getPort();
			}
			urlFile = theUrl.getFile();
		}
		else
		{
			urlHost = host;
			urlFile = url;
		}
		
		
		
		//String returnString = connectClient("GET / HTTP/1.0\n\rHost: /~kobus/simple.html\n\rConnection: close\n\r", "www.cs.utah.edu", 80);
		
		//String returnString = connectClient("GET /~kobus/simple.html HTTP/1.0\n\rConnection: close\n\r", "www.cs.utah.edu", 80);
		
		StringBuilder returnBuilder = new StringBuilder();
		
		returnBuilder.append("GET ");
		returnBuilder.append(urlFile);
		returnBuilder.append(" ");
		returnBuilder.append(version);
		returnBuilder.append("\n\rConnection: close\n\r");
		for(String h : headers)
		{
			returnBuilder.append(h + "\n\r");
		}
		
		String returnInput = returnBuilder.toString();
		
		
		//System.out.println("returnInput: " + returnInput);
		//System.out.println("host: " + urlHost);
		//System.out.println("port: " + urlPort);
		
		String returnString = "";
		try 
		{
			returnString = connectClient(returnInput, urlHost, urlPort);
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
		}
		
		//String returnString = connectClient(requestString, "www.cs.utah.edu", 80);
		
		//outToClient.writeBytes(requestString.toUpperCase() + "\n");
		try 
		{
			System.out.println("Returning this: " + returnString);
			outToClient.writeBytes(returnString + "\n\r");
		} 
		catch (IOException e) 
		{
			//e.printStackTrace();
		}
	}
	
	public String connectClient(String message, String hostname, int port) throws Exception
	{
		
		String response = "";

		Socket clientSocket = new Socket(hostname, port);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		outToServer.writeBytes(message + "\n");
		
		String nextLine = "";
		while(nextLine != null)
		{
			nextLine = inFromServer.readLine();
			response += nextLine + "\n";
		}
		clientSocket.close();
		return response;
		
	}
}
