<%@ page language="java" import="cs5530.*" import="java.sql.*" %>
<%
   String newPoiNameString = request.getParameter( "newPoiName" );
   session.setAttribute( "theNewPoiName", newPoiNameString );
   
   String newPoiUrlString = request.getParameter( "newPoiUrl" );
   session.setAttribute( "theNewPoiUrl", newPoiUrlString );
   
   String newPoiPhoneString = request.getParameter( "newPoiPhone" );
   session.setAttribute( "theNewPoiPhone", newPoiPhoneString );
   
   String newPoiEstablishedString = request.getParameter( "newPoiEstablished" );
   session.setAttribute( "theNewPoiEstablished", newPoiEstablishedString );
   
   String newPoiHoursString = request.getParameter( "newPoiHours" );
   session.setAttribute( "theNewPoiHours", newPoiHoursString );
   
   String newPoiPriceString = request.getParameter( "newPoiPrice" );
   session.setAttribute( "theNewPoiPrice", newPoiPriceString );
   
   String newPoiStreetString = request.getParameter( "newPoiStreet" );
   session.setAttribute( "theNewPoiStreet", newPoiStreetString );
   
   String newPoiCityString = request.getParameter( "newPoiCity" );
   session.setAttribute( "theNewPoiCity", newPoiCityString );
   
   String newPoiStateString = request.getParameter( "newPoiState" );
   session.setAttribute( "theNewPoiState", newPoiStateString );
   
   String newPoiZipString = request.getParameter( "newPoiZip" );
   session.setAttribute( "theNewPoiZip", newPoiZipString );
   
   /*
   String newPoiKeywordsString = request.getParameter( "newPoiKeywords" );
   session.setAttribute( "theNewPoiKeywords", newPoiKeywordsString );
   
   String newPoiCategoriesString = request.getParameter( "newPoiCategories" );
   session.setAttribute( "theNewPoiCategories", newPoiZipString );
   
   String newPoiPicsString = request.getParameter( "newPoiPics" );
   session.setAttribute( "theNewPoiPics", newPoiPicsString );
   */
%>
<HTML>
<head>
<title> POI Added </title>
</head>
<BODY>
<%
Connector con = new Connector();
Statement stmt = con.stmt;
try
{
	Address newAddress = new Address(stmt, newPoiStreetString, newPoiCityString, newPoiStateString, Integer.parseInt(newPoiZipString));
	POI newPoi = new POI(stmt, newPoiNameString, newPoiUrlString, newPoiPhoneString, Integer.parseInt(newPoiEstablishedString), newPoiHoursString, Double.parseDouble(newPoiPriceString), newAddress);
	newPoi.register();	
	out.println("Successfully added new POI: " + newPoiNameString);
	out.println("<br> To add keywords, categories, or pictures to this POI, go to \"Update the info of a POI\"");
	
	
	/*
	String[] keywordArray = newPoiKeywordsString.split(" ");
	ArrayList<String> keywordList = new ArrayList<String>();
	for(String s : keywordArray)
	{
		if(!((s.replaceAll("\\s","")).equals("")))
		{
			keywordList.add(s.replaceAll("\\s",""));
		}
	}

	String[] categoryArray = newPoiCategoriesString.split(" ");
	ArrayList<String> categoryList = new ArrayList<String>();
	for(String s : categoryArray)
	{
		if(!((s.replaceAll("\\s","")).equals("")))
		{
			categoryList.add(s.replaceAll("\\s",""));
		}
	}
	
	String[] picArray = newPoiPicsString.split(" ");
	ArrayList<String> picList = new ArrayList<String>();
	for(String s : picArray)
	{
		if(!((s.replaceAll("\\s","")).equals("")))
		{
			picList.add(s.replaceAll("\\s",""));
		}
	}
	
	for(String s : keywordList)
	{
		cs5530.Driver.addKey(newPoi.);
	}
	*/
}
catch(Exception e2)
{
	//out.println("Failed");
}

%>

<br><br><a href="admin.jsp">Back to admin options</a><br>

<br><a href="index.html">Back to the login page</a><br>

</BODY>
</HTML>