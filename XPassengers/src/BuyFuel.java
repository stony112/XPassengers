import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class BuyFuel extends JFrame {

	private JPanel contentPane;
	private JTextField buyJetA1;
	private JTextField buyAvGas;
	
	float jetA1Tank = 0;
	float avGasTank = 0;
	
	public BuyFuel() {
		setTitle("Buy Fuel");
		databaseAccess dao = new databaseAccess();
		XPassengersUtils utils = new XPassengersUtils();
		int activeAirline = utils.getActiveAirline();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 310, 366);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		BuyFuel jframe = this;
		
		JLabel lblCurrentPrices = new JLabel("Current Prices");
		lblCurrentPrices.setBounds(10, 11, 107, 14);
		contentPane.add(lblCurrentPrices);
		
		JLabel lblJetA = new JLabel("Jet A1:");
		lblJetA.setBounds(10, 36, 71, 14);
		contentPane.add(lblJetA);
		
		JLabel lblAvgas = new JLabel("AvGas:");
		lblAvgas.setBounds(10, 61, 71, 14);
		contentPane.add(lblAvgas);
		float jetA1 = dao.getLastFuelprice(utils.jetA1);
		float avGas = dao.getLastFuelprice(utils.avgas);
		JLabel jeta1 = new JLabel("");
		jeta1.setBounds(91, 36, 46, 14);
		contentPane.add(jeta1);
		jeta1.setText(String.valueOf(jetA1));
		
		JLabel avgas = new JLabel("");
		avgas.setBounds(91, 61, 46, 14);
		contentPane.add(avgas);
		avgas.setText(String.valueOf(avGas));
		
		JLabel lblCurrentFuelIn = new JLabel("Current Fuel in Tanks");
		lblCurrentFuelIn.setBounds(10, 98, 127, 14);
		contentPane.add(lblCurrentFuelIn);
		
		JLabel label = new JLabel("AvGas:");
		label.setBounds(10, 148, 71, 14);
		contentPane.add(label);
		
		JLabel label_1 = new JLabel("Jet A1:");
		label_1.setBounds(10, 123, 71, 14);
		contentPane.add(label_1);
		
		try {
			ResultSet fuelTanks = dao.getSingleContent("availableFuelJetA1,availableFuelAvGas", "airlines", activeAirline);
			jetA1Tank = fuelTanks.getFloat("availableFuelJetA1");
			avGasTank = fuelTanks.getFloat("availableFuelAvGas");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		JLabel jeta1Tank = new JLabel("");
		jeta1Tank.setBounds(91, 123, 218, 14);
		contentPane.add(jeta1Tank);
		jeta1Tank.setText(String.valueOf(jetA1Tank) + "kg");
		
		JLabel avgasTank = new JLabel("");
		avgasTank.setBounds(91, 148, 218, 14);
		contentPane.add(avgasTank);
		avgasTank.setText(String.valueOf(avGasTank) + "kg");
		
		JLabel lblBuyFuel = new JLabel("Buy Fuel");
		lblBuyFuel.setBounds(10, 187, 127, 14);
		contentPane.add(lblBuyFuel);
		
		JLabel lblJetA_1 = new JLabel("Jet A1");
		lblJetA_1.setBounds(10, 212, 71, 14);
		contentPane.add(lblJetA_1);
		
		JLabel lblAvgas_1 = new JLabel("AvGas");
		lblAvgas_1.setBounds(10, 237, 71, 14);
		contentPane.add(lblAvgas_1);
		
		buyJetA1 = new JTextField();
		buyJetA1.setBounds(91, 209, 86, 20);
		contentPane.add(buyJetA1);
		buyJetA1.setColumns(10);
		
		buyAvGas = new JTextField();
		buyAvGas.setBounds(91, 234, 86, 20);
		contentPane.add(buyAvGas);
		buyAvGas.setColumns(10);
		
		JLabel lblLbs = new JLabel("kg");
		lblLbs.setBounds(187, 212, 46, 14);
		contentPane.add(lblLbs);
		
		JLabel lblLbs2 = new JLabel("kg");
		lblLbs2.setBounds(187, 237, 46, 14);
		contentPane.add(lblLbs2);
		
		JButton btnSave = new JButton("Buy");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int activeAirline = utils.getActiveAirline();
				Double balance = null;
				float countJetA1 = Float.parseFloat(buyJetA1.getText());
				float countAvGas = Float.parseFloat(buyAvGas.getText());
				float price = (countJetA1 * jetA1) + (countAvGas * avGas);
				double completeJetA1 = countJetA1 + jetA1Tank;
				double completeAvGas = countAvGas + avGasTank;
				try {
					balance = dao.getSingleContent("balance", "airlines", activeAirline).getDouble("balance");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (balance >= price) {
					Double newBalance = balance - price;
					dao.buyFuel(activeAirline, newBalance, completeJetA1, completeAvGas, jframe);					
				} else {
					JOptionPane.showMessageDialog(jframe, "Sorry! You can't afford this airplane", "Not enough money", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnSave.setBounds(66, 293, 89, 23);
		contentPane.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jframe.dispose();
			}
		});
		btnCancel.setBounds(187, 293, 89, 23);
		contentPane.add(btnCancel);
	}
}
