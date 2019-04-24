<%-- 
    Document   : orderpage
    Created on : Apr 23, 2019, 10:52:48 AM
    Author     : caspe
--%>
<%@page import="java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    List<Integer> carportSelectWidth = (List<Integer>) request.getAttribute("carportSelectWidth");
    List<Integer> carportSelectLength = (List<Integer>) request.getAttribute("carportSelectLength");
    List<Integer> shedSelectWidth = (List<Integer>) request.getAttribute("shedSelectWidth");
    List<Integer> shedSelectLength = (List<Integer>) request.getAttribute("shedSelectLength");
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

            <form>
                <div class="form-group">
                    <label for="carport-width">Carport bredde</label>
                    <select class="form-control" name="carport-width" id="carport-width">
                        <option value=0>Vælg bredde</option>
                        <% for (int width : carportSelectWidth) {%>
                        <option value=<%= width%>><%= width%> cm</option>
                        <% } %>
                    </select>
                </div>

                <div class="form-group">
                    <label for="carport-length">Carport længde</label>
                    <select class="form-control" name="carport-length" id="carport-length">
                        <option value=0>Vælg længde</option>
                        <% for (int length : carportSelectLength) {%>
                        <option value=<%= length%>><%= length%> cm</option>
                        <% } %>
                    </select>
                </div>

                <div class="form-group">
                    <label for="roof-material">Tag</label>
                    <select class="form-control" name="roof-material" id="roof-material">
                        <option value=0>Vælg tag</option>
                        <option value="Plasttrapezplader">Plasttrapezplader</option>
                    </select>
                </div>
                    
                    <div class="form-group">
                    <label for="roof-angle">Tag-vinkel</label>
                    <select class="form-control" name="roof-angle" id="roof-angle">
                        <option value=0>Ingen rejsning LOL</option>
                        <option value="10">10</option>
                        <option value="20">20</option>
                    </select>
                </div>

                <div class="form-group">
                    <label for="shed-width">Redskabsrum bredde:</label>
                    <select class="form-control" name="shed-width" id="shed-width">
                        <option value=0>Ønsker ikke redskabsrum</option>
                        <% for (int width : shedSelectWidth) {%>
                        <option value=<%= width%>><%= width%> cm</option>
                        <% } %>
                    </select>
                </div>

                <div class="form-group">
                    <label for="shed-length">Redskabsrum længde:</label>
                    <select class="form-control" name="shed-length" id="shed-length">
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

                <div class="form-group">
                    <label for="message">Evt. bemærkninger</label>
                    <textarea class="form-control" name="message" id="message"></textarea>
                </div>

            </form>

        </div><!-- container end -->
    </body>
</html>
