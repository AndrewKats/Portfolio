<%@ page language="java" import="cs5530.*" import="java.sql.*" import="java.util.ArrayList" %>
<%   
   String trustedLimitString = request.getParameter( "trustedLimit" );
   session.setAttribute( "theTrustedfulLimit", trustedLimitString );
%>
<HTML>
<head>
<title>Most Trusted</title>
</head>
<BODY>
<%
Connector con = new Connector();
Statement stmt = con.stmt;

try
{
	String result = cs5530.Driver.mostTrustedUsers(Integer.parseInt(trustedLimitString), stmt);
	out.println(result);
}
catch(Exception e)
{
	out.println("Failed.");
}
%>

<br><a href="mostTrusted.jsp">Try with a different limit</a><br>

<br><br><a href="admin.jsp">Back to admin options</a><br>

<br><br><a href="startPage.jsp">Back to homepage</a><br>

</BODY>
</HTML>