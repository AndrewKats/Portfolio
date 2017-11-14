<%@ page language="java" import="cs5530.*" import="java.sql.*" import="java.util.ArrayList" %>
<%
   String fbLimitString = request.getParameter( "fbLimit" );
   session.setAttribute( "theFbLimit", fbLimitString );
   
   String chosenPoiString = (String) session.getAttribute("theChosenPoi");
%>
<HTML>
<head>
<title>Useful feedback</title>
</head>
<BODY>
<%
Connector con = new Connector();
Statement stmt = con.stmt;

try
{
	String result = cs5530.Driver.usefulFeedbacks(Integer.parseInt(chosenPoiString), Integer.parseInt(fbLimitString), stmt);
	out.println(result);
}
catch(Exception e)
{
	out.println("Failed");
}
%>

<br><a href= <%= "POI.jsp?chosenPoi=" + chosenPoiString %> >Back to place</a><br>

</BODY>
</HTML>