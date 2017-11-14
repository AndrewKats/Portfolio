<%@ page language="java" import="cs5530.*" import="java.sql.*" %>
<%
	String loginString = request.getParameter( "login" );
	if(loginString!=null)
	{
		session.setAttribute( "theLogin", loginString );
	}
	String passwordString = request.getParameter( "password" );
	if(passwordString!=null)
	{
		session.setAttribute( "thePassword", passwordString );
	}
%>
<HTML>
<head>
<title>Homepage</title>
</head>
<BODY>
<%
boolean success = false;
Connector con = new Connector();
Statement stmt = con.stmt;
try
{
	if(request.getParameter( "login" ) == null)
	{
		success=true;
		loginString=(String) session.getAttribute("theLogin");
	}
	else
	{
		User loggedInUser = cs5530.Driver.existingUser(stmt, loginString);
		if(!(loggedInUser.password).equals(passwordString))
		{
			success=false;
		}
		else{
			success=true;
		}
	}
	
}
catch(Exception e)
{
	
}
try
{
	if(success)
	{
		if(loginString!=null)
		{
			out.println("Logged in as " + loginString + "<br>");
		}
		if(cs5530.Driver.existingUser(stmt, loginString).isAdmin)
		{
			%>
			<br><a href="admin.jsp">Admin options</a><br>
			<%
		}
		%>
		<br><a href="search.jsp">Search for points of interest</a><br>
		<br><a href="categoryStats.jsp">See category statistics</a><br>
		<br><a href="confirmVisits.jsp">View and confirm your added visits</a><br>
		<br><a href="trustUser.jsp">Trust/Distrust a user</a><br>
		<br><a href="findDOS.jsp">Find degrees of separation between two users</a><br>
	<%
	}
	else
	{
		out.println("Login failed");
	}
}
catch(Exception e)
{
	
}
%>


<br><a href="index.jsp">Back to the login page</a><br>

</BODY>
</HTML>