package cs5530;

import java.sql.*;

public class Visit 
{
	public double cost; 
	public int partySize; 
	public String login; 
	public String pid; 
	public int year;
	public int month;
	public int day;
	private Statement stmt;
	private int vid;
	
	
	public Visit(Statement stmt, double cost, int partySize, String login, String pid, int year, int month, int day)
	{
		this.cost=cost;
		this.partySize=partySize;
		this.login=login;
		this.pid=pid;
		this.year=year;
		this.month=month;
		this.day=day;
		this.stmt=stmt;
	}
	
	public void register() throws Exception
	{
		String eventQuery = "Insert into VisEvent (cost, partySize) values (" + cost + "," + partySize + ")";	
		String vidQuery = "Select vid from VisEvent where vid >= ALL (Select vid from VisEvent)";	
		
		try
		{
			stmt.executeUpdate(eventQuery);			
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+eventQuery+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		ResultSet vidResult;
		try
		{
			vidResult = stmt.executeQuery(vidQuery);
			vidResult.next();
			vid = vidResult.getInt("vid");
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+vidQuery+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		String visitQuery = "Insert into Visit (login, pid, vid, visitdate) values (\"" + login + "\"," + pid + "," + vid + ",'" + year + "-" + month + "-" + day + "')";
		
		try
		{
			stmt.executeUpdate(visitQuery);			
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+visitQuery+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
	}
}
