<%@ page language="java" import="cs5530.*" import="java.sql.*" %>
<%
   String chosenPoiString = (String) session.getAttribute("theChosenPoi");
   String loginString = (String) session.getAttribute("theLogin");
%>
<html>
<head>
<title>Give Feedback</title>
<script LANGUAGE="javascript">
function check_all_fields(form_obj)
{
	if( form_obj.fbScore.value == "")
	{
		alert("Please fill in all information.");
		return false;
	}
	return true;
}

</script>
</head>

<body>
<%
Connector con = new Connector();
Statement stmt = con.stmt;
try
{
out.println("<big><b>Give feedback to " + (cs5530.Driver.existingPoi(stmt, chosenPoiString)).name + "<br><br></b></big>");
}
catch(Exception e)
{
	System.out.println("Failed");
}
%>
<FORM METHOD=get onsubmit="return check_all_fields(this)" ACTION="feedbackGiven.jsp">
Score (out of 10):    <BR><INPUT TYPE=NUMBER NAME=fbScore min=0 max=10 SIZE=20><BR>
Text review (optional): <BR><INPUT TYPE=TEXT NAME=fbText maxlength=99 style="width:400;"><BR>
<P><INPUT TYPE=SUBMIT><BR><br>

</body>
</html>
