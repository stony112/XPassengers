import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JSlider;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JTextPane;
import java.awt.Color;
import java.awt.SystemColor;
import javax.swing.UIManager;

public class EditAircraft extends JFrame {

	private JPanel contentPane;
	int airplaneSeats = 0;
	int airplaneID = 0;
	int activeAirline = 0;
	/**
	 * Create the frame.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 * @throws FileNotFoundException 
	 */
	
	public void setAirplaneID(int id) {
		airplaneID = id;
	}
	
	public int getAirplaneID() {
		return airplaneID;
	}
	
	public void setActiveAirline(int id) {
		activeAirline = id;
	}
	
	public int getActiveAirline() {
		return activeAirline;
	}
	
	public void setAirplaneSeats(int seats) {
		airplaneSeats = seats;
	}
	
	public int getAirplaneSeats() {
		return airplaneSeats;
	}
	
	public EditAircraft() throws ClassNotFoundException, SQLException, FileNotFoundException {
		XPassengersUtils utils = new XPassengersUtils();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 526, 330);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextPane textPane = new JTextPane();
		textPane.setBackground(new Color(204, 204, 204));
		textPane.setForeground(Color.RED);
		textPane.setEditable(false);
		textPane.setBounds(10, 169, 490, 66);
		contentPane.add(textPane);
		
		EditAircraft jframe = this;
		databaseAccess dao = new databaseAccess();
		int activeAirline = utils.getActiveAirline();
		HashMap<String, Object> airplaneWheres = new HashMap<String, Object>();
		airplaneWheres.put("airlineid", airlineid);
		ResultSet airplanes = dao.select("airlines_airplanes", "*", airplaneWheres);
		
