<%@tag description="Overall Page template" pageEncoding="UTF-8"%>
<%@ tag import="java.sql.*" %>
<%@ tag import="java.util.Date" %>
<jsp:useBean id="utils" class="XPassengersWeb.XPassengersUtils"/>
<jsp:useBean id="dao" class="XPassengersWeb.databaseAccess"/>
<%
	ResultSet airline = dao.getSingleContent("*", "airlines", utils.getActiveAirline());
	int id = Integer.valueOf(request.getParameter("id"));
	ResultSet aircraft = dao.getSingleContent("*", "airlines_airplanes", id);
	ResultSet aircraftDetail = dao.getSingleContent("*", "airplanes", aircraft.getInt("airplaneid"));
	int seats = aircraftDetail.getInt("seats");
	int curBus = aircraft.getInt("business");
	int curFir = aircraft.getInt("first");
	int curEco = aircraft.getInt("economy");
	double relEcoFirst = utils.relEcoFirst;
	double relEcoBusni = utils.relEcoBusni;
	double relBusniFirst = utils.relBusniFirst;
	int fuelquantity = aircraft.getInt("fuelquantity");
	String fuelType = aircraftDetail.getString("fuelType");
	int fuelWeight = aircraftDetail.getInt("fuel");
	double fuelAvailable = 0;
	if (fuelType.equals(utils.avgas)) {
		fuelAvailable = airline.getDouble("availableFuelAvGas");
	} else if (fuelType.equals(utils.jetA1)) {
		fuelAvailable = airline.getDouble("availableFuelJetA1");
	}
%>

<script>
window.onload = function () {
	var sliderFirst = document.getElementById("xp_sliderFirst");
	var sliderBusiness = document.getElementById("xp_sliderBusiness");
	var sliderEconomy = document.getElementById("xp_sliderEconomy");
	var sliderRefuel = document.getElementById("xp_sliderRefuel");
	var outputFirst = document.getElementById("xp_sliderFirstValue");
	var outputBusiness = document.getElementById("xp_sliderBusinessValue");
	var outputEconomy = document.getElementById("xp_sliderEconomyValue");
	var outputRefuel = document.getElementById("xp_sliderRefuelValue");
	var fuelInTank = document.getElementById("fuelInTank");
	var valueEco = document.getElementById("valueEco");
	var curFuelInTank = <%= fuelAvailable %>;
	var curFuel = <%= fuelquantity %>;
	var save = document.getElementById("saveAircraft");
	getSeatConfig (sliderBusiness.value, sliderFirst.value);
	
	outputFirst.innerHTML = sliderFirst.value;
	sliderFirst.oninput = function() {
		outputFirst.innerHTML = this.value;
		outputEconomy.innerHTML = getSeatConfig(sliderBusiness, sliderFirst, "first");
		valueEco.value = getSeatConfig(sliderBusiness, sliderFirst, "first");
	}		
	
	outputBusiness.innerHTML = sliderBusiness.value;
	sliderBusiness.oninput = function() {
		outputBusiness.innerHTML = this.value;
		outputEconomy.innerHTML = getSeatConfig(sliderBusiness, sliderFirst, "business");
		valueEco.value = getSeatConfig(sliderBusiness, sliderFirst, "first");
	}
	
	outputRefuel.innerHTML = sliderRefuel.value;
	sliderRefuel.oninput = function() {
		var refuel = this.value - curFuel;
		var newFuelInTank = curFuelInTank - refuel;
		if (newFuelInTank < 0) {
			save.disabled = true;
		} else {
			if (save.disabled == true) {
				save.disabled = false;
			}
			fuelInTank.innerHTML = newFuelInTank;
			outputRefuel.innerHTML = this.value;
		}
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
	saveAircraft.value = "save"+<%= aircraftDetail.getInt("id") %>;
	
	return eco;	
}

function buyFuel() {
	console.log("XXX");
	modal = document.getElementById("buyFuelModal");
	modal.style.display = "block";
}
</script>
<a href="/aircrafts/aircrafts.jsp">Back</a>
<form method="post" action="/EditAircraftServlet">
	<h3><%= aircraftDetail.getString("name") %></h3>
	<div class="xp_seatconfig xp_detailWrapper">
		<h4>Seat-Config</h4>
		<% if (seats >= 130) {%>
			<div class="xp_sliderWrapper">
				<label for="sliderFirst" class="xp_sliderLabel">First</label>
				<input name="sliderFirst" value="<%= curFir %>" type="range" min="0" max="<%= (int) (seats/relEcoFirst) %>" value="<%= aircraft.getInt("first") %>" class="xp_slider" id="xp_sliderFirst"/>
				<span id="xp_sliderFirstValue" class="xp_sliderValue"></span>
			</div>
		<% } %>
		<% if (seats >= 15) { %>
			<div class="xp_sliderWrapper">
				<label for="sliderBusiness" class="xp_sliderLabel">Business</label>
				<input name="sliderBusiness" value="<%= curBus %>" type="range" min="0" max="<%= (int) (seats/relEcoBusni) %>" value="<%= aircraft.getInt("business") %>" class="xp_slider" id="xp_sliderBusiness"/>
				<span id="xp_sliderBusinessValue" class="xp_sliderValue"></span>
			</div>
		<% } %>
		<div class="xp_sliderWrapper">
			<label for="sliderEconomy" class="xp_sliderLabel">Economy</label>
			<span id="xp_sliderEconomyValue" class="xp_sliderValue"><%= curEco %></span>
			<input name="valueEco" id="valueEco" type="hidden" value="<%= curEco %>"/>
		</div>
	</div>
	<div class="xp_fuel xp_detailWrapper">
		<h4>Fuel</h4>
		<div class="xp_fuelDetails">
			<div class="xp_fuelDetail">
				<span>Current Fuel: </span><span><%= fuelquantity %></span><span> <%= fuelType %></span>
			</div>
			<div class="xp_fuelDetail">
				<span>Fuel in Storage: </span><span id="fuelInTank"><%= fuelAvailable %></span><span> <%= fuelType %></span>
			</div>
			<div class="xp_sliderWrapper">
				<label for="sliderRefuel" class="xp_sliderLabel">Refuel</label>
				<input name="sliderRefuel" type="range" min="0" max="<%= fuelWeight %>" value="<%= fuelquantity %>" class="xp_slider" id="xp_sliderRefuel"/>
				<span id="xp_sliderRefuelValue" class="xp_sliderValue"></span><span>/<%= fuelWeight %></span>
			</div>
			<input type="button" id="buyFuel" name="buyFuel" onclick="buyFuel()" value="Buy Fuel" />
			<input type="hidden" name="fuelType" value="<%= fuelType %>" />
		</div>
	</div>
	<button type="submit" id="saveAircraft" name="save" value="save">Save</button>
</form>
<div id="buyFuelModal" class="xp_modal" style="display: none">
	<h4>Buy Fuel</h4>
	<div>
		<label for="fuelType">Fuel Type: </label>
		<select name="fuelType" name="fuelType">
			<option value="jetA1">JetA1</option>
			<option value="AvGas">AvGas</option>
		</select>
	</div>
	<div>
		<label for="fuelAmmount">Fuel Ammount: </label>
		<input type="number" name="fuelAmmount"/>
	</div>
</div>
<script>
function buyFuel() {
	console.log("XXX");
	modal = document.getElementById("buyFuelModal");
	modal.style.display = "block";
}
</script>