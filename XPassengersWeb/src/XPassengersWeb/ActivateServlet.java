package XPassengersWeb;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ActivateServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5206832970304118504L;

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
