package XPassengersWeb;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EndFlightServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		databaseAccess dao = new databaseAccess();
		XPassengersUtils utils = new XPassengersUtils();
		int id = Integer.parseInt(request.getParameter("id"));
		float flighthours = Float.parseFloat(request.getParameter("flighthours"));
		int points = Integer.parseInt(request.getParameter("points"));
		float distance = Float.parseFloat(request.getParameter("distance"));
		System.out.println(id);
		System.out.println(flighthours);
		System.out.println(points);
		System.out.println(distance);
		dao.endFlight(id,flighthours,points,distance);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
	}
}
