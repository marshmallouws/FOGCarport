<%-- 
    Document   : orderdetails
    Created on : 25-04-2019, 12:09:53
    Author     : Martin
--%>
<%@page import="entity.Customer"%>
<%@page import="entity.Order"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    Order order = (Order) request.getAttribute("order");
    Customer c = (Customer) request.getAttribute("customer");
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
            <h1>Bestilling #<%=order.getId()%></h1> 
            <p>Tilbud bestilt: <%= order.getDate()%></p>

            <div class="card" style="width: 18rem;">
                <div class="card-body">
                    <h5 class="card-title">Kunde information</h5>
                    <p class="card-text"><%=c.getName()%></p>
                    <p class="card-text"><%=c.getAddress() + ", " + c.getZip()%></p>
                    <p class="card-text"><%=c.getEmail()%></p>
                    <p class="card-text"><%=c.getPhone()%></p>
                </div>
            </div>

            <br>

            <table class="table table-hover">
                <tr>
                    <th>Længde</th>
                    <th>Bredde</th>
                    <th>Højde</th>
                    <th>Skur længde</th>
                    <th>Skur bredde</th>
                    <th>Tag vinkel</th>
                </tr> 
                <tr>
                    <td><%=order.getHeight()%></td>
                    <td><%=order.getWidth()%></td> 
                    <td><%=order.getLenght()%></td>
                    <td><%=order.getShedLength()%></td>
                    <td><%=order.getShedWidth()%></td>
                    <td><%=order.getRoofAngle()%></td>
                </tr> 
            </table>
        </div>
    </body>
</html>
