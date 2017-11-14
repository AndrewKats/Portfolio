<%@ page language="java" import="cs5530.*" import="java.sql.*" %>
<%
   String loginString = (String) session.getAttribute("theLogin");
   String chosenPoiString = (String) session.getAttribute("theChosenPoi");
%>
<html>
<head>
<title>Add Favorite</title>
</head>
<body>
<%
Connector con = new Connector();
Statement stmt = con.stmt;

try
{
	User loggedIn = cs5530.Driver.existingUser(stmt, loginString);
	loggedIn.favorite(Integer.parseInt(chosenPoiString));
	out.println("Successfully added to Favorites");
}
catch(Exception e)
{
	out.println("This place is already in your favorites");
}
%>
<br><a href= <%= "POI.jsp?chosenPoi=" + chosenPoiString %> >Back to place</a><br>
</body>
</html>