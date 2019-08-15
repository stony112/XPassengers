<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@ tag import="java.sql.*" %>
<%@ tag import="java.util.Date" %>
<jsp:useBean id="utils" class="XPassengersWeb.XPassengersUtils"/>
<jsp:useBean id="dao" class="XPassengersWeb.databaseAccess"/>

<a class="xp_createNew" href="newFlight.jsp">Plan new Flight</a>
<%
ResultSet flights = dao.getResults("flights");
while (flights.next()) {
	%> 
	<div class="xp_flightSnippet">
		<span><%= flights.getInt("id") %></span>
		<span><%= flights.getString("from") %></span>
		<span><%= flights.getString("to") %></span>
		<span><%= flights.getInt("points") %></span>
		<span><%= flights.getDouble("gain") %></span>
	</div>
	<%
}
%>