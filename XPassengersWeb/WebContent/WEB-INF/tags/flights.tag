<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@ tag import="java.sql.*" %>
<%@ tag import="java.util.Date" %>
<%@ tag import="java.util.HashMap" %>
<jsp:useBean id="utils" class="XPassengersWeb.XPassengersUtils"/>
<jsp:useBean id="dao" class="XPassengersWeb.databaseAccess"/>

<a class="xp_createNew" href="newFlight.jsp">Plan new Flight</a>
<%
HashMap<String,Object> wheres = new HashMap<String,Object>();
wheres.put("airlineid", utils.getActiveAirline());
ResultSet flights = dao.select("flights", "*", wheres);
if (flights != null) {
	while (flights.next()) {
		%> 
		<div class="xp_flightSnippet">
			<span><%= flights.getInt("id") %></span>
			<span><%= flights.getString("fromICAO") %></span>
			<span><%= flights.getString("toICAO") %></span>
			<span><%= flights.getInt("points") %></span>
			<span><%= flights.getDouble("gain") %></span>
		</div>
		<%
	}
}
%>