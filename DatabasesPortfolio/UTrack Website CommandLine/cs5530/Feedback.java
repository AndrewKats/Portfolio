package cs5530;

import java.sql.*;

public class Feedback 
{
	public int score;
	public String text;
	public int year;
	public int month;
	public int day;
	public String pid;
	public String login;
	private Statement stmt;
	
	
	public Feedback(Statement stmt, int score, String text, int year, int month, int day, String pid, String login)
	{
		this.stmt=stmt;
		this.score=score;
		this.text=text;
		this.year=year;
		this.month=month;
		this.day=day;
		this.pid=pid;
		this.login=login;
	}
	
	public void register() throws Exception
	{
		String query = "Insert into Feedback (score, text, fbdate, pid, login) values (" + score + ",\"" + text 
				+ "\",'" + year + "-" + month + "-" + day + "'," + pid + ",\"" + login + "\")";
		
		try
		{
			stmt.executeUpdate(query);			
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
	}
}
