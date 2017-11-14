<html>
<head>
<title>Find Popular Places</title>
<script LANGUAGE="javascript">
function check_all_fields(form_obj)
{
	if( form_obj.popCategory.value == ""
	|| form_obj.popLimit.value == "")
	{
		alert("Please fill in all information.");
		return false;
	}
	return true;
}

</script>
</head>

<body>

<FORM METHOD=get onsubmit="return check_all_fields(this)" ACTION="foundPopular.jsp">
Category:    <br><INPUT TYPE=TEXT NAME=popCategory SIZE=20><BR>
Limit:    <br><INPUT TYPE=NUMBER NAME=popLimit min=1 SIZE=20><BR>
<P><INPUT TYPE=SUBMIT><BR><br>

</body>
</html>
