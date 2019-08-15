<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@ tag import="java.sql.*" %>
<jsp:useBean id="utils" class="XPassengersWeb.XPassengersUtils"/>
<jsp:useBean id="dao" class="XPassengersWeb.databaseAccess"/>
<%
	int activeAirline = utils.getActiveAirline();
	ResultSet prices = dao.getSingleContent("*","airlines", activeAirline);
	double priceFirst = prices.getDouble("priceFirst");
	double priceBusiness = prices.getDouble("priceBusiness");
	double priceEconomy = prices.getDouble("priceEconomy");
	double priceCargo = prices.getDouble("priceCargo");
	double freeLuggage = prices.getDouble("freeLuggage");
%>
<form method="post" action="/SavePricesServlet">
<div class="xp_options">
	<div class="xp_option">
		<label for="priceFirst" class="xp_optionsLabel">Price First-Class-Ticket: </label>
		<input type="number" name="priceFirst" class="xp_optionsInput" value="<%= priceFirst %>"/>
	</div>
	<div class="xp_option">
		<label for="priceBusiness" class="xp_optionsLabel">Price Business-Class-Ticket: </label>
		<input type="number" name="priceBusiness" class="xp_optionsInput" value="<%= priceBusiness %>"/>
	</div>
	<div class="xp_option">
		<label for="priceEconomy" class="xp_optionsLabel">Price Economy-Class-Ticket: </label>
		<input type="number" name="priceEconomy" class="xp_optionsInput" value="<%= priceEconomy %>"/>
	</div>
	<div class="xp_option">
		<label for="priceCargo" class="xp_optionsLabel">Price/lbs Cargo: </label>
		<input type="number" name="priceCargo" class="xp_optionsInput" value="<%= priceCargo %>"/>
	</div>
	<div class="xp_option">
		<label for="freeLuggage" class="xp_optionsLabel">Free Luggage (lbs): </label>
		<input type="number" name="freeLuggage" class="xp_optionsInput" value="<%= freeLuggage %>"/>
	</div>
	<input type="submit"/>
</div>
</form>