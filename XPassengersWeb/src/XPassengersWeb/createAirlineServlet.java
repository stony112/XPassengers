package XPassengersWeb;

import javax.servlet.http.HttpServlet;

import java.sql.SQLException;

import javax.servlet.http.*;

public class createAirlineServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5353385910285148549L;

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
