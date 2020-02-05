package XPassengersWeb;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EndFlightServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6617240182175724472L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		databaseAccess dao = new databaseAccess();
		XPassengersUtils utils = new XPassengersUtils();
		int activeAirline = utils.getActiveAirline();
		int id = Integer.parseInt(request.getParameter("id"));
		float flighthours = Float.parseFloat(request.getParameter("flighthours"));
		int points = Integer.parseInt(request.getParameter("points"));
		float distance = Float.parseFloat(request.getParameter("distance"));
		double fuelquantity = Double.parseDouble(request.getParameter("curFuel"));
		String destination = request.getParameter("destination");
		int drinksServed = Integer.parseInt(request.getParameter("drinks"));
		int coldFoodServed = Integer.parseInt(request.getParameter("coldfood"));
		int hotFoodServed = Integer.parseInt(request.getParameter("hotfood"));
		int satisfaction = Integer.parseInt(request.getParameter("satisfaction"));
		String emergency = request.getParameter("emergency");
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
			double priceDrink = airlineDetails.getDouble("priceDrink");
			double priceHotFood = airlineDetails.getDouble("priceHotFood");
			double priceColdFood = airlineDetails.getDouble("priceColdFood");
			int pilotID = flightDetails.getInt("pilotID");
			int planeID = flightDetails.getInt("airplaneid");
			double valueableDistance = distance/1000/100;
			gain = ((valueableCargo * priceCargo * valueableDistance) + (valueableDistance * priceFirst * first) + (valueableDistance * priceBusiness * business) + (valueableDistance * priceEconomy * economy) + (drinksServed * priceDrink) + (coldFoodServed * priceColdFood) + (hotFoodServed * priceHotFood));
			double balance = dao.getSingleContent("balance", "airlines", activeAirline).getDouble("balance");
			flighthours = flighthours/60/60;
			ResultSet curPilot = dao.getSingleContent("*", "pilots", pilotID);
			int curPoints = curPilot.getInt("points");
			float curFlighthours = curPilot.getFloat("flighthours");
			int newPoints = curPoints + points;
			float newFlighthours = curFlighthours + flighthours;
			HashMap<String, Object> updateMap = new HashMap<String, Object>();
			updateMap.put("points", newPoints);
			updateMap.put("flighthours", newFlighthours);
			int newLicense = dao.checkLicense(curPilot.getInt("licenseID"),newPoints,newFlighthours);
			if (newLicense != -1) {
				updateMap.put("licenseID", newLicense);
			}
			dao.update("pilots", updateMap, pilotID);
			updateMap.clear();
			ResultSet curAirplane = dao.getAirlinesAirplanesPlaneData(airlineID, planeID);
			int planeHours = curAirplane.getInt("flighthours");
			planeHours = (int) (planeHours + flighthours);
			updateMap.put("flighthours", planeHours);
			updateMap.put("lastflight", utils.getSQLTodaysDate());
			updateMap.put("lastposition", destination);
			updateMap.put("fuelquantity", fuelquantity*utils.toLbs);
			HashMap<String, Object> wheres = new HashMap<String, Object>();
			wheres.put("airlineid", activeAirline);
			wheres.put("airplaneid", planeID);
			dao.update("airlines_airplanes", updateMap, wheres);
			dao.endFlight(id,flighthours,points,distance,gain,satisfaction);
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
