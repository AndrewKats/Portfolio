<%@ page language="java" import="cs5530.*" import="java.sql.*" %>
<%
   String veryUsefulFidString = request.getParameter( "veryUsefulFid" );
   session.setAttribute( "theVeryUsefulFid", veryUsefulFidString );
   
   String chosenPoiString = (String) session.getAttribute("theChosenPoi");
   String loginString = (String) session.getAttribute("theLogin");
%>
<HTML>
<head>
<title>Very Useful</title>
</head>
<BODY>
<%
Connector con = new Connector();
Statement stmt = con.stmt;

try
{
	User loggedIn = cs5530.Driver.existingUser(stmt, loginString);
	loggedIn.rateFeedback(Integer.parseInt(veryUsefulFidString), 2);
	out.println("Successfully rated this feedback as Very Useful");
}
catch(Exception e)
{
	out.println("You've already rated this feedback");
}
%>

<br><a href= <%= "POI.jsp?chosenPoi=" + chosenPoiString %> >Back to place</a><br>

</BODY>
</HTML>