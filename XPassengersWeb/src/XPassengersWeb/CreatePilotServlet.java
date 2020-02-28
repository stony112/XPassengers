package XPassengersWeb;

import javax.servlet.http.HttpServlet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import javax.servlet.http.*;

public class CreatePilotServlet extends HttpServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -978547270865542264L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	@Override
   public void doPost(HttpServletRequest request, HttpServletResponse response) {
		DatabaseAccess dao = new DatabaseAccess();
		XPassengersUtils utils = new XPassengersUtils();
		String firstname = request.getParameter("pilotfirstname");
		String lastname = request.getParameter("pilotlastname");
		String birthday = request.getParameter("pilotbirthday");
		String airline = request.getParameter("pilotairline");
		
		SimpleDateFormat formatBirthday = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(formatBirthday.parse(birthday));
			//dao.createPilot(firstname, lastname, utils.getSQLDate(c.getTime()), Integer.parseInt(airline));
			HashMap<String, Object> insertMap = new HashMap<String, Object>();
			insertMap.put("firstname", firstname);
			insertMap.put("lastname", lastname);
			insertMap.put("birthday", utils.getSQLDate(c.getTime()));
			insertMap.put("airlineID", Integer.parseInt(airline));
			insertMap.put("licenseID", 1);
			insertMap.put("flighthours", 0);
			insertMap.put("points", 0);
			dao.insert("pilots", insertMap);
		} catch (ParseException | NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		utils.redirect(request, response);	
		
   }
}
