import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class SetActiveAirline extends JFrame {

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public SetActiveAirline() throws SQLException {
		XPassengersUtils utils = new XPassengersUtils();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 300, 160);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		SetActiveAirline jframe = this;
		
		databaseAccess dao = new databaseAccess();
		dao.initDB();
		ResultSet airlines = dao.getResults("airlines");
		
		if (airlines.next()) {
			ArrayList<String> airlinesList = new ArrayList();
			airlinesList.add(utils.buildIDName(airlines.getInt("id"), airlines.getString("name")));
			while (airlines.next()) {
				airlinesList.add(utils.buildIDName(airlines.getInt("id"), airlines.getString("name")));
			}
			JComboBox comboBox = new JComboBox(airlinesList.toArray());
			comboBox.setBounds(95, 11, 150, 20);
			contentPane.add(comboBox);
			JLabel lblSelectAirline = new JLabel("Select Airline");
			lblSelectAirline.setBounds(10, 14, 75, 14);
			contentPane.add(lblSelectAirline);
			JButton btnSave = new JButton("Save");
			btnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Object activeAirline = comboBox.getSelectedItem();
					int id = utils.getContentID(activeAirline);
					XPassengers.ini.put("airlines","activeAirline",id);
					try {
						XPassengers.ini.store();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					jframe.dispose();
				}
			});
			btnSave.setBounds(86, 87, 89, 23);
			contentPane.add(btnSave);
		} else {
			JLabel lblNoAirlineAvailable = new JLabel("No Airline available");
			lblNoAirlineAvailable.setBounds(10, 11, 97, 14);
			contentPane.add(lblNoAirlineAvailable);
			JButton btnNewButton = new JButton("New Airline");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Airlines airlines = new Airlines();
					airlines.setVisible(true);
				}
			});
			btnNewButton.setBounds(10, 36, 89, 23);
			contentPane.add(btnNewButton);
		}
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jframe.dispose();
			}
		});
		btnCancel.setBounds(185, 87, 89, 23);
		contentPane.add(btnCancel);
	}
}
