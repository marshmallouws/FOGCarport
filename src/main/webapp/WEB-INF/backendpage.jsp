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

    String currLogged = ((Employee) session.getAttribute("user")).getInitials();
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
            <h1>Alle bestillinger</h1>

            <h6><i>Logget på som:</i> <%=currLogged%></h6>
        </div>
        <div class="separator" style="margin-top:30px;"></div>
        <div class="page-wrapper">
            <h3>Tildelte Bestillinger</h3>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Længde</th>
                        <th>Bredde</th>
                        <th>Højde</th>
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
                        <td><a class="btn btn-primary" href="byggecenter?view=orderinfo&orderID=<%= o.getId()%>">Vis</a></td>
                        <td><a class="btn btn-secondary" href="./byggecenter?view=carport&orderID=<%= o.getId()%>">Stykliste</a></td>
                    </tr>
                    <%}%>
                </tbody>
            </table>

            <div class="separator" style="margin-top:50px;"></div>

            <h3>Afventende Bestillinger</h3>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Længde</th>
                        <th>Bredde</th>
                        <th>Højde</th>
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
                        <td><%= "Ikke tildelt"%></td>
                        <td><%= o.getDate()%></td>
                        <td><a class="btn btn-primary" href="byggecenter?view=orderinfo&orderID=<%= o.getId()%>">Vis</a></td>
                        <td><a class="btn btn-info" href="byggecenter?view=assignorder&orderID=<%= o.getId()%>">Tildel</a></td>
                    </tr>
                    <% }%>
                </tbody>
            </table>
        </div> <!-- container end -->
    </body>
</html>
