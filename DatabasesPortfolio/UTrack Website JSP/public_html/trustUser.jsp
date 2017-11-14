<html>
<head>
<title>Trust a user</title>
<script LANGUAGE="javascript">
function check_all_fields(form_obj)
{
	if( form_obj.trustLogin.value == "")
	{
		alert("Please fill in all information.");
		return false;
	}
	return true;
}

</script>
</head>

<body>

<FORM METHOD=get onsubmit="return check_all_fields(this)" ACTION="userTrusted.jsp">
User:    <br><INPUT TYPE=TEXT NAME=trustLogin SIZE=20><BR>
Trust or Distrust: <br><INPUT TYPE=radio NAME=trustType VALUE=Trust checked> <INPUT TYPE=radio NAME=trustType VALUE=Distrust><BR>
<P><INPUT TYPE=SUBMIT><BR><br>

<br><a href="startPage.jsp">Back to the homepage</a><br>

</body>
</html>
