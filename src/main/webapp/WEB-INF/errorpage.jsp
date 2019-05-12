<%-- 
    Document   : errorpage
    Created on : May 8, 2019, 8:15:39 PM
    Author     : caspe
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Fejlside</title>
    </head>
    <body>
        <h1>Hov! Noget gik galt..</h1>
        <p><%= request.getAttribute("error") %></p>
    </body>
</html>
