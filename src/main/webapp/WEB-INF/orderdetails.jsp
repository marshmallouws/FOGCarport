<%-- 
    Document   : orderdetails
    Created on : 25-04-2019, 12:09:53
    Author     : Martin
--%>
<%@page import="entity.Order"%>
<jsp:include page="header.jsp"></jsp:include>
<% Order order = (Order) request.getAttribute("order"); %>
    <body>
        <div class="container">
            <h1>Details for order #<%=order.getId()%></h1> 
            <p>Ordered: <%= order.getDate() %></p>
            <table class="table table-hover">
                <tr>
                    <th>Height</th>
                    <th>Width</th> 
                    <th>Length</th>
                    <th>Shed Length</th>
                    <th>Shed width</th>
                    <th>Roof angle</th>
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
