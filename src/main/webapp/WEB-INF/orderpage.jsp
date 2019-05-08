<%-- 
    Document   : orderpage
    Created on : Apr 23, 2019, 10:52:48 AM
    Author     : caspe
--%>
<%@page import="entity.Product"%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    List<Integer> carportSelectWidth = (List<Integer>) request.getAttribute("carportSelectWidth");
    List<Integer> carportSelectLength = (List<Integer>) request.getAttribute("carportSelectLength");
    List<Integer> shedSelectWidth = (List<Integer>) request.getAttribute("shedSelectWidth");
    List<Integer> shedSelectLength = (List<Integer>) request.getAttribute("shedSelectLength");
    List<Product> roofTypes = (List<Product>) request.getAttribute("roofTypes");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Orderpage</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    </head>
    <body>
        <div class="container">
            <h1>Orderpage</h1>

            <form method="POST" action="byggecenter?view=addorder">
                <div class="form-group">
                    <label for="carportWidth">Carport bredde</label>
                    <select class="form-control" name="carportWidth" id="carportWidth">
                        <option value=0>Vælg bredde</option>
                        <% for (int width : carportSelectWidth) {%>
                        <option value=<%= width%>><%= width%> cm</option>
                        <% } %>
                    </select>
                </div>

                <div class="form-group">
                    <label for="carportLength">Carport længde</label>
                    <select class="form-control" name="carportLength" id="carportLength">
                        <option value=0>Vælg længde</option>
                        <% for (int length : carportSelectLength) {%>
                        <option value=<%= length%>><%= length%> cm</option>
                        <% } %>
                    </select>
                </div>

                <div class="form-group">
                    <label for="roofType">Tag</label>
                    <select class="form-control" name="roofType" id="roofType">
                        <option value=0>Vælg tag</option>
                        <% for (Product roof : roofTypes) { %>
                        <option value=<%= roof.getId() %>><%= roof.getName() %></option>
                        <% } %>
                    </select>
                </div>
                    
                    <div class="form-group">
                    <label for="roofAngle">Tag-vinkel</label>
                    <select class="form-control" name="roofAngle" id="roofAngle">
                        <option value=0>Ingen rejsning</option>
                        <option value="10">10</option>
                        <option value="20">20</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="shedWidth">Redskabsrum bredde:</label>
                    <select class="form-control" name="shedWidth" id="shedWidth">
                        <option value=0>Ønsker ikke redskabsrum</option>
                        <% for (int width : shedSelectWidth) {%>
                        <option value=<%= width%>><%= width%> cm</option>
                        <% } %>
                    </select>
                </div>

                <div class="form-group">
                    <label for="shedLength">Redskabsrum længde:</label>
                    <select class="form-control" name="shedLength" id="shedLength">
                        <option value=0>Ønsker ikke redskabsrum</option>
                        <% for (int length : shedSelectLength) {%>
                        <option value=<%= length%>><%= length%> cm</option>
                        <% }%>
                    </select>
                </div>

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

 <!--               <div class="form-group">
                    <label for="message">Evt. bemærkninger</label>
                    <textarea class="form-control" name="message" id="message"></textarea>
                </div>
-->
                <button type="submit" class="btn btn-success">Bestil Carport</button>    

            </form>

        </div><!-- container end -->
    </body>
</html>
