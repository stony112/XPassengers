package XPassengersWeb;

import javax.servlet.http.HttpServlet;

import java.sql.SQLException;
import java.util.Enumeration;

import javax.servlet.http.*;

public class createAirlineServlet extends HttpServlet{
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		databaseAccess dao = new databaseAccess();
		XPassengersUtils utils = new XPassengersUtils();
		String name = request.getParameter("airlinename");
		String homebase = request.getParameter("homebase");
		String iata = request.getParameter("iata");
		
		try {
			dao.createAirline(name, homebase, iata);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		utils.redirect(request, response);
   }
}
