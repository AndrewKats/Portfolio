<html>
<head>
<title>Create a UTRACK account</title>
<script LANGUAGE="javascript">
function check_all_fields(form_obj)
{
	if( form_obj.newLogin.value == "" 
	|| form_obj.newPassword.value == "" 
	|| form_obj.newName.value == "" 
	|| form_obj.newPhone.value == "" 
	|| form_obj.newStreet.value == "" 
	|| form_obj.newCity.value == "" 
	|| form_obj.newState.value == "" 
	|| form_obj.newZip.value == "")
	{
		alert("Please fill in all information.");
		return false;
	}
	return true;
}

</script>
</head>

<body>
Enter your information below
<FORM METHOD=get onsubmit="return check_all_fields(this)" ACTION="userCreated.jsp">
Login:    <br><INPUT TYPE=TEXT NAME=newLogin maxlength=30 SIZE=20><BR>
Password: <br><INPUT TYPE=PASSWORD NAME=newPassword SIZE=20><BR>
Name:    <br><INPUT TYPE=TEXT NAME=newName SIZE=20><BR>
Phone: <br><INPUT TYPE=TEXT NAME=newPhone maxlength=10 SIZE=20><BR>
Street Address: <br><INPUT TYPE=TEXT NAME=newStreet SIZE=20><BR>
City: <br><INPUT TYPE=TEXT NAME=newCity SIZE=20><BR>
State (two-letter postal code): <br><INPUT TYPE=TEXT NAME=newState minlength=2 maxlength=2 SIZE=20><BR>
Zip: <br><INPUT TYPE=TEXT NAME=newZip maxlength=9 SIZE=20><BR>
<P><INPUT TYPE=SUBMIT><BR><br>

<br><a href="index.jsp">Back to the login page</a><br>

</body>
</html>
