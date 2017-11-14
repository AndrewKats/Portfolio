<html>
<head>
<title>Find Most Expensive</title>
<script LANGUAGE="javascript">
function check_all_fields(form_obj)
{
	if( form_obj.expCategory.value == ""
	|| form_obj.expLimit.value == "")
	{
		alert("Please fill in all information.");
		return false;
	}
	return true;
}

</script>
</head>

<body>

<FORM METHOD=get onsubmit="return check_all_fields(this)" ACTION="foundExpensive.jsp">
Category:    <br><INPUT TYPE=TEXT NAME=expCategory SIZE=20><BR>
Limit:    <br><INPUT TYPE=NUMBER NAME=expLimit min=1 SIZE=20><BR>
<P><INPUT TYPE=SUBMIT><BR><br>

</body>
</html>
