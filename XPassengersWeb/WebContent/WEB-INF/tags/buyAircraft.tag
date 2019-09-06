<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@ tag import="java.sql.*" %>
<jsp:useBean id="utils" class="XPassengersWeb.XPassengersUtils"/>
<jsp:useBean id="dao" class="XPassengersWeb.databaseAccess"/>

<form method="post" action="/BuyAircraftServlet">
	<%
		ResultSet airplanes = dao.getResults("airplanes");
		while (airplanes.next()) {
			int activeAirline = utils.getActiveAirline();
			double price = airplanes.getDouble("price");
			double balance = dao.getSingleContent("balance", "airlines", activeAirline).getDouble("balance");
	%>
		<div class="xp_aircraftSnippet">
			<div class="xp_aircraftInfoWrapper">
				<h4><%= airplanes.getString("name") %></h4>
				<div class="xp_aircraftInfo">
					<div>
						<span class="xp_aircraftInfoName">Empty-Weight:</span><span class="xp_aircraftInfoValue"><%= airplanes.getDouble("emptyweight") %></span>
					</div>
					<div>
						<span class="xp_aircraftInfoName">Take-Off-Weight:</span><span class="xp_aircraftInfoValue"><%= airplanes.getDouble("toweight") %></span>
					</div>
					<div>
						<span class="xp_aircraftInfoName">Fuel-Weight:</span><span class="xp_aircraftInfoValue"><%= airplanes.getDouble("fuel") %></span>
					</div>
					<div>
						<span class="xp_aircraftInfoName">Seats:</span><span class="xp_aircraftInfoValue"><%= airplanes.getDouble("seats") %></span>
					</div>
					<div>
						<span class="xp_aircraftInfoName">Price:</span><span class="xp_aircraftInfoValue"><%= utils.doubleToPrice(price) %> €</span>
					</div>
				</div>
			</div>
			<% if (balance >= price) {%>
				<button type="submit" name="buy" value="buy<%= airplanes.getInt("id") + "-" + price %>">buy</button>
			<% } else {%>
				<span class="xp_cantafford">not enough money</span>
			<% } %>
		</div>
	<% } %>
</form>