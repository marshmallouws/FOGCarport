<%-- 
    Document   : landingpage
    Created on : 24-Apr-2019, 13:16:57
    Author     : Casper
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Ordren er oprettet og modtaget!</h1>
        <p>Vi kontakter dig hurtigst muligt med en pris på din nye carport</p>
        
        <%= request.getAttribute("succes") %>
    </body>
</html>
