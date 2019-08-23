package XPassengersWeb;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JOptionPane;

public class createFlightServlet extends HttpServlet  {
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response) {
		
	}
	
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response) {
		databaseAccess dao = new databaseAccess();
		XPassengersUtils utils = new XPassengersUtils();
		int activeAirline = utils.getActiveAirline();
		String start = request.getParameter("from");
		String dest = request.getParameter("to");
		try {
			int pID = Integer.parseInt(request.getParameter("aircraft")); 
			String airplane = dao.getSingleContent("name", "airplanes", pID).getString("name");
			utils.ini.remove("flight");
			utils.ini.put("flight", "airplane", airplane);
			
			HashMap<String, Object> planeConfig = dao.getPlaneConfig(activeAirline, pID);
			
			Double maxFirst = XPassengersUtils.firstMax;
			Double maxBusiness = XPassengersUtils.businessMax;
			Double maxEconomy = XPassengersUtils.economyMax;
			Double maxCargo = XPassengersUtils.cargoMax;
			
			ResultSet prices = dao.getSingleContent("priceFirst,priceBusiness,priceEconomy,priceCargo,freeLuggage", "airlines", activeAirline);
			Double priceEconomy = prices.getDouble("priceEconomy");
			Double priceBusiness = prices.getDouble("priceBusiness");
			Double priceFirst = prices.getDouble("priceFirst");
			Double priceCargo = prices.getDouble("priceCargo");
			int freeLuggage = (int) prices.getDouble("freeLuggage");
			
			double relMaxCurrentFirst = priceFirst / maxFirst;
			double relMaxCurrentBusiness = priceBusiness / maxBusiness;
			double relMaxCurrentEconomy = priceEconomy / maxEconomy;
			double relMaxCurrentCargo = priceCargo / maxCargo;
								
			
			int usedFirst = (int) planeConfig.get("first");
			int usedBusiness = (int) planeConfig.get("business");
			int usedEconomy = (int) planeConfig.get("economy");
			double useableWeight = (double) planeConfig.get("useableWeight") / utils.toLbs;
			
			if (relMaxCurrentFirst > 0.3) {
				usedFirst = (int) (usedFirst * (1-relMaxCurrentFirst));
			}
			
			if (relMaxCurrentBusiness > 0.3) {
				usedBusiness = (int) (usedBusiness * (1-relMaxCurrentBusiness)); 
			}
			
			if (relMaxCurrentEconomy > 0.3) {
				usedEconomy = (int) (usedEconomy * (1-relMaxCurrentEconomy));
			}
			
			utils.ini.put("flight", "firstClass", usedFirst);
			utils.ini.put("flight", "businessClass", usedBusiness);
			utils.ini.put("flight", "economyClass", usedEconomy);
			
			int quality = (int) planeConfig.get("quality");
			utils.ini.put("flight", "quality", quality);
			
			int currentFuel = (int) planeConfig.get("fuel");
			
			int fuel = (int) Math.round(Integer.parseInt(request.getParameter("fuel")) / utils.toLbs);
			utils.ini.put("flight", "fuel", fuel);	
			dao.updatePlaneFuel(pID, fuel);
			String fuelType = (String) planeConfig.get("fuelType"); 
			double fuelDif = fuel - currentFuel;
			dao.storeFuel(fuelDif,fuelType);
			
			int completePassengers = usedFirst + usedBusiness + usedEconomy;
			int passWeight = 0;
			int passCargo = 0;
			int cargo = 0;
			int valueablePassCargo = 0;
			
			for (int i = 1; i<= completePassengers; i++) {
				int randPassWeight = ThreadLocalRandom.current().nextInt(50,130);
				int randCargoWeight = ThreadLocalRandom.current().nextInt(10,50);
				
				if (randCargoWeight > freeLuggage) {
					valueablePassCargo = valueablePassCargo + (randCargoWeight - freeLuggage);
				}
				
				passWeight = passWeight + randPassWeight;
				passCargo = passCargo + randCargoWeight;
			}
			
			cargo = passWeight + passCargo;
			useableWeight = useableWeight - cargo;
			if (relMaxCurrentCargo > 0.3) {
				useableWeight = (int) (useableWeight * (1-relMaxCurrentCargo));
			}
			useableWeight = useableWeight - fuel;
			boolean overloaded;					
			if (useableWeight < 0) {
				overloaded = true;						
			} else {
				overloaded = false;
			}
			
			useableWeight = useableWeight + fuel;
			int valueableCargo = (int) (valueablePassCargo + useableWeight);
			utils.ini.put("flight", "cargo", (int) useableWeight + cargo);
			utils.ini.put("flight","valueableCargo", valueableCargo);
			utils.ini.put("flight","overloaded", overloaded);
			long id = dao.createNewFlight(start, dest, cargo, fuel, pID, usedFirst, usedBusiness, usedEconomy, valueableCargo);
			utils.ini.put("flight","ID", id);
			utils.redirect(request, response);
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		utils.ini.put("flight","startAirport",start);
		utils.ini.put("flight","destAirport",dest);
		
		try {
			utils.ini.store();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
