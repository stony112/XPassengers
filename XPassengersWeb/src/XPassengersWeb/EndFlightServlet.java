package XPassengersWeb;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EndFlightServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		databaseAccess dao = new databaseAccess();
		XPassengersUtils utils = new XPassengersUtils();
		int activeAirline = utils.getActiveAirline();
		int id = Integer.parseInt(request.getParameter("id"));
		float flighthours = Float.parseFloat(request.getParameter("flighthours"));
		int points = Integer.parseInt(request.getParameter("points"));
		float distance = Float.parseFloat(request.getParameter("distance"));
		double gain = 0;
		try {
			ResultSet flightDetails = dao.getSingleContent("*", "flights", id);
			int airlineID = flightDetails.getInt("airlineid");
			ResultSet airlineDetails = dao.getSingleContent("*","airlines", airlineID);
			int valueableCargo = flightDetails.getInt("valueableCargo");
			int economy = flightDetails.getInt("economyclass");
			int business = flightDetails.getInt("businessclass");
			int first = flightDetails.getInt("firstclass");
			double priceFirst = airlineDetails.getDouble("priceFirst");
			double priceBusiness = airlineDetails.getDouble("priceBusiness");
			double priceEconomy = airlineDetails.getDouble("priceEconomy");
			double priceCargo = airlineDetails.getDouble("priceCargo");
			int pilotID = flightDetails.getInt("pilotID");
			ResultSet pilotDetails = dao.getSingleContent("*", "pilots", pilotID);
			int pilotPoints = pilotDetails.getInt("points");
			int pilotFlighthours = pilotDetails.getInt("flighthours");
			pilotPoints = pilotPoints + points;
			pilotFlighthours = (int) (pilotFlighthours + flighthours);
			double valueableDistance = distance/1000/100;
			gain = ((valueableCargo * priceCargo * valueableDistance) + (valueableDistance * priceFirst * first) + (valueableDistance * priceBusiness * business) + (valueableDistance * priceEconomy * economy));
			double balance = dao.getSingleContent("balance", "airlines", activeAirline).getDouble("balance");
			dao.endFlight(id,flighthours,points,distance,gain);
			dao.updateBalance(balance + gain);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
	}
}
