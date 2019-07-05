import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
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

public class BuyAirplane extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 */
	public BuyAirplane() throws ClassNotFoundException, SQLException, FileNotFoundException {
		BuyAirplane jframe = this;
		setTitle("Buy Airplane");
		XPassengersUtils utils = new XPassengersUtils();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 479, 132);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		databaseAccess dao = new databaseAccess();
		dao.initDB();
		ResultSet airplanes = dao.getResults("airplanes");
		
		if (!airplanes.next()) {
			utils.checkNewPlanes();
			airplanes = dao.getResults("airplanes");
		}		
		
		ArrayList<String> airplanesList = new ArrayList<String>();
		airplanesList.add(utils.buildIDNamePrice(airplanes.getInt("id"), airplanes.getString("name"), airplanes.getFloat("price")));
		while (airplanes.next()) {
			airplanesList.add(utils.buildIDNamePrice(airplanes.getInt("id"), airplanes.getString("name"), airplanes.getFloat("price")));
		}
		JComboBox comboBox = new JComboBox(airplanesList.toArray());
		comboBox.setBounds(95, 11, 350, 20);
		contentPane.add(comboBox);
		JLabel lblSelectAirplane = new JLabel("Select Airplane");
		lblSelectAirplane.setBounds(10, 14, 75, 14);
		contentPane.add(lblSelectAirplane);
		JButton btnSave = new JButton("Buy");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Object choosedPlane = comboBox.getSelectedItem();
				int planeID = utils.getContentID(choosedPlane);
				float price = utils.getSelectedPrice(choosedPlane);
				int activeAirline = utils.getActiveAirline();
				Double balance = null;
				try {
					balance = dao.getSingleContent("balance", "airlines", activeAirline).getDouble("balance");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (balance >= price) {
					Double newBalance = balance - price;
					dao.buyPlane(planeID, newBalance, jframe);					
				} else {
					JOptionPane.showMessageDialog(jframe, "Sorry! You can't afford this airplane", "Not enough money", JOptionPane.ERROR_MESSAGE);
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
}
