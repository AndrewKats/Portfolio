package cs4480;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

/*
 * Andrew Katsanevas
 * 2/13/2017
 * CS 4480
 * PA 1
 */
public class ProxyConnector extends Thread
{
	// Input from client
	BufferedReader inFromClient;
	
	// Output to client
	DataOutputStream outToClient;
	
	// Socket used to connect to client
	Socket clientSocket;
	
	// Constructor that sets global client variables
	public ProxyConnector(Socket connectionSocket) throws IOException
	{
		inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
		outToClient = new DataOutputStream(connectionSocket.getOutputStream());
		clientSocket = connectionSocket;
	}
	
	
	// Run when the threaded object is started
	public void run()
	{
		// Obtain a request from the client
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
				requestString += nextInputLine + "\n";
			}
		} 
		catch (IOException e) 
		{
			//e.printStackTrace();
		}
		
		
		//System.out.println("This is the request: " + requestString);
		
		
		
		String host = "";
		String[] requestArray = requestString.split("\n");
		String firstLine = requestArray[0];
		
		// Read the headers
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
			
		
		// Check the request line for the right amount of arguments
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
		
		
		// Put the input arguments into variables
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
				
		
		if(!version.contains("1.0"))
		{
			try 
			{
				outToClient.writeBytes("Only version 1.0 is supported\n");
			} 
			catch (IOException e) 
			{
				//e.printStackTrace();
			}
			return;
		}
		
		// Catch error of using methods that aren't implemented
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
				outToClient.writeBytes("Not implemented\n");
			} 
			catch (IOException e) 
			{
				//e.printStackTrace();
			}
			return;
		}
		
		// Check if the URL is a complete URL
		boolean isUrl = false;
		if(url.startsWith("http"))
		{
			isUrl = true;
		}
		
		
		
		// Store pieces of the URL in variables
		String urlProtocol = "";
		String urlHost = "";
		int urlPort = 80;
		String urlFile = "";
		
		// If complete 
		if(isUrl)
		{
			URL theUrl = null;
			try 
			{
				theUrl = new URL(url);
			} 
			catch (MalformedURLException e) 
			{
				try 
				{
					outToClient.writeBytes("Bad URL");
				} 
				catch (IOException e1) 
				{
					//e1.printStackTrace();
				}
				//System.out.println("Bad URL");
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
		// If partial
		else
		{
			urlHost = host;
			urlFile = url;
		}
		
		
		// Build the input to the HTTP request
		StringBuilder returnBuilder = new StringBuilder();
		returnBuilder.append("GET ");
		returnBuilder.append(urlFile);
		returnBuilder.append(" ");
		returnBuilder.append(version);
		returnBuilder.append("\r\nConnection: close\r\n");
		for(String h : headers)
		{
			returnBuilder.append(h + "\r\n");
		}
		
		String returnInput = returnBuilder.toString();
		
		
		try 
		{	
			// Now send the given request to the appropriate server
			connectToServer(returnInput, urlHost, urlPort);			
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
		}
	}
	
	
	public void connectToServer(String message, String hostname, int port) throws NoSuchAlgorithmException, IOException
	{
		//System.out.println("The message: " + message);
		
		// Make a new socket to connect to the server
		Socket serverSocket = new Socket(hostname, port);
		
		// Send to the server
		DataOutputStream outToServer = new DataOutputStream(serverSocket.getOutputStream());
		outToServer.writeBytes(message + "\r\n");
		
		// Get a response from the server
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];
		while(true)
		{
			int n = serverSocket.getInputStream().read(buffer);
			if(n < 0)
			{
				break;
			}
			byteStream.write(buffer,0,n);
		}
		byte received[] = byteStream.toByteArray();
		
		// Store the byte values of new line and carriage return characters
		byte nLine = (byte) '\n';
		byte cReturn = (byte) '\r';
		
		// Go through the received bytes and go until the new lines/carriage returns are found
		int offSet = 0;
		for(int i=0; i < received.length; i++)
		{
			if(received[i] == cReturn)
			{
				if(received[i+1] == nLine && received[i+2] == cReturn && received[i + 3] == nLine)
				{
					offSet = i + 4;
					break;
				}
			}
		}
		
		// Get the portion we want to check with Cymru
		byte[] sendToCymru = Arrays.copyOfRange(received , offSet,  received.length);
		
		// Get the header section
		byte[] headerSection = Arrays.copyOf(received, offSet);
		
		// Close the server socket, we're done with it
		serverSocket.close();
		
		// Check the received file for malware
		boolean safe = malwareSafe(sendToCymru);
		
		
		if(safe)
		{
			//System.out.println("SAFE");
			
			// If it's safe, return it to the client
			outToClient.write(received);
		}
		else
		{
			//System.out.println("NOT SAFE");
			
			// If it's not safe, tell that to the client
			outToClient.write(headerSection);
			outToClient.writeBytes("<!DOCTYPE html>\r\n<html>\r\n<body>\r\n<h1>BLOCKED: This may be malware</h1>\r\n</body>\r\n</html>\r\n");
		}
		
		return;
	}
	
	
	// Use Cymru to check a file for malware
	public boolean malwareSafe(byte[] check) throws NoSuchAlgorithmException, UnknownHostException, IOException
	{
		// Digest the file in MD5 mode
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] digested = md.digest(check);
		
		// Build a String with the bytes
		StringBuilder hashBuilder = new StringBuilder();
		for(byte bt : digested)
		{
			hashBuilder.append(String.format("%02x", bt));
		}
		String theHash = hashBuilder.toString();
		
		//System.out.println("The hash: " + theHash);
		
		// Create a new connection with Cymru
		Socket cymruSocket = new Socket("hash.cymru.com", 43);
		BufferedReader inFromCymru = new BufferedReader(new InputStreamReader(cymruSocket.getInputStream()));
		DataOutputStream outToCymru = new DataOutputStream(cymruSocket.getOutputStream());
		
		// Send Cymru the file in question
		outToCymru.writeBytes(theHash+"\r\n");
		outToCymru.flush();
		
		// Receive the response from Cymru
		String cymruResponse = "";		
		String nextLine = "";
		while(nextLine != null)
		{
			nextLine = inFromCymru.readLine();
			cymruResponse += nextLine + "\r\n";
		}
		cymruSocket.close();
		
		
		// If Cymru does not have the file in its malware database, it's safe
		if(cymruResponse.contains("NO_DATA"))
		{
			return true;
		}
		// ... or not
		return false;
	}

}
