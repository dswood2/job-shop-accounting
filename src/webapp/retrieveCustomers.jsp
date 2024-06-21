<%@ page import="jobshop.DataHandler1" %>
<%@ page import="java.util.List" %>
<%@ page import="java.sql.SQLException" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
    int minCategory = Integer.parseInt(request.getParameter("minCategory"));
    int maxCategory = Integer.parseInt(request.getParameter("maxCategory"));

    try {
    	DataHandler1 dbHandler = new DataHandler1();
        List<String> customerNames = dbHandler.retrieveCustomerNamesByCategoryRange(minCategory, maxCategory);
        %>
        <!DOCTYPE html>
        <html>
        <head>
            <title>Customer Names</title>
        </head>
        <body>
            <h1>Customer Names within Category Range <%= minCategory %> to <%= maxCategory %></h1>
            <table>
                <tr>
                    <th>Customer Name</th>
                </tr>
                <% 
                    for (String customer : customerNames) {
                %>
                    <tr>
                        <td><%= customer %></td>
                    </tr>
                <% } %>
            </table>
        </body>
        </html>
        <%
    } catch (SQLException e) {
        e.printStackTrace();
        // Handle exception accordingly
    }
%>
