<%@ page language="java" import="cs5530.*" import="java.sql.*" %>
<%
   String newLoginString = request.getParameter( "newLogin" );
   session.setAttribute( "theNewLogin", newLoginString );
   
   String newPasswordString = request.getParameter( "newPassword" );
   session.setAttribute( "theNewPassword", newPasswordString );
   
   String newNameString = request.getParameter( "newName" );
   session.setAttribute( "theNewName", newNameString );
   
   String newPhoneString = request.getParameter( "newPhone" );
   session.setAttribute( "theNewPhone", newPhoneString );
   
   String newStreetString = request.getParameter( "newStreet" );
   session.setAttribute( "theNewStreet", newStreetString );
   
   String newCityString = request.getParameter( "newCity" );
   session.setAttribute( "theNewCity", newCityString );
   
   String newStateString = request.getParameter( "newState" );
   session.setAttribute( "theNewState", newStateString );
   
   String newZipString = request.getParameter( "newZip" );
   session.setAttribute( "theNewZip", newZipString );
%>
<HTML>
<head>
<title>Account Created</title>
</head>
<BODY>
<%
boolean success = false;
Connector con = new Connector();
Statement stmt = con.stmt;
try
{
	cs5530.Driver.existingUser(stmt, newLoginString);
}
catch(Exception e)
{
	try
	{
		Address newAddress = new Address(stmt, newStreetString, newCityString, newStateString, Integer.parseInt(newZipString));
		User newUser = new User(stmt, newLoginString, newNameString, false, newPasswordString, newPhoneString, newAddress);
		newUser.register();
		success = true;
	}
	catch(Exception e2)
	{
		
	}
}

if(success)
{
	out.println("Congratulations " + newLoginString + " you have successfully registered!");
}
else
{
	out.println("Registration failed. The login " + newLoginString + " is already taken.");
}
%>

<br><a href="index.jsp">Back to the login page</a><br>

</BODY>
</HTML>