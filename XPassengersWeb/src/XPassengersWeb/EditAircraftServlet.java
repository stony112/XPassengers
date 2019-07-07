package XPassengersWeb;

import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EditAircraftServlet  extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		XPassengersUtils utils = new XPassengersUtils();
		databaseAccess dao = new databaseAccess();
		
		String button = request.getParameter("save");
		String idEco = button.replace("save", "");
		int separatorIndex = idEco.indexOf("-");
		int airplaneID = Integer.valueOf(idEco.substring(0,separatorIndex));
		int eco = Integer.valueOf(idEco.substring(separatorIndex+1));
		int first = Integer.valueOf(request.getParameter("sliderFirst"));
		int business = Integer.valueOf(request.getParameter("sliderBusiness"));
		try {
			int activeAirline = utils.getActiveAirline();
			dao.updatePlane(activeAirline, airplaneID, eco, business, first);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		utils.redirect(request, response);
   }
}
