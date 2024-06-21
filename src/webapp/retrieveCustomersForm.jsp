<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Retrieve Customers</title>
</head>
    <body>
    <%@page import="jobshop.DataHandler1"%>
    <%@page import="java.sql.ResultSet"%>
    <%@page import="java.sql.Array"%>
    
    <h1>Retrieve Customers by Category Range</h1>
    <form action="retrieveCustomers.jsp" method="post">
        Min Category: <input type="number" name="minCategory"><br>
        Max Category: <input type="number" name="maxCategory"><br>
        <input type="submit" value="Submit">
    </form>
    
    </body>
</html>
