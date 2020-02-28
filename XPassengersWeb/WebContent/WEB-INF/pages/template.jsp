<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:useBean id="utils" class="XPassengersWeb.XPassengersUtils"/>
<jsp:useBean id="dao" class="XPassengersWeb.DatabaseAccess"/>
<!DOCTYPE html>
<html>
	<head>
		<% utils.setIni(); 
		String airlineName = dao.getSingleContent("*", "airlines", utils.getActiveAirline()).getString("name");
		
		%>
		<meta charset="ISO-8859-1">
		<link href="includes/style.css" rel="stylesheet" type="text/css"/>
		<title>XPassengersWeb</title>
	</head>
	<body class="xp">
		<header class="xp_header">
			<h1>XPassengers v2</h1>
			<h2>Active Airline: <%= airlineName %></h2>
		</header>
	</body>
</html>