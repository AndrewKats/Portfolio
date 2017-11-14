package cs5530;

import java.sql.*;
import java.util.Calendar;

public class User 
{
	public String login;
	public String name;
	public boolean isAdmin;
	public String password;
	public String phone;
	public Address address;
	private Statement stmt;
	
	public User(Statement stmt, String login, String name, boolean isAdmin, String password, String phone, Address address)
	{
		this.login=login;
		this.name=name;
		this.isAdmin=isAdmin;
		this.password=password;
		this.phone=phone;
		this.address=address;
		this.stmt=stmt;
	}
	

	public void register() throws Exception
	{
		String userQuery = "Insert into Users (login, name, isAdmin, password, phone) values (\"" 
				+ login + "\",\"" + name + "\"," + isAdmin + ",\"" + password + "\",\"" + phone + "\");";
		
		String addressQuery = "Insert into Addresses (streetAddress, city, statePostal, zip) value ("
				+ "\"" + address.streetAddress + "\",\""  + address.city + "\",\"" + address.statePostal + "\"," + address.zip + ")";
		
		String aidQuery = "Select aid from Addresses where streetAddress=\"" + address.streetAddress 
				+ "\" and city=\"" + address.city + "\" and statePostal=\"" + address.statePostal + "\" and zip=\"" + address.zip + "\"";
						
		try
		{
			stmt.executeUpdate(userQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+userQuery+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		try
		{
			stmt.executeUpdate(addressQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+addressQuery+"\n");
			System.err.println(e.getMessage());
			//throw(e);
		}
		
		ResultSet aidResult;
		try
		{
			aidResult = stmt.executeQuery(aidQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+aidQuery+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		String aidString = "";
		while (aidResult.next())
		{
			aidString = "" + aidResult.getInt("aid");
		}
		
		String hasAddressQuery = "Insert into UserHasAddress (login, aid) values (\"" + login + "\"," + aidString + ")";
		
		try
		{
			stmt.executeUpdate(hasAddressQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+hasAddressQuery+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
	}
	
	public void Trust(User trustedUser) throws Exception
	{
		String deleteQuery = "Delete from Trust where login1=\"" + login + "\" and login2=\"" + trustedUser.login + "\"";
		String query = "Insert into Trust (login1, login2, isTrusted) values (\"" + login + "\",\"" + trustedUser.login + "\",true)";
		
		try
		{
			stmt.executeUpdate(deleteQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+deleteQuery+"\n");
			//System.err.println(e.getMessage());
			throw(e);
		}
		
		try
		{
			stmt.executeUpdate(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			//System.err.println(e.getMessage());
			throw(e);
		}
	}
	
	public void Trust(String trustedLogin) throws Exception
	{
		String deleteQuery = "Delete from Trust where login1=\"" + login + "\" and login2=\"" + trustedLogin + "\"";
		String query = "Insert into Trust (login1, login2, isTrusted) values (\"" + login + "\",\"" + trustedLogin + "\",true)";
		
		try
		{
			stmt.executeUpdate(deleteQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+deleteQuery+"\n");
			//System.err.println(e.getMessage());
			throw(e);
		}
		
		try
		{
			stmt.executeUpdate(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			//System.err.println(e.getMessage());
			throw(e);
		}
	}
	
	public void Distrust(User distrustedUser) throws Exception
	{
		String deleteQuery = "Delete from Trust where login1=\"" + login + "\" and login2=\"" + distrustedUser.login + "\"";
		String query = "Insert into Trust (login1, login2, isTrusted) values (\"" + login + "\",\"" + distrustedUser.login + "\",false)";
		
		try
		{
			stmt.executeUpdate(deleteQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+deleteQuery+"\n");
			//System.err.println(e.getMessage());
			throw(e);
		}
		
		try
		{
			stmt.executeUpdate(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			//System.err.println(e.getMessage());
			throw(e);
		}
	}
	
	public void Distrust(String distrustedLogin) throws Exception
	{
		String deleteQuery = "Delete from Trust where login1=\"" + login + "\" and login2=\"" + distrustedLogin + "\"";
		String query = "Insert into Trust (login1, login2, isTrusted) values (\"" + login + "\",\"" + distrustedLogin + "\",false)";
		
		try
		{
			stmt.executeUpdate(deleteQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+deleteQuery+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
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
	
	public void favorite(int pid) throws Exception
	{
		Calendar cal = Calendar.getInstance();
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH) + 1;
		int year = cal.get(Calendar.YEAR);
		
		String query = "Insert into Favorites (pid, login, favdate) values (" + pid + ",\"" + login + "\",'" + year + "-" + month + "-" + day + "')";
		
		try 
		{
			stmt.executeUpdate(query);
		} 
		catch (Exception e) 
		{
			System.err.println("Already favorited");
			//System.err.println(e.getMessage());
			//throw (e);
		}
		
	}
	
	public void rateFeedback(int fid, int rating) throws Exception
	{
		String query = "Insert into Rates (login, fid, rating) values (\"" + login + "\"," + fid + "," + rating + ")";
		try 
		{
			stmt.executeUpdate(query);
		} 
		catch (Exception e) 
		{
			// stuff
			//System.err.println("Already rated");
			//System.err.println(e.getMessage());
			throw (e);
		}
	}
}
