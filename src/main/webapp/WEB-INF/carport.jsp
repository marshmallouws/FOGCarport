<%-- 
    Document   : carport
    Created on : 08-May-2019, 11:14:01
    Author     : Casper
--%>

<%@page import="entity.Order"%>
<%@page import="entity.Odetail"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% List<Odetail> odetails = (List<Odetail>) request.getAttribute("carport"); %>
<% Order order = (Order) request.getAttribute("order"); %>

<jsp:include page="header.jsp"></jsp:include>
    <body>
        <div class="container">
        <h1>Stykliste</h1>
        
        <p>Ordrenummer: <%= order.getId() %></p>
        <p>Kunde: <%= order.getCustomerId() %></p>
        <p>Bestilling: Carport <%= order.getWidth() + " x " + order.getLenght() %> </p>
        
        <table class="table">
            <thead>
                    <tr>
                        <th>Varenummer</th>
                        <th>Placering</th>
                        <th>Navn</th>
                        <th>Længde</th>
                        <th>Antal</th>
                        <th>Beløb</th>
                        <th>Beskrivelse</th>
                    </tr>
                </thead>
                <tbody>
                    <% for (Odetail o : odetails) { %>
                    <tr>
                        <td><%= o.getProduct().getVariant_id() %></td>
                        <td><%= o.getProduct().getCategory().getName() %></td>
                        <td><%= o.getProduct().getName() %></td>
                        <td><%= o.getProduct().getLength() %> cm</td>
                        <td><%= o.getQty() %> stk</td>
                        <td><%= o.getAmount() %> kr.</td>
                        <td><%= o.getComment() %></td>
                    </tr>
                    <% } %>
                </tbody>
        </table>
                
        </div><!-- container end -->
    </body>
</html>
