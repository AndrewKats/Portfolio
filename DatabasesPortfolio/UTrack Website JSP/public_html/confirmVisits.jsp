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
<title>Confirm or Remove Visits</title>
</head>
<BODY>

<% 
Connector con = new Connector();
Statement stmt = con.stmt;

try
{
	for(Visit v : visits)
	{
		out.println("Visited " + cs5530.Driver.existingPoi(stmt, v.pid).name + " on " + v.month + "/" + v.day + "/" + v.year + " in a party of " + v.partySize + " costing $" + v.cost + "<br>");
	}
}
catch(Exception e)
{
	out.println("Failed");
}
%>
<br><a href="visitsConfirmed.jsp">Confirm these visits</a><br>
<br><a href="visitsCancelled.jsp">Remove these visits</a><br>
<br><a href="startPage.jsp">Back to homepage</a><br>

</BODY>
</HTML>