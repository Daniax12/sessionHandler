
<%-- 
    Document   : login
    Created on : 13 oct. 2023, 10:34:16
    Author     : aram
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> Principal SERVER </title>
    </head>
    <body>
        <h1>Hello World From Principal server</h1>
        <form action="SessionHandlerServlet" method="POST">
            <input type="text" name="name" placeholder="name">
            <input type="submit" value="Send">
        </form>
    </body>
</html>