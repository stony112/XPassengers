package XPassengersWeb;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BuyFuelServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		XPassengersUtils utils = new XPassengersUtils();
		databaseAccess dao = new databaseAccess();
		java.sql.Date date = utils.getSQLTodaysDate();
		String fuelType = request.getParameter("fuelType");
		double fuelAmmount = Double.parseDouble(request.getParameter("fuelAmmount"));
		int activeAirline = utils.getActiveAirline();
		double curFuel = 0;
		try {
			ResultSet airline = dao.getSingleContent("*", "airlines", activeAirline);
			double balance = airline.getDouble("balance");
			float fuelPrice = dao.getLastFuelprice(fuelType,date);;
			if (fuelType.equals(utils.jetA1)) {
				curFuel = airline.getDouble("availableFuelJetA1");
			} else {
				curFuel = airline.getDouble("availableFuelAvGas");
			}
			double fuelCosts = fuelPrice * fuelAmmount;
			double newBalance = balance - fuelCosts;
			double newFuel = curFuel + fuelAmmount;
			dao.updateBalance(newBalance);
			dao.buyFuel(fuelType, newFuel);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		utils.redirect(request, response);
   }
}
