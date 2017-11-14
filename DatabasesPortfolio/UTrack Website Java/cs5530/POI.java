package cs5530;

import java.sql.*;

public class POI 
{
	public String name; 
	public String url; 
	public String phone; 
	public int established; 
	public String hours; 
	public double price;
	public Address address;
	public String pidString;
	private Statement stmt;
	
	public POI(Statement stmt, String name, String url, String phone, int established, String hours, double price, Address address) throws SQLException
	{
		this.name=name;
		this.url=url;
		this.phone=phone;
		this.established=established;
		this.hours=hours;
		this.price=price;
		this.address=address;
		this.stmt=stmt;
	}
	
	public void register() throws Exception
	{		
		String POIQuery = "Insert into POI (name, url, phone, established, hours, price) values (\"" 
				+ name + "\",\"" + url + "\",\"" + phone + "\"," + established + ",\"" + hours + "\"," + price + ");";
		
		String addressQuery = "Insert into Addresses (streetAddress, city, statePostal, zip) value ("
				+ "\"" + address.streetAddress + "\",\""  + address.city + "\",\"" + address.statePostal + "\"," + address.zip + ")";
			
		String aidQuery = "Select aid from Addresses where streetAddress=\"" + address.streetAddress 
				+ "\" and city=\"" + address.city + "\" and statePostal=\"" + address.statePostal + "\" and zip=\"" + address.zip + "\"";		
		
		try
		{
			stmt.executeUpdate(POIQuery);			
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+POIQuery+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		try
		{
			stmt.executeUpdate(addressQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Address already in database");
			// System.err.println(e.getMessage());
			// throw(e);
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
		
		
		String pidQuery = "Select pid from POI where name=\"" + name + "\" and url=\"" + url + "\" and phone=\"" + phone + "\" and established=" 
				+ established + " and hours=\"" + hours + "\" and price=" + price;	
		ResultSet pidResult;
		try
		{
			pidResult = stmt.executeQuery(pidQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+pidQuery+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}			
		pidString = "";
		while (pidResult.next())
		{
			pidString = "" + pidResult.getInt("pid");
		}
		
		
		String hasAddressQuery = "Insert into POIHasAddress (pid, aid) values (" + pidString + "," + aidString + ")";
		
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
	
	
}
