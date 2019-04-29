<%@page import="java.util.List"%>
<%@page import="entity.Order"%>
<%-- 
    Document   : orderpage-admin
    Created on : Apr 29, 2019, 12:34:28 PM
    Author     : caspe
--%>
<jsp:include page="header.jsp"></jsp:include>

<% Order order = (Order) request.getAttribute("order");
    List<Integer> carportSelectWidth = (List<Integer>) request.getAttribute("carportSelectWidth");
    List<Integer> carportSelectLength = (List<Integer>) request.getAttribute("carportSelectLength");
    List<Integer> carportSelectHeight = (List<Integer>) request.getAttribute("carportSelectHeight");
    List<Integer> shedSelectWidth = (List<Integer>) request.getAttribute("shedSelectWidth");
    List<Integer> shedSelectLength = (List<Integer>) request.getAttribute("shedSelectLength");
    List<Integer> roofSelectAngle = (List<Integer>) request.getAttribute("roofSelectAngle");
%>
<body>
    <div class="container">
        <h1>Orderdetails-admin</h1>

        <button type="button" id="editBtn">Edit</button>
        <form method="POST" id="orderForm">

            <div class="form-group">
                <label for="orderID">Ordrenummer</label>
                <input type="text" class="form-control input-disabled" name="orderID" id="orderID" value="<%= order.getId()%>">
            </div>

            <div class="form-group">
                <label for="orderDate">Bestillingsdato</label>
                <input type="text" class="form-control input-disabled" name="orderDate" id="orderDate" value="<%= order.getDate()%>">
            </div>

            <div class="form-group">
                <label for="carportLength">Carport l�ngde</label>
                <select class="form-control input-disabled" name="carportLength" id="carportLength">
                    <option value=0>V�lg l�ngde</option>
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
                    <option value="0">V�lg bredde</option>
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
                <label for="carportHeight">Carport h�jde</label>
                <select class="form-control input-disabled" name="carportHeight" id="carportHeight">
                    <option value=0>V�lg h�jde</option>
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
                <select class="form-control" name="roofMaterial" id="roofMaterial">
                    <option value=0>V�lg tag</option>
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
                    <option value="<%= angle%>" selected><%= angle%> cm</option>
                    <% } else {%>
                    <option value="<%= angle%>"><%= angle%> cm</option>
                    <% }
                        } %>
                </select>
            </div>

            <div class="form-group">
                <label for="shedWidth">Redskabsrum bredde:</label>
                <select class="form-control input-disabled" name="shedWidth" id="shedWidth">
                    <option value=0>�nsker ikke redskabsrum</option>
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
                <label for="shedLength">Redskabsrum l�ngde:</label>
                <select class="form-control input-disabled" name="shedLength" id="shedLength">
                    <option value=0>�nsker ikke redskabsrum</option>
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
            <!--
                            <div class="form-group">
                                <label for="fullname">Navn</label>
                                <input type="text" class="form-control" name="fullname" id="fullname">
                            </div>
            
                            <div class="form-group">
                                <label for="address">Adresse</label>
                                <input type="text" class="form-control" name="address" id="address">
                            </div>
            
                            <div class="form-group">
                                <label for="zip">Postnummer</label>
                                <input type="text" class="form-control" name="zip" id="zip">
                            </div>
            
                            <div class="form-group">
                                <label for="phone">Telefon</label>
                                <input type="text" class="form-control" name="phone" id="phone">
                            </div>
            
                            <div class="form-group">
                                <label for="email">Email</label>
                                <input type="text" class="form-control" name="email" id="email">
                            </div>
            
                            <div class="form-group">
                                <label for="message">Evt. bem�rkninger</label>
                                <textarea class="form-control" name="message" id="message"></textarea>
                            </div>
            -->
            <button type="submit" class="btn btn-success">Gem</button>    

        </form>

    </div> <!-- container end -->

    <script>
        $(document).ready(function () {
            
            // sets disabled on input fields with class 'input-disabled'
            $(".input-disabled").prop("disabled", true);

            // removes disabled on inputs by clicking the edit btn
            $(document).ready(function () {
                $("#editBtn").click(function () {
                    $(".input-disabled").removeAttr("disabled");
                });
            });
        });
    </script>

</body>
</html>