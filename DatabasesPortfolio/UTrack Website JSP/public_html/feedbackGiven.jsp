<%@ page language="java" import="cs5530.*" import="java.sql.*" import="java.util.Calendar" %>
<%
   String fbScoreString = request.getParameter( "fbScore" );
   session.setAttribute( "theFbScore", fbScoreString );
   
   String fbTextString = request.getParameter( "fbText" );
   session.setAttribute( "theFbText", fbTextString );
   
   String chosenPoiString = (String) session.getAttribute("theChosenPoi");
   String loginString = (String) session.getAttribute("theLogin");
%>
<HTML>
<head>
<title>Feedback Given</title>
</head>
<BODY>
<%
Connector con = new Connector();
Statement stmt = con.stmt;

try
{
	Calendar cal = Calendar.getInstance();
	int day = cal.get(Calendar.DAY_OF_MONTH);
	int month = cal.get(Calendar.MONTH) + 1;
	int year = cal.get(Calendar.YEAR);
	
	Feedback newFb = new Feedback(stmt, Integer.parseInt(fbScoreString), fbTextString, year, month, day, chosenPoiString, loginString);
	newFb.register();
	out.println("Successfully gave feedback");
}
catch(Exception e)
{
	out.println("You've already given feedback for this place");
}
%>

<br><a href= <%= "POI.jsp?chosenPoi=" + chosenPoiString %> >Back to place</a><br>

</BODY>
</HTML>