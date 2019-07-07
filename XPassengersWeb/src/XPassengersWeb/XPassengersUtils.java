package XPassengersWeb;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Scanner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTextPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.ini4j.InvalidFileFormatException;
import org.ini4j.Wini;

public class XPassengersUtils {
	public double relEcoFirst = 2.5;
	public double relEcoBusni = 1.3;
	public double relBusniFirst = 2.1;
	String jetA1 = "jetA1";
	String avgas = "avgas";
	double toLbs = 2.20462;
	Wini ini;
	
	public void setIni() {
		File configIni;
		try {
			configIni = new File("D:\\EclipseProjects\\XPassengers\\XPassengersWeb\\WebContent\\config\\config.ini");
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
	
	public double getRelEcoFirst() {
		return relEcoFirst;
	}
	
	public double getRelEcoBusni() {
		return relEcoBusni;
	}
	
	public double getRelBusniFirst() {
		return relBusniFirst;
	}
	
	public String buildIDName(int id, String name) {
		StringBuilder idName = new StringBuilder();
		idName.append(id);
		idName.append(" - ");
		idName.append(name);
		return idName.toString();
	}
	
	public String buildIDNamePrice(int id, String name, float price) {
		StringBuilder idName = new StringBuilder();
		DecimalFormat df = new DecimalFormat("#,###.##");
		df.setRoundingMode(RoundingMode.HALF_EVEN);
		String priceDF = df.format(price);
		idName.append(id);
		idName.append(" - ");
		idName.append(name);
		idName.append(" - ");
		idName.append(priceDF);
		return idName.toString();
	}
	
	public String buildIDNamePrice(int id, String name, double price) {
		StringBuilder idName = new StringBuilder();
		DecimalFormat df = new DecimalFormat("#,###.##");
		df.setRoundingMode(RoundingMode.HALF_EVEN);
		String priceDF = df.format(price);
		idName.append(id);
		idName.append(" - ");
		idName.append(name);
		idName.append(" - ");
		idName.append(priceDF);
		return idName.toString();
	}
	
	public int getContentID(Object content) {
		String selectedObject = content.toString();
		String[] parts = selectedObject.split(" - ");
		int id = Integer.parseInt(parts[0]);
		return id;
	}
	
	public float getSelectedPrice(Object content) {
		String selectedObject = content.toString();
		String[] parts = selectedObject.split(" - ");
		String priceString = parts[2].replace(".", "");
		priceString = priceString.replace(",", ".");
		float price = Float.parseFloat(priceString);
		return price;
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
		databaseAccess dao = new databaseAccess();
		
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
		databaseAccess dao = new databaseAccess();
		dao.initDB();
		String airplanePath;
		String airplaneName;
		String airplaneACFName;
		int counter = 0, seats = 0;
		Double maxFuel = (double) 0,toWeight = (double) 0,emptyWeight = (double) 0;
		float price;
		int eng = 0, prop = 0;
		String xplanePath = ini.get("options","xplanePath");
		File aircraftFolder = new File(xplanePath + "/Aircraft");
		for (final File subdirectories : aircraftFolder.listFiles()) {
			if (subdirectories.isDirectory()) {
				for (final File aircraft : subdirectories.listFiles()) {
					airplanePath = aircraft.getAbsolutePath();
					for (final File aircraftACF : aircraft.listFiles()) {
						if (aircraftACF.getName().toLowerCase().endsWith(".acf")) {
							ResultSet availableAirplanes = dao.getResults("airplanes");
							ArrayList<String> availableNames = new ArrayList<>();
							ArrayList<String> availablePaths = new ArrayList<>();
							while (availableAirplanes.next()) {
								availableNames.add(availableAirplanes.getString("name"));
								availablePaths.add(availableAirplanes.getString("path"));
							}
							airplaneACFName = aircraftACF.getName();
							airplaneName = airplaneACFName.replace(".acf", "");
							if (!(availableNames.contains(airplaneName) && availablePaths.contains(airplanePath))) {
								final Scanner scanner = new Scanner(aircraftACF);
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
									}
								}
								counter++;
								scanner.close();
								price = getPrice(eng,prop,toWeight);
								seats = getSeats(airplaneName);
								String fuelType;
								if (eng == prop) {
									fuelType = avgas;
								} else {
									fuelType = jetA1;
								}
								dao.createAirplane(airplaneName, toWeight, maxFuel, emptyWeight, airplanePath, eng, prop, price, seats, fuelType);
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
	
	public int getSeats(String a) {
		int seats = 0;
		if (a.contains("ASK21") || a.contains("L5_Sentinel")) {
			seats = 1;
		} else if (a.contains("DR401") || a.contains("Cessna_172") || a.contains("c400")) {
			seats = 3;
		} else if (a.contains("Orbiter") || a.contains("Baron_58")) {
			seats = 5;
		} else if (a.contains("CirrusSF50") || a.contains("C90B")) {
			seats = 6;
		} else if (a.contains("S-76")) {
			seats = 12;
		} else if (a.contains("DC-3")) {
			seats = 32;
		} else if (a.contains("E-170")) {
			seats = 80;
		} else if (a.contains("E-195")) {
			seats = 126;
		} else if (a.contains("B733") || a.contains("A320")) {
			seats = 150;
		} else if (a.contains("a319")) {
			seats = 156;
		} else if (a.contains("MD80")) {
			seats = 172;
		} else if (a.contains("b738") || a.contains("b739") || a.contains("737")) {
			seats = 189;
		} else if (a.contains("757-200")) {
			seats = 239;
		} else if (a.contains("767-200")) {
			seats = 290;
		} else if (a.contains("757-300")) {
			seats = 295;
		} else if (a.contains("787")) {
			seats = 335;
		} else if (a.contains("767-300")) {
			seats = 351;
		} else if (a.contains("a330") || a.contains("A350")) {
			seats = 440;
		} else if (a.contains("747-100")) {
			seats = 550;
		} else if (a.contains("B748")) {
			seats = 605;
		} else if (a.contains("747-400")) {
			seats = 660;
		}
		return seats;
	}
	
	public void updateSliders(JSlider f, JSlider b, JLabel fc, JLabel bc, JLabel fcv, JLabel bcv, JTextPane textPane, int seats) {
		changeVisible(b,bc,bcv,false);
		changeVisible(f,fc,fcv,false);
		if (seats < 15) {
			textPane.setText("Your aircraft is to small for Business- or First-Class-Seats (below max of 15 seats).\nYou are not able to change seatplan");
		} else if (seats < 130) {
			changeVisible(b,bc,bcv,true);
			textPane.setText("Your aircraft is to small for First-Class (below max of 130 seats)");
		} else {
			changeVisible(b,bc,bcv,true);
			changeVisible(f,fc,fcv,true);
			textPane.setText("");
		}
		//b.setValue(0);
		b.setMinimum(0);
		int businessMax = (int) (seats/relEcoBusni);
		b.setMaximum(businessMax);
		//f.setValue(0);
		int firstMax = (int) (seats/relEcoFirst);
		f.setMinimum(0);
		f.setMaximum(firstMax);
	}
	
	public void changeVisible(JSlider slider, JLabel label, JLabel value, boolean visible) {
		slider.setVisible(visible);
		label.setVisible(visible);
		value.setVisible(visible);
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
	
	public void changeSliderSpinner(JSlider slider, JSpinner spinner) {
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSlider source = (JSlider)e.getSource();
				if (!source.getValueIsAdjusting()) {
					int sliderValue = source.getValue();
					spinner.setValue(sliderValue);
				}
			}
		});
	}
	
	public void changeSpinnerSlider(JSlider slider, JSpinner spinner, double sliderMax, JFrame jframe) {
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				JSpinner source = (JSpinner)e.getSource();
				int sliderValue = (int) source.getValue();
				if (sliderValue > sliderMax) {
					JOptionPane.showMessageDialog(jframe, "Over maximum: " + sliderMax,"Value too high", JOptionPane.ERROR_MESSAGE);
				}
				slider.setValue(sliderValue);
			}
		});
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
} 
