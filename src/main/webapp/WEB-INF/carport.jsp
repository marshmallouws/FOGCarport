<%-- 
    Document   : carport
    Created on : 08-May-2019, 11:14:01
    Author     : Casper
--%>

<%@page import="entity.Odetail"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% List<Odetail> odetails = (List<Odetail>) request.getAttribute("carport"); %>

<jsp:include page="header.jsp"></jsp:include>
    <body>
        <h1>Hello World!</h1>
        
        <table class="table">
            <thead>
                    <tr>
                        <th>id</th>
                        <th>Product</th>
                        <th>Navn</th>
                        <th>orderID</th>
                        <th>Qty</th>
                        <th>Amount</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Odetail o : odetails) { %>
                    <tr>
                        <td><%= o.getId() %></td>
                        <td><%= o.getProduct() %></td>
                        <td><%= o.getProduct().getName() %></td>
                        <td><%= o.getOrder_id() %></td>
                        <td><%= o.getQty() %></td>
                        <td><%= o.getAmount() %></td>
                    </tr>
                    <% } %>
                </tbody>
        </table>
    </body>
</html>
