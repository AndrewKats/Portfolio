<html>
<head>
<title>Most Useful Users</title>
<script LANGUAGE="javascript">
function check_all_fields(form_obj)
{
	if( form_obj.usefulLimit.value == "")
	{
		alert("Please fill in all information.");
		return false;
	}
	return true;
}

</script>
</head>

<body>

<FORM METHOD=get onsubmit="return check_all_fields(this)" ACTION="foundMostUseful.jsp">
Limit:    <br><INPUT TYPE=NUMBER NAME=usefulLimit min=1 SIZE=20><BR>
<P><INPUT TYPE=SUBMIT><BR>

<br><br><a href="admin.jsp">Back to admin options</a><br>

<br><br><a href="startPage.jsp">Back to homepage</a><br>

</body>
</html>
