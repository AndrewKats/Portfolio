<html>
<head>
<title>Find Highly Rated</title>
<script LANGUAGE="javascript">
function check_all_fields(form_obj)
{
	if( form_obj.hrCategory.value == ""
	|| form_obj.hrLimit.value == "")
	{
		alert("Please fill in all information.");
		return false;
	}
	return true;
}

</script>
</head>

<body>

<FORM METHOD=get onsubmit="return check_all_fields(this)" ACTION="foundHighlyRated.jsp">
Category:    <br><INPUT TYPE=TEXT NAME=hrCategory SIZE=20><BR>
Limit:    <br><INPUT TYPE=NUMBER NAME=hrLimit min=1 SIZE=20><BR>
<P><INPUT TYPE=SUBMIT><BR><br>

</body>
</html>
