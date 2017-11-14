<html>
<head>
<title>Add a POI</title>
<script LANGUAGE="javascript">
function check_all_fields(form_obj)
{
	if( form_obj.newPoiName.value == "" 
	|| form_obj.newPoiUrl.value == "" 
	|| form_obj.newPoiPhone.value == "" 
	|| form_obj.newPoiEstablished.value == "" 
	|| form_obj.newPoiHours.value == "" 
	|| form_obj.newPoiPrice.value == "" 
	|| form_obj.newPoiStreet.value == "" 
	|| form_obj.newPoiCity.value == ""
	|| form_obj.newPoiState.value == ""
	|| form_obj.newPoiZip.value == "")
	{
		alert("Please fill in all information.");
		return false;
	}
	return true;
}

</script>
</head>

<body>

<FORM METHOD=get onsubmit="return check_all_fields(this)" ACTION="POIadded.jsp">
Name:    <br><INPUT TYPE=TEXT NAME=newPoiName SIZE=20><BR>
URL: <br><INPUT TYPE=TEXT NAME=newPoiUrl SIZE=17><BR>
Phone:    <br><INPUT TYPE=TEXT NAME=newPoiPhone maxlength=10 SIZE=20><BR>
Year Established: <br><INPUT TYPE=NUMBER NAME=newPoiEstablished min=0 max=3000 SIZE=20><BR>
Hours: <br><INPUT TYPE=TEXT NAME=newPoiHours SIZE=20><BR>
Price: <br><INPUT TYPE=NUMBER NAME=newPoiPrice min=0 step=0.01 SIZE=20><BR>
Street Address: <br><INPUT TYPE=TEXT NAME=newPoiStreet SIZE=20><BR>
City: <br><INPUT TYPE=TEXT NAME=newPoiCity SIZE=20><BR>
State (two-letter postal code): <br><INPUT TYPE=TEXT NAME=newPoiState minlength=2 maxlength=2 SIZE=20><BR>
Zip: <br><INPUT TYPE=TEXT NAME=newPoiZip maxlength=9 SIZE=20><BR>
<!-- Keywords (separated by spaces): <BR><INPUT TYPE=TEXT NAME=newPoiKeywords SIZE=20><BR>
Categories (separated by spaces): <BR><INPUT TYPE=TEXT NAME=newPoiCategories SIZE=20><BR>
Pics (URL's separated by spaces): <BR><INPUT TYPE=TEXT NAME=newPoiPics SIZE=20><BR>-->
<P><INPUT TYPE=SUBMIT><BR><br>

</body>
</html>
