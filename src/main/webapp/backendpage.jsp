<%-- 
    Document   : backend
    Created on : 24-Apr-2019, 14:42:04
    Author     : Casper
--%>

<%@page import="java.util.List"%>
<%@page import="entity.Order"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% List<Order> orders = (List<Order>) request.getAttribute("orders"); %>

<jsp:include page="header.jsp"></jsp:include>

    <div class="container">
        <body>
            <h1>Backend Page</h1>

            <h3>Orders</h3>
            <table>
                <thead>
                    <tr>
                        <th>id</th>
                        <th>Carport længde</th>
                        <th>Carport bredde</th>
                        <th>Carport højde</th>
                        <th>Skur længde</th>
                        <th>Skur bredde</th>
                        <th>Tag vinkel</th>
                        <th>Dato</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                <% for (Order o : orders) {%>
                <tr>
                    <td><%= o.getId()%></td>
                    <td><%= o.getLenght()%></td>
                    <td><%= o.getWidth()%></td>
                    <td><%= o.getHeight()%></td>
                    <td><%= o.getShedLength()%></td>
                    <td><%= o.getShedWidth()%></td>
                    <td><%= o.getRoofAngle()%></td>
                    <td><%= o.getDate()%></td>
                    <td><form method="POST" action="FrontController?command=orderinfo&orderID=<%= o.getId()%>"><button type="submit">Vis Ordre</button></form></td>
                </tr>
                <% }%>
            </tbody>
        </table>
</div> <!-- container end -->
</body>
</html>
