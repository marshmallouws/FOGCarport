<%@page import="entity.Customer"%>
<%@page import="entity.Employee"%>
<%@page import="java.util.List"%>
<%@page import="entity.Order"%>
<%@page import="entity.Carport"%>
<%-- 
    Document   : orderpage-admin
    Created on : Apr 29, 2019, 12:34:28 PM
    Author     : caspe
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<% Order order = (Order) request.getAttribute("order");
    List<Integer> carportSelectWidth = (List<Integer>) request.getAttribute("carportSelectWidth");
    List<Integer> carportSelectLength = (List<Integer>) request.getAttribute("carportSelectLength");
    List<Integer> carportSelectHeight = (List<Integer>) request.getAttribute("carportSelectHeight");
    List<Integer> shedSelectWidth = (List<Integer>) request.getAttribute("shedSelectWidth");
    List<Integer> shedSelectLength = (List<Integer>) request.getAttribute("shedSelectLength");
    List<Integer> roofSelectAngle = (List<Integer>) request.getAttribute("roofSelectAngle");
    List<Employee> employees = (List<Employee>) request.getAttribute("employees");
    Customer customer = (Customer) request.getAttribute("customer");
    Carport carport = (Carport) request.getAttribute("carport");
%>
<!DOCTYPE html>
<html>
    <head>
        <%@ include file="/WEB-INF/parts/headmeta.jspf" %>
        <title>Bestilling #<%=order.getId()%> - Fog</title>
    </head>
    <body>
        <%@ include file="/WEB-INF/parts/navigation.jspf" %>
        <div class="page-wrapper menu-spacer" style="margin-bottom:20px;">
            <h1>Bestilling #<%=order.getId()%></h1> 

            <div id="search-container">
                <input style="width: 220px;display: inline-block;vertical-align: middle;" class="form-control" type="text" name="searchInput" id="searchInput" placeholder="Indtast bestillingsnummer..">
                <button class="btn btn-primary" type="button" id="searchBtn">Gå til bestilling</button>    
            </div>



            <% if (request.getAttribute("error") != null) {%>
            <p><%= request.getAttribute("error")%></p>
            <% }%>
            <form method="POST" id="orderForm" action="byggecenter?view=updateorder">

                <div class="row">
                    <div class="col">

                        <div class="form-group">
                            <label for="order">Ordrenummer</label>
                            <input type="text" class="form-control" name="order" id="order" value="<%= order.getId()%>">
                        </div>
                        <input type="hidden" class="input-disabled" name="orderID" id="orderID" value="<%= order.getId()%>">


                        <div class="form-group">
                            <label for="orderDate">Bestillingsdato</label>
                            <input type="text" class="form-control" name="orderDate" id="orderDate" value="<%= order.getDate()%>">
                        </div>

                        <div class="form-group">
                            <label for="employee">Ansvarlig</label>
                            <select class="form-control input-disabled" name="employee" id="employee">
                                <option value=0>Vælg medarbejder</option>
                                <%
                                    for (Employee employee : employees) {
                                        if (employee.getId() == order.employeeId()) {
                                %>
                                <option value="<%= order.employeeId()%>" selected><%= order.employeeId()%></option>
                                <%} else {%>
                                <option value="<%= employee.getId()%>"><%= employee.getInitials()%></option>
                                <% }
                                    }%>

                            </select>
                        </div>


                        <div class="form-group">
                            <label for="fullname">Navn</label>
                            <input type="text" class="form-control" name="fullname" id="fullname" value="<%= customer.getName()%>">
                        </div>

                        <div class="form-group">
                            <label for="address">Adresse</label>
                            <input type="text" class="form-control" name="address" id="address" value="<%= customer.getAddress()%>">
                        </div>

                        <div class="form-group">
                            <label for="zip">Postnummer</label>
                            <input type="text" class="form-control" name="zip" id="zip" value="<%= customer.getZip()%>">
                        </div>

                        <div class="form-group">
                            <label for="phone">Telefon</label>
                            <input type="text" class="form-control" name="phone" id="phone" value="<%= customer.getPhone()%>">
                        </div>

                        <div class="form-group">
                            <label for="email">Email</label>
                            <input type="text" class="form-control" name="email" id="email" value="<%= customer.getEmail()%>">
                        </div>

                        <div class="form-group">
                            <label for="message">Evt. bemærkninger</label>
                            <textarea class="form-control" name="message" id="message"></textarea>
                        </div>

                    </div>

                    <div class="col">
                        <div class="form-group">
                            <label for="carportLength">Carport længde</label>
                            <select class="form-control input-disabled" name="carportLength" id="carportLength">
                                <option value=0>Vælg længde</option>
                                <%
                                    for (int length : carportSelectLength) {
                                        if (length == order.getLenght()) {
                                %>
                                <option value="<%= length%>" selected><%= length%> cm</option>
                                <% } else {%>
                                <option value="<%= length%>"><%= length%> cm</option>
                                <% }
                                    } %>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="carportWidth">Carport bredde</label>
                            <select class="form-control input-disabled" name="carportWidth" id="carportWidth">
                                <option value="0">Vælg bredde</option>
                                <%
                                    for (int width : carportSelectWidth) {
                                        if (width == order.getWidth()) {
                                %>
                                <option value="<%= width%>" selected><%= width%> cm</option>
                                <% } else {%>
                                <option value="<%= width%>"><%= width%> cm</option>
                                <% }
                                    } %>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="carportHeight">Carport højde</label>
                            <select class="form-control input-disabled" name="carportHeight" id="carportHeight">
                                <option value=0>Vælg højde</option>
                                <%
                                    for (int height : carportSelectHeight) {
                                        if (height == order.getHeight()) {
                                %>
                                <option value="<%= height%>" selected><%= height%> cm</option>
                                <% } else {%>
                                <option value="<%= height%>"><%= height%> cm</option>
                                <% }
                                    } %>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="roofMaterial">Tag</label>
                            <select class="form-control input-disabled" name="roofMaterial" id="roofMaterial">
                                <option value=0>Vælg tag</option>
                                <option value="Plasttrapezplader">Plasttrapezplader</option>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="roofAngle">Tag-vinkel</label>
                            <select class="form-control input-disabled" name="roofAngle" id="roofAngle">
                                <option value=0>Ingen rejsning</option>
                                <%
                                    for (int angle : roofSelectAngle) {
                                        if (angle == order.getRoofAngle()) {
                                %>
                                <option value="<%= angle%>" selected><%= angle%>°</option>
                                <% } else {%>
                                <option value="<%= angle%>"><%= angle%>°</option>
                                <% }
                                    } %>
                            </select>
                        </div>

                        <div class="custom-control custom-switch">
                            <% if (order.getShedLength() > 0 && order.getShedWidth() > 0) { %>
                            <input type="checkbox" class="custom-control-input input-disabled" id="switch1" name="example" checked>
                            <% } else {  %>
                            <input type="checkbox" class="custom-control-input input-disabled" id="switch1" name="example">
                            <% } %>
                            <label class="custom-control-label" for="switch1">Redskabsrum</label>
                        </div>

                        <div class="form-group">
                            <label for="shedWidth">Redskabsrum bredde</label>
                            <select class="form-control input-disabled" name="shedWidth" id="shedWidth">
                                <option value=0>Ønsker ikke redskabsrum</option>
                                <%
                                    for (int width : shedSelectWidth) {
                                        if (width == order.getShedWidth()) {
                                %>
                                <option value="<%= width%>" selected><%= width%> cm</option>
                                <% } else {%>
                                <option value="<%= width%>"><%= width%> cm</option>
                                <% }
                                    } %>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="shedLength">Redskabsrum længde</label>
                            <select class="form-control input-disabled" name="shedLength" id="shedLength">
                                <option value=0>Ønsker ikke redskabsrum</option>
                                <%
                                    for (int length : shedSelectLength) {
                                        if (length == order.getShedLength()) {
                                %>
                                <option value="<%= length%>" selected><%= length%> cm</option>
                                <% } else {%>
                                <option value="<%= length%>"><%= length%> cm</option>
                                <% }
                                    }%>
                            </select>
                        </div>

                        <div class="form-group">
                            <label for="calcPrice">Pris for materialer</label>
                            <input type="text" class="form-control" name="calcPrice" id="price" value="<%= carport.getPrice()%>">
                            </select>
                        </div>
                        <div class="form-group">
                            <label for="price">Salgspris</label>
                            <%
                                if (order.getSalesPrice() == 0) {
                            %>
                            <input type="number" class="form-control input-disabled" name="price" id="price" placeholder="Ikke angivet">
                            <%
                            } else {
                            %> 
                            <input type="number" class="form-control input-disabled" name="price" id="price" value="<%= order.getSalesPrice()%>">
                            <%
                                }
                            %>
                            </select>
                        </div>
                    </div>
                </div>

                <button type="button" class="btn btn-primary" id="editBtn">Redigér</button>
                <button type="submit" class="btn btn-success" id="updateBtn">Gem</button>
                <a style="float:right;" class="btn btn-primary" id="pdfBtn" href="./byggecenter?view=downloadpdf&orderID=<%= order.getId()%>">Hent PDF</a>

            </form>
    

        </div> <!-- container end -->

        <script>
            $(document).ready(function () {
                // sets disabled on input fields with class 'input-disabled'
                // removes it again from 'editBtn'
                $("#orderForm :input").each(function () {
                    var input = $(this);
                    input.prop("disabled", true);

                    $("#editBtn").removeAttr("disabled");
                });

                // removes disabled on inputs by clicking the 'edit' btn
                $("#editBtn").click(function () {
                    var inputs = $(".input-disabled");

                    if (inputs.hasClass("active")) {
                        inputs.prop("disabled", true);
                        inputs.removeClass("active");
                        $("#updateBtn").prop("disabled", true);
                    } else {
                        $(".input-disabled").removeAttr("disabled").addClass("active");
                        $("#updateBtn").removeAttr("disabled");
                    }

                });

                //search function
                $("#searchBtn").click(function () {
                    var search = $("#searchInput").val();
                    var id = parseInt(search, 10);

                    if (isNaN(id)) {
                        // add error handling
                    } else {
                        window.location = "byggecenter?view=orderinfoadmin&orderID=" + id;
                    }

                });
            });
        </script>

    </body>
</html>
