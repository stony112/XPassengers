package XPassengersWeb;

import javax.servlet.http.HttpServlet;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;

import javax.servlet.http.*;

public class createPilotServlet extends HttpServlet{
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	@Override
   public void doPost(HttpServletRequest request, HttpServletResponse response) {
		databaseAccess dao = new databaseAccess();
		XPassengersUtils utils = new XPassengersUtils();
		String firstname = request.getParameter("pilotfirstname");
		String lastname = request.getParameter("pilotlastname");
		String birthday = request.getParameter("pilotbirthday");
		String airline = request.getParameter("pilotairline");
		
		SimpleDateFormat formatBirthday = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c = Calendar.getInstance();
		try {
			c.setTime(formatBirthday.parse(birthday));
			c.add(Calendar.DAY_OF_MONTH,1);
			dao.createPilot(firstname, lastname, utils.getSQLDate(c.getTime()), Integer.parseInt(airline));
		} catch (ParseException | NumberFormatException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		utils.redirect(request, response);	
		
   }
}
