<%@ page language="java" import="cs5530.*" import="java.sql.*" import="java.util.ArrayList" %>
<%
   String popCategoryString = request.getParameter( "popCategory" );
   session.setAttribute( "thePopCategory", popCategoryString );
   
   String popLimitString = request.getParameter( "popLimit" );
   session.setAttribute( "thePopLimit", popLimitString );
%>
<HTML>
<head>
<title>Most Popular</title>
</head>
<BODY>
<%
Connector con = new Connector();
Statement stmt = con.stmt;

try
{
	String result = cs5530.Driver.statMostPopularPOIsInCategory(popCategoryString, Integer.parseInt(popLimitString), stmt);
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