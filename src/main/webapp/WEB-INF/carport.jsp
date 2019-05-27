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
<% String error = (String) request.getAttribute("error");%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="/WEB-INF/parts/headmeta.jspf" %>
        <title>Stykliste - <%-- Bestilling #<%=order.getId()%> --%> Fog</title>
    </head>
    <body>
        <%@ include file="/WEB-INF/parts/navigation.jspf" %>
        <div class="page-wrapper menu-spacer">

            <% if (error != null) {%>

            <p><%= error%></p>

            <% } else {%>

            <h1>Stykliste</h1>

            <p>Ordrenummer: <%= order.getId()%></p>
            <p>Kunde: <%= order.getCustomerId()%></p>
            <p>
                Bestilling: Carport <%= order.getWidth() + " x " + order.getLenght()%>
                <% if (order.getShedLength() < 0 && order.getShedWidth() < 0) { %>
                uden skur
                <% } else {%>
                med skur <%= order.getShedWidth() + " x " + order.getShedLength()%>
                <% }%>
            </p>
            <p>Pris: <%= carport.getPrice()%></p>


            <form method="POST" action="byggecenter?view=carportEdit">
                <h5>Træ & Tagplader</h5>
                <table class="table table-striped table-sm" id="stykliste">
                    <thead>
                        <tr>
                            <th>Varenummer</th>
                            <th>Navn</th>
                            <th>Længde</th>
                            <th>Antal</th>
                            <th>Beløb</th>
                            <th>Placering</th>
                            <th>Beskrivelse</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Odetail o : carport.getWoodsList()) {%>
                        <tr>
                    <input type="hidden" name="id[]" value="<%= o.getId()%>">
                    <td><%= o.getProduct().getVariant_id()%></td>
                    <td><%= o.getProduct().getName()%></td>
                    <td><%= o.getProduct().getLength()%> cm</td>
                    <td><%= o.getQty()%> stk</td>
                    <td><%= (int) o.getAmount()%> kr.</td>
                    <td><span class="badge badge-light"><%= o.getProduct().getCategory().getName()%></span></td>
                    <td><textarea name="comments[]"><%= o.getComment()%></textarea></td>
                    </tr>
                    <% }%>
                    </tbody>
                </table>
                <h5>Beslag & Skruer</h5>
                <table class="table table-striped table-sm">
                    <thead>
                        <tr>
                            <th>Varenummer</th>
                            <th>Navn</th>
                            <th>Længde</th>
                            <th>Antal</th>
                            <th>Beløb</th>
                            <th>Placering</th>
                            <th>Beskrivelse</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Odetail o : carport.getScrewsList()) {%>
                        <tr>
                    <input type="hidden" name="id[]" value="<%= o.getId()%>">
                    <td><%= o.getProduct().getVariant_id()%></td>
                    <td><%= o.getProduct().getName()%></td> 
                    <td><%= o.getProduct().getLength()%> cm</td>
                    <td><%= o.getQty()%> stk</td>
                    <td><%= (int) o.getAmount()%> kr.</td>
                    <td><span class="badge badge-light"><%= o.getProduct().getCategory().getName()%></span></td>
                    <td><textarea name="comments[]"><%= o.getComment()%></textarea></td>
                    </tr>
                    <% }%>
                    </tbody>
                </table>
                <input type="hidden" name="orderID" value="<%= order.getId()%>">
                <input type="submit" class="btn btn-primary" value="Gem ændringer" style="margin-bottom:40px;">
            </form>

            <% }%> <!-- end of else statement handling error-->

        </div><!-- container end -->

        <script>
            $(document).ready(function () {
                //$("#editBox").hide();

                $("#stykliste tr").click(function () {
                    var id = $(this).find(".odetailID").text();
                    var text = $(this).find(".odetailComment").text();
                    var input = $("#comment");
                    input.val(text);
                    $("#editBox").show();
                });
            });
        </script>
    </body>
</html>
