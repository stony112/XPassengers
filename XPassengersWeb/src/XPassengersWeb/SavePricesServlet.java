package XPassengersWeb;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SavePricesServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8373675648464245720L;

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		XPassengersUtils utils = new XPassengersUtils();
		databaseAccess dao = new databaseAccess();
		double first = Double.parseDouble(request.getParameter("priceFirst"));
		double business = Double.parseDouble(request.getParameter("priceBusiness"));
		double economy = Double.parseDouble(request.getParameter("priceEconomy"));
		double cargo = Double.parseDouble(request.getParameter("priceCargo"));
		double free = Double.parseDouble(request.getParameter("freeLuggage"));
		
		dao.setPrices(utils.getActiveAirline(), first, business, economy, cargo, free);
		utils.redirect(request, response);
   }
}
