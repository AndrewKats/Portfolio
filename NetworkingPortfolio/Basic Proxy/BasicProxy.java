package cs4480;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

public class BasicProxy 
{
	public static void main(String[] args) throws Exception
	{
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String portString = br.readLine();
		int portInt = Integer.parseInt(portString);
		
		connectServer(portInt);
		
		
		br.close();
		
	}
	
	public static void connectServer(int port) throws Exception
	{
		
		ServerSocket welcomeSocket = new ServerSocket(port);
		Socket connectionSocket = welcomeSocket.accept();
		BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		
		while(true)
		{
			String requestString = inFromClient.readLine() + "\n";
			String nextInputLine = "";
			while((nextInputLine = inFromClient.readLine()) != null && nextInputLine.length()!=0 )
			{			
				//nextInputLine = inFromClient.readLine();
				//System.out.println("Next input: " + nextInputLine + "\n");
				requestString += nextInputLine + "\n";
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
				outToClient.writeBytes("Bad Request\n");
			}
			
			String method = firstArray[0];
			String url = firstArray[1];
			String version = firstArray[2];
			
			System.out.println("Method: " + method);
			System.out.println("URL: " + url);
			System.out.println("Version: " + version);
			
			
			if(method.equals("POST") || method.equals("PUT") || method.equals("DELETE") || method.equals("OPTIONS") || method.equals("CONNECT"))
			{
				outToClient.writeBytes("Not implemented\n");
				return;
			}
			else if(method.equals("GET"))
			{
				
			}
			else
			{
				outToClient.writeBytes("Bad Request\n");
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
				URL theUrl = new URL(url);
				
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
			
			
			System.out.println("returnInput: " + returnInput);
			System.out.println("host: " + urlHost);
			System.out.println("port: " + port);
			
			String returnString = connectClient(returnInput, urlHost, urlPort);
			
			//String returnString = connectClient(requestString, "www.cs.utah.edu", 80);
			
			//outToClient.writeBytes(requestString.toUpperCase() + "\n");
			outToClient.writeBytes(returnString + "\n");	
		}
		
	}
	
	public static String connectClient(String message, String hostname, int port) throws Exception
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
