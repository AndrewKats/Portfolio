<html>
<head>
<title>Degrees of Separation</title>
<script LANGUAGE="javascript">
function check_all_fields(form_obj)
{
	if( form_obj.dosLogin1.value == ""
	|| form_obj.dosLogin2.value == "")
	{
		alert("Please fill in all information.");
		return false;
	}
	return true;
}

</script>
</head>

<body>

<FORM METHOD=get onsubmit="return check_all_fields(this)" ACTION="foundDOS.jsp">
User1:    <br><INPUT TYPE=TEXT NAME=dosLogin1 SIZE=20><BR>
User2:    <br><INPUT TYPE=TEXT NAME=dosLogin2 SIZE=20><BR>
<P><INPUT TYPE=SUBMIT><BR><br>

<br><a href="startPage.jsp">Back to the homepage</a><br>

</body>
</html>
