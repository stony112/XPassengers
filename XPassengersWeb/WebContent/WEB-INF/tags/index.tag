<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@ tag import="java.sql.*" %>
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
<div class="xp_airlineInfo xp_info">
	<span>Active Airline: <%= airlineName %></span>
	<span>Balance: <%= price %> €</span>
</div>
<div class="xp_pilotInfo xp_info">
	<span><%= pilotName %></span>
</div>