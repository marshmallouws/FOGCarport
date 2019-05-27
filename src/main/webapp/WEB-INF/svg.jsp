<%-- 
    Document   : svg
    Created on : May 10, 2019, 11:59:49 PM
    Author     : caspe
--%>

<%@page import="entity.Carport"%>
<%@page import="entity.Order"%>
<%@page import="data.DevMapper"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    Order order = (Order) request.getAttribute("order");
    //Order order = new Order(270, 600, 820, 300, 300, 10, 12);
    //Carport carport = new Carport(new logic.LogicFacade().buildCarport(order));
    int carportWidth = order.getWidth();
    int carportLength = order.getLenght();

    int canvasWidth = 800;
    int canvasHeight = 1000;

    //int spaer = carport.getCountCategory(8); // spær
    int spaer = 15; // spær
%>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Carport SVG</title>
    </head>
    <body>
        <svg width="<%= canvasWidth%>" height="<%= canvasHeight%>">
        <text x="0" y="0" fill="red">Test</text>
        <!-- Grunden -->
        <rect x="0" y="100" width="<%= carportLength%>" height="<%= carportWidth%>" style="fill:white;stroke-width:0;stroke:rgb(0,0,0)" />
        <!-- Spær -->

        <%
            int step = carportLength / spaer;
            int x = 10;
            for (int i = 0; i < spaer; i++) {
        %>
        <line x1="<%= x-10 %>" y1="70" x2="<%= x-10 %>" y2="90" style="fill:white;stroke-width:1;stroke:rgb(0,0,0)" />
        <line x1="<%= x-10 %>" y1="80" x2="<%= x+25 %>" y2="80" style="fill:white;stroke-width:1;stroke:rgb(0,0,0)" />
        <line x1="<%= x+25 %>" y1="70" x2="<%= x+25 %>" y2="90" style="fill:white;stroke-width:1;stroke:rgb(0,0,0)" />
        <text x="<%= x%>" y="70"><%= step%></text>
        
        <rect x="<%= x %>" y="100" width="10" height="600" style="fill:white;stroke-width:1;stroke:rgb(0,0,0)" />
        <% x += step; %>

        <% }%>
        <!-- Stolper Carport -->
        <rect x="<%= canvasWidth - order.getShedLength()%>" y="120" width="20" height="20" style="fill:white;stroke-width:1;stroke:rgb(0,0,0)" />
        <rect x="<%= canvasWidth - 60 %>" y="120" width="20" height="20" style="fill:white;stroke-width:1;stroke:rgb(0,0,0)" />
        <rect x="<%= canvasWidth - order.getShedLength()%>" y="380" width="20" height="20" style="fill:white;stroke-width:1;stroke:rgb(0,0,0)" />
        <rect x="<%= canvasWidth - 60 %>" y="380" width="20" height="20" style="fill:white;stroke-width:1;stroke:rgb(0,0,0)" />
        <rect x="<%= canvasWidth - order.getShedLength()%>" y="660" width="20" height="20" style="fill:white;stroke-width:1;stroke:rgb(0,0,0)" />
        <rect x="<%= canvasWidth - 60 %>" y="660" width="20" height="20" style="fill:white;stroke-width:1;stroke:rgb(0,0,0)" />

        <!-- Remme -->
        <rect x="0" y="125" width="<%= carportLength%>" height="10" style="fill:white;stroke-width:1;stroke:rgb(0,0,0)" />
        <rect x="0" y="665" width="<%= carportLength%>" height="10" style="fill:white;stroke-width:1;stroke:rgb(0,0,0)" />
        
        <!-- Text -->
        <text x="<%= canvasWidth / 2 - 120 %>" y="<%= canvasHeight - 240 %>" fill="black"><%= carportLength%></text>
        <text x="<%= canvasWidth - 30 %>" y="<%= canvasHeight / 2 - 120 %>" fill="black"><%= carportWidth %></text>
        </svg>

        <br>
        <!-- SIDEVIEW -->
        <svg width="<%= canvasWidth%>" height="<%= canvasHeight/3%>">
        <!-- Stolper -->
        <rect x="60" y="0" width="15" height="200" style="fill:white;stroke-width:1;stroke:rgb(0,0,0)" />
        <rect x="350" y="5" width="15" height="195" style="fill:white;stroke-width:1;stroke:rgb(0,0,0)" />
        <!-- Top -->
        <rect x="0" y="0" width="<%= carportLength%>" height="15" transform="rotate(0.4)" style="fill:white;stroke-width:1;stroke:rgb(0,0,0)" />
        <rect x="0" y="15" width="<%= carportLength%>" height="15" transform="rotate(0.4)" style="fill:white;stroke-width:1;stroke:rgb(0,0,0)"/>
        <!-- Skur -->
        <rect x="<%= canvasWidth - order.getShedLength() - 80 %>" y="20" width="<%= order.getShedLength() %>" height="185" style="fill:white;stroke-width:1;stroke:rgb(0,0,0)" />
        <%
            step = order.getShedLength() / 50;
            x = canvasWidth - order.getShedLength() - 80;
            for (int i = 0; i < 50; i++) {
        %>
        <line x1="<%= x%>" y1="20" x2="<%= x%>" y2="205" style="fill:white;stroke-width:1;stroke:rgb(0,0,0)" />
        <%
            x += step;
            }
        %>
        
        </svg>
    </body>
</html>
