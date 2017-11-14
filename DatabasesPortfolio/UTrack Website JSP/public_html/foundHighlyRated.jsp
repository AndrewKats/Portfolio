<%@ page language="java" import="cs5530.*" import="java.sql.*" import="java.util.ArrayList" %>
<%
   String hrCategoryString = request.getParameter( "hrCategory" );
   session.setAttribute( "theHrCategory", hrCategoryString );
   
   String hrLimitString = request.getParameter( "hrLimit" );
   session.setAttribute( "theHrLimit", hrLimitString );
%>
<HTML>
<head>
<title>Highly Rated</title>
</head>
<BODY>
<%
Connector con = new Connector();
Statement stmt = con.stmt;

try
{
	String result = cs5530.Driver.statHighestRatedPOIsInCategory(hrCategoryString, Integer.parseInt(hrLimitString), stmt);
	if(result.equals(""))
	{
		out.println("That category does not exist");
	}
	else
	{
		out.println(result);
	}
}
catch(Exception e)
{
	out.println("That category does not exist.");
}
%>
<br><br><a href="startPage.jsp">Back to homepage</a><br>

</BODY>
</HTML>