<%@ page language="java" import="cs5530.*" import="java.sql.*" import="java.util.ArrayList" %>
<%
	ArrayList<Visit> visits = new ArrayList<Visit>(); 

	if(((ArrayList<Visit>) session.getAttribute("theVisits"))==null)
	{
		visits = new ArrayList<Visit>();  
	}
	else
	{
	   visits = (ArrayList<Visit>) session.getAttribute("theVisits");
	}		
%>
<HTML>
<head>
<title>Visits Confirmed</title>
</head>
<BODY>

<% 
Connector con = new Connector();
Statement stmt = con.stmt;

try
{
	for(Visit v : visits)
	{
		v.register();
	}
	visits.clear();
	session.setAttribute("theVisits", visits);
	out.println("Successfully confirmed visits");
}
catch(Exception e)
{
	out.println("Failed");
}
%>
<br><a href="startPage.jsp">Back to homepage</a><br>

</BODY>
</HTML>