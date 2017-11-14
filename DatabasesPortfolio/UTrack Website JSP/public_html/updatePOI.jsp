<html>
<head>
<title>Update a POI</title>
<script LANGUAGE="javascript">
function check_all_fields(form_obj)
{
	if( form_obj.updatePid.value == "")
	{
		alert("Please fill in all information.");
		return false;
	}
	return true;
}

</script>
</head>

<body>

<FORM METHOD=get onsubmit="return check_all_fields(this)" ACTION="POIupdated.jsp">
POI ID: <br><INPUT TYPE=NUMBER NAME=updatePid min=0 step=1 SIZE=20><BR>
Name:    <br><INPUT TYPE=TEXT NAME=updateName SIZE=20><BR>
URL: <br><INPUT TYPE=TEXT NAME=updateUrl SIZE=17><BR>
Phone:    <br><INPUT TYPE=TEXT NAME=updatePhone maxlength=10 SIZE=20><BR>
Year Established: <br><INPUT TYPE=NUMBER NAME=updateEstablished min=0 max=3000 SIZE=20><BR>
Hours: <br><INPUT TYPE=TEXT NAME=updateHours SIZE=20><BR>
Price: <br><INPUT TYPE=NUMBER NAME=updatePrice min=0 max=2000000 step=0.01 SIZE=20><BR>
Street Address: <br><INPUT TYPE=TEXT NAME=updateStreet SIZE=20><BR>
City: <br><INPUT TYPE=TEXT NAME=updateCity SIZE=20><BR>
State (two-letter postal code): <br><INPUT TYPE=TEXT NAME=updateState minlength=2 maxlength=2 SIZE=20><BR>
Zip: <br><INPUT TYPE=TEXT NAME=updateZip maxlength=9 SIZE=20><BR>
Add Keywords (Separated by spaces): <br><INPUT TYPE=TEXT NAME=updateKeywords SIZE=20><BR>
Add a Category (Separated by spaces): <br><INPUT TYPE=TEXT NAME=updateCategories SIZE=20><BR>
Add a Picture (Separated by spaces): <br><INPUT TYPE=TEXT NAME=updatePics SIZE=20><BR>
<P><INPUT TYPE=SUBMIT><BR><br>

<br><a href="admin.jsp">Back to admin options</a><br>

<br><a href="startPage.jsp">Back to the homepage</a><br>

</body>
</html>
