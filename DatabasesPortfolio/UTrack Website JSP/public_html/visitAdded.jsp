<%@ page language="java" import="cs5530.*" import="java.sql.*" import="java.util.ArrayList" %>
<%
	String newCostString = request.getParameter("newCost");
	session.setAttribute("theNewCost", newCostString);
	
	String newPartySizeString = request.getParameter("newPartySize");
	session.setAttribute("theNewPartySize", newPartySizeString);
	
	String newYearString = request.getParameter("newYear");
	session.setAttribute("theNewYear", newYearString);
	
	String newMonthString = request.getParameter("newMonth");
	session.setAttribute("theNewMonth", newMonthString);
	
	String newDayString = request.getParameter("newDay");
	session.setAttribute("theNewDay", newDayString);
	
	String loginString = (String) session.getAttribute("theLogin");
	String chosenPoiString = (String) session.getAttribute("theChosenPoi");

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
<title>Visit Added</title>
</head>
<BODY>

<% 
Connector con = new Connector();
Statement stmt = con.stmt;

try
{
	cs5530.POI seePOI = cs5530.Driver.existingPoi(stmt, chosenPoiString);	
	cs5530.Visit newVisit = new cs5530.Visit(stmt, Double.parseDouble(newCostString), Integer.parseInt(newPartySizeString), loginString, chosenPoiString, Integer.parseInt(newYearString), Integer.parseInt(newMonthString), Integer.parseInt(newDayString));
	visits.add(newVisit);
	session.setAttribute("theVisits", visits);
	out.println("Successfully added visit<br>");
	
	String suggestions = cs5530.Driver.suggestOnVisit(newVisit.login, Integer.parseInt(newVisit.pid), stmt);
	out.println("<big><b><br>If you liked " + seePOI.name + ", you might also like:</b></big><BR>");
	out.println(suggestions);
}
catch(Exception e)
{
	out.println("Failed");
}

%>

<br><a href="confirmVisits.jsp">View and confirm added visits</a><br>
<br><a href= <%= "POI.jsp?chosenPoi=" + chosenPoiString %> >Back to place</a><br>

</BODY>
</HTML>