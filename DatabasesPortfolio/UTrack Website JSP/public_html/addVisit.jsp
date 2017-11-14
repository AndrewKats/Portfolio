<%@ page language="java" import="cs5530.*" import="java.sql.*" %>
<%
   String loginString = (String) session.getAttribute("theLogin");
   String chosenPoiString = (String) session.getAttribute("theChosenPoi");
%>
<html>
<head>
<title>Add a visit</title>
<script LANGUAGE="javascript">
function check_all_fields(form_obj)
{
	if( form_obj.newCost.value == "" 
	|| form_obj.newPartySize.value == "" 
	|| form_obj.newYear.value == "" 
	|| form_obj.newMonth.value == "" 
	|| form_obj.newDay.value == "")
	{
		alert("Please fill in all information.");
		return false;
	}
	return true;
}
</script>
</head>
<body>

<FORM METHOD=get onsubmit="return check_all_fields(this)" ACTION="visitAdded.jsp">
Cost: <BR><INPUT TYPE=NUMBER NAME=newCost min=0 step=0.01 SIZE=20><BR>
Party Size:    <BR><INPUT TYPE=NUMBER NAME=newPartySize min=1 SIZE=20><BR>
Year:    <BR><INPUT TYPE=NUMBER NAME=newYear min=0 max=3000 SIZE=20><BR>
Month:    <BR><INPUT TYPE=NUMBER NAME=newMonth min=1 max=12 SIZE=20><BR>
Day of Month:    <BR><INPUT TYPE=NUMBER NAME=newDay min=1 max=31 SIZE=20><BR>
<P><INPUT TYPE=SUBMIT><BR><br>

<br><a href= <%= "POI.jsp?chosenPoi=" + chosenPoiString %> >Back to place</a><br>
</body>
</html>