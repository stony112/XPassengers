<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@ tag import="java.sql.*" %>
<%@ tag import="java.util.Date" %>
<jsp:useBean id="utils" class="XPassengersWeb.XPassengersUtils"/>
<jsp:useBean id="dao" class="XPassengersWeb.databaseAccess"/>
<%
	int id = Integer.valueOf(request.getParameter("id"));
	ResultSet aircraft = dao.getSingleContent("*", "airlines_airplanes", id);
	ResultSet aircraftDetail = dao.getSingleContent("*", "airplanes", aircraft.getInt("airplaneid"));
	int seats = aircraftDetail.getInt("seats");
	int curBus = aircraft.getInt("business");
	int curFir = aircraft.getInt("first");
	double relEcoFirst = utils.relEcoFirst;
	double relEcoBusni = utils.relEcoBusni;
	double relBusniFirst = utils.relBusniFirst;
%>

<script>
window.onload = function () {
	var sliderFirst = document.getElementById("xp_sliderFirst");
	var sliderBusiness = document.getElementById("xp_sliderBusiness");
	var sliderEconomy = document.getElementById("xp_sliderEconomy");
	var outputFirst = document.getElementById("xp_sliderFirstValue");
	var outputBusiness = document.getElementById("xp_sliderBusinessValue");
	var outputEconomy = document.getElementById("xp_sliderEconomyValue");
	
	sliderFirst.value = <%= curFir %>;
	sliderBusiness.value = <%= curBus %>;
	getSeatConfig (sliderBusiness.value, sliderFirst.value);
	
	outputFirst.innerHTML = sliderFirst.value;
	sliderFirst.oninput = function() {
		outputFirst.innerHTML = this.value;
		outputEconomy.innerHTML = getSeatConfig(sliderBusiness, sliderFirst, "first");
	}		
	
	outputBusiness.innerHTML = sliderBusiness.value;
	sliderBusiness.oninput = function() {
		outputBusiness.innerHTML = this.value;
		outputEconomy.innerHTML = getSeatConfig(sliderBusiness, sliderFirst, "business");
	}
}

function getSeatConfig(b, f, slider) {
	var relEcoFirst = <%= relEcoFirst %>
	, relEcoBusni =  <%= relEcoBusni %>
	, relBusniFirst =  <%= relBusniFirst %>
	, seats = <%= seats %>
	
	business = b.value*relEcoBusni;
	first = f.value*relEcoFirst;
	
	eco = 0;
	eco = seats - business - first;
	eco = Math.floor(eco);
	if (slider === "first") {
		b.max = Math.floor((business + eco) / relEcoBusni);
	} else if (slider === "business") {
		f.max = Math.floor((first + eco) / relEcoFirst); 
	}
	
	saveAircraft = document.getElementById("saveAircraft");
	saveAircraft.value = "save"+<%= aircraftDetail.getInt("id") %>+"-" + eco;
	
	return eco;	
}
</script>

<form method="post" action="/EditAircraftServlet">
	<h3><%= aircraftDetail.getString("name") %></h3>
	<div class="xp_seatconfig">
		<% if (seats >= 130) {%>
			<div class="xp_sliderWrapper">
				<label for="sliderFirst" class="xp_sliderLabel">First</label>
				<input name="sliderFirst" type="range" min="0" max="<%= (int) (seats/relEcoFirst) %>" value="<%= aircraft.getInt("first") %>" class="xp_slider" id="xp_sliderFirst"/>
				<span id="xp_sliderFirstValue" class="xp_sliderValue"></span>
			</div>
		<% } %>
		<% if (seats >= 15) { %>
			<div class="xp_sliderWrapper">
				<label for="sliderBusiness" class="xp_sliderLabel">Business</label>
				<input name="sliderBusiness" type="range" min="0" max="<%= (int) (seats/relEcoBusni) %>" value="<%= aircraft.getInt("business") %>" class="xp_slider" id="xp_sliderBusiness"/>
				<span id="xp_sliderBusinessValue" class="xp_sliderValue"></span>
			</div>
		<% } %>
		<div class="xp_sliderWrapper">
			<label for="sliderEconomy" class="xp_sliderLabel">Economy</label>
			<span id="xp_sliderEconomyValue" class="xp_sliderValue"></span>
		</div>
	</div>
	<button type="submit" id="saveAircraft" name="save" value="save">Save</button>
</form>