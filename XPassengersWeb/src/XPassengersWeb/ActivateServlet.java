package XPassengersWeb;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActivateServlet extends HttpServlet {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		XPassengersUtils utils = new XPassengersUtils();
		String button = request.getParameter("activate");
		int airlineID = Integer.valueOf(button.replace("activate", ""));
		String type;
		String referer = request.getHeader("referer");
		type = referer.substring(referer.lastIndexOf("/") + 1);
		type = type.replace(".jsp", "");
		utils.setActive(airlineID,type);
		utils.redirect(request, response);
   }
}
