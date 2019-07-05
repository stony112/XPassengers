import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.ini4j.Wini;

import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import java.io.File;
import java.io.FileNotFoundException;

public class XPassengers extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	static Wini ini;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					File configIni = new File("config.ini");
					if (!configIni.exists()) {
						configIni.createNewFile();
					}
					ini = new Wini(configIni);
					XPassengers frame = new XPassengers();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public XPassengers() throws SQLException {
		XPassengersUtils utils = new XPassengersUtils();
		databaseAccess dao = new databaseAccess();
		dao.initDB();
		dao.createFuelprice(utils.jetA1);
		dao.createFuelprice(utils.avgas);
		setTitle("XPassengers");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 656, 475);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 630, 21);
		contentPane.add(menuBar);
		
		JMenu mnAirlines = new JMenu("Airlines");
		menuBar.add(mnAirlines);
		
		JMenuItem mntmNewAirline = new JMenuItem("New Airline");
		mntmNewAirline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Airlines airlines = new Airlines();
				airlines.setVisible(true);
			}
		});
		mnAirlines.add(mntmNewAirline);
		
		JMenuItem mntmSetActiveAirline = new JMenuItem("Set Active Airline");
		mntmSetActiveAirline.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SetActiveAirline setActiveAirline;
				try {
					setActiveAirline = new SetActiveAirline();
					setActiveAirline.setVisible(true);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		});
		mnAirlines.add(mntmSetActiveAirline);
		
		JMenuItem mntmPrices = new JMenuItem("Prices");
		mntmPrices.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EditPrices editPrices;
				try {
					editPrices = new EditPrices();
					editPrices.setVisible(true);
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		mnAirlines.add(mntmPrices);
		
		JMenuItem mntmBuyFuel = new JMenuItem("Buy Fuel");
		mntmBuyFuel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				BuyFuel buyFuel;
				buyFuel = new BuyFuel();
				buyFuel.setVisible(true);
			}
		});
		mnAirlines.add(mntmBuyFuel);
		
		JMenu mnPilots = new JMenu("Pilots");
		menuBar.add(mnPilots);
		
		JMenuItem mntmNewPilot = new JMenuItem("New Pilot");
		mntmNewPilot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Pilots pilots;
				try {
					pilots = new Pilots();
					pilots.setVisible(true);
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		mnPilots.add(mntmNewPilot);
		
		JMenuItem mntmSetActivePilot = new JMenuItem("Set Active Pilot");
		mntmSetActivePilot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SetActivePilot setActivePilot;
				try {
					setActivePilot = new SetActivePilot();
					setActivePilot.setVisible(true);
				} catch (ClassNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		});
		mnPilots.add(mntmSetActivePilot);
		
		JMenu mnAirplanes = new JMenu("Aircrafts");
		menuBar.add(mnAirplanes);
		
		JMenuItem mntmCheckForNew = new JMenuItem("Check for new Aircrafts");
		mntmCheckForNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					int counter = utils.checkNewPlanes();
					JOptionPane.showMessageDialog(contentPane, counter + " New Aircrafts Found");
				} catch (FileNotFoundException | ClassNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		mnAirplanes.add(mntmCheckForNew);
		
		JMenuItem mntmEditAirplane = new JMenuItem("Buy Aircraft");
		mntmEditAirplane.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BuyAirplane buyAirplane;
				try {
					buyAirplane = new BuyAirplane();
					buyAirplane.setVisible(true);
				} catch (ClassNotFoundException | FileNotFoundException | SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		mnAirplanes.add(mntmEditAirplane);
		
		JMenuItem mntmEditAircraft = new JMenuItem("Edit Aircraft");
		mntmEditAircraft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				EditAircraft editAircraft;
				try {
					editAircraft = new EditAircraft();
					editAircraft.setVisible(true);
				} catch (ClassNotFoundException | FileNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
			}
		});
		mnAirplanes.add(mntmEditAircraft);
		
		JMenuItem mntmSellAircraft = new JMenuItem("Sell Aircraft");
		mntmSellAircraft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SellAircraft sellAircraft;
				sellAircraft = new SellAircraft();
				sellAircraft.setVisible(true);
			}
		});
		mnAirplanes.add(mntmSellAircraft);
		
		JMenu mnFlights = new JMenu("Flights");
		menuBar.add(mnFlights);
		
		JMenuItem mntmPlanNewFlight = new JMenuItem("Plan new Flight");
		mntmPlanNewFlight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PlanNewFlight planNewFlight; 
				try {
					planNewFlight = new PlanNewFlight();
					planNewFlight.setVisible(true);
				} catch (ClassNotFoundException | FileNotFoundException | SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		});
		mnFlights.add(mntmPlanNewFlight);
		
		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);
		
		JMenuItem mntmOptions = new JMenuItem("Options");
		mntmOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Options options;
				options = new Options();
				options.setVisible(true);
			}
		});
		mnHelp.add(mntmOptions);
		
		JLabel lblActiveAirline = new JLabel("Active Airline");
		lblActiveAirline.setBounds(10, 32, 116, 14);
		contentPane.add(lblActiveAirline);
		
		JLabel activeAirlineLabel = new JLabel("");
		activeAirlineLabel.setBounds(136, 32, 494, 14);
		contentPane.add(activeAirlineLabel);
		
		int activeAirlineID = utils.getActiveAirline();
		ResultSet activeAirline = dao.getSingleContent("*", "airlines", activeAirlineID);
		activeAirlineLabel.setText(activeAirline.getString("name"));
		
		JLabel lblBalance = new JLabel("Balance");
		lblBalance.setBounds(10, 66, 96, 14);
		contentPane.add(lblBalance);
		
		JLabel airlineBalanceLabel = new JLabel("");
		airlineBalanceLabel.setBounds(136, 66, 494, 14);
		contentPane.add(airlineBalanceLabel);
		double balance = activeAirline.getDouble("balance");
		airlineBalanceLabel.setText(utils.doubleToPrice(balance));
		
		JLabel lblAircrafts = new JLabel("Aircrafts");
		lblAircrafts.setBounds(10, 91, 96, 14);
		contentPane.add(lblAircrafts);
		
		JLabel aircraftsCount = new JLabel("");
		aircraftsCount.setBounds(136, 91, 494, 14);
		contentPane.add(aircraftsCount);
		
		int countAircraft = dao.countWhere("airlines_airplanes", "airlineid="+activeAirlineID);
		aircraftsCount.setText(Integer.toString(countAircraft));
		
		JLabel lblPilots = new JLabel("Pilots");
		lblPilots.setBounds(10, 116, 96, 14);
		contentPane.add(lblPilots);
		
		JLabel pilotsCount = new JLabel("");
		pilotsCount.setBounds(136, 116, 46, 14);
		contentPane.add(pilotsCount);
		
		int countPilots = dao.countWhere("pilots", "airlineid="+activeAirlineID);
		pilotsCount.setText(Integer.toString(countPilots));
		
		JLabel lblFuel = new JLabel("JetA1");
		lblFuel.setBounds(10, 141, 46, 14);
		contentPane.add(lblFuel);
		
		JLabel availableFuelJetA1 = new JLabel("");
		availableFuelJetA1.setBounds(136, 141, 133, 14);
		contentPane.add(availableFuelJetA1);
		availableFuelJetA1.setText(activeAirline.getDouble("availableFuelJetA1") + "kg");
		
		JLabel lblAvgas = new JLabel("AvGas");
		lblAvgas.setBounds(10, 166, 46, 14);
		contentPane.add(lblAvgas);
		
		JLabel availableFuelAvGas = new JLabel("");
		availableFuelAvGas.setBounds(136, 166, 133, 14);
		contentPane.add(availableFuelAvGas);
		availableFuelAvGas.setText(activeAirline.getDouble("availableFuelAvGas") + "kg");
	}
}
