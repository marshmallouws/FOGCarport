<%-- 
    Document   : orderpage
    Created on : Apr 23, 2019, 10:52:48 AM
    Author     : caspe
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
        
        <form>
            <select name="carport-width">
                <option value="240">240</option>
                <option value="750">750</option>
            </select>
            
            <select name="carport-length">
                <option value="240">240</option>
                <option value="780">780</option>
            </select>
            
            <select name="roof">
                <option value="Plasttrapezplader">Plasttrapezplader</option>
            </select>
            
            <select name="shed-width">
                <option value="210">210</option>
                <option value="720">720</option>
            </select>
            
            <select name="shed-length">
                <option value="150">150</option>
                <option value="690">690</option>
            </select>
            
            <input type="text" name="fullname" placeholder="Navn">
            <input type="text" name="address" placeholder="Adresse">
            <input type="text" name="zipCity" placeholder="Postnummer og by">
            <input type="text" name="phone" placeholder="Telefon">
            <input type="text" name="email" placeholder="Email">
            <textarea name="message"></textarea>
            
        </form>
        
    </body>
</html>
