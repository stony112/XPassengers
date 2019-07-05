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
	double relEcoFirst = 2.5;
	double relEcoBusni = 1.3;
	double relBusniFirst = 2.1;
%>

<script>
window.onload = function () {
	var relEcoFirst = <%= relEcoFirst %>,
	, relEcoBusni =  <%= relEcoBusni %>,
	, relBusniFirst =  <%= relBusniFirst %>
	, seats = <%= seats %>
	
	var sliderFirst = document.getElementById("xp_sliderFirst");
	var sliderBusiness = document.getElementById("xp_sliderBusiness");
	var sliderEconomy = document.getElementById("xp_sliderEconomy");
	var outputFirst = document.getElementById("xp_sliderFirstValue");
	var outputBusiness = document.getElementById("xp_sliderBusinessValue");
	var outputEconomy = document.getElementById("xp_sliderEconomyValue");
	
	if (seats < 15) {
		
	} else if (seats < 130) {
		
	} else {
		
	}
	
	outputFirst.innerHTML = sliderFirst.value;
	sliderFirst.oninput = function() {
		outputFirst.innerHTML = this.value;
	}		
	
	outputBusiness.innerHTML = sliderBusiness.value;
	sliderBusiness.oninput = function() {
		outputBusiness.innerHTML = this.value;
	}		
	
	outputEconomy.innerHTML = sliderEconomy.value;
	sliderEconomy.oninput = function() {
		outputEconomy.innerHTML = this.value;
	}
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
			<input name="sliderEconomy"type="range" min="0" max="<%= seats %>" value="<%= aircraft.getInt("economy") %>" class="xp_slider" id="xp_sliderEconomy"/>
			<span id="xp_sliderEconomyValue" class="xp_sliderValue"></span>
		</div>
	</div>
</form>