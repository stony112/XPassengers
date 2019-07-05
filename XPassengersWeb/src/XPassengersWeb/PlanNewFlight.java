package XPassengersWeb;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.SliderUI;
import javax.swing.JTextField;
import javax.swing.JSlider;
import javax.swing.JSpinner;

public class PlanNewFlight extends JFrame {

	private JPanel contentPane;
	private JTextField startAirport;
	private JTextField destAirport;
	int planeID;
	double maxFuel;
	HashMap<String, Object> planeConfig;
	
	public void setPlaneID(int id) {
		planeID = id;
	}
	
	public int getPlaneID() {
		return planeID;
	}
	
	public PlanNewFlight() throws ClassNotFoundException, SQLException, FileNotFoundException {
		XPassengersUtils utils = new XPassengersUtils();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 650, 330);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		PlanNewFlight jframe = this;
		databaseAccess dao = new databaseAccess();
		dao.initDB();
		int activeAirline = utils.getActiveAirline();
		ResultSet airplanes = dao.getAirlinesAirplanesData("*", activeAirline);
		
		JLabel lblStartAirport = new JLabel("Start Airport (ICAO)");
		lblStartAirport.setBounds(10, 39, 165, 14);
		contentPane.add(lblStartAirport);
		
		startAirport = new JTextField();
		startAirport.setBounds(185, 36, 86, 20);
		contentPane.add(startAirport);
		startAirport.setColumns(10);
		startAirport.setEditable(false);
		
		JLabel lblDestinationAirporticao = new JLabel("Destination Airport (ICAO)");
		lblDestinationAirporticao.setBounds(10, 64, 165, 14);
		contentPane.add(lblDestinationAirporticao);
		
		destAirport = new JTextField();
		destAirport.setBounds(185, 61, 86, 20);
		contentPane.add(destAirport);
		destAirport.setColumns(10);
		
		JLabel lblRefuelAirplane = new JLabel("Refuel Aircraft");
		lblRefuelAirplane.setBounds(10, 111, 86, 14);
		contentPane.add(lblRefuelAirplane);
		
		JSlider sliderFuel = new JSlider();
		sliderFuel.setBounds(185, 111, 200, 14);
		contentPane.add(sliderFuel);
		
		
		JLabel lblLbs = new JLabel("kg");
		lblLbs.setBounds(491, 111, 46, 14);
		contentPane.add(lblLbs);
		
