<%@ page language="java" import="cs5530.*" import="java.sql.*" import="java.util.ArrayList" %>
<%
   String chosenPoiString = request.getParameter( "chosenPoi" );
   session.setAttribute( "theChosenPoi", chosenPoiString );
   
   String loginString = (String) session.getAttribute("theLogin");
%>
<HTML>
<head>
<title>Place</title>
<script LANGUAGE="javascript">
function check_all_fields(form_obj)
{
	if( form_obj.fbLimit.value == "")
	{
		alert("Please fill in all information.");
		return false;
	}
	return true;
}

</script>
</head>
<BODY>
<a href="search.jsp">Go back to search</a><br>
<% 
Connector con = new Connector();
Statement stmt = con.stmt;

try
{
	cs5530.POI seePOI = cs5530.Driver.existingPoi(stmt, chosenPoiString);
	ArrayList<String> categories = cs5530.Driver.categoriesOfPoi(stmt, chosenPoiString);
	ArrayList<String> keywords = cs5530.Driver.keywordsOfPoi(stmt, chosenPoiString);
	ArrayList<String> pics = cs5530.Driver.picsOfPoi(stmt, chosenPoiString);
	
	out.println("<b><big>" + seePOI.name + "</big></b> ");
	%>
	<a href="favorite.jsp"> Add to Favorites</a>
	<a href="addVisit.jsp"> Add a Visit</a>
	<a href="giveFeedback.jsp"> Give Feedback</a><br>
	<%
	out.println("Website: " + seePOI.url + "<br>");
	out.println("Phone: " + seePOI.phone + "<br>");
	out.println("Hours: " + seePOI.hours + "<br>");
	out.println("Price: $" + seePOI.price + "<br>");
	out.println("Established: " + seePOI.established + "<br>");
	out.println("Address: " + seePOI.address.streetAddress + ", " + seePOI.address.city + ", " + seePOI.address.statePostal + " " + seePOI.address.zip + "<br>");
	
	out.println("Categories: ");
	for(String c : categories)
	{
		out.println(c + " ");
	}
	out.println("<br>");
	
	out.println("Keywords: ");
	for(String k : keywords)
	{
		out.println(k + " ");
	}
	out.println("<br>");
	
	out.println("Pictures: ");
	for(String p : pics)
	{
		out.println(p + " ");
	}
	out.println("<br>");
	%>
	<br><FORM METHOD=get onsubmit="return check_all_fields(this)" ACTION="usefulFeedbacks.jsp">
	See most useful feebacks: <BR>Limit<INPUT TYPE=NUMBER NAME=fbLimit min=1 SIZE=4><BR>
	<P><INPUT TYPE=SUBMIT><BR><br>

	<b>All Feedback:</b><br><br>
	<%
	try
	{
		String feedbackString = cs5530.Driver.webFeedbacks(Integer.parseInt(chosenPoiString), stmt);
		String[] feedbackArray = feedbackString.split("herestheseparator");
		for(String s : feedbackArray)
		{
			if(!(s.replaceAll("\\s","").equals("")))
			{
				int index = s.indexOf("[");
				String fid = s.substring(0,index);
				String rest = s.substring(index);
				out.println(rest);
				
				int index1 = s.indexOf(" ");
				int index2 = s.indexOf(" ", index1+1);
				String fbName = s.substring(index1+1, index2);
				
				if(!(fbName.equals(loginString)))
				{
					out.println("<a href=\"rateUseless.jsp?uselessFid=" + fid + "\">Useless</a>");
					out.println("<a href=\"rateUseful.jsp?usefulFid=" + fid + "\">Useful</a>");
					out.println("<a href=\"rateVeryUseful.jsp?veryUsefulFid=" + fid + "\">Very Useful</a><br><br>");
				}
			}
		}
		if(feedbackString.equals(""))
		{
			out.println("No feedback has been given for this place yet.<br>");
		}
	}
	catch(Exception e)
	{
		out.println("Failed to find feedback");
	}
}
catch(Exception e)
{
	out.println("That ID does not exist");
}
%>



<br><a href="startPage.jsp">Go back to homepage</a><br>

</BODY>
</HTML>