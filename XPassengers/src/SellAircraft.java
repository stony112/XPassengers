import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class SellAircraft extends JFrame {

	private JPanel contentPane;
	public SellAircraft() {
		XPassengersUtils utils = new XPassengersUtils();
		setTitle("Sell Aircraft");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 578, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		SellAircraft jframe = this;
		databaseAccess dao = new databaseAccess();
		int activeAirline = utils.getActiveAirline();
		ResultSet airplanes = dao.getAirlinesAirplanesData("*", activeAirline);
		
		try {
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
				ResultSet airplane = dao.getSingleContent("*", "airplanes", airplaneid);
				airplanesList.add(utils.buildIDNamePrice(airplane.getInt("id"), airplane.getString("name"), airplane.getFloat("price")));
				while (airplanes.next()) {
					airplaneid = airplanes.getInt("airplaneid");
					airplane = dao.getSingleContent("*", "airplanes", airplaneid);
					int quality = airplanes.getInt("quality");
					int flighthours = airplanes.getInt("flighthours");
					float newPrice = airplane.getFloat("price");
					double sellingPrice = utils.getSellingPrice(quality,flighthours,newPrice);
					airplanesList.add(utils.buildIDNamePrice(airplane.getInt("id"), airplane.getString("name"), sellingPrice));
				}
				contentPane.setLayout(null);
				JComboBox selectAirplane = new JComboBox(airplanesList.toArray());
				selectAirplane.setSelectedIndex(1); 
				Object choosedPlane = selectAirplane.getSelectedItem();
				int planeID = utils.getContentID(choosedPlane);
				
				selectAirplane.setBounds(95, 11, 350, 20);
				contentPane.add(selectAirplane);
				JLabel lblSelectAirplane = new JLabel("Select Airplane");
				lblSelectAirplane.setBounds(10, 14, 75, 14);
				contentPane.add(lblSelectAirplane);
				
				JButton btnSave = new JButton("Sell");
				btnSave.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						try {
							Object choosedPlane = selectAirplane.getSelectedItem();
							int planeID = utils.getContentID(choosedPlane);
							float price = utils.getSelectedPrice(choosedPlane);							
							int activeAirline = utils.getActiveAirline();
							Double balance = null;
							balance = dao.getSingleContent("balance", "airlines", activeAirline).getDouble("balance");
							Double newBalance = balance + price;
							dao.sellPlane(planeID, newBalance, jframe);	
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}						
					}
				});
				btnSave.setBounds(257, 56, 89, 23);
				contentPane.add(btnSave);
				
				JButton btnCancel = new JButton("Cancel");
				btnCancel.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						jframe.dispose();
					}
				});
				btnCancel.setBounds(356, 56, 89, 23);
				contentPane.add(btnCancel);
			}
		} catch (HeadlessException | ClassNotFoundException | FileNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
