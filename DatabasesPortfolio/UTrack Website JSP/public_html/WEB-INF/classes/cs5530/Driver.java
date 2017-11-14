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

	/*
	 * Search by price range, address (city/state), keywords, or category.
	 * Optional sort methods are by price, average score, or average trusted
	 * score.
	 */
	public static String searchPOI(String user, Statement stmt, double lowPrice, double highPrice, String city,
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
		else if(sortMethod == "average score")
		{
			query = "Select P.pid, P.name, P.url, P.phone, P.established, P.hours, P.price, AVG(F.score) as AverageScore, A.city, A.statePostal "
					+ "from POI P, POIHasAddress PH, Addresses A, Feedback F "
					+ "where P.pid=PH.pid and PH.aid=A.aid and P.pid=F.pid";
		}
		else
		{
			query = "Select P.pid, P.name, P.url, P.phone, P.established, P.hours, P.price, A.city, A.statePostal "
					+ "from POI P, POIHasAddress PH, Addresses A "
					+ "where P.pid=PH.pid and PH.aid=A.aid";
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
		
		while (results.next())
		{
			if(sortMethod=="average score" || sortMethod=="average trusted score")
			{
				resultstr += "ID=" + results.getInt("pid") + " " + results.getString("name") + " (" + results.getString("city") + ", " 
					+ results.getString("statePostal") + ")" + " [" + results.getDouble("AverageScore") + "/10]<br>";	
			}
			else if(sortMethod=="price")
			{
				resultstr +=  "ID=" + results.getInt("pid") + " [$" + results.getDouble("price") + "] " + results.getString("name") + " (" + results.getString("city") + ", " 
					+ results.getString("statePostal") + ")<br>";	
			}
			else
			{
				resultstr += "ID=" + results.getInt("pid") + " " + results.getString("name") + " (" + results.getString("city") + ", " 
					+ results.getString("statePostal") + ")<br>";	
			}
		}
		return resultstr;
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
	
	public static POI existingPoi(Statement stmt, String ID) throws SQLException
	{
		String query = "Select * from POI P, Addresses A, POIHasAddress PA where P.pid=PA.pid and PA.aid=A.aid and P.pid=\"" + ID + "\"";
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
		return new POI(stmt, result.getString("name"), result.getString("url"), result.getString("phone"), result.getInt("established"), result.getString("hours"), result.getDouble("price"), new Address(stmt, result.getString("streetAddress"), result.getString("city"), result.getString("statePostal"), result.getInt("zip")));
	}
	
	public static ArrayList<String> categoriesOfPoi(Statement stmt, String ID) throws SQLException
	{
		String query = "Select * from POI P, Categories C, HasCategories HC where P.pid=HC.pid and HC.cid=C.cid and P.pid=\"" + ID + "\"";
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
		ArrayList<String> categories = new ArrayList<String>();
		while(result.next())
		{
			categories.add(result.getString("cname"));
		}
		
		return categories;
	}
	
	public static ArrayList<String> keywordsOfPoi(Statement stmt, String ID) throws SQLException
	{
		String query = "Select * from POI P, Keywords K, HasKeywords HK where P.pid=HK.pid and HK.kwid=K.kwid and P.pid=\"" + ID + "\"";
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
		ArrayList<String> keywords = new ArrayList<String>();
		while(result.next())
		{
			keywords.add(result.getString("word"));
		}
		
		return keywords;
	}
	
	public static ArrayList<String> picsOfPoi(Statement stmt, String ID) throws SQLException
	{
		String query = "Select * from POI P, Pics PIC, HasPics HP where P.pid=HP.pid and HP.picid=PIC.picid and P.pid=\"" + ID + "\"";
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
		ArrayList<String> pics = new ArrayList<String>();
		while(result.next())
		{
			pics.add(result.getString("PIC.url"));
		}
		
		return pics;
	}
	
	public static void addKeyWordPOI(int pid, String word, String language, Statement stmt2) throws Exception
	{
		int kwid;
		
		String wordQuery = "Insert into Keywords (word, language) values (\"" + word + "\",\"" + language + "\")";	
		try
		{
			stmt2.executeUpdate(wordQuery);
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
			kwidResult = stmt2.executeQuery(kwidQuery);
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
			stmt2.executeUpdate(HkQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Already has keyword\n");
			System.err.println(e.getMessage());
			//throw(e);
		}
	}
	
	public static void addCategoryPOI(int pid, String cname, Statement stmt2) throws SQLException
	{
		int cid;
		
		String wordQuery = "Insert into Categories (cname) values (\"" + cname + "\")";	
		try
		{
			stmt2.executeUpdate(wordQuery);
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
			cidResult = stmt2.executeQuery(cidQuery);
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
			stmt2.executeUpdate(HcQuery);
        } 
		catch(Exception e) 
		{
			//System.err.println("Already has category");
			//System.err.println(e.getMessage());
			//throw(e);
		}
	}
	
	public static void addPicPOI(int pid, String url, Statement stmt2) throws Exception
	{
		int picid;
		
		String picQuery = "Insert into Pics (url) values (\"" + url + "\")";	
		try
		{
			stmt2.executeUpdate(picQuery);
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
			picidResult = stmt2.executeQuery(picidQuery);
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
			stmt2.executeUpdate(HpQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Already has picture\n");
			//System.err.println(e.getMessage());
			//throw(e);
		}
	}
	
	
	
	public static void updatePOIName(int pid, String newName, Statement stmt2) throws Exception
	{
		String query = "Update POI set name=\"" + newName + "\" where pid=" + pid;
		
		try
		{
			stmt2.executeUpdate(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
	}
	
	public static void updatePOIUrl(int pid, String newUrl, Statement stmt2) throws Exception
	{
		String query = "Update POI set url=\"" + newUrl + "\" where pid=" + pid;
		
		try
		{
			stmt2.executeUpdate(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
	}
	
	public static void updatePOIPhone(int pid, String newPhone, Statement stmt2) throws Exception
	{
		String query = "Update POI set phone=\"" + newPhone + "\" where pid=" + pid;
		
		try
		{
			stmt2.executeUpdate(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
	}
	
	public static void updatePOIEstablished(int pid, int newEstablished, Statement stmt2) throws Exception
	{
		String query = "Update POI set established=" + newEstablished + " where pid=" + pid;
		
		try
		{
			stmt2.executeUpdate(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
	}
	
	public static void updatePOIHours(int pid, String newHours, Statement stmt2) throws Exception
	{
		String query = "Update POI set hours=\"" + newHours + "\" where pid=" + pid;
		
		try
		{
			stmt2.executeUpdate(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
	}
	
	public static void updatePOIPrice(int pid, double newPrice, Statement stmt2) throws Exception
	{
		String query = "Update POI set price=" + newPrice + " where pid=" + pid;
		
		try
		{
			stmt2.executeUpdate(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
	}
	
	public static void updatePOIAddress(int pid, Address newAddress, Statement stmt2) throws Exception
	{		
		newAddress.register();
		
		String aidQuery = "Select aid from Addresses where streetAddress=\"" + newAddress.streetAddress 
				+ "\" and city=\"" + newAddress.city + "\" and statePostal=\"" + newAddress.statePostal + "\" and zip=\"" + newAddress.zip + "\"";
			
		ResultSet aidResult;
		try
		{
			aidResult = stmt2.executeQuery(aidQuery);
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
			stmt2.executeUpdate(hasAddressQuery);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+hasAddressQuery+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
	}
	
	
	
	public static String statMostPopularPOIsInCategory(String category, int limit, Statement stmt2) throws Exception
	{
		String query = "Select v.pid, p.name, count(v.pid) as count "
				+ "from POI p, Visit v, Categories c, HasCategories hc "
				+ "where p.pid=v.pid and v.pid=hc.pid and hc.cid=c.cid and c.cname=\"" + category
				+ "\" group by v.pid order by count desc limit " + limit;
		
		String resultstr="";
		ResultSet results;
		try
		{
			results = stmt2.executeQuery(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		while (results.next())
		{
			resultstr += results.getInt("pid") + " [" + results.getInt("count") + " visits] " + results.getString("name") + "<br>";	
		}
		return resultstr;
	}
	
	public static String statMostExpensivePOIsInCategory(String category, int limit, Statement stmt2) throws Exception
	{
		String query = "Select v.pid, p.name, Truncate(AVG(ve.cost/ve.partySize), 2) avgprice "
				+ "from POI p, Visit v, VisEvent ve, Categories c, HasCategories hc "
				+ "where p.pid=v.pid and v.pid=hc.pid and hc.cid=c.cid and v.vid=ve.vid and c.cname=\"" 
				+ category + "\" group by v.pid order by avgprice desc limit " + limit;
		
		String resultstr="";
		ResultSet results;
		try
		{
			results = stmt2.executeQuery(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		while (results.next())
		{
			resultstr += results.getInt("pid") + " [$" + results.getDouble("avgprice") + "] " + results.getString("name") + "<br>";	
		}
		return resultstr;
	}
	
	public static String statHighestRatedPOIsInCategory(String category, int limit, Statement stmt2) throws Exception
	{
		String query = "Select p.pid, p.name, AVG(f.score) avgscore "
				+ "from POI p, Feedback f, Categories c, HasCategories hc "
				+ "where p.pid=f.pid and hc.pid=p.pid and hc.cid=c.cid and c.cname=\"" + category
				+ "\" group by p.pid order by avgscore desc  limit " + limit;
		
		String resultstr="";
		ResultSet results;
		try
		{
			results = stmt2.executeQuery(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		while (results.next())
		{
			resultstr += results.getInt("pid") + " [" + results.getDouble("avgscore") + "/10] " + results.getString("name") + "<br>";	
		}
		return resultstr;
	}
	
	
	
	public static String usefulFeedbacks(int pid, int limit, Statement stmt2) throws Exception
	{
		String query = "Select truncate(avg(rating), 2) avgrating, f.login, f.fbdate, f.score, f.text "
				+ "from POI p, Feedback f, Rates r "
				+ "where p.pid=f.pid and f.fid=r.fid and p.pid=" + pid
				+ " group by f.fid order by avgrating desc limit " + limit + "";
		
		String resultstr="";
		ResultSet results;
		try
		{
			results = stmt2.executeQuery(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		while (results.next())
		{
			resultstr += "<br>{Usefulness: " + results.getDouble("avgrating") + "} [" 
					+ results.getInt("score") + "/10] " + results.getString("login") + " (" + results.getString("fbdate") + ")<br>" 
					+ results.getString("text") + "<br>";	
		}
		return resultstr;
	}
	
	public static String webFeedbacks(int pid, Statement stmt2) throws Exception
	{
		String query = "Select f.fid, f.login, f.fbdate, f.score, f.text "
				+ "from POI p, Feedback f "
				+ "where p.pid=f.pid and p.pid=" + pid;
		
		String resultstr="";
		ResultSet results;
		try
		{
			results = stmt2.executeQuery(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		while (results.next())
		{
			resultstr += "herestheseparator" + results.getInt("fid") + "[" + results.getInt("score") + "/10] " 
					+ results.getString("login") + " (" + results.getString("fbdate") + ")<br>" 
					+ results.getString("text") + "<br>";
		}
		return resultstr;
	}
	
	public static String suggestOnVisit(String login, int pid, Statement stmt2) throws Exception
	{
		String query = "Select p.pid, p.name from Visit v, POI p where v.pid=p.pid and v.login !=\"" 
				+ login + "\" and v.pid !=" + pid + " and exists (Select * from Visit v2 where v.login=v2.login and v2.pid=" 
				+ pid + ") group by p.pid order by count(p.pid) desc limit 5";
		
		String resultstr="";
		ResultSet results;
		try
		{
			results = stmt2.executeQuery(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		while (results.next())
		{
			resultstr += results.getInt("pid") + " " 
					+ results.getString("name") + "<br>";	
		}
		return resultstr;
	}

	
	public static int degreesOfSeparation(String login1, String login2, Statement stmt2) throws Exception
	{	
		String query = "Select newF.login from Favorites newF where newF.login=\"" + login2 +  "\" and newF.login in (Select theirF.login from Favorites myF, Favorites theirF "
				+ "where myF.login =\"" + login1 + "\" and theirF.pid=myF.pid and theirF.login!=\"" + login1 + "\")";
		
		ResultSet results;
		try
		{
			results = stmt2.executeQuery(query);
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
	
	public static String mostTrustedUsers(int limit, Statement stmt2) throws Exception
	{
		String query = "Select u.login, sum(btv.ConvertValue) as trustScore "
				+ "from Users u, Trust t, BooleanToValue btv "
				+ "where u.login=t.login2 and btv.OriginalBoolean=t.isTrusted "
				+ "group by u.login order by trustScore desc limit " + limit;
		
		String resultstr="";
		ResultSet results;
		try
		{
			results = stmt2.executeQuery(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		while (results.next())
		{
			resultstr += "[ " + results.getInt("trustScore") + "] " 
					+ results.getString("login") + "<br>";	
		}
		return resultstr;
	}
	
	public static String mostUsefulUsers(int limit, Statement stmt2) throws Exception
	{
		String query = "Select u.login, Truncate(avg(r.rating), 2) avgrating "
				+ "from Users u, Feedback f, Rates r "
				+ "where u.login=f.login and f.fid=r.fid "
				+ "group by u.login order by avgrating desc limit " + limit;
		
		String resultstr="";
		ResultSet results;
		try
		{
			results = stmt2.executeQuery(query);
        } 
		catch(Exception e) 
		{
			System.err.println("Unable to execute query:"+query+"\n");
			System.err.println(e.getMessage());
			throw(e);
		}
		
		while (results.next())
		{
			resultstr += "[" + results.getDouble("avgrating") + "] " 
					+ results.getString("login") + "<br>";	
		}
		return resultstr;
	}
}
