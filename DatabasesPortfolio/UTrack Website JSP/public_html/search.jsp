<html>
<head>
<title>Search for places</title>
</head>

<body>
Fill in any of the following fields to find places of interest. <br>
(Leave all blank to see all places)<br><br>
<FORM METHOD=get ACTION="searchResult.jsp">
Minimum Price:    <BR><INPUT TYPE=NUMBER NAME=searchMinPrice SIZE=20><BR>
Maximum Price: <BR><INPUT TYPE=NUMBER NAME=searchMaxPrice SIZE=17><BR>
City:    <BR><INPUT TYPE=TEXT NAME=searchCity SIZE=20><BR>
State: <BR><INPUT TYPE=TEXT NAME=searchState maxlength=2 SIZE=20><BR>
Keywords (separated by spaces): <BR><INPUT TYPE=TEXT NAME=searchKeywords SIZE=20><BR>
Categories (separated by spaces): <BR><INPUT TYPE=TEXT NAME=searchCategories SIZE=20><BR>
Sort Method (Enter "1" to sort by cost, "2" to sort by rating, or "3" to sort by ratings by ratings by users you trust. Otherwise, no sorting will be used.): 
<BR><INPUT TYPE=NUMBER NAME=searchSort min=0 max=3 SIZE=20><BR>
<P><INPUT TYPE=SUBMIT><BR><br>

<br><a href="startPage.jsp">Back to the homepage</a><br>

</body>
</html>