		if (airplanes.next()) {
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
			selectAirplane.setSelectedIndex(0); 
			Object choosedPlane = selectAirplane.getSelectedItem();
			int planeID = utils.getContentID(choosedPlane);
			
			selectAirplane.setBounds(95, 11, 350, 20);
			contentPane.add(selectAirplane);
			JLabel lblSelectAirplane = new JLabel("Select Airplane");
			lblSelectAirplane.setBounds(10, 14, 75, 14);
			contentPane.add(lblSelectAirplane);
			
			JLabel lblLivery = new JLabel("Select Livery");
			lblLivery.setBounds(10, 39, 75, 14);
			contentPane.add(lblLivery);
			ArrayList<String> liveryList = utils.getLiveries(planeID);
			
			JComboBox selectLivery = new JComboBox(liveryList.toArray());
			selectLivery.setBounds(95, 36, 350, 20);
			contentPane.add(selectLivery);
			
			JLabel lblFirstclass = new JLabel("Firstclass");
			lblFirstclass.setBounds(10, 94, 71, 14);
			contentPane.add(lblFirstclass);
			JSlider sliderFirstclass = new JSlider();
			sliderFirstclass.setBounds(99, 94, 200, 14);
			contentPane.add(sliderFirstclass);
			
			JLabel lblBusinessclass = new JLabel("Businessclass");
			lblBusinessclass.setBounds(10, 119, 64, 14);
			contentPane.add(lblBusinessclass);			
			JSlider sliderBusinessclass = new JSlider();
			sliderBusinessclass.setBounds(99, 119, 200, 14);
			contentPane.add(sliderBusinessclass);
			
			JLabel lblEconomyclass = new JLabel("Economyclass");
			lblEconomyclass.setBounds(10, 144, 87, 14);
			contentPane.add(lblEconomyclass);
			
			setAirplaneSeats(dao.getSingleContent("seats", "airplanes", planeID).getInt("seats"));
			
			JLabel lblEconomyclassValue = new JLabel(Integer.toString(airplaneSeats));
			lblEconomyclassValue.setBounds(300,144,50,14);
			contentPane.add(lblEconomyclassValue);
			JLabel lblBusinessclassValue = new JLabel("0");
			lblBusinessclassValue.setBounds(300,119,50,14);
			contentPane.add(lblBusinessclassValue);
			JLabel lblFirstclassValue = new JLabel("0");
			lblFirstclassValue.setBounds(300,94,50,14);
			contentPane.add(lblFirstclassValue);			
			
			ResultSet currentAircraftData = dao.getAirlinesAirplanesPlaneData(activeAirline, planeID);
			sliderBusinessclass.setValue(currentAircraftData.getInt("business"));
			lblBusinessclassValue.setText(Integer.toString(currentAircraftData.getInt("business")));
			sliderFirstclass.setValue(currentAircraftData.getInt("first"));
			lblFirstclassValue.setText(Integer.toString(currentAircraftData.getInt("first")));
			lblEconomyclassValue.setText(Integer.toString(currentAircraftData.getInt("economy")));
			
			utils.updateSliders(sliderFirstclass, sliderBusinessclass, lblFirstclass, lblBusinessclass, lblFirstclassValue, lblBusinessclassValue, textPane, airplaneSeats);
			
			sliderBusinessclass.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					JSlider source = (JSlider)e.getSource();
					if (!source.getValueIsAdjusting()) {
						int sliderValue = source.getValue();
						lblBusinessclassValue.setText(Integer.toString(sliderValue));
						int seatConfig = utils.getSeatConfig(Integer.parseInt(lblEconomyclassValue.getText()), sliderValue, sliderFirstclass.getValue(), airplaneSeats, "business");
						lblEconomyclassValue.setText(Integer.toString(seatConfig));
					}
				}
			});
			
			sliderFirstclass.addChangeListener(new ChangeListener() {
				public void stateChanged(ChangeEvent e) {
					JSlider source = (JSlider)e.getSource();
					if (!source.getValueIsAdjusting()) {
						int sliderValue = source.getValue();
						lblFirstclassValue.setText(Integer.toString(sliderValue));
						int seatConfig = utils.getSeatConfig(Integer.parseInt(lblEconomyclassValue.getText()), sliderBusinessclass.getValue(), sliderValue, airplaneSeats, "first");
						lblEconomyclassValue.setText(Integer.toString(seatConfig));
					}
				}
			});
			
			
			JButton btnSave = new JButton("Save");
			btnSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					int economy = Integer.parseInt(lblEconomyclassValue.getText());
					int business = sliderBusinessclass.getValue();
					int first = sliderFirstclass.getValue();
					int airplaneid = getAirplaneID();
					String livery = selectLivery.getSelectedItem().toString();
					
					if (economy < 0 || business < 0 || first < 0) {
						JOptionPane.showMessageDialog(jframe, "negative seat configuration",null,JOptionPane.ERROR_MESSAGE);
					} else {
						try {
							dao.updatePlane(activeAirline, airplaneid, livery, economy, business, first);
							JOptionPane.showMessageDialog(jframe, "Aircraft successfully saved");
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
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
			
			selectAirplane.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Object choosedPlane = selectAirplane.getSelectedItem();
					int planeID = utils.getContentID(choosedPlane);
					setAirplaneID(planeID);
					try {
						int airplaneSeats = dao.getSingleContent("seats", "airplanes", planeID).getInt("seats");
						setAirplaneSeats(airplaneSeats);
						utils.updateSliders(sliderFirstclass, sliderBusinessclass, lblFirstclass, lblBusinessclass, lblFirstclassValue, lblBusinessclassValue, textPane, airplaneSeats);
						ResultSet currentAircraftData = dao.getAirlinesAirplanesPlaneData(activeAirline, planeID);
						sliderBusinessclass.setValue(currentAircraftData.getInt("business"));
						sliderFirstclass.setValue(currentAircraftData.getInt("first"));
						lblEconomyclassValue.setText(Integer.toString(currentAircraftData.getInt("economy")));
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					ArrayList<String> liveryList = utils.getLiveries(planeID); 
					selectLivery.removeAllItems();
					for (String s:liveryList) {
						selectLivery.addItem(s);
					}
				}
			});
		} else {	
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
		}
	}
}
