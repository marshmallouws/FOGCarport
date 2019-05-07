<%-- 
    Document   : backend
    Created on : 24-Apr-2019, 14:42:04
    Author     : Casper
--%>

<%@page import="java.util.List"%>
<%@page import="entity.Order"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% List<Order> allOrders = (List<Order>) request.getAttribute("allOrders"); %>
<% List<Order> unassignedOrders = (List<Order>) request.getAttribute("unassignedOrders"); %>

<jsp:include page="header.jsp"></jsp:include>

    <div class="container">
        <body>
            <h1>Backend Page</h1>

            <h3>Alle Ordrer</h3>
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
                <% for (Order o : allOrders) {%>
                <tr>
                    <td><%= o.getId()%></td>
                    <td><%= o.getLenght()%></td>
                    <td><%= o.getWidth()%></td>
                    <td><%= o.getHeight()%></td>
                    <td><%= o.getShedLength()%></td>
                    <td><%= o.getShedWidth()%></td>
                    <td><%= o.getRoofAngle()%></td>
                    <td><% 
                        if(o.getEmpl() == null) {
                            out.println("Ikke tildelt");
                        } else {
                            o.getEmpl().getInitials();
                        }
                        
                        %></td>
                    <td><%= o.getDate()%></td>
                    <td><form method="POST" action="byggecenter?view=orderinfo&orderID=<%= o.getId()%>"><button type="submit">Vis</button></form></td>
                    <td><form method="POST" action="byggecenter?view=assignorder"><input type="hidden" name="orderID" value="<%= o.getId()%>"><button type="submit">Assign</button></form></td>
                </tr>
                <% }%>
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
