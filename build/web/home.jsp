<%@page import="java.util.HashMap"%>
<%@ page import="utils.MySessionHandler" %>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
    HashMap<String, Object> mySessionHandler = (HashMap<String, Object>) request.getAttribute("session_handler");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Dania Server</title>
    </head>
    <body>
        <h1>Hello <%= mySessionHandler.get("name") %> </h1>
        <a href="DeconnexionServlet">
          Deconnect  
        </a>
    </body>
</html>