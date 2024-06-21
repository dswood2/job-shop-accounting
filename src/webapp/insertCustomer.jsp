<%@ page import="jobshop.DataHandler1" %>
<%@ page import="java.sql.SQLException" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    String customerName = request.getParameter("customerName");
    String customerAddress = request.getParameter("customerAddress");
    int category = Integer.parseInt(request.getParameter("category"));

    try {
    	DataHandler1 dbHandler = new DataHandler1();
        boolean success = dbHandler.insertCustomer(customerName, customerAddress, category);
        if (success) {
            %>
            <!DOCTYPE html>
            <html>
            <head>
                <title>Insertion Result</title>
            </head>
            <body>
                <h1>Insertion Result</h1>
                <p>Customer inserted successfully!</p>
            </body>
            </html>
            <%
        } else {
            %>
            <!DOCTYPE html>
            <html>
            <head>
                <title>Insertion Result</title>
            </head>
            <body>
                <h1>Insertion Result</h1>
                <p>Failed to insert customer.</p>
            </body>
            </html>
            <%
        }
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle exception accordingly
    }
%>
