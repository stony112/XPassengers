<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@attribute name="header" fragment="true" %>
<%@attribute name="footer" fragment="true" %>
<%@ tag import="java.sql.*" %>
<%@ tag import="java.util.Date" %>
<jsp:useBean id="utils" class="XPassengersWeb.XPassengersUtils"/>
<jsp:useBean id="dao" class="XPassengersWeb.databaseAccess"/>
<%
	utils.setIni(); 
	int activeAirline = utils.getActiveAirline();
	int activePilot = utils.getActivePilot();
	String airlineName;
	String pilotName;
	double balance = 0;
	String price = "";
	if (activeAirline == 0) {
		airlineName = "No Active Airline";
	} else {	
		
		ResultSet activeAirlineSet = dao.getSingleContent("*", "airlines", activeAirline);
		airlineName = activeAirlineSet.getString("name");
		balance = activeAirlineSet.getDouble("balance");
		price = utils.doubleToPrice(balance);
	}
	if (activePilot == 0) {
		pilotName = "No Active Pilot";
	} else {
		ResultSet activePilotSet = dao.getSingleContent("*", "pilots", activePilot);
		String firstname = activePilotSet.getString("firstname");
		String lastname = activePilotSet.getString("lastname");
		pilotName = firstname + " " + lastname;
	}
%>
<html>
  <head>
		<meta charset="ISO-8859-1">
		<link href="/includes/css/style.css" rel="stylesheet" type="text/css"/>		
		<title>XPassengersWeb</title>
	</head>
	<body class="xp">
		<header class="xp_header">
			<a href="/" class="xp_home">XPassengers v2</a>
			<div class="xp_menu">
				<ul>
					<li class="xp_menuitem"><a href="/airlines/airlines.jsp">Airlines</a></li>
					<li class="xp_menuitem"><a href="/airlines/prices.jsp">Prices</a></li>
					<li class="xp_menuitem"><a href="/pilots/pilots.jsp">Pilots</a></li>
					<li class="xp_menuitem"><a href="/aircrafts/aircrafts.jsp">Aircrafts</a></li>
					<li class="xp_menuitem"><a href="/flights/flights.jsp">Flights</a></li>
					<li class="xp_menuitem"><a href="/options.jsp">Options</a></li>
				</ul>
			</div>
			<div class="xp_header_info">
				<div class="xp_airlineInfo xp_info">
					<span>Active Airline: <%= airlineName %></span>
					<span>Balance: <%= price %> €</span>
				</div>
				<div class="xp_pilotInfo xp_info">
					<span><%= pilotName %></span>
				</div>
			</div>
		</header>
		<div class="xp_body">
			<jsp:doBody/>
		</div>
	</body>
</html>