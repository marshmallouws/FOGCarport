<%-- 
    Document   : index
    Created on : Apr 25, 2019, 1:59:39 PM
    Author     : caspe
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Index</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        <a href="FrontController?command=orderpage">Frontend</a>
        <a href="FrontController?command=backendpage">Backend</a>
        
        <form method="POST" action="FrontController?command=login">
            <input type="text" name="username" id="username" placeholder="Brugernavn">
            <input type="password" name="password" id="password" placeholder="Password">
            <button type="submit">Login</button>
        </form>
        
    </body>
</html>