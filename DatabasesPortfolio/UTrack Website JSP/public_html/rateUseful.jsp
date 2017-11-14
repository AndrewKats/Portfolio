<%@ page language="java" import="cs5530.*" import="java.sql.*" %>
<%
   String usefulFidString = request.getParameter( "usefulFid" );
   session.setAttribute( "theUsefulFid", usefulFidString );
   
   String chosenPoiString = (String) session.getAttribute("theChosenPoi");
   String loginString = (String) session.getAttribute("theLogin");
%>
<HTML>
<head>
<title>Useful</title>
</head>
<BODY>
<%
Connector con = new Connector();
Statement stmt = con.stmt;

try
{
	User loggedIn = cs5530.Driver.existingUser(stmt, loginString);
	loggedIn.rateFeedback(Integer.parseInt(usefulFidString), 1);
	out.println("Successfully rated this feedback as Useful");
}
catch(Exception e)
{
	out.println("You've already rated this feedback");
}
%>

<br><a href= <%= "POI.jsp?chosenPoi=" + chosenPoiString %> >Back to place</a><br>

</BODY>
</HTML>