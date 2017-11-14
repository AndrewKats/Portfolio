package cs5530;

import java.sql.*;

public class Address 
{
	public String streetAddress; 
	public String city;
	public String statePostal; 
	public int zip;
	private Statement stmt;
	
	public Address(Statement stmt, String streetAddress, String city, String statePostal, int zip)
	{
		this.streetAddress = streetAddress;
		this.city = city;
		this.statePostal = statePostal;
		this.zip = zip;
		this.stmt=stmt;
	}
	
	public void register() throws Exception
	{
		String addressQuery = "Insert into Addresses (streetAddress, city, statePostal, zip) value ("
				+ "\"" + streetAddress + "\",\""  + city + "\",\"" + statePostal + "\"," + zip + ")";
		
		
		try
		{
			stmt.executeUpdate(addressQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Address already exists\n");
			System.err.println(e.getMessage());
			throw(e);
		}
	}
}
