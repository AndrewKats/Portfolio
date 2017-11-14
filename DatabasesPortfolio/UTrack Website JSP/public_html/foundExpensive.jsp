<%@ page language="java" import="cs5530.*" import="java.sql.*" import="java.util.ArrayList" %>
<%
   String expCategoryString = request.getParameter( "expCategory" );
   session.setAttribute( "theExpCategory", expCategoryString );
   
   String expLimitString = request.getParameter( "expLimit" );
   session.setAttribute( "theExpLimit", expLimitString );
%>
<HTML>
<head>
<title>Most Expensive</title>
</head>
<BODY>
<%
Connector con = new Connector();
Statement stmt = con.stmt;

try
{
	String result = cs5530.Driver.statMostExpensivePOIsInCategory(expCategoryString, Integer.parseInt(expLimitString), stmt);
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