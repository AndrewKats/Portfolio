<%@ page language="java" import="cs5530.*" import="java.sql.*" import="java.util.ArrayList" %>
<%
   String trustLoginString = request.getParameter( "trustLogin" );
   session.setAttribute( "theTrustLogin", trustLoginString );
   
   String trustTypeString = request.getParameter( "trustType" );
   session.setAttribute( "theTrustType", trustTypeString );
   
   String loginString = (String) session.getAttribute("theLogin");
%>
<HTML>
<head>
<title>Trust or Distrust User</title>
</head>
<BODY>
<%
Connector con = new Connector();
Statement stmt = con.stmt;

try
{
	User loggedIn = cs5530.Driver.existingUser(stmt, loginString);
	if((loggedIn.login).equals(trustLoginString))
	{
		out.println("You can't trust/distrust yourself.");
	}
	if(trustTypeString.equals("Trust"))
	{
		loggedIn.Trust(trustLoginString);
		out.println("Successfully trusted " + trustLoginString);
	}
	else
	{
		loggedIn.Distrust(trustLoginString);
		out.println("Successfully distrusted " + trustLoginString);
	}
}
catch(Exception e)
{
	out.println("That user does not exist.");
}
%>
<br><br><a href="startPage.jsp">Back to homepage</a><br>

</BODY>
</HTML>