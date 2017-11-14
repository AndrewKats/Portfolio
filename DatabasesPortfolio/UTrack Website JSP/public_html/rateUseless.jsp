<%@ page language="java" import="cs5530.*" import="java.sql.*" %>
<%
   String uselessFidString = request.getParameter( "uselessFid" );
   session.setAttribute( "theUselessFid", uselessFidString );
   
   String chosenPoiString = (String) session.getAttribute("theChosenPoi");
   String loginString = (String) session.getAttribute("theLogin");
%>
<HTML>
<head>
<title>Useless</title>
</head>
<BODY>
<%
Connector con = new Connector();
Statement stmt = con.stmt;

try
{
	User loggedIn = cs5530.Driver.existingUser(stmt, loginString);
	loggedIn.rateFeedback(Integer.parseInt(uselessFidString), 0);
	out.println("Successfully rated this feedback as Useless");
}
catch(Exception e)
{
	out.println("You've already rated this feedback");
}
%>

<br><a href= <%= "POI.jsp?chosenPoi=" + chosenPoiString %> >Back to place</a><br>

</BODY>
</HTML>