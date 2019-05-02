<%-- 
    Document   : orderdetails
    Created on : 25-04-2019, 12:09:53
    Author     : Martin
--%>
<%@page import="entity.Customer"%>
<%@page import="entity.Order"%>
<jsp:include page="header.jsp"></jsp:include>
<% 
    Order order = (Order) request.getAttribute("order"); 
    Customer c = (Customer) request.getAttribute("customer");
%>
    <body>
        <div class="container">
            <h1>Details for order #<%=order.getId()%></h1> 
            <p>Ordered: <%= order.getDate() %></p>
            
            <div class="card" style="width: 18rem;">
                <div class="card-body">
                  <h5 class="card-title">Customer information</h5>
                  <p class="card-text"><%=c.getName()%></p>
                  <p class="card-text"><%=c.getEmail()%></p>
                  <p class="card-text"><%=c.getPhone()%></p>
                </div>
            </div>
            
            <br>
            
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
