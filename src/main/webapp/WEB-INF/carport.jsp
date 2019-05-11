<%-- 
    Document   : carport
    Created on : 08-May-2019, 11:14:01
    Author     : Casper
--%>

<%@page import="entity.Carport"%>
<%@page import="entity.Order"%>
<%@page import="entity.Odetail"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<% Carport carport = (Carport) request.getAttribute("carport"); %>
<% Order order = (Order) request.getAttribute("order");%>

<jsp:include page="header.jsp"></jsp:include>
    <body>
        <div class="container">
            <h1>Stykliste</h1>

        <p>Ordrenummer: <%= order.getId()%></p>
        <p>Kunde: <%= order.getCustomerId()%></p>
        <p>
            Bestilling: Carport <%= order.getWidth() + " x " + order.getLenght()%>
            <% if (order.getShedLength() < 0 && order.getShedWidth() < 0) { %>
            uden skur
            <% }else { %>
            med skur <%= order.getShedWidth() + " x " + order.getShedLength() %>
            <% } %>
        </p>
        <p>Pris: <%= carport.getPrice() %></p>

        <button type="button" class="button btn-primary">Gem</button>
        <button type="button" class="button btn-success">Send</button>
        <form method="POST" action="byggecenter?view=carportEdit">
            <input type="submit">
        <table class="table" id="stykliste">
            <thead>
                <tr>
                    <th>id</th>
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
                <% for (Odetail o : carport.getItems() ) {%>
                <tr>
                    <input type="hidden" name="id[]" value="<%= o.getId() %>">
                    <td class="odetailID"><%= o.getId()%></td>
                    <td><%= o.getProduct().getVariant_id()%></td>
                    <td><%= o.getProduct().getCategory().getName()%></td>
                    <td><%= o.getProduct().getName()%></td>
                    <td><%= o.getProduct().getLength()%> cm</td>
                    <td><%= o.getQty()%> stk</td>
                    <td><%= o.getAmount()%> kr.</td>
                    <td><textarea name="comments[]"><%= o.getComment()%></textarea></td>
                    <td class="odetailComment"><%= o.getComment()%></td>

                </tr>
                <% }%>
            </tbody>
        </table>
        </form>
        <button type="button" class="button btn-primary">Gem</button>
        <button type="button" class="button btn-success">Send</button>


        <div id="editBox">
            <form>
                <input type="text" name="comment" id="comment">
                <button type="submit" class="button btn-primary">OK</button>
            </form>
            <p>Rediger Beskrivelse</p>
        </div>

    </div><!-- container end -->

    <script>
        $(document).ready(function () {
            //$("#editBox").hide();

            $("#stykliste tr").click(function () {
                var id = $(this).find(".odetailID").text();
                var text = $(this).find(".odetailComment").text();
                var input = $("#comment");
                input.val(text);
                console.log(id);
                $("#editBox").show();
            });
        });
    </script>
</body>
</html>