		JSpinner spinnerFuel = new JSpinner();
		spinnerFuel.setBounds(395, 108, 86, 20);
		contentPane.add(spinnerFuel);
		
		
		if (!airplanes.next()) {
			String[] options = new String[2];
			options[0] = new String("Buy");
			options[1] = new String("Cancel");
			int optionReturns = JOptionPane.showOptionDialog(null, "You don't have any aircrafts yet","No Aircraft available",0,JOptionPane.INFORMATION_MESSAGE,null,options,null);
			if (optionReturns == 0) {
				BuyAirplane buyAirplane = new BuyAirplane();
				buyAirplane.setVisible(true);
				this.dispose();
			} else {
				jframe.dispose();
			}
		} else {	
			ArrayList<String> airplanesList = new ArrayList();
			int airplaneid = airplanes.getInt("airplaneid");
			String airplane = dao.getSingleContent("name", "airplanes", airplaneid).getString("name");
			airplanesList.add(utils.buildIDName(airplaneid, airplane));
			while (airplanes.next()) {
				airplaneid = airplanes.getInt("airplaneid");
				airplane = dao.getSingleContent("name", "airplanes", airplaneid).getString("name");
				airplanesList.add(utils.buildIDName(airplaneid, airplane));
			}
			contentPane.setLayout(null);
			JComboBox selectAirplane = new JComboBox(airplanesList.toArray());
			selectAirplane.setSelectedIndex(1); 
			Object choosedPlane = selectAirplane.getSelectedItem();
			int pID = utils.getContentID(choosedPlane);
			setPlaneID(pID);
			planeConfig = dao.getPlaneConfig(activeAirline, pID);			
			selectAirplane.setBounds(185, 11, 350, 20);
			contentPane.add(selectAirplane);
			JLabel lblSelectAirplane = new JLabel("Select Airplane");
			lblSelectAirplane.setBounds(10, 14, 165, 14);
			contentPane.add(lblSelectAirplane);
			
			//planeConfig = dao.getPlaneConfig(activeAirline, pID);
			if (planeConfig.get("lastPos") != null) {
				startAirport.setText((String) planeConfig.get("lastPos"));
			} else {
				ResultSet airline = dao.getSingleContent("homebase", "airlines", activeAirline);
				String homebase = airline.getString("homebase");
				startAirport.setText(homebase);
			}
			maxFuel = (double) planeConfig.get("maxFuel");
			sliderFuel.setMaximum((int) maxFuel);
			spinnerFuel.setValue(planeConfig.get("fuel"));
			sliderFuel.setValue((int) planeConfig.get("fuel")); 			
			
			utils.changeSliderSpinner(sliderFuel, spinnerFuel);
			utils.changeSpinnerSlider(sliderFuel, spinnerFuel, maxFuel, jframe);
			selectAirplane.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Object choosedPlane = selectAirplane.getSelectedItem();
					int pID = utils.getContentID(choosedPlane);
					setPlaneID(pID);
					try {
						planeConfig = dao.getPlaneConfig(activeAirline, pID);
						maxFuel = (double) planeConfig.get("maxFuel");
						sliderFuel.setMaximum((int) maxFuel);
						sliderFuel.setValue((int) planeConfig.get("fuel")); 
						spinnerFuel.setValue(planeConfig.get("fuel"));
						if (planeConfig.get("lastPos") != null) {
							startAirport.setText((String) planeConfig.get("lastPos"));
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
		}
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String start = startAirport.getText();
				String dest = destAirport.getText();
				try {
					int pID = getPlaneID(); 
					String airplane = dao.getSingleContent("name", "airplanes", pID).getString("name");
					XPassengers.ini.remove("flight");
					XPassengers.ini.put("flight", "airplane", airplane);
					
					String livery = (String) planeConfig.get("livery");
					XPassengers.ini.put("flight", "livery", livery);
					
					Double maxFirst = EditPrices.firstMax;
					Double maxBusiness = EditPrices.businessMax;
					Double maxEconomy = EditPrices.economyMax;
					Double maxCargo = EditPrices.cargoMax;
					
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
					
					XPassengers.ini.put("flight", "firstClass", usedFirst);
					XPassengers.ini.put("flight", "businessClass", usedBusiness);
					XPassengers.ini.put("flight", "economyClass", usedEconomy);
					
					int quality = (int) planeConfig.get("quality");
					XPassengers.ini.put("flight", "quality", quality);
					
					int currentFuel = (int) planeConfig.get("fuel");
					
					int fuel = (int) Math.round(sliderFuel.getValue() / utils.toLbs);
					XPassengers.ini.put("flight", "fuel", fuel);	
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
						JOptionPane.showMessageDialog(jframe, "the aircraft is overloaded!","Overloaded", JOptionPane.WARNING_MESSAGE);
						overloaded = true;						
					} else {
						overloaded = false;
					}
					
					useableWeight = useableWeight + fuel;
					
					XPassengers.ini.put("flight", "cargo", (int) useableWeight + cargo);
					XPassengers.ini.put("flight","valueableCargo", (int) valueablePassCargo + useableWeight);
					XPassengers.ini.put("flight","overloaded", overloaded);
					JOptionPane.showMessageDialog(jframe, "Flight successfully planned","Flight planned", JOptionPane.INFORMATION_MESSAGE);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				XPassengers.ini.put("flight","startAirport",start);
				XPassengers.ini.put("flight","destAirport",dest);
				
				try {
					XPassengers.ini.store();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnSave.setBounds(312, 257, 89, 23);
		contentPane.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jframe.dispose();
			}
		});
		btnCancel.setBounds(411, 257, 89, 23);
		contentPane.add(btnCancel);
	}
}
