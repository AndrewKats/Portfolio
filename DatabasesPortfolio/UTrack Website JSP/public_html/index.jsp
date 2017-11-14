<%@ page language="java" import="cs5530.*" import="java.sql.*" import="java.util.ArrayList" %>
<%
	if(((ArrayList<Visit>) session.getAttribute("theVisits"))!=null)
	{
		session.setAttribute("theVisits", new ArrayList<String>());;
	}	
%>

<html>
<head>
<title>Welcome to UTRACK</title>
</head>
<body>
WELCOME TO THE UTRACK SYSTEM<br><br>

<FORM METHOD=POST ACTION="startPage.jsp">
Login:    <BR><INPUT TYPE=TEXT NAME=login SIZE=20><BR>
Password: <BR><INPUT TYPE=PASSWORD NAME=password SIZE=20>
<P><INPUT TYPE=SUBMIT><BR><br>

<a href="createUser.jsp">Register a new account</a><br><br>

(Note: to test as an admin, log in as Login=admin Password=admin. To test as a normal user, use Login=WebTest2 Password=test)

</body>
</html>
