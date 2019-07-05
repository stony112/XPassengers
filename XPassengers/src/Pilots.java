import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Pilots extends JFrame {

	private JPanel contentPane;
	private JTextField firstname;
	private JTextField lastname;
	private JLabel lblBirthday;
	private JLabel lblAirline;
	private JLabel lblNoAirline;
	/**
	 * Create the frame.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public Pilots() throws ClassNotFoundException, SQLException {
		Pilots jframe = this;
		XPassengersUtils utils = new XPassengersUtils();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 344, 233);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblFirstname = new JLabel("Firstname");
		lblFirstname.setBounds(10, 11, 101, 14);
		contentPane.add(lblFirstname);
		
		firstname = new JTextField();
		firstname.setBounds(95, 8, 202, 20);
		contentPane.add(firstname);
		firstname.setColumns(10);
		
		JLabel lblLastname = new JLabel("Lastname");
		lblLastname.setBounds(10, 36, 76, 14);
		contentPane.add(lblLastname);
		
		lastname = new JTextField();
		lastname.setBounds(95, 33, 202, 20);
		contentPane.add(lastname);
		lastname.setColumns(10);
		
		lblBirthday = new JLabel("Birthday");
		lblBirthday.setBounds(10, 61, 46, 14);
		contentPane.add(lblBirthday);
		
		UtilDateModel model = new UtilDateModel();
		Properties p = new Properties();
		p.put("text.today", "Today");
		p.put("text.month", "Month");
		p.put("text.year", "Year");
		JDatePanelImpl datePanel = new JDatePanelImpl(model, p);
		JDatePickerImpl birthday = new JDatePickerImpl(datePanel, new DateLabelFormatter());
		birthday.setBounds(95, 58, 202, 20);
		contentPane.add(birthday);
		
		lblAirline = new JLabel("Airline");
		lblAirline.setBounds(10, 86, 46, 14);
		contentPane.add(lblAirline);
		
				
		databaseAccess dao = new databaseAccess();
		dao.initDB();
		ResultSet airlines = dao.getResults("airlines");
		
		if (airlines.next()) {
			ArrayList<String> airlinesList = new ArrayList();
			airlinesList.add(utils.buildIDName(airlines.getInt("id"), airlines.getString("name")));
			while (airlines.next()) {
				airlinesList.add(utils.buildIDName(airlines.getInt("id"), airlines.getString("name")));
			}
			JComboBox airline = new JComboBox(airlinesList.toArray());
			airline.setBounds(95, 83, 202, 20);
			contentPane.add(airline);
			JButton btnSave = new JButton("Save");
			btnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					databaseAccess dao = new databaseAccess();
					String piFirstname = firstname.getText();
					String piLastname = lastname.getText();
					java.sql.Date piBirthday = utils.getSQLDate((java.util.Date) birthday.getModel().getValue());
					Object activeAirline = airline.getSelectedItem();
					int piAirline = utils.getContentID(activeAirline);
					
					try {
						dao.createPilot(piFirstname,piLastname,piBirthday,piAirline);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					jframe.dispose();
				}
			});
			btnSave.setBounds(109, 160, 89, 23);
			contentPane.add(btnSave);	
		} else {
			lblNoAirline = new JLabel("No Airline available");
			lblNoAirline.setBounds(95, 83, 202, 20);
			contentPane.add(lblNoAirline);
		}
		
	
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jframe.dispose();
			}
		});
		btnCancel.setBounds(208, 160, 89, 23);
		contentPane.add(btnCancel);
	}
}
