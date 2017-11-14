/*
 *  Written by Andrew Katsanevas 
 *  for CS 5530
 *  3/23/2016
 */

package cs5530;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

public class Driver 
{
	private static User loggedIn;
	private static Statement stmt;
	
	@SuppressWarnings("null")
	public static void main(String[] args) 
	{
		Connector con=null;
		String choice;
		int c=0;
		String login;
		String password;
		boolean isLoggedIn = false;
		try 
		{
			con = new Connector();
			//System.out.println ("Database connection established");
			stmt = con.stmt;
			
			BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
			
			while(true)
			{
				 if(!isLoggedIn)
				 {
					 System.out.println("\n        Welcome to the UTrack System     ");
			    	 System.out.println("1. Login");
			    	 System.out.println("2. Register as a new user");
			    	 System.out.println("3. Exit");
			    	 System.out.println("Please enter your choice:");
			    	 
			    	 while ((choice = in.readLine()) == null && choice.length() == 0);
	            	 try
	            	 {
	            		 c = Integer.parseInt(choice);
	            	 }
	            	 catch (Exception e)
	            	 {
	            		 
	            		 continue;
	            	 }
	            	 if(c<1 || c>3)
	            	 {
	            		 continue;
	            	 }
				 }
				 else
				 {
					 c=1;
				 }
            	 // Login
            	 if(c==1)
            	 {
            		 if(!isLoggedIn)
            		 {
	            		 System.out.println("Enter your username:");
	            		 while ((login = in.readLine()) == null && login.length() == 0);
	            		 System.out.println("Enter your password:");
	            		 while ((password = in.readLine()) == null && password.length() == 0);
	            		 
	            		 try
	            		 {
	            			 loggedIn = existingUser(stmt, login);
	            		 }
	            		 catch(Exception e)
	            		 {
	            			 System.out.println("User not found\n");
	            			 continue;
	            		 }
	            		 if(!(loggedIn.password).equals(password))
	            		 {
	            			 System.out.println("Incorrect password\n");
	            			 continue;
	            		 }
	            		 isLoggedIn = true;
            		 }
            		 
            		 System.out.println("\nWhat would you like to do?");
            		 System.out.println("1.  Search for places");
    		    	 System.out.println("2.  Add a visit");
    		    	 System.out.println("3.  Add a favorite place");
    		    	 System.out.println("4.  Give feedback");
    		    	 System.out.println("5.  Rate feedback");
    		    	 System.out.println("6.  Find useful feedback for a place");
    		    	 System.out.println("7.  Trust or distrust a user");
    		    	 System.out.println("8.  Find degrees of separation between two users");
    		    	 System.out.println("9.  Find the most popular places in a category");
    		    	 System.out.println("10. Find the most expensive places in a category");
    		    	 System.out.println("11. Find the most highly rated places in a category");
    		    	 System.out.println("12. Exit");
    		    	 
    		    	 if(loggedIn.isAdmin)
    		    	 {
    		    		 System.out.println("-------------------");
    		    		 System.out.println("Admin Options");
    		    		 System.out.println("-------------------");
    		    		 System.out.println("13. Add a new POI");
    		    		 System.out.println("14. Update the info of a POI");
    		    		 System.out.println("15. Find most trusted users");
    		    		 System.out.println("16. Find most useful users");
    		    	 }
    		    	
    		    	 System.out.println("Please enter your choice:");
            		 
    		    	 while ((choice = in.readLine()) == null && choice.length() == 0);
                	 try
                	 {
                		 c = Integer.parseInt(choice);
                	 }
                	 catch (Exception e)
                	 {
                		 
                		 continue;
                	 }
                	 if(c<1 || c>16)
                	 {
                		 continue;
                	 }
                	 
                	 // Search
                	 if(c==1)
                	 {
                		 String lowPrice;
                		 String highPrice;
                		 String city=null;
                		 String state=null;
                		 ArrayList<String> keywords = new ArrayList<String>();
                		 ArrayList<String> categories = new ArrayList<String>();
                		 String sortMethod="none";
                		 
                		 System.out.println("Enter a minimum price:");
            			 while ((lowPrice = in.readLine()) == null && lowPrice.length() == 0); 
            			 
            			 System.out.println("Enter a maximum price:");
            			 while ((highPrice = in.readLine()) == null && highPrice.length() == 0); 
            			  
            			 
            			 System.out.println("Would you like to filter by state?");
            			 System.out.println("1. yes");
            			 System.out.println("2. no");
            			 System.out.println("Please enter your choice:");
            			 while ((choice = in.readLine()) == null && choice.length() == 0);
                    	 try
                    	 {
                    		 c = Integer.parseInt(choice);
                    	 }
                    	 catch (Exception e)
                    	 {
                    		 
                    		 continue;
                    	 }
                    	 if(c<1 || c>2)
                    	 {
                    		 continue;
                    	 }
                    	 
                    	 if(c==1)
                    	 {
                    		 System.out.println("Enter the postal code of the state:");
                			 while ((state = in.readLine()) == null && state.length() == 0); 
                    	 }	       
            			 
                    	 
                    	 System.out.println("Would you like to filter by city?");
            			 System.out.println("1. yes");
            			 System.out.println("2. no");
            			 System.out.println("Please enter your choice:");
            			 while ((choice = in.readLine()) == null && choice.length() == 0);
                    	 try
                    	 {
                    		 c = Integer.parseInt(choice);
                    	 }
                    	 catch (Exception e)
                    	 {
                    		 
                    		 continue;
                    	 }
                    	 if(c<1 || c>2)
                    	 {
                    		 continue;
                    	 }
                    	 
                    	 if(c==1)
                    	 {
                    		 System.out.println("Enter the city:");
                			 while ((city = in.readLine()) == null && city.length() == 0); 
                    	 }	 
                    	 
                    	 
                    	 System.out.println("Would you like to filter by keywords?");
            			 System.out.println("1. yes");
            			 System.out.println("2. no");
            			 System.out.println("Please enter your choice:");
            			 while ((choice = in.readLine()) == null && choice.length() == 0);
                    	 try
                    	 {
                    		 c = Integer.parseInt(choice);
                    	 }
                    	 catch (Exception e)
                    	 {
                    		 
                    		 continue;
                    	 }
                    	 if(c<1 || c>2)
                    	 {
                    		 continue;
                    	 }
                    	 
                    	 if(c==1)
                    	 {
                    		 boolean done = false;                		 
                    		 while(!done)
                    		 {
                    			 String keyword;
	                    		 System.out.println("Enter a keyword:");
	                			 while ((keyword = in.readLine()) == null && keyword.length() == 0); 
	                			 
	                			 keywords.add(keyword);
	                			 
	                			 System.out.println("Would you like to add another keyword?");
	                			 System.out.println("1. yes");
	                			 System.out.println("2. no");
	                			 System.out.println("Please enter your choice:");
	                			 while ((choice = in.readLine()) == null && choice.length() == 0);
	                        	 try
	                        	 {
	                        		 c = Integer.parseInt(choice);
	                        	 }
	                        	 catch (Exception e)
	                        	 {
	                        		 
	                        		 continue;
	                        	 }
	                        	 if(c<1 || c>2)
	                        	 {
	                        		 continue;
	                        	 }
	                        	 
	                        	 if(c==2)
	                        	 {
	                        		 done=true;
	                        	 }
                    		 }
                    	 }	 
                    	 
                    	 
                    	 System.out.println("Would you like to filter by category?");
            			 System.out.println("1. yes");
            			 System.out.println("2. no");
            			 System.out.println("Please enter your choice:");
            			 while ((choice = in.readLine()) == null && choice.length() == 0);
                    	 try
                    	 {
                    		 c = Integer.parseInt(choice);
                    	 }
                    	 catch (Exception e)
                    	 {
                    		 
                    		 continue;
                    	 }
                    	 if(c<1 || c>2)
                    	 {
                    		 continue;
                    	 }
                    	 
                    	 if(c==1)
                    	 {
                    		 boolean done = false;                		 
                    		 while(!done)
                    		 {
                    			 String category;
	                    		 System.out.println("Enter a category:");
	                			 while ((category = in.readLine()) == null && category.length() == 0); 
	                			 
	                			 categories.add(category);
	                			 
	                			 System.out.println("Would you like to add another category?");
	                			 System.out.println("1. yes");
	                			 System.out.println("2. no");
	                			 System.out.println("Please enter your choice:");
	                			 while ((choice = in.readLine()) == null && choice.length() == 0);
	                        	 try
	                        	 {
	                        		 c = Integer.parseInt(choice);
	                        	 }
	                        	 catch (Exception e)
	                        	 {
	                        		 
	                        		 continue;
	                        	 }
	                        	 if(c<1 || c>2)
	                        	 {
	                        		 continue;
	                        	 }
	                        	 
	                        	 if(c==2)
	                        	 {
	                        		 done=true;
	                        	 }
                    		 }
                    	 }	 
                    	 
                    	 System.out.println("Sort by");
            			 System.out.println("1. price");
            			 System.out.println("2. average score");
            			 System.out.println("3. average score from trusted users");
            			 System.out.println("4. none");
            			 System.out.println("Please enter your choice:");
            			 while ((choice = in.readLine()) == null && choice.length() == 0);
                    	 try
                    	 {
                    		 c = Integer.parseInt(choice);
                    	 }
                    	 catch (Exception e)
                    	 {
                    		 
                    		 continue;
                    	 }
                    	 if(c<1 || c>4)
                    	 {
                    		 continue;
                    	 }
                    	 
                    	 if(c==1)
                    	 {
                    		 sortMethod="price";
                    	 }
                    	 if(c==2)
                    	 {
                    		 sortMethod="average score";
                    	 }
                    	 if(c==3)
                    	 {
                    		 sortMethod="average trusted score";
                    	 }

                    	 
                		 searchPOI(loggedIn.login, stmt, Double.parseDouble(lowPrice), Double.parseDouble(highPrice), city, state, keywords, categories, sortMethod);
                	 }
                	 // Visit
                	 else if(c==2)
                	 {
                		 ArrayList<Visit> visits = new ArrayList<Visit>();
                		 boolean done = false;
                		 while(!done)
                		 {
	                		 String name;
	                		 String pid;
	                		 String cost;
	                		 String partySize; 
	                		 String year;
	                		 String month;
	                		 String day;
	                		 
	                		 System.out.println("Enter the name of the place you visited:");
	            			 while ((name = in.readLine()) == null && name.length() == 0);         			 
	            			
	            			 String query = "Select p.pid, p.name, a.streetAddress, a.city, a.statePostal "
	            			 		+ "from POI p, POIHasAddress ha, Addresses a "
	            			 		+ "where p.pid=ha.pid and ha.aid=a.aid and p.name like \"%" + name + "%\"";
	            			 
	            			 String resultstr="";
							ResultSet results;
							try
							{
								results = stmt.executeQuery(query);
					        } 
							catch(Exception e) 
							{
								System.err.println("Unable to execute query:"+query+"\n");
								System.err.println(e.getMessage());
								throw(e);
							}
							
							resultstr += "Places: \npid\tname\taddress\n";
							while (results.next())
							{
								resultstr += results.getInt("pid") + "\t" + results.getString("name") + "\t" + results.getString("streetAddress") + ", " + results.getString("city") + ", " + results.getString("statePostal") + "\n";	
							}
							System.out.println(resultstr);
	            				
							System.out.println("Choose the pid:");
	            			while ((pid = in.readLine()) == null && pid.length() == 0); 
	            			 
	            			System.out.println("Enter how much the visit cost:");
	            			while ((cost = in.readLine()) == null && cost.length() == 0); 
	            			
	            			System.out.println("Enter the size of your party:");
	            			while ((partySize = in.readLine()) == null && partySize.length() == 0);
	            			
	            			System.out.println("Enter the year of your visit:");
	            			while ((year = in.readLine()) == null && year.length() == 0); 
	            			
	            			System.out.println("Enter the month of your visit (as a number):");
	            			while ((month = in.readLine()) == null && month.length() == 0); 
	            			
	            			System.out.println("Enter the day of the month of your visit:");
	            			while ((day = in.readLine()) == null && day.length() == 0); 
	            			
	            			Visit newVisit = new Visit(stmt, Double.parseDouble(cost), Integer.parseInt(partySize), loggedIn.login, pid, Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
	            			visits.add(newVisit);
	            			
	            			System.out.println("Would you like to add another visit?");
	            			System.out.println("1. yes");
	            			System.out.println("2. no");
	            			 System.out.println("Please enter your choice:");
	            			 while ((choice = in.readLine()) == null && choice.length() == 0);
	                    	 try
	                    	 {
	                    		 c = Integer.parseInt(choice);
	                    	 }
	                    	 catch (Exception e)
	                    	 {
	                    		 
	                    		 continue;
	                    	 }
	                    	 if(c<1 || c>2)
	                    	 {
	                    		 continue;
	                    	 }
	                    	 
	                    	 if(c==2)
	                    	 {
	                    		done = true; 
	                    	 }	            			         			
                		 }	
                		
                		 for(Visit v : visits)
                		 {
                			 System.out.println("Visited POI " + v.pid + " on " + v.month + "/" + v.day + "/" + v.year + " in a party of " + v.partySize + " costing $" + v.cost);
                		 }
                		 
                		 System.out.println("Confirm these visits?");
	            			System.out.println("1. yes");
	            			System.out.println("2. no");
	            			 System.out.println("Please enter your choice:");
	            			 while ((choice = in.readLine()) == null && choice.length() == 0);
	                    	 try
	                    	 {
	                    		 c = Integer.parseInt(choice);
	                    	 }
	                    	 catch (Exception e)
	                    	 {
	                    		 
	                    		 continue;
	                    	 }
	                    	 
	                    	 if(c<1 || c>2)
	                    	 {
	                    		 continue;
	                    	 }
	      
	                    	 if(c==2)
	                    	 {
	                    		continue; 
	                    	 }	  
                		 
                		 for(Visit v : visits)
                		 {
                			 v.register();
                			 System.out.println("Based on your visit to POI " + v.pid + " we suggest...");
                			 suggestOnVisit(v.login, Integer.parseInt(v.pid));
                		 }
                		 
                	 }
                	 // Favorite
                	 else if(c==3)
                	 {
                		 String pid;
                		 String name;
                		 
                		 System.out.println("Add favorite by pid or name?");
                		 System.out.println("1. pid");
                		 System.out.println("2. name");
						 System.out.println("Please enter your choice:");
						 while ((choice = in.readLine()) == null && choice.length() == 0);
						 try
						 {
							 c = Integer.parseInt(choice);
						 }
						 catch (Exception e)
						 {
							 
							 continue;
						 }
						 if(c<1 || c>2)
                    	 {
                    		 continue;
                    	 }
      
						 // By pid
						 if(c==1)
						 {
							 System.out.println("Enter the pid of the place to favorite:");
	            			 while ((pid = in.readLine()) == null && pid.length() == 0);  
	            			 loggedIn.favorite(Integer.parseInt(pid));
						 }
						 // By name
                    	 if(c==2)
                    	 {
                    		 System.out.println("Enter the name of the place to favorite:");
	            			 while ((name = in.readLine()) == null && name.length() == 0);         			 
	            			
	            			 String query = "Select p.pid, p.name, a.streetAddress, a.city, a.statePostal "
	            			 		+ "from POI p, POIHasAddress ha, Addresses a "
	            			 		+ "where p.pid=ha.pid and ha.aid=a.aid and p.name like \"%" + name + "%\"";
	            			 
	            			 String resultstr="";
							ResultSet results;
							try
							{
								results = stmt.executeQuery(query);
					        } 
							catch(Exception e) 
							{
								System.err.println("Unable to execute query:"+query+"\n");
								System.err.println(e.getMessage());
								throw(e);
							}
							
							resultstr += "Places: \npid\tname\t\taddress\n";
							while (results.next())
							{
								resultstr += results.getInt("pid") + "\t" + results.getString("name") + "\t\t" + results.getString("streetAddress") + ", " + results.getString("city") + ", " + results.getString("statePostal") + "\n";	
							}
							System.out.println(resultstr);
	            				
							System.out.println("Choose the pid:");
	            			while ((pid = in.readLine()) == null && pid.length() == 0); 
	            			loggedIn.favorite(Integer.parseInt(pid));
                    	 }	
                	 }
                	 // Give feedback
                	 else if(c==4)
                	 {
                		 String name;
                		 String pid;
                		 
                		 System.out.println("Enter the name of the place to review:");
            			 while ((name = in.readLine()) == null && name.length() == 0);         			 
            			
            			 String query = "Select p.pid, p.name, a.streetAddress, a.city, a.statePostal "
            			 		+ "from POI p, POIHasAddress ha, Addresses a "
            			 		+ "where p.pid=ha.pid and ha.aid=a.aid and p.name like \"%" + name + "%\"";
            			 
            			 String resultstr="";
						ResultSet results;
						try
						{
							results = stmt.executeQuery(query);
				        } 
						catch(Exception e) 
						{
							System.err.println("Unable to execute query:"+query+"\n");
							System.err.println(e.getMessage());
							throw(e);
						}
						
						resultstr += "Places: \npid\tname\taddress\n";
						while (results.next())
						{
							resultstr += results.getInt("pid") + "\t" + results.getString("name") + "\t" + results.getString("streetAddress") + ", " + results.getString("city") + ", " + results.getString("statePostal") + "\n";	
						}
						System.out.println(resultstr);
            				
						System.out.println("Choose the pid:");
            			while ((pid = in.readLine()) == null && pid.length() == 0); 
            			
            			String score;
            			String text = "";
            			
            			
            			System.out.println("Enter a score:");
           			 	while ((score = in.readLine()) == null && score.length() == 0);
           			 	
           			 	Calendar cal = Calendar.getInstance();
           			 	int day = cal.get(Calendar.DAY_OF_MONTH);
           			 	int month = cal.get(Calendar.MONTH) + 1;
           			 	int year = cal.get(Calendar.YEAR);
           			 	        			 	
           			 	System.out.println("Would you like to add a text review to your feedback?");
           			 	System.out.println("1. yes");
           			 	System.out.println("2. no");;
           			 	System.out.println("Please enter your choice:");
           			 	while ((choice = in.readLine()) == null && choice.length() == 0);
           			 	try
           			 	{
           			 		c = Integer.parseInt(choice);
           			 	}	
           			 	catch (Exception e)
           			 	{
                   		 
           			 		continue;
           			 	}
                   	 
           			 	if(c<1 || c>3)
           			 	{
           			 		continue;
           			 	}
           			 	
           			 	if(c==1)
           			 	{
           			 		System.out.println("Enter text feedback:");
           			 		while ((text = in.readLine()) == null && text.length() == 0);
           			 	}
           			 	
           			 	Feedback fb = new Feedback(stmt, Integer.parseInt(score), text, year, month, day, pid, loggedIn.login);
           			 	fb.register();
                	 }
                	 // Rate feedback
                	 else if(c==5)
                	 {
                		 String name;
                		 String pid;
                		 
                		 System.out.println("Enter the name of a place to see its feedback:");
            			 while ((name = in.readLine()) == null && name.length() == 0);         			 
            			
            			 String query = "Select p.pid, p.name, a.streetAddress, a.city, a.statePostal "
            			 		+ "from POI p, POIHasAddress ha, Addresses a "
            			 		+ "where p.pid=ha.pid and ha.aid=a.aid and p.name like \"%" + name + "%\"";
            			 
            			 String resultstr="";
						ResultSet results;
						try
						{
							results = stmt.executeQuery(query);
				        } 
						catch(Exception e) 
						{
							System.err.println("Unable to execute query:"+query+"\n");
							System.err.println(e.getMessage());
							throw(e);
						}
						
						resultstr += "Places: \npid\tname\taddress\n";
						while (results.next())
						{
							resultstr += results.getInt("pid") + "\t" + results.getString("name") + "\t" + results.getString("streetAddress") + ", " + results.getString("city") + ", " + results.getString("statePostal") + "\n";	
						}
						System.out.println(resultstr);
            				
						System.out.println("Choose the pid:");
            			while ((pid = in.readLine()) == null && pid.length() == 0); 
            			
            			// more
            			   			 
           			
            			String fid;
            			String rating;
            			
           			 	String fidQuery = "Select f.fid, f.login, f.score, f.text from POI p, Feedback f where p.pid=f.pid and p.pid=" + pid;
           			 
           			 	String fidResultstr="";
						ResultSet fidResults;
						try
						{
							fidResults = stmt.executeQuery(fidQuery);
				        } 
						catch(Exception e) 
						{
							System.err.println("Unable to execute query:"+fidQuery+"\n");
							System.err.println(e.getMessage());
							throw(e);
						}
						
						fidResultstr += "Places: \nfid\tuser\tscore\ttext\n";
						while (fidResults.next())
						{
							fidResultstr += fidResults.getInt("fid") + "\t" + fidResults.getString("login") + "\t" + fidResults.getInt("score") + "\t" + fidResults.getString("text") + "\n";	
						}
						System.out.println(fidResultstr);
           				
						System.out.println("Choose the fid:");
						while ((fid = in.readLine()) == null && fid.length() == 0); 
           			
						System.out.println("Give a usefulness rating (0, 1, or 2):");
						while ((rating = in.readLine()) == null && rating.length() == 0); 
           			
            			loggedIn.rateFeedback(Integer.parseInt(fid), Integer.parseInt(rating));
                	 }
                	 // Find useful feedback 
                	 else if(c==6)
                	 {               		 
                		 String name;
                		 String pid;
                		 
                		 System.out.println("Enter the name of the place to see its feedback:");
            			 while ((name = in.readLine()) == null && name.length() == 0);         			 
            			
            			 String query = "Select p.pid, p.name, a.streetAddress, a.city, a.statePostal "
            			 		+ "from POI p, POIHasAddress ha, Addresses a "
            			 		+ "where p.pid=ha.pid and ha.aid=a.aid and p.name like \"%" + name + "%\"";
            			 
            			 String resultstr="";
						ResultSet results;
						try
						{
							results = stmt.executeQuery(query);
				        } 
						catch(Exception e) 
						{
							System.err.println("Unable to execute query:"+query+"\n");
							System.err.println(e.getMessage());
							throw(e);
						}
						
						resultstr += "Places: \npid\tname\taddress\n";
						while (results.next())
						{
							resultstr += results.getInt("pid") + "\t" + results.getString("name") + "\t" + results.getString("streetAddress") + ", " + results.getString("city") + ", " + results.getString("statePostal") + "\n";	
						}
						System.out.println(resultstr);
            				
						System.out.println("Choose the pid:");
            			while ((pid = in.readLine()) == null && pid.length() == 0); 
            			
            			String limit;
               		 
               		 	System.out.println("Enter the limit of feedbacks:");
               		 	while ((limit = in.readLine()) == null && limit.length() == 0);
               		 	
               		 	usefulFeedbacks(Integer.parseInt(pid), Integer.parseInt(limit));
                	 }
                	 // Trust
                	 else if(c==7)
                	 {
                		 String trustUser;
                		 	                    	 
                		 System.out.println("Enter the username of a user:");
            			 while ((trustUser = in.readLine()) == null && trustUser.length() == 0);  
                	
            			 System.out.println("Do you trust or distrust this user?");
                		 System.out.println("1. trust");
                		 System.out.println("2. distrust");
                		 System.out.println("3. cancel");
                		 System.out.println("Please enter your choice:");
            			 while ((choice = in.readLine()) == null && choice.length() == 0);
                    	 try
                    	 {
                    		 c = Integer.parseInt(choice);
                    	 }
                    	 catch (Exception e)
                    	 {
                    		 
                    		 continue;
                    	 }
                    	 
                    	 if(c<1 || c>3)
                    	 {
                    		 continue;
                    	 }
      
                    	 if(c==1)
                    	 {
                    		loggedIn.Trust(trustUser);
                    	 }	 
                    	 if(c==2)
                    	 {
                    		 loggedIn.Distrust(trustUser);
                    	 }	 
                    	 if(c==3)
                    	 {
                    		 continue;
                    	 }	 
                	 }
                	 // Degrees of separation
                	 else if(c==8)
                	 {
                		 String login1;
                		 String login2;
                		 
                		 System.out.println("Enter first user:");
            			 while ((login1 = in.readLine()) == null && login1.length() == 0);
            			 
            			 System.out.println("Enter second user:");
            			 while ((login2 = in.readLine()) == null && login2.length() == 0);
                		 
                		 int dos = degreesOfSeparation(login1, login2);
                		 
                		 if(dos==0)
                		 {
                			 System.out.println("Degree of separation between " + login1 + " and " + login2 + " is not 1 or 2\n");
                		 }
                		 else
                		 {
                			 System.out.print("Degree of separation between " + login1 + " and " + login2 + " = " + dos + "\n");
                		 }
                	 }
                	 // Popular in category 
                	 else if(c==9)
                	 {
                		 String category;
                		 String limit;
                		 
                		 System.out.println("Enter a category:");
            			 while ((category = in.readLine()) == null && category.length() == 0);
            			 
            			 System.out.println("Enter the limit of places:");
            			 while ((limit = in.readLine()) == null && limit.length() == 0);
            			 
                		 statMostPopularPOIsInCategory(category, Integer.parseInt(limit));
                	 }
                	 // Expensive in category
                	 else if(c==10)
                	 {
                		 String category;
                		 String limit;
                		 
                		 System.out.println("Enter a category:");
            			 while ((category = in.readLine()) == null && category.length() == 0);
            			 
            			 System.out.println("Enter the limit of places:");
            			 while ((limit = in.readLine()) == null && limit.length() == 0);
            			 
                		 statMostExpensivePOIsInCategory(category, Integer.parseInt(limit));
                	 }
                	 // Highest rated in category
                	 else if(c==11)
                	 {
                		 String category;
                		 String limit;
                		 
                		 System.out.println("Enter a category:");
            			 while ((category = in.readLine()) == null && category.length() == 0);
            			 
            			 System.out.println("Enter the limit on returned places:");
            			 while ((limit = in.readLine()) == null && limit.length() == 0);
            			 
                		 statHighestRatedPOIsInCategory(category, Integer.parseInt(limit));
                	 }
                	 // Exit
                	 else if(c==12)
                	 {
                		 System.out.println("Thanks for using UTrack!");
                		 con.stmt.close();  
                		 break;
                	 }
                	 // Add new POI
                	 else if(c==13)
                	 {
                		 if(!loggedIn.isAdmin)
                		 {
                			 continue;
                		 }
                		 
                		 String name;
                		 String url;
                		 String phone;
                		 String est;
                		 String hours;
                		 String price;
                		 String street;
                		 String city;
                		 String state;
                		 String zip;
                		 
                		 System.out.println("Enter the name of the POI:");
            			 while ((name = in.readLine()) == null && name.length() == 0);  
                	
            			 System.out.println("Enter the URL of the POI:");
            			 while ((url = in.readLine()) == null && url.length() == 0);
            			 
            			 System.out.println("Enter the phone number of the POI:");
            			 while ((phone = in.readLine()) == null && phone.length() == 0);
            			 
            			 System.out.println("Enter the year the POI was established:");
            			 while ((est = in.readLine()) == null && est.length() == 0);
            			 
            			 System.out.println("Enter the hours of the POI:");
            			 while ((hours = in.readLine()) == null && hours.length() == 0);
            			 
            			 System.out.println("Enter the price of the POI:");
            			 while ((price = in.readLine()) == null && price.length() == 0);
            			 
            			 System.out.println("Enter the street address of the POI:");
            			 while ((street = in.readLine()) == null && street.length() == 0);
            			 
            			 System.out.println("Enter the city of the POI:");
            			 while ((city = in.readLine()) == null && city.length() == 0);
            			 
            			 System.out.println("Enter the state postal code of the POI:");
            			 while ((state = in.readLine()) == null && state.length() == 0);
            			 
            			 System.out.println("Enter the zip code of the POI:");
            			 while ((zip = in.readLine()) == null && zip.length() == 0);
            			 
            			 Address newAd = new Address(stmt, street, city, state, Integer.parseInt(zip));
                		 POI newPoi = new POI(stmt, name, url, phone, Integer.parseInt(est), hours, Double.parseDouble(price), newAd);
                		 newPoi.register();
                	 }
                	 // Update POI
                	 else if(c==14)
                	 {
                		 if(!loggedIn.isAdmin)
                		 {
                			 continue;
                		 }
                		 
                		 String pid;
                		 System.out.println("Enter the pid of the POI to change:");
            			 while ((pid = in.readLine()) == null && pid.length() == 0);
            			 
            			 
                		 System.out.println("What to change about this POI?");
                		 System.out.println("1. name");
                		 System.out.println("2. url");
                		 System.out.println("3. phone");
                		 System.out.println("4. established");
                		 System.out.println("5. hours");
                		 System.out.println("6. price");
                		 System.out.println("7. address");
                		 System.out.println("8. add a keyword");
                		 System.out.println("9. add a category");
                		 System.out.println("10. add a picture");
                		 System.out.println("Please enter your choice:");
            			 while ((choice = in.readLine()) == null && choice.length() == 0);
                    	 try
                    	 {
                    		 c = Integer.parseInt(choice);
                    	 }
                    	 catch (Exception e)
                    	 {
                    		 
                    		 continue;
                    	 }
                    	 
                    	 if(c<1 || c>11)
                    	 {
                    		 continue;
                    	 }
                    	 if(c==1)
                    	 {
                    		 String name;
                    		 System.out.println("Enter a name:");
                			 while ((name = in.readLine()) == null && name.length() == 0);
                			 updatePOIName(Integer.parseInt(pid), name);
                    	 }
                    	 if(c==2)
                    	 {
                    		 String url;
                    		 System.out.println("Enter a URL:");
                			 while ((url = in.readLine()) == null && url.length() == 0);
                			 updatePOIUrl(Integer.parseInt(pid), url);
                    	 }
                    	 if(c==3)
                    	 {
                    		 String phone;
                    		 System.out.println("Enter a phone number:");
                			 while ((phone = in.readLine()) == null && phone.length() == 0);
                			 updatePOIPhone(Integer.parseInt(pid), phone);
                    	 }
                    	 if(c==4)
                    	 {
                    		 String est;
                    		 System.out.println("Enter a year:");
                			 while ((est = in.readLine()) == null && est.length() == 0);
                			 updatePOIEstablished(Integer.parseInt(pid), Integer.parseInt(est));
                    	 }
                    	 if(c==5)
                    	 {
                    		 String hours;
                    		 System.out.println("Enter hours:");
                			 while ((hours = in.readLine()) == null && hours.length() == 0);
                			 updatePOIHours(Integer.parseInt(pid), hours);
                    	 }
                    	 if(c==6)
                    	 {
                    		 String price;
                    		 System.out.println("Enter a price:");
                			 while ((price = in.readLine()) == null && price.length() == 0);
                			 updatePOIPrice(Integer.parseInt(pid), Double.parseDouble(price));
                    	 }
                    	 if(c==7)
                    	 {
                    		 Address address;
                    		 String street;
                    		 String city;
                    		 String state;
                    		 String zip;
                    		 
                    		 System.out.println("Enter a street address:");
                			 while ((street = in.readLine()) == null && street.length() == 0);
                			 
                			 System.out.println("Enter a city:");
                			 while ((city = in.readLine()) == null && city.length() == 0);
                			 
                			 System.out.println("Enter a state postal code:");
                			 while ((state = in.readLine()) == null && state.length() == 0);
                			 
                			 System.out.println("Enter a zip code:");
                			 while ((zip = in.readLine()) == null && zip.length() == 0);
                			 
                			 address = new Address(stmt, street, city, state, Integer.parseInt(zip));
                			 
                			 updatePOIAddress(Integer.parseInt(pid), address);
                    	 }
                    	 if(c==8)
                    	 {
                    		 String keyword;
                    		 System.out.println("Enter a keyword:");
                			 while ((keyword = in.readLine()) == null && keyword.length() == 0);
                			 
                			 addKeyWordPOI(Integer.parseInt(pid), keyword, "English");
                    	 }
                    	 if(c==9)
                    	 {
                    		 String category;
                    		 System.out.println("Enter a category:");
                			 while ((category = in.readLine()) == null && category.length() == 0);
                			 
                			 addCategoryPOI(Integer.parseInt(pid), category);
                    	 }
                    	 if(c==10)
                    	 {
                    		 String pic;
                    		 System.out.println("Enter a picture URL:");
                			 while ((pic = in.readLine()) == null && pic.length() == 0);
                			 
                			 addPicPOI(Integer.parseInt(pid), pic);
                    	 }
      	 
                	 }
                	 // Most trusted users
                	 else if(c==15)
                	 {
                		 if(!loggedIn.isAdmin)
                		 {
                			 continue;
                		 }
                		 
                		 String limit;
                		 
                		 System.out.println("Enter a user amount limit:");
            			 while ((limit = in.readLine()) == null && limit.length() == 0);   
                		 
            			 mostTrustedUsers(Integer.parseInt(limit));
                	 }
                	 // Most useful users
                	 else if(c==16)
                	 {
                		 if(!loggedIn.isAdmin)
                		 {
                			 continue;
                		 }
                		 
                		 String limit;
                		 
                		 System.out.println("Enter a user amount limit:");
            			 while ((limit = in.readLine()) == null && limit.length() == 0);   
                		 
            			 mostUsefulUsers(Integer.parseInt(limit));
                	 }
            	 }
            	 // Register
            	 else if(c==2)
            	 {
            		 String newLogin;
            		 String newName;
            		 String newPassword;
            		 String newPhone;
            		 String newStreetAddress;
            		 String newCity;
            		 String newStatePostal;
            		 String newZip;
            		 System.out.println("Create a username:");
            		 while ((newLogin = in.readLine()) == null && newLogin.length() == 0);
            		 
            		 try
            		 {
            		 existingUser(stmt, newLogin);
            		 }
            		 catch(Exception e)
            		 {
            			 System.out.println("Create a password:");
            			 while ((newPassword = in.readLine()) == null && newPassword.length() == 0);         			 
            			
            			 System.out.println("Enter your name:");
            			 while ((newName = in.readLine()) == null && newName.length() == 0);
            			 
            			 System.out.println("Enter your phone number:");
            			 while ((newPhone = in.readLine()) == null && newPhone.length() == 0);
            			 
            			 System.out.println("Enter your street address:");
            			 while ((newStreetAddress = in.readLine()) == null && newStreetAddress.length() == 0);
            		 
            			 System.out.println("Enter your city:");
            			 while ((newCity = in.readLine()) == null && newCity.length() == 0);
            			 
            			 System.out.println("Enter your state postal code:");
            			 while ((newStatePostal = in.readLine()) == null && newStatePostal.length() == 0);
            		 
            			 System.out.println("Enter your zip code:");
            			 while ((newZip = in.readLine()) == null && newZip.length() == 0);
            		 
            			 Address newAddress = new Address(stmt, newStreetAddress, newCity, newStatePostal, Integer.parseInt(newZip));
            			 User newUser = new User(stmt, newLogin, newName, false, newPassword, newPhone, newAddress);
            			 newUser.register();
            			 
            			 System.out.println("Your account has been successfully registered");
            			 continue;
            		 }
            		       		 
            		 System.out.println("Username is already taken");
        			 continue;
            		 
            	 }
            	 // Exit
            	 else if(c==3)
            	 {
            		 System.out.println("Thanks for using UTrack!");
            		 con.stmt.close(); 
            		 break;
            	 }
			}
			
			
			con.closeConnection();
		} 
		catch (Exception e)
        {
       	 e.printStackTrace();
       	 System.err.println ("Either connection error or query execution error!");
        }
        finally
        {
       	 if (con != null)
       	 {
       		 try
       		 {
       			 con.closeConnection();
       			// System.out.println ("Database connection terminated");
       		 }
       	 
       		 catch (Exception e) { /* ignore close errors */ }
       	 }	 
        }
	}

	/*
	 * Search by price range, address (city/state), keywords, or category.
	 * Optional sort methods are by price, average score, or average trusted
	 * score.
	 */
	public static void searchPOI(String user, Statement stmt, double lowPrice, double highPrice, String city,
			String state, ArrayList<String> keywords, ArrayList<String> categories, String sortMethod)	throws SQLException 
	{
		String query;
		if (sortMethod == "average trusted score") 
		{
			query = "Select P.pid, P.name, P.url, P.phone, P.established, P.hours, P.price, AVG(F.score) as AverageScore, A.city, A.statePostal "
					+ "from POI P, POIHasAddress PH, Addresses A, Feedback F, Trust T "
					+ "where P.pid=PH.pid and PH.aid=A.aid and P.pid=F.pid and T.login1=\""
					+ user + "\" and F.login=T.login2 and T.isTrusted=1";
		}
		else
		{
			query = "Select P.pid, P.name, P.url, P.phone, P.established, P.hours, P.price, AVG(F.score) as AverageScore, A.city, A.statePostal "
					+ "from POI P, POIHasAddress PH, Addresses A, Feedback F "
					+ "where P.pid=PH.pid and PH.aid=A.aid and P.pid=F.pid";
		}

		query += " and price >= " + lowPrice;
		query += " and price <= " + highPrice;

		if (city != null) 
		{
			query += " and city=\"" + city + "\"";
		}
		if (state != null) 
		{
			query += " and statePostal=\"" + state + "\"";
		}

		if (keywords.size() > 0) {
			for (String word : keywords) {
				query += " and Exists(Select * from POI P2, HasKeywords HK, Keywords K "
						+ "where P2.pid=HK.pid and HK.kwid=K.kwid and P.pid=P2.pid and K.word = \"" + word + "\")";
			}
		}

		if (categories.size() > 0) {
			for (String cat : categories) {
				query += " and Exists(Select * from POI P3, HasCategories HC, Categories C where P3.pid=HC.pid and HC.cid=C.cid and P.pid=P3.pid and C.cname= \"" + cat + "\")";
			}
		}

		query += " GROUP BY P.pid";

		if (sortMethod == "price") {
			query += " ORDER BY price ASC;";
		} else if (sortMethod == "average score" || sortMethod == "average trusted score") {
			query += " ORDER BY AverageScore DESC";
		}

		String resultstr = "";
		ResultSet results;
		try 
		{
			results = stmt.executeQuery(query);
		} 
		catch (Exception e) 
		{
			System.err.println("Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			throw (e);
		}
		
		resultstr += "\npid\tscore\tcity\t\tstatePostal\tname\n";
		while (results.next())
		{
			resultstr += results.getInt("pid") + "\t" + results.getDouble("AverageScore") 
				+ "\t" + results.getString("city") + "\t\t" + results.getString("statePostal") 
				+ "\t" + results.getString("name") + "\n";	
		}
		System.out.println(resultstr);
	}
	
	public static User existingUser(Statement stmt, String existingLogin) throws SQLException
	{
		String query = "Select * from Users U, Addresses A, UserHasAddress HA where U.login=HA.login and HA.aid=A.aid and U.login=\"" + existingLogin + "\"";
		ResultSet result;
		try 
		{
			result = stmt.executeQuery(query);
		} 
		catch (Exception e) 
		{
			// Add errors for other queries
			System.err.println("Unable to execute query:" + query + "\n");
			System.err.println(e.getMessage());
			throw (e);
		}
		result.next();
		return new User(stmt, existingLogin, result.getString("name"), result.getBoolean("isAdmin"), result.getString("password"), result.getString("phone"), new Address(stmt, result.getString("streetAddress"), result.getString("city"), result.getString("statePostal"), result.getInt("zip")));
	}
	

	public static void addKeyWordPOI(int pid, String word, String language) throws Exception
	{
		int kwid;
		
		String wordQuery = "Insert into Keywords (word, language) values (\"" + word + "\",\"" + language + "\")";	
		try
		{
			stmt.executeUpdate(wordQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Keyword already exists\n");
			//System.err.println(e.getMessage());
			//throw(e);
		}
		
		String kwidQuery = "Select kwid from Keywords where word=\"" + word + "\" and language=\"" + language + "\"";
		
		ResultSet kwidResult = null;
		try
		{
			kwidResult = stmt.executeQuery(kwidQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Can't find kwid");
			//System.err.println(e.getMessage());
			//throw(e);
		}
		
		kwidResult.next();
		kwid = kwidResult.getInt("kwid");
		
		String HkQuery = "Insert into HasKeywords (pid, kwid) values (" + pid + "," + kwid + ")";
		
		try
		{
			stmt.executeUpdate(HkQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Already has keyword\n");
			System.err.println(e.getMessage());
			//throw(e);
		}
	}
	
	public static void addCategoryPOI(int pid, String cname) throws SQLException
	{
		int cid;
		
		String wordQuery = "Insert into Categories (cname) values (\"" + cname + "\")";	
		try
		{
			stmt.executeUpdate(wordQuery);
        } 
		catch(Exception e) 
		{
			//System.err.println("Category already exists");
			//System.err.println(e.getMessage());
			//throw(e);
		}
		
		String cidQuery = "Select cid from Categories where cname=\"" + cname + "\"";
		
		ResultSet cidResult;
		try
		{
			cidResult = stmt.executeQuery(cidQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Can't find cid");
			//System.err.println(e.getMessage());
			throw(e);
		}
		
		cidResult.next();
		cid = cidResult.getInt("cid");
		
		String HcQuery = "Insert into HasCategories (pid, cid) values (" + pid + "," + cid + ")";
		
		try
		{
			stmt.executeUpdate(HcQuery);
        } 
		catch(Exception e) 
		{
			//System.err.println("Already has category");
			//System.err.println(e.getMessage());
			//throw(e);
		}
	}
	
	public static void addPicPOI(int pid, String url) throws Exception
	{
		int picid;
		
		String picQuery = "Insert into Pics (url) values (\"" + url + "\")";	
		try
		{
			stmt.executeUpdate(picQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Picture already exists");
			//System.err.println(e.getMessage());
			//throw(e);
		}
		
		String picidQuery = "Select picid from Pics where url=\"" + url + "\"";
		
		ResultSet picidResult = null;
		try
		{
			picidResult = stmt.executeQuery(picidQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Can't find picid");
			//System.err.println(e.getMessage());
			//throw(e);
		}
		
		picidResult.next();
		picid = picidResult.getInt("picid");
		
		String HpQuery = "Insert into HasPics (pid, picid) values (" + pid + "," + picid + ")";
		
		try
		{
			stmt.executeUpdate(HpQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Already has picture\n");
			//System.err.println(e.getMessage());
			//throw(e);
		}
	}
	
	
	
	public static void updatePOIName(int pid, String newName) throws Exception
	{
		String query = "Update POI set name=\"" + newName + "\" where pid=" + pid;
		
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
	
	public static void updatePOIUrl(int pid, String newUrl) throws Exception
	{
		String query = "Update POI set url=\"" + newUrl + "\" where pid=" + pid;
		
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
	
	public static void updatePOIPhone(int pid, String newPhone) throws Exception
	{
		String query = "Update POI set phone=\"" + newPhone + "\" where pid=" + pid;
		
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
	
	public static void updatePOIEstablished(int pid, int newEstablished) throws Exception
	{
		String query = "Update POI set established=" + newEstablished + " where pid=" + pid;
		
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
	
	public static void updatePOIHours(int pid, String newHours) throws Exception
	{
		String query = "Update POI set hours=\"" + newHours + "\" where pid=" + pid;
		
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
	
	public static void updatePOIPrice(int pid, double newPrice) throws Exception
	{
		String query = "Update POI set price=" + newPrice + " where pid=" + pid;
		
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
	
	public static void updatePOIAddress(int pid, Address newAddress) throws Exception
	{		
		newAddress.register();
		
		String aidQuery = "Select aid from Addresses where streetAddress=\"" + newAddress.streetAddress 
				+ "\" and city=\"" + newAddress.city + "\" and statePostal=\"" + newAddress.statePostal + "\" and zip=\"" + newAddress.zip + "\"";
			
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
			
		aidResult.next();
		String aidString = "" + aidResult.getInt("aid");	
		
		String hasAddressQuery = "Update POIHasAddress set aid=" + aidString + " where pid=" + pid;
		
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
	
	
	
	public static void statMostPopularPOIsInCategory(String category, int limit) throws Exception
	{
		String query = "Select v.pid, p.name, count(v.pid) as count "
				+ "from POI p, Visit v, Categories c, HasCategories hc "
				+ "where p.pid=v.pid and v.pid=hc.pid and hc.cid=c.cid and c.cname=\"" + category
				+ "\" group by v.pid order by count desc limit " + limit;
		
		String resultstr="";
		ResultSet results;
		try
		{
			results = stmt.executeQuery(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		resultstr += category + "\ncount\tpid\tname\n";
		while (results.next())
		{
			resultstr += results.getInt("count") + "\t" + results.getInt("pid") + "\t" + results.getString("name") + "\n";	
		}
		System.out.println(resultstr);
	}
	
	public static void statMostExpensivePOIsInCategory(String category, int limit) throws Exception
	{
		String query = "Select v.pid, p.name, Truncate(AVG(ve.cost/ve.partySize), 2) avgprice "
				+ "from POI p, Visit v, VisEvent ve, Categories c, HasCategories hc "
				+ "where p.pid=v.pid and v.pid=hc.pid and hc.cid=c.cid and v.vid=ve.vid and c.cname=\"" 
				+ category + "\" group by v.pid order by avgprice desc limit " + limit;
		
		String resultstr="";
		ResultSet results;
		try
		{
			results = stmt.executeQuery(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		resultstr += category + "\nprice\tpid\tname\n";
		while (results.next())
		{
			resultstr += "$" + results.getDouble("avgprice") + "\t" + results.getInt("pid") + "\t" + results.getString("name") + "\n";	
		}
		System.out.println(resultstr);
	}
	
	public static void statHighestRatedPOIsInCategory(String category, int limit) throws Exception
	{
		String query = "Select p.pid, p.name, AVG(f.score) avgscore "
				+ "from POI p, Feedback f, Categories c, HasCategories hc "
				+ "where p.pid=f.pid and hc.pid=p.pid and hc.cid=c.cid and c.cname=\"" + category
				+ "\" group by p.pid order by avgscore desc  limit " + limit;
		
		String resultstr="";
		ResultSet results;
		try
		{
			results = stmt.executeQuery(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		resultstr += category + "\nscore\tpid\tname\n";
		while (results.next())
		{
			resultstr += results.getDouble("avgscore") + "\t" + results.getInt("pid") + "\t" + results.getString("name") + "\n";	
		}
		System.out.println(resultstr);
	}
	
	
	
	public static void usefulFeedbacks(int pid, int limit) throws Exception
	{
		String query = "Select truncate(avg(rating), 2) avgrating, f.login, f.fbdate, f.score, f.text "
				+ "from POI p, Feedback f, Rates r "
				+ "where p.pid=f.pid and f.fid=r.fid and p.pid=" + pid
				+ " group by f.fid order by avgrating desc limit " + limit + "";
		
		String resultstr="";
		ResultSet results;
		try
		{
			results = stmt.executeQuery(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		resultstr += "pid: " + pid + "\nuseful\tuser\t\tdate\t\tscore\treview\n";
		while (results.next())
		{
			resultstr += results.getDouble("avgrating") + "\t" 
					+ results.getString("login") + "\t" + results.getString("fbdate") + "\t" 
					+ results.getInt("score") + "\t" + results.getString("text") + "\n";	
		}
		System.out.println(resultstr);
	}
	
	public static void suggestOnVisit(String login, int pid) throws Exception
	{
		String query = "Select p.pid, p.name from Visit v, POI p where v.pid=p.pid and v.login !=\"" 
				+ login + "\" and v.pid !=" + pid + " and exists (Select * from Visit v2 where v.login=v2.login and v2.pid=" 
				+ pid + ") group by p.pid order by count(p.pid) desc limit 5";
		
		String resultstr="";
		ResultSet results;
		try
		{
			results = stmt.executeQuery(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		resultstr += "Suggested Places For You:" + "\npid\tname\n";
		while (results.next())
		{
			resultstr += results.getInt("pid") + "\t" 
					+ results.getString("name") + "\n";	
		}
		System.out.println(resultstr);
	}

	
	public static int degreesOfSeparation(String login1, String login2) throws Exception
	{	
		String query = "Select newF.login from Favorites newF where newF.login=\"" + login2 +  "\" and newF.login in (Select theirF.login from Favorites myF, Favorites theirF "
				+ "where myF.login =\"" + login1 + "\" and theirF.pid=myF.pid and theirF.login!=\"" + login1 + "\")";
		
		ResultSet results;
		try
		{
			results = stmt.executeQuery(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		if(results.isBeforeFirst())
		{
			return 1;
		}
		
		
		String query2 = "Select distinct newF.login from Favorites newF where newF.login=\"" 
				+ login2 + "\" and newF.login in "
				+ "(Select distinct otherF.secondlog from Favorites myF, "
				+ "(Select F1.login as firstlog, F2.login as secondlog, F2.pid "
				+ "from Favorites F1, Favorites F2  where F1.pid=F2.pid and F1.login!=\"" 
				+ login1 +"\" and F2.login!=\"" + login1 + "\" and F1.login!=F2.login ) as otherF "
				+ "where myF.login=\"" + login1 + "\" and otherF.pid!=myF.pid and firstlog!=\"" 
				+ login1 + "\" and secondlog!=\"" + login1 + "\")";
		
		ResultSet results2;
		try
		{
			results2 = stmt.executeQuery(query2);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query2+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		if(results2.isBeforeFirst())
		{
			return 2;
		}
		
		return 0;	
	}
	
	public static void mostTrustedUsers(int limit) throws Exception
	{
		String query = "Select u.login, sum(btv.ConvertValue) as trustScore "
				+ "from Users u, Trust t, BooleanToValue btv "
				+ "where u.login=t.login2 and btv.OriginalBoolean=t.isTrusted "
				+ "group by u.login order by trustScore desc limit " + limit;
		
		String resultstr="";
		ResultSet results;
		try
		{
			results = stmt.executeQuery(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		resultstr += "Most Trusted Users" + "\ntrust\tuser\n";
		while (results.next())
		{
			resultstr += results.getInt("trustScore") + "\t" 
					+ results.getString("login") + "\n";	
		}
		System.out.println(resultstr);
	}
	
	public static void mostUsefulUsers(int limit) throws Exception
	{
		String query = "Select u.login, Truncate(avg(r.rating), 2) avgrating "
				+ "from Users u, Feedback f, Rates r "
				+ "where u.login=f.login and f.fid=r.fid "
				+ "group by u.login order by avgrating desc limit " + limit;
		
		String resultstr="";
		ResultSet results;
		try
		{
			results = stmt.executeQuery(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		resultstr += "Most Useful Users" + "\nlogin\tusefulness\n";
		while (results.next())
		{
			resultstr += results.getString("login") + "\t" 
					+ results.getDouble("avgrating") + "\n";	
		}
		System.out.println(resultstr);
	}
}
