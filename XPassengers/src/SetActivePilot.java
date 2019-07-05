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

public class SetActivePilot extends JFrame {

	private JPanel contentPane;
	/**
	 * Create the frame.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public SetActivePilot() throws ClassNotFoundException, SQLException {
		XPassengersUtils utils = new XPassengersUtils();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 300, 160);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		SetActivePilot jframe = this;
		
		databaseAccess dao = new databaseAccess();
		dao.initDB();
		ResultSet pilots = dao.getResults("pilots");
		
		if (pilots.next()) {
			ArrayList<String> pilotsList = new ArrayList<String>();
			StringBuilder name = new StringBuilder();
			name.append(pilots.getString("firstname"));
			name.append(" ");
			name.append(pilots.getString("lastname"));
			pilotsList.add(utils.buildIDName(pilots.getInt("id"), name.toString()));
			while (pilots.next()) {
				pilotsList.add(utils.buildIDName(pilots.getInt("id"), name.toString()));
			}
			JComboBox comboBox = new JComboBox(pilotsList.toArray());
			comboBox.setBounds(95, 11, 150, 20);
			contentPane.add(comboBox);
			JLabel lblSelectAirline = new JLabel("Select Pilot");
			lblSelectAirline.setBounds(10, 14, 75, 14);
			contentPane.add(lblSelectAirline);
			JButton btnSave = new JButton("Save");
			btnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					Object activePilot = comboBox.getSelectedItem();
					int id = utils.getContentID(activePilot);
					XPassengers.ini.put("pilots","activePilot",id);
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
			JLabel lblNoPilotAvailable = new JLabel("No Pilot available");
			lblNoPilotAvailable.setBounds(10, 11, 97, 14);
			contentPane.add(lblNoPilotAvailable);
			JButton btnNewButton = new JButton("New Pilot");
			btnNewButton.addActionListener(new ActionListener() {
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
			btnNewButton.setBounds(10, 36, 89, 23);
			contentPane.add(btnNewButton);
		}
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jframe.dispose();
			}
		});
		contentPane.setLayout(null);
		btnCancel.setBounds(185, 87, 89, 23);
		contentPane.add(btnCancel);
	}

}
