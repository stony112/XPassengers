package XPassengersWeb;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EndFlightServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		String flighthours = request.getParameter("flighthours");
		System.out.println(flighthours);
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		String flighthours = request.getParameter("flighthours");
		System.out.println(flighthours);
	}
}
