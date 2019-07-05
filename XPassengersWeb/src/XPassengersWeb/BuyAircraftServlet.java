package XPassengersWeb;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

public class BuyAircraftServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		XPassengersUtils utils = new XPassengersUtils();
		databaseAccess dao = new databaseAccess();
		String button = request.getParameter("buy");
		String idPrice = button.replace("buy", "");
		int separatorIndex = idPrice.indexOf("-");
		int airplaneID = Integer.valueOf(idPrice.substring(0,separatorIndex));
		double price = Double.parseDouble(idPrice.substring(separatorIndex + 1));
		int activeAirline = utils.getActiveAirline();
		Double balance = null;
		try {
			balance = dao.getSingleContent("balance", "airlines", activeAirline).getDouble("balance");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Double newBalance = balance - price;
		dao.buyPlane(airplaneID, newBalance);
		utils.redirect(request, response);
   }
}
