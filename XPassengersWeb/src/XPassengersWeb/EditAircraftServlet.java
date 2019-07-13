package XPassengersWeb;

import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditAircraftServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2128987932996582489L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		XPassengersUtils utils = new XPassengersUtils();
		databaseAccess dao = new databaseAccess();		
		int activeAirline = utils.getActiveAirline();
		String button = request.getParameter("save");
		int airplaneID = Integer.valueOf(button.replace("save", ""));
		int eco = Integer.valueOf(request.getParameter("valueEco"));
		int first = Integer.valueOf(request.getParameter("sliderFirst"));
		int business = Integer.valueOf(request.getParameter("sliderBusiness"));
		int fuel = Integer.valueOf(request.getParameter("sliderRefuel"));
		try {
			double curFuel = dao.getAirlinesAirplanesPlaneData(activeAirline, airplaneID).getDouble("fuelquantity");
			double fuelDif = fuel - curFuel; 
			dao.updatePlane(activeAirline, airplaneID, eco, business, first);
			String fuelType = request.getParameter("fuelType");
			dao.storeFuel(fuelDif, fuelType);
			dao.updatePlaneFuel(airplaneID, fuel);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		utils.redirect(request, response);
   }
}
