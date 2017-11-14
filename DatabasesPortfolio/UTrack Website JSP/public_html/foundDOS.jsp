<%@ page language="java" import="cs5530.*" import="java.sql.*" import="java.util.ArrayList" %>
<%
   String dosLogin1String = request.getParameter( "dosLogin1" );
   session.setAttribute( "theDosLogin1", dosLogin1String );
   
   String dosLogin2String = request.getParameter( "dosLogin2" );
   session.setAttribute( "theDosLogin2", dosLogin2String );
%>
<HTML>
<head>
<title>Degrees of Separation</title>
</head>
<BODY>
<%
Connector con = new Connector();
Statement stmt = con.stmt;

try
{
	if(dosLogin1String.equals(dosLogin2String))
	{
		out.println("Cannot enter the same user twice");
	}
	else
	{
		int dos = cs5530.Driver.degreesOfSeparation(dosLogin1String, dosLogin2String, stmt);
		
		if(dos == 0)
		{
			out.println("Degree of separation between " + dosLogin1String + " and " + dosLogin2String + " is not 1 or 2");
		}
		if(dos == 1)
		{
			out.println("Degree of separation between " + dosLogin1String + " and " + dosLogin2String + " is 1");
		}
		if(dos == 2)
		{
			out.println("Degree of separation between " + dosLogin1String + " and " + dosLogin2String + " is 2");
		}
	}
}
catch(Exception e)
{
	out.println("Degree of separation between " + dosLogin1String + " and " + dosLogin2String + " is not 1 or 2");
}
%>
<br><br><a href="findDOS.jsp">Try different users</a><br>
<br><br><a href="startPage.jsp">Back to homepage</a><br>

</BODY>
</HTML>