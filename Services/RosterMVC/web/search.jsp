<%-- 
    Document   : roster
    Created on : Feb 28, 2015, 10:40:48 PM
    Author     : andrew
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <link href="/ThreeRegion/webhome.css" rel="stylesheet" type="text/css">
    <script src="http://code.jquery.com/jquery-1.11.2.min.js"></script>
</head>

<script>
$(document).ready(function(){

});
</script>
<body>
  <h1 class="heading">Search</h1>
  <form action="/RosterMVC/rosterREST" method="GET" >
    Psuid:
    <input type="text" name="psuid">
    <input type="submit" value="SEARCH"/>
  </form>
</body>
</html>

