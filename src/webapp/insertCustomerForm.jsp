<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert Customer</title>
</head>
    <body>
    <%@page import="jobshop.DataHandler1"%>
    <%@page import="java.sql.ResultSet"%>
    <%@page import="java.sql.Array"%>
    
     <h1>Insert Customer</h1>
    <form action="insertCustomer.jsp" method="post">
        Customer Name: <input type="text" name="customerName"><br>
        Customer Address: <input type="text" name="customerAddress"><br>
        Category: <input type="number" name="category"><br>
        <input type="submit" value="Submit">
    </form>
    
    </body>
</html>
