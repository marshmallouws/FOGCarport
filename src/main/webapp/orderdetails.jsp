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
            <h1>Single order page</h1> 
            <p><%= order.getDate() %></p>
        </div>
    </body>
</html>
