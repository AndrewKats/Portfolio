<%@ page language="java" import="cs5530.*" import="java.sql.*" import="java.util.ArrayList" %>
<%
   String searchMinPriceString = request.getParameter( "searchMinPrice" );
   session.setAttribute( "theSearchMinPrice", searchMinPriceString );
   
   String searchMaxPriceString = request.getParameter( "searchMaxPrice" );
   session.setAttribute( "theSearchMaxPrice", searchMaxPriceString );
   
   String searchCityString = request.getParameter( "searchCity" );
   session.setAttribute( "theSearchCity", searchCityString );
   
   String searchStateString = request.getParameter( "searchState" );
   session.setAttribute( "theSearchState", searchStateString );
   
   String searchKeywordsString = request.getParameter( "searchKeywords" );
   session.setAttribute( "theSearchKeywords", searchKeywordsString );
   
   String searchCategoriesString = request.getParameter( "searchCategories" );
   session.setAttribute( "theSearchCategories", searchCategoriesString );
   
   String searchSortString = request.getParameter( "searchSort" );
   session.setAttribute( "theSearchSort", searchSortString );
%>
<HTML>
<head>
<title>Search results</title>
<script LANGUAGE="javascript">
function check_all_fields(form_obj)
{
	if( form_obj.chosenPoi.value == "")
	{
		alert("Please fill in all information.");
		return false;
	}
	return true;
}

</script>
</head>
<BODY>
<%
Connector con = new Connector();
Statement stmt = con.stmt;
if(searchMinPriceString.equals(""))
{
	searchMinPriceString = "0";
}

if(searchMaxPriceString.equals(""))
{
	searchMaxPriceString = "10000000";
}

if((searchCityString.replaceAll("\\s","")).equals(""))
{
	searchCityString = null;
}

if((searchStateString.replaceAll("\\s","")).equals(""))
{
	searchStateString = null;
}

String sortMethod = null;
if(searchSortString.equals("1"))
{
	sortMethod = "price";
}
if(searchSortString.equals("2"))
{
	sortMethod = "average score";
}
if(searchSortString.equals("3"))
{
	sortMethod = "average trusted score";
}

String[] keywordArray = searchKeywordsString.split(" ");
ArrayList<String> keywordList = new ArrayList<String>();
for(String s : keywordArray)
{
	if(!((s.replaceAll("\\s","")).equals("")))
	{
		keywordList.add(s.replaceAll("\\s",""));
	}
}

String[] categoryArray = searchCategoriesString.split(" ");
ArrayList<String> categoryList = new ArrayList<String>();
for(String s : categoryArray)
{
	if(!((s.replaceAll("\\s","")).equals("")))
	{
		categoryList.add(s.replaceAll("\\s",""));
	}
}

try
{
	String result = cs5530.Driver.searchPOI((String) session.getAttribute("theLogin"), stmt, Double.parseDouble(searchMinPriceString), Double.parseDouble(searchMaxPriceString), searchCityString, searchStateString, keywordList, categoryList, sortMethod);
	if(result.equals(""))
	{
		out.println("No results found. Try different search parameters.<br>");
	}
	out.println(result);
}
catch(Exception e)
{
	out.println("Failed");
}
%>

<br><FORM METHOD=get onsubmit="return check_all_fields(this)" ACTION="POI.jsp">
Enter the ID of a place for more information: <BR><INPUT TYPE=NUMBER NAME=chosenPoi SIZE=4><BR>
<P><INPUT TYPE=SUBMIT><BR>

<br><a href="search.jsp">Do another search</a><br>
<br><a href="startPage.jsp">Back to the homepage</a><br>

</BODY>
</HTML>