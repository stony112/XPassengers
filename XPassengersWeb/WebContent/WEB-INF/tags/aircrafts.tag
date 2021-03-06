<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@ tag import="java.sql.*" %>
<%@ tag import="java.util.Date" %>
<%@ tag import="java.util.HashMap" %>
<jsp:useBean id="utils" class="XPassengersWeb.XPassengersUtils"/>
<jsp:useBean id="dao" class="XPassengersWeb.DatabaseAccess"/>
<div>
	<form method="post" action="/CheckNewPlanes">
		<button type="submit">Check New Planes</button>
	</form>
</div>
<div>
	<a class="xp_createNew" href="buyAircraft.jsp">Buy new Aircraft</a>
	<form method="post" action="/SellAircraftServlet">
	<%
		HashMap<String, Object> airplaneWheres = new HashMap<String, Object>();
		airplaneWheres.put("airlineid", utils.getActiveAirline());
		ResultSet boughtAirplanes = dao.select("airlines_airplanes", "*", airplaneWheres);
		if (boughtAirplanes == null) {
			utils.checkNewPlanes();
		}
		while (boughtAirplanes.next()) {
			double price;
			int airplaneID = boughtAirplanes.getInt("airplaneid"); 
			ResultSet airplanes = dao.getSingleContent("*", "airplanes", airplaneID);
			int quality = boughtAirplanes.getInt("quality");
			int flighthours = boughtAirplanes.getInt("flighthours");
			
			String curPos = ((boughtAirplanes.getString("lastposition") == null) ? "" : boughtAirplanes.getString("lastposition"));
			Timestamp lf  = boughtAirplanes.getTimestamp("lastflight");
			String lastflight = "";
			if (lf != null) {
				lastflight = new java.util.Date(lf.getTime()).toGMTString().replace(" GMT", "");
			}
			price = utils.getSellingPrice(quality, flighthours, airplanes.getFloat("price"));
			double fuelquantity = boughtAirplanes.getDouble("fuelquantity");
			double fuelweight = airplanes.getDouble("fuel");
			double pec = fuelquantity/fuelweight*100;
			
			%> 
			<div class="xp_aircraftSnippet">
				<div class="xp_aircraftInfoWrapper">
					<a href="aircraft.jsp?id=<%= boughtAirplanes.getInt("id") %>" class="xp_aircraftLink">
						<h4><%= airplanes.getString("name") %></h4>
						<div class="xp_aircraftInfo">
							<div>
								<span class="xp_aircraftInfoName">Empty-Weight:</span><span class="xp_aircraftInfoValue"><%= airplanes.getDouble("emptyweight") %></span>
							</div>
							<div>
								<span class="xp_aircraftInfoName">Take-Off-Weight:</span><span class="xp_aircraftInfoValue"><%= airplanes.getDouble("toweight") %></span>
							</div>
							<div>
								<span class="xp_aircraftInfoName">Fuel-Weight:</span><span class="xp_aircraftInfoValue"><%= fuelweight %></span>
							</div>
							<div>
								<span class="xp_aircraftInfoName">Fuel-Quantity:</span><span class="xp_aircraftInfoValue"><%= fuelquantity %> (<%= Math.ceil(pec) %> %)</span>
							</div>
							<div class="xp_seatconfig">
								<div>
									<span class="xp_aircraftInfoName">Max-Seats:</span><span class="xp_aircraftInfoValue"><%= airplanes.getInt("seats") %></span>
								</div>
								<div>
									<span class="xp_aircraftInfoName">First:</span><span class="xp_aircraftInfoValue"><%= boughtAirplanes.getInt("first") %></span>
								</div>
								<div>
									<span class="xp_aircraftInfoName">Business:</span><span class="xp_aircraftInfoValue"><%= boughtAirplanes.getInt("business")  %></span>
								</div>
								<div>
									<span class="xp_aircraftInfoName">Economy:</span><span class="xp_aircraftInfoValue"><%= boughtAirplanes.getInt("economy")  %></span>
								</div>
							</div>
							<div>
								<span class="xp_aircraftInfoName">Quality:</span><span class="xp_aircraftInfoValue"><%= quality %></span>
							</div>
							<div>
								<span class="xp_aircraftInfoName">Flighthours:</span><span class="xp_aircraftInfoValue"><%= flighthours %></span>
							</div>
							<div>
								<span class="xp_aircraftInfoName">Last Flight:</span><span class="xp_aircraftInfoValue"><%= lastflight %></span>
							</div>
							<div>
								<span class="xp_aircraftInfoName">Current Position:</span><span class="xp_aircraftInfoValue"><%= curPos %></span>
							</div>
							<div>
								<span class="xp_aircraftInfoName">Sell-Price:</span><span class="xp_aircraftInfoValue"><%= utils.doubleToPrice(price) %> €</span>
							</div>
						</div>
					</a>
				</div>
				<button type="submit" name="sell" value="sell<%= airplanes.getInt("id") + "-" + price %>">sell</button>
			</div>
			<%
		}
	%>
	</form>
</div>