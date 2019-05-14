<%-- 
    Document   : backend
    Created on : 24-Apr-2019, 14:42:04
    Author     : Casper
--%>

<%@page import="entity.Employee"%>
<%@page import="java.util.List"%>
<%@page import="entity.Order"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% List<Order> myOrders = (List<Order>) request.getAttribute("myOrders"); %>
<% List<Order> unassignedOrders = (List<Order>) request.getAttribute("unassignedOrders");

    String currLogged = ((Employee)session.getAttribute("user")).getInitials();
%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="/WEB-INF/parts/headmeta.jspf" %>
        <title>Bestillings Liste - Fog</title>
    </head>
    <body>
        <%@ include file="/WEB-INF/parts/navigation.jspf" %>
        <div class="page-wrapper menu-spacer">
        <body>
            <h1>Backend Page</h1>
            
            <h3>Logged in as: <%=currLogged%></h3>

            <h3>Mine ordrer</h3>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>id</th>
                        <th>Carport længde</th>
                        <th>Carport bredde</th>
                        <th>Carport højde</th>
                        <th>Skur længde</th>
                        <th>Skur bredde</th>
                        <th>Tag vinkel</th>
                        <th>Ansvarlig</th>
                        <th>Dato</th>
                        <th></th>
                        <th></th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
                <% for (Order o : myOrders) {%>
                <tr>
                    <td><%= o.getId()%></td>
                    <td><%= o.getLenght()%></td>
                    <td><%= o.getWidth()%></td>
                    <td><%= o.getHeight()%></td>
                    <td><%= o.getShedLength()%></td>
                    <td><%= o.getShedWidth()%></td>
                    <td><%= o.getRoofAngle()%></td>
                    <td><%= o.getEmpl().getInitials()%></td>
                    <td><%= o.getDate()%></td>
                    <td><form method="POST" action="byggecenter?view=orderinfo&orderID=<%= o.getId()%>"><button type="submit">Vis</button></form></td>
                    <td><form method="POST" action="byggecenter?view=assignorder"><input type="hidden" name="orderID" value="<%= o.getId()%>"><button type="submit">Assign</button></form></td>
                    <td><a href="./byggecenter?view=carport&orderID=<%= o.getId() %>">Stykliste</a></td>
                </tr>
                <%}%>
            </tbody>
        </table>

        <h3>Unassigned Ordrer</h3>
        <table class="table table-striped">
            <thead>
                <tr>
                    <th>id</th>
                    <th>Carport længde</th>
                    <th>Carport bredde</th>
                    <th>Carport højde</th>
                    <th>Skur længde</th>
                    <th>Skur bredde</th>
                    <th>Tag vinkel</th>
                    <th>Ansvarlig</th>
                    <th>Dato</th>
                    <th></th>
                    <th></th>
                </tr>
            </thead>
            <tbody>
                <% for (Order o : unassignedOrders) {%>
                <tr>
                    <td><%= o.getId()%></td>
                    <td><%= o.getLenght()%></td>
                    <td><%= o.getWidth()%></td>
                    <td><%= o.getHeight()%></td>
                    <td><%= o.getShedLength()%></td>
                    <td><%= o.getShedWidth()%></td>
                    <td><%= o.getRoofAngle()%></td>
                    <td><%= "Ikke tildelt" %></td>
                    <td><%= o.getDate()%></td>
                    <td><form method="POST" action="byggecenter?view=orderinfo&orderID=<%= o.getId()%>"><button type="submit">Vis</button></form></td>
                    <td><form method="POST" action="byggecenter?view=assignorder"><input type="hidden" name="orderID" value="<%= o.getId()%>"><button type="submit">Assign</button></form></td>
                </tr>
                <% }%>
            </tbody>
        </table>
</div> <!-- container end -->
</body>
</html>
