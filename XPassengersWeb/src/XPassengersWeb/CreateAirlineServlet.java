package XPassengersWeb;

import javax.servlet.http.HttpServlet;

import java.sql.SQLException;

import javax.servlet.http.*;

public class CreateAirlineServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5353385910285148549L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		DatabaseAccess dao = new DatabaseAccess();
		XPassengersUtils utils = new XPassengersUtils();
		String name = request.getParameter("airlinename");
		String homebase = request.getParameter("homebase");
		String iata = request.getParameter("iata");
		double balance = 500000;
		HashMap<String, Object> airlines = new HashMap<String, Object>();
		airlines.put("name", name);
		airlines.put("homebase", homebase);
		airlines.put("iata", iata);
		airlines.put("balance", balance);
		try {
			dao.insert("airlines", airlines);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		utils.redirect(request, response);
   }
}
