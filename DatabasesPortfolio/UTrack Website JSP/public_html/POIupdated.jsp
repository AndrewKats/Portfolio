<%@ page language="java" import="cs5530.*" import="java.sql.*" import="java.util.ArrayList"%>
<%
   String updatePidString = request.getParameter( "updatePid" );
   session.setAttribute( "theUpdatePid", updatePidString );
   	
   String updateNameString = request.getParameter( "updateName" );
   session.setAttribute( "theUpdateName", updateNameString );
   
   String updateUrlString = request.getParameter( "updateUrl" );
   session.setAttribute( "theUpdateUrl", updateUrlString );
   
   String updatePhoneString = request.getParameter( "updatePhone" );
   session.setAttribute( "theUpdatePhone", updatePhoneString );
   
   String updateEstablishedString = request.getParameter( "updateEstablished" );
   session.setAttribute( "theUpdateEstablished", updateEstablishedString );
   
   String updateHoursString = request.getParameter( "updateHours" );
   session.setAttribute( "theUpdateHours", updateHoursString );
   
   String updatePriceString = request.getParameter( "updatePrice" );
   session.setAttribute( "theUpdatePrice", updatePriceString );
   
   String updateStreetString = request.getParameter( "updateStreet" );
   session.setAttribute( "theUpdateStreet", updateStreetString );
   
   String updateCityString = request.getParameter( "updateCity" );
   session.setAttribute( "theUpdateCity", updateCityString );
   
   String updateStateString = request.getParameter( "updateState" );
   session.setAttribute( "theUpdateState", updateStateString );
   
   String updateZipString = request.getParameter( "updateZip" );
   session.setAttribute( "theUpdateZip", updateZipString );
   
   String updateKeywordsString = request.getParameter( "updateKeywords" );
   session.setAttribute( "theUpdateKeywords", updateKeywordsString );
   
   String updateCategoriesString = request.getParameter( "updateCategories" );
   session.setAttribute( "theUpdateCategories", updateZipString );
   
   String updatePicsString = request.getParameter( "updatePics" );
   session.setAttribute( "theUpdatePics", updatePicsString );
%>
<HTML>
<head>
<title>Updated POI</title>
</head>
<BODY>
<%
Connector con = new Connector();
Statement stmt = con.stmt;
try
{
	POI oldPoi = cs5530.Driver.existingPoi(stmt, updatePidString);
	Address oldAddress = oldPoi.address;
	
	if(!(updateNameString.replaceAll("\\s","").equals("")))
	{
		cs5530.Driver.updatePOIName(Integer.parseInt(updatePidString), updateNameString, stmt);
	}
	
	if(!(updateUrlString.replaceAll("\\s","").equals("")))
	{
		cs5530.Driver.updatePOIUrl(Integer.parseInt(updatePidString), updateUrlString, stmt);
	}
	
	if(!(updatePhoneString.replaceAll("\\s","").equals("")))
	{
		cs5530.Driver.updatePOIPhone(Integer.parseInt(updatePidString), updatePhoneString, stmt);
	}
	
	if(!(updateEstablishedString.replaceAll("\\s","").equals("")))
	{
		cs5530.Driver.updatePOIEstablished(Integer.parseInt(updatePidString), Integer.parseInt(updateEstablishedString), stmt);
	}
	
	if(!(updateHoursString.replaceAll("\\s","").equals("")))
	{
		cs5530.Driver.updatePOIHours(Integer.parseInt(updatePidString), updateHoursString, stmt);
	}
	
	if(!(updatePriceString.replaceAll("\\s","").equals("")))
	{
		cs5530.Driver.updatePOIPrice(Integer.parseInt(updatePidString), Double.parseDouble(updatePriceString), stmt);
	}
	

	boolean changeAddress = false;
	if(!(updateStreetString.replaceAll("\\s","").equals("")))
	{
		oldAddress.streetAddress = updateStreetString;
		changeAddress=true;
	}
	
	if(!(updateCityString.replaceAll("\\s","").equals("")))
	{
		oldAddress.city = updateCityString;
		changeAddress=true;
	}
	
	if(!(updateStateString.replaceAll("\\s","").equals("")))
	{
		oldAddress.statePostal = updateStateString;
		changeAddress=true;
	}
	
	if(!(updateZipString.replaceAll("\\s","").equals("")))
	{
		oldAddress.zip = Integer.parseInt(updateZipString);
		changeAddress=true;
	}
	
	if(changeAddress)
	{
		cs5530.Driver.updatePOIAddress(Integer.parseInt(updatePidString), oldAddress, stmt);
	}

	if(!(updateKeywordsString.replaceAll("\\s","").equals("")))
	{
		String[] keywordArray = updateKeywordsString.split(" ");
		ArrayList<String> keywordList = new ArrayList<String>();
		for(String s : keywordArray)
		{
			if(!((s.replaceAll("\\s","")).equals("")))
			{
				keywordList.add(s.replaceAll("\\s",""));
			}
		}
		
		for(String kw : keywordList)
		{
			cs5530.Driver.addKeyWordPOI(Integer.parseInt(updatePidString), kw, "English", stmt);
		}
	}

	if(!(updateCategoriesString.replaceAll("\\s","").equals("")))
	{
		String[] categoryArray = updateCategoriesString.split(" ");
		ArrayList<String> categoryList = new ArrayList<String>();
		for(String s : categoryArray)
		{
			if(!((s.replaceAll("\\s","")).equals("")))
			{
				categoryList.add(s.replaceAll("\\s",""));
			}
		}
		
		for(String cat : categoryList)
		{
			cs5530.Driver.addCategoryPOI(Integer.parseInt(updatePidString), cat, stmt);
		}
	}
	
	if(!(updatePicsString.replaceAll("\\s","").equals("")))
	{
		String[] picArray = updatePicsString.split(" ");
		ArrayList<String> picList = new ArrayList<String>();
		for(String s : picArray)
		{
			if(!((s.replaceAll("\\s","")).equals("")))
			{
				picList.add(s.replaceAll("\\s",""));
			}
		}
		
		for(String pic : picList)
		{
			cs5530.Driver.addPicPOI(Integer.parseInt(updatePidString), pic, stmt);
		}
	}
	
	
	out.println("Successfully updated POI");
	
}
catch(Exception e2)
{
	out.println("Failed");
}

%>

<br><br><a href="updatePOI.jsp">Update another POI</a><br>

<br><a href="admin.jsp">Back to admin options</a><br>

<br><a href="startPage.html">Back to the homepage</a><br>

</BODY>
</HTML>