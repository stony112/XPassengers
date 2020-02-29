package XPassengersWeb;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

public class XPassengersUtils {
	public double relEcoFirst = 2.5;
	public double relEcoBusni = 1.3;
	public double relBusniFirst = 2.1;
	public String jetA1 = "jetA1";
	public String avgas = "avgas";
	double toLbs = 2.20462;
	public final static double economyMax = 100;
	public final static double businessMax = 200;
	public final static double firstMax = 400;
	public final static double cargoMax = 30;
	public final static double freeMax = 50;
	Wini ini;
	
	public static Map<String, Integer> seatMapping;
	static {
		seatMapping = new HashMap<>();
		seatMapping.put("ASK21", 1);
		seatMapping.put("L5_Sentinel", 1);
		seatMapping.put("DR401", 3);
		seatMapping.put("DR401_CDI155", 3);
		seatMapping.put("Cessna_172", 3);
		seatMapping.put("Cessna_172SP", 3);
		seatMapping.put("Cessna_172SP_G1000", 3);
		seatMapping.put("Cessna_172SP_seaplane", 3);
		seatMapping.put("c400", 3);
		seatMapping.put("Orbiter", 5);
		seatMapping.put("Baron_58", 5);
		seatMapping.put("CirrusSF50", 6);
		seatMapping.put("C90B", 6);
		seatMapping.put("S-76", 12);
		seatMapping.put("S-76C", 12);
		seatMapping.put("DC-3", 32);
		seatMapping.put("VSL DC-3", 32);
		seatMapping.put("E-170", 80);
		seatMapping.put("SSGE-170LR_Evo_11", 80);
		seatMapping.put("Dash8Q400_XP11", 86);
		seatMapping.put("E-195", 126);
		seatMapping.put("SSGE-195LR_Evo_11", 126);
		seatMapping.put("737", 149);
		seatMapping.put("b737", 149);
		seatMapping.put("B733", 150);		
		seatMapping.put("a319", 160);
		seatMapping.put("a319_StdDef", 160);
		seatMapping.put("a319_XP10", 160);
		seatMapping.put("MD80", 172);
		seatMapping.put("A320", 180);
		seatMapping.put("a320neo", 180);
		seatMapping.put("b738", 189);
		seatMapping.put("b738_4k", 189);
		seatMapping.put("b739", 189);
		seatMapping.put("b739_4k", 189);		
		seatMapping.put("b737_4k", 149);
		seatMapping.put("757-200", 239);
		seatMapping.put("757-200_xp11", 239);
		seatMapping.put("767-200", 290);
		seatMapping.put("757-300", 295);
		seatMapping.put("757-300_xp11", 295);
		seatMapping.put("787", 335);
		seatMapping.put("B7879", 335);
		seatMapping.put("767-300", 351);
		seatMapping.put("767-300ER_xp11", 351);
		seatMapping.put("a330", 440);
		seatMapping.put("A350", 440);
		seatMapping.put("A350_xp11", 440);
		seatMapping.put("747-100", 550);
		seatMapping.put("B747-100 NASA", 550);
		seatMapping.put("B748", 605);
		seatMapping.put("SSG_B748-I_11", 605);
		seatMapping.put("747-400", 660);
	}

	public void setIni() {
		File configIni;
		try {
			configIni = new File("E:\\EclipseProjects\\XPassengers\\XPassengersWeb\\WebContent\\config\\config.ini");
			try {
				ini = new Wini(configIni);
			} catch(FileNotFoundException e) {
				configIni.createNewFile();
			} finally {
				ini = new Wini(configIni);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public int getActiveAirline() {
		setIni();
		int id;
		try {
			id = Integer.parseInt(ini.get("airlines","active"));		
		} catch (NullPointerException | NumberFormatException e) {
			id = 0;
		}
		return id;
	}
	
	public int getActivePilot() {
		setIni();
		int id;
		try {
			id = Integer.parseInt(ini.get("pilots","active"));		
		} catch (NullPointerException | NumberFormatException e) {
			id = 0;
		}
		return id;
	}
	
	public String getXPlanePath() {
		setIni();
		String path = ini.get("options","xplanePath");
		return path;
	}
	
	public void setActiveAirline(int id) {
		setIni();
		ini.put("airlines","activeAirline",id);
		try {
			ini.store();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setActive(int id, String type) {
		setIni();
		ini.put(type,"active",id);
		try {
			ini.store();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public java.sql.Date getSQLDate(java.util.Date date) {
		java.sql.Date sqlDate = new java.sql.Date(date.getTime());
		return sqlDate;
	}
	
	public java.sql.Date getSQLTodaysDate() {
		Calendar calendar = Calendar.getInstance();
		java.util.Date currentDate = calendar.getTime();
		java.sql.Date date = new java.sql.Date(currentDate.getTime());
		
		return date;
	}
	
	public ArrayList<String> getLiveries(int airplaneid) {
		ArrayList<String> liveries = new ArrayList<String>();
		DatabaseAccess dao = new DatabaseAccess();
		
		try {
			dao.initDB();
			String airplanePath = dao.getSingleContent("path", "airplanes", airplaneid).getString("path") + "\\liveries";
			File liveryFolder = new File(airplanePath);
			if (liveryFolder.exists()) {
				for (final File subdirectories : liveryFolder.listFiles()) {
					if (subdirectories.isDirectory()) {
						liveries.add(subdirectories.getName());
					}
				}
				return liveries;
			}			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		liveries.add("none");
		return liveries;
	}

	public int checkNewPlanes() throws ClassNotFoundException, SQLException, InvalidFileFormatException, IOException {
		DatabaseAccess dao = new DatabaseAccess();
		dao.initDB();
		setIni();
		String airplanePath;
		String airplaneName;
		String airplaneACFName;
		int counter = 0, seats = 0;
		double maxFuel = 0,toWeight = 0,emptyWeight = 0;
		float green_hi_N1 = 0;
		float price;
		int eng = 0, prop = 0, engType = 0, minLicense = 1;
		String xplanePath = ini.get("options","xplanePath");
		File aircraftFolder = new File(xplanePath + "/Aircraft");
		ResultSet availableAirplanes = dao.getResults("airplanes");
		ArrayList<String> availableNames = new ArrayList<>();
		ArrayList<String> availablePaths = new ArrayList<>();
		while (availableAirplanes.next()) {
			availableNames.add(availableAirplanes.getString("name"));
			availablePaths.add(availableAirplanes.getString("path"));
		}
		for (final File subdirectories : aircraftFolder.listFiles()) {
			if (subdirectories.isDirectory()) {
				for (final File aircraft : subdirectories.listFiles()) {
					airplanePath = aircraft.getAbsolutePath();
					for (final File aircraftACF : aircraft.listFiles()) {
						if (aircraftACF.getName().toLowerCase().endsWith(".acf")) {
							HashMap<String, Object> airplaneData = new HashMap<String, Object>();							
							airplaneACFName = aircraftACF.getName();
							airplaneName = airplaneACFName.replace(".acf", "");
							airplaneData.put("name", airplaneName);
							if (!(availableNames.contains(airplaneName) && availablePaths.contains(airplanePath))) {
								final Scanner scanner = new Scanner(aircraftACF);
								System.out.println(airplaneName);
								while (scanner.hasNextLine()) {
									final String lineFromFile = scanner.nextLine();
									if (lineFromFile.contains("acf/_m_fuel_max_tot")) {
										maxFuel = Double.parseDouble(lineFromFile.replace("P acf/_m_fuel_max_tot ", ""));
									} else if (lineFromFile.contains("acf/_m_max")) {
										toWeight = Double.parseDouble(lineFromFile.replace("P acf/_m_max ", ""));
									} else if (lineFromFile.contains("acf/_m_empty")) {
										emptyWeight = Double.parseDouble(lineFromFile.replace("P acf/_m_empty ", ""));
									} else if (lineFromFile.contains("acf/_num_engn")) {
										eng = Integer.parseInt(lineFromFile.replace("P acf/_num_engn ", ""));
									} else if (lineFromFile.contains("acf/_num_prop")) {
										prop = Integer.parseInt(lineFromFile.replace("P acf/_num_prop ", ""));
									} else if (lineFromFile.contains("acf/_green_hi_N1 ")) {
										green_hi_N1 = Float.parseFloat(lineFromFile.replace("P acf/_green_hi_N1 ", ""));
									}
								}
								counter++;
								scanner.close();
								price = getPrice(eng,prop,toWeight);
								if (seatMapping.get(airplaneName) == null) {
									System.out.println("Seatmapping for airplane " + airplaneName + " not available");
									seats = 0;
								} else {
									seats = seatMapping.get(airplaneName);
								}
								if (eng == prop) {
									if (green_hi_N1 > 0) {
										engType = 2;
									} else {
										engType = 1;
									}										
								} else {
									engType = 3;
								}
								
								if (engType == 1 && eng == 1 && emptyWeight/toLbs <= 1500) {
									minLicense = 1;
								} else if (engType == 1 && eng > 1 && emptyWeight/toLbs <= 2500) {
									minLicense = 4;
								} else if (engType <= 2 && emptyWeight/toLbs <= 12000) {
									minLicense = 5;
								} else if (engType <= 3 && emptyWeight/toLbs <= 20000) {
									minLicense = 7;
								} else if (engType <= 3 && emptyWeight/toLbs <= 40000) {
									minLicense = 8;
								} else if (engType <= 3 && emptyWeight/toLbs <= 80000) {
									minLicense = 10;
								} else {
									minLicense = 11;
								}
								// name,toweight,fuel,emptyweight,path,engines,props,price,seats,engType,minLicense
								airplaneData.put("toweight", toWeight);
								airplaneData.put("fuel", maxFuel);
								airplaneData.put("emptyweight", emptyWeight);
								airplaneData.put("path", airplanePath);
								airplaneData.put("engines", eng);
								airplaneData.put("props", prop);
								airplaneData.put("price", price);
								airplaneData.put("seats", seats);
								airplaneData.put("engType", engType);
								airplaneData.put("minLicense", minLicense);

								dao.insert("airplanes", airplaneData);
							}
						}
					}
				}
			}
		}
		return counter;
	}
	
	public Float getPrice(int eng, int prop, double toWeight) {
		float price = 0;		
		if (prop == 1) {
			if (toWeight < 1000) {
				price = createPrice(20000, 50000);
			} else if (toWeight < 2000) {
				price = createPrice(60000, 80000);
			} else if (toWeight < 3000) {
				price = createPrice(90000, 150000);
			} else {
				price = createPrice(160000, 250000);
			}
			
		} else if (prop == 2) {
			if (toWeight < 5000) {
				price = createPrice(100000, 150000);
			} else if (toWeight < 10000) {
				price = createPrice(250000, 750000);
			} else if (toWeight < 20000) {
				price = createPrice(1000000, 5000000);
			} else {
				price = createPrice(7500000, 12000000);
			}
		} else if (prop > 2) {
			if (toWeight < 20000) {
				price = createPrice(12000000, 15000000);
			} else if (toWeight < 150000) {
				price = createPrice(20000000, 50000000);
			} else {
				price = createPrice(900000, 170000000);
			}
		} else {
			if (eng == 0) {
				price = createPrice(80000, 120000);
			} else if (eng == 1) {
				if (toWeight < 10000) {
					price = createPrice(1500000, 2500000);
				} else {
					price = createPrice(5000000, 10000000);
				}
			} else if (eng == 2) {
				if (toWeight < 60000) {
					price = createPrice(50000000, 75000000);
				} else if (toWeight < 200000) {
					price = createPrice(75000000, 100000000);
				} else if (toWeight < 550000) {
					price = createPrice(150000000, 250000000);
				} else {
					price = createPrice(270000000, 350000000);
				}
			} else if (eng == 3) {
				price = createPrice(175000000, 225000000);
			} else if (eng == 4) {
				if (toWeight < 700000) {
					price = createPrice(225000000, 300000000);
				} else {
					price = createPrice(350000000, 450000000);
				}
			} else {
				price = createPrice(800000000,2000000000);
			}
		}
		return price;
	}
	
	public float createPrice(int min, int max) {
		float price = (float) (min + (Math.random() * (max - min) + 1)); 
		return price;
	}
	
	public int getSeatConfig(int e, int b, int f, int seats, String changed) {
		int remaining = 0;
		int business = (int) (b*relEcoBusni);
		int first = (int) (f*relEcoFirst);
		remaining = seats - business - first;		
		
		return remaining;
	}
	
	public String doubleToPrice(double number) {
		String pattern = "###,###.00";
		DecimalFormat decimalFormat = new DecimalFormat(pattern);
		//decimalFormat.applyLocalizedPattern(pattern);
		String price = decimalFormat.format(number);
		return price;
	}

	public double getSellingPrice(int quality, int flighthours, float newPrice) {
		double sellingPrice;
		float repairPrice = getRepairPrice(quality, newPrice);
		sellingPrice = newPrice - repairPrice*1.1;
		
		if (flighthours > 10 && flighthours <= 100) {
			sellingPrice = sellingPrice * getRelatives(10,100,0.9,1,flighthours);
		} else if (flighthours > 100 && flighthours <= 200) {
			sellingPrice = sellingPrice * getRelatives(100,200,0.5,0.9,flighthours);
		} else if (flighthours > 200 && flighthours <= 500) {
			sellingPrice = sellingPrice * getRelatives(200,500,0.2,0.5,flighthours);
		} else if (flighthours > 500) {
			sellingPrice = sellingPrice * getRelatives(500,1000,0.05,0.2,flighthours);
		} else {
			return newPrice;
		}
		
		if (sellingPrice < 0) {
			sellingPrice = newPrice * 0.005;
		}
		
		return sellingPrice;
	}
	
	private double getRelatives(int min, int max, double d, double e, int currentValue) {
		double relatives = 0;
		int steps = max - min;
		double values = e - d;
		double valuePerStep = values / steps;
		
		int stepsToCurrent = currentValue - min;
		
		relatives = stepsToCurrent * valuePerStep + d; 
		
		return relatives;
	}

	private float getRepairPrice(int quality, float newPrice) {
		float repairPrice = 0;
		int repairValue = 100-quality;
		if (quality == 100) {
			return 0;
		} else if (quality < 100 && quality >= 75){
			repairPrice = repairValue/1000*newPrice;
		} else if (quality < 75 && quality >= 50){
			repairPrice = repairValue/500*newPrice;
		} else if (quality < 50 && quality >= 25){
			repairPrice = repairValue/250*newPrice;
		} else {
			return newPrice;
		}
		
		return repairPrice;
	}
	
	public void debugRequestParams(HttpServletRequest request) {
		System.out.println("START");
		System.out.println("REQUEST METHOD");
		
		System.out.println(request.getMethod());
		
		System.out.println("HEADERS");
		
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
		  String headerName = headerNames.nextElement();
		  System.out.println("Header Name - " + headerName + ", Value - " + request.getHeader(headerName));
		}
		
		System.out.println("PARAMETERS");
		
		Enumeration<String> params = request.getParameterNames(); 
		while(params.hasMoreElements()){
		 String paramName = params.nextElement();
		 System.out.println("Parameter Name - "+paramName+", Value - "+request.getParameter(paramName));
		}
		
		System.out.println("END");
	}
	
	public void redirect(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendRedirect(request.getHeader("referer"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void redirect(HttpServletRequest request, HttpServletResponse response, HashMap<String,String> parameter) {
		try {
			StringBuilder url = new StringBuilder();
			String referer = new URI(request.getHeader("referer")).getPath();
			url.append(referer);
			int counter = 1;
			for (String i : parameter.keySet()) {
				if (counter == 1) {
					url.append("?");
				} else {
					url.append("&");
				}
				url.append(i);
				url.append("=");
				url.append(parameter.get(i));
			}
			response.sendRedirect(url.toString());
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
} 
