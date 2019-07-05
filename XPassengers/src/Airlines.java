import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;

public class Airlines extends JFrame {

	private JPanel contentPane;
	private JTextField airlineName;
	private JTextField airlineICAO;
	private JTextField airlineIATA;

	/**
	 * Create the frame.
	 */
	public Airlines() {
		setTitle("New Airline");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 374, 219);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Airlines jframe = this;
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				databaseAccess dao = new databaseAccess();
				String alName = airlineName.getText();
				String alicao = airlineICAO.getText();
				String aliata = airlineIATA.getText();
				
				try {
					dao.createAirline(alName, alicao, aliata);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				jframe.dispose();
			}
		});
		btnSave.setBounds(129, 95, 89, 23);
		contentPane.add(btnSave);
		
		JLabel lblName = new JLabel("Name");
		lblName.setBounds(10, 11, 46, 14);
		contentPane.add(lblName);
		
		airlineName = new JTextField();
		airlineName.setBounds(129, 8, 200, 20);
		contentPane.add(airlineName);
		airlineName.setColumns(10);
		
		JLabel lblHomebaseicao = new JLabel("Homebase (ICAO)");
		lblHomebaseicao.setBounds(10, 36, 106, 14);
		contentPane.add(lblHomebaseicao);
		
		airlineICAO = new JTextField();
		airlineICAO.setBounds(129, 33, 86, 20);
		contentPane.add(airlineICAO);
		airlineICAO.setColumns(10);
		
		JLabel lblIata = new JLabel("IATA");
		lblIata.setBounds(10, 61, 46, 14);
		contentPane.add(lblIata);
		
		airlineIATA = new JTextField();
		airlineIATA.setBounds(129, 58, 86, 20);
		contentPane.add(airlineIATA);
		airlineIATA.setColumns(10);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jframe.dispose();
			}
		});
		btnCancel.setBounds(240, 95, 89, 23);
		contentPane.add(btnCancel);
	}
}
