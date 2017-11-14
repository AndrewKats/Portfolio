package cs4480;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;

/*
 * Andrew Katsanevas
 * 2/13/2017
 * CS 4480
 * PA 1
 */
public class ProxyAccepter 
{
	// Creates connections between this server and clients
	public static void main(String[] args) throws Exception
	{
		// Get a user-inputted port
		System.out.println("Enter a port and press ENTER");	
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String portString = br.readLine();
		int portInt = Integer.parseInt(portString);
		
		// Open connection to clients
		connectToClients(portInt);
		
		br.close();
		
	}
	
	// Open the server to clients
	public static void connectToClients(int port) throws Exception
	{
		ServerSocket welcomeSocket = new ServerSocket(port);
		// Continually accept new clients
		while(true)
		{
			// Start a new threaded object for each client
			new ProxyConnecter(welcomeSocket.accept()).start();
		}
	}
	
}
