<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@ tag import="java.sql.*" %>
<jsp:useBean id="utils" class="XPassengersWeb.XPassengersUtils"/>
<jsp:useBean id="dao" class="XPassengersWeb.databaseAccess"/>
<% ResultSet aircrafts = dao.getAirlinesAirplanesData("*", utils.getActiveAirline()); %>
<form method="post" action="/createFlightServlet">
	<fieldset>
		From (ICAO) <input type="text" name="from" /><br />
		To (ICAO) <input type="text" name="to"/><br />
		Aircraft <select name="aircraft" size="1">
			<%
				while (aircrafts.next()) {
					int airplaneID = aircrafts.getInt("airplaneid");
					%><option value="<%= airplaneID %>"><%= dao.getSingleContent("*", "airplanes", airplaneID).getString("name") %></option><%
				}
			%>
		</select><br />
		Fuel (kgs) <input type="number" name="fuel" />
	</fieldset>
	<input type="submit" value="Save"/>
</form>