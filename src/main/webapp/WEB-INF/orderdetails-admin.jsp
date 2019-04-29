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
                <input type="text" class="form-control" name="orderID" id="orderID" value="<%= order.getId()%>" disabled>
            </div>
            
            <div class="form-group">
                <label for="orderDate">Bestillingsdato</label>
                <input type="text" class="form-control" name="orderDate" id="orderDate" value="<%= order.getDate() %>" disabled>
            </div>

             <div class="form-group">
                <label for="carportLength">Carport længde</label>
                <select class="form-control" name="carportLength" id="carportLength" disabled>
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
                <select class="form-control" name="carportWidth" id="carportWidth" disabled>
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
                <select class="form-control" name="carportHeight" id="carportHeight" disabled>
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
                <select class="form-control" name="roofMaterial" id="roofMaterial">
                    <option value=0>Vælg tag</option>
                    <option value="Plasttrapezplader">Plasttrapezplader</option>
                </select>
            </div>

            <div class="form-group">
                <label for="roofAngle">Tag-vinkel</label>
                <select class="form-control" name="roofAngle" id="roofAngle" disabled>
                    <option value=0>Ingen rejsning</option>
                    <%
                        for (int angle : roofSelectAngle) {
                            if (angle == order.getRoofAngle()) {
                    %>
                    <option value="<%= angle %>" selected><%= angle %> cm</option>
                    <% } else {%>
                    <option value="<%= angle %>"><%= angle %> cm</option>
                    <% }
                        } %>
                </select>
            </div>

            <div class="form-group">
                <label for="shedWidth">Redskabsrum bredde:</label>
                <select class="form-control" name="shedWidth" id="shedWidth" disabled>
                    <option value=0>Ønsker ikke redskabsrum</option>
                    <%
                        for (int width : shedSelectWidth) {
                            if (width == order.getShedWidth()) {
                    %>
                    <option value="<%= width%>" selected><%= width%> cm</option>
                    <% } else { %>
                    <option value="<%= width%>"><%= width%> cm</option>
                    <% }
                        } %>
                </select>
            </div>

            <div class="form-group">
                <label for="shedLength">Redskabsrum længde:</label>
                <select class="form-control" name="shedLength" id="shedLength" disabled>
                    <option value=0>Ønsker ikke redskabsrum</option>
                    <% 
                        for (int length : shedSelectLength) {
                            if (length == order.getShedLength()) {
                    %>
                    <option value="<%= length%>" selected><%= length%> cm</option>
                    <% } else { %>
                    <option value="<%= length%>"><%= length%> cm</option>
                    <% } }%>
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
                                <label for="message">Evt. bemærkninger</label>
                                <textarea class="form-control" name="message" id="message"></textarea>
                            </div>
            -->
            <button type="submit" class="btn btn-success">Gem</button>    

        </form>

    </div> <!-- container end -->
    
    <script>
        $(document).ready(function() {
            $("#editBtn").click(function() {
               $("form#orderForm :input").each(function() {
                   
               }); 
            });
        });
    </script>
    
</body>
</html>
