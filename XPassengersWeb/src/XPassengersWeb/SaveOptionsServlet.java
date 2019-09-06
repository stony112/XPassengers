package XPassengersWeb;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SaveOptionsServlet extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7249639756136535366L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		XPassengersUtils utils = new XPassengersUtils();
		String path = request.getParameter("xp_options");
		utils.setIni();
		utils.ini.put("options","xplanePath",path);
		try {
			utils.ini.store();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			utils.redirect(request, response);
		}
	}
}
