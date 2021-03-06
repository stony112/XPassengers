package XPassengersWeb;

import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SellAircraftServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7461925385781412331L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		XPassengersUtils utils = new XPassengersUtils();
		DatabaseAccess dao = new DatabaseAccess();
		String button = request.getParameter("sell");
		String idPrice = button.replace("sell", "");
		int separatorIndex = idPrice.indexOf("-");
		int airplaneID = Integer.valueOf(idPrice.substring(0,separatorIndex));
		double price = Double.parseDouble(idPrice.substring(separatorIndex + 1));
		int activeAirline = utils.getActiveAirline();
		try {
			double balance = dao.getSingleContent("balance", "airlines", activeAirline).getDouble("balance");
			Double newBalance = balance + price;
			dao.sellPlane(airplaneID, newBalance);	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		utils.redirect(request, response);
   }
}
