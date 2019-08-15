package XPassengersWeb;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.RoundingMode;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSlider;
import javax.swing.JSpinner;

public class EditPrices extends JFrame {

	private JPanel contentPane;
	


	/**
	 * Create the frame.
	 * @throws SQLException 
	 * @throws ClassNotFoundException 
	 */
	public EditPrices() throws ClassNotFoundException, SQLException {
		setTitle("Prices");
		XPassengersUtils utils = new XPassengersUtils();
		databaseAccess dao = new databaseAccess();
		int activeAirline = utils.getActiveAirline();
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 421, 326);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		ResultSet prices = dao.getSingleContent("priceFirst,priceBusiness,priceEconomy,priceCargo,freeLuggage", "airlines", activeAirline);
		Double priceEconomy = prices.getDouble("priceEconomy");
		Double priceBusiness = prices.getDouble("priceBusiness");
		Double priceFirst = prices.getDouble("priceFirst");
		Double priceCargo = prices.getDouble("priceCargo");
		Double freeLuggage = prices.getDouble("freeLuggage");
		
		EditPrices jframe = this;
		
		JLabel lblEconomyClass = new JLabel("Economy Class");
		lblEconomyClass.setBounds(10, 61, 110, 14);
		contentPane.add(lblEconomyClass);
		
		JLabel lblBusinessClass = new JLabel("Business Class");
		lblBusinessClass.setBounds(10, 86, 110, 14);
		contentPane.add(lblBusinessClass);
		
		JLabel lblFirstClass = new JLabel("First Class");
		lblFirstClass.setBounds(10, 111, 110, 14);
		contentPane.add(lblFirstClass);

		JSlider sliderEconomy = new JSlider();
		sliderEconomy.setMaximum(economyMax.intValue());
		sliderEconomy.setBounds(104, 61, 200, 14);
		contentPane.add(sliderEconomy);
		sliderEconomy.setValue(priceEconomy.intValue());
		
		JSlider sliderBusiness = new JSlider();
		sliderBusiness.setMaximum(businessMax.intValue());
		sliderBusiness.setBounds(104, 86, 200, 14);
		contentPane.add(sliderBusiness);
		sliderBusiness.setValue(priceBusiness.intValue());
		
		JSlider sliderFirst = new JSlider();
		sliderFirst.setMaximum((int) firstMax.intValue());
		sliderFirst.setBounds(104, 111, 200, 14);
		contentPane.add(sliderFirst);
		sliderFirst.setValue(priceFirst.intValue());
		
		JSpinner spinnerEconomy = new JSpinner();
		spinnerEconomy.setBounds(314, 58, 81, 20);
		contentPane.add(spinnerEconomy);
		spinnerEconomy.setValue(priceEconomy);
		
		JSpinner spinnerBusiness = new JSpinner();
		spinnerBusiness.setBounds(314, 83, 81, 20);
		contentPane.add(spinnerBusiness);
		spinnerBusiness.setValue(priceBusiness);
		
		JSpinner spinnerFirst = new JSpinner();
		spinnerFirst.setBounds(314, 108, 81, 20);
		contentPane.add(spinnerFirst);
		spinnerFirst.setValue(priceFirst);
		
		JSlider sliderFreeLuggage = new JSlider();
		sliderFreeLuggage.setMaximum(freeMax.intValue());
		sliderFreeLuggage.setBounds(104, 177, 200, 14);
		contentPane.add(sliderFreeLuggage);
		sliderFreeLuggage.setValue(freeLuggage.intValue());
		
		JSpinner spinnerFreeLuggage = new JSpinner();
		spinnerFreeLuggage.setBounds(314, 174, 81, 20);
		contentPane.add(spinnerFreeLuggage);
		
		JSlider sliderCargoPrice = new JSlider();
		sliderCargoPrice.setMaximum(cargoMax.intValue());
		sliderCargoPrice.setBounds(104, 205, 200, 14);
		contentPane.add(sliderCargoPrice);
		sliderCargoPrice.setValue(priceCargo.intValue());
		
		JSpinner spinnerCargoPrice = new JSpinner();
		spinnerCargoPrice.setBounds(314, 202, 81, 20);
		contentPane.add(spinnerCargoPrice);
		
		utils.changeSliderSpinner(sliderFirst, spinnerFirst);
		utils.changeSliderSpinner(sliderBusiness, spinnerBusiness);
		utils.changeSliderSpinner(sliderEconomy, spinnerEconomy);
		utils.changeSliderSpinner(sliderCargoPrice, spinnerCargoPrice);
		utils.changeSliderSpinner(sliderFreeLuggage, spinnerFreeLuggage);
		
		utils.changeSpinnerSlider(sliderFirst, spinnerFirst, firstMax, jframe);
		utils.changeSpinnerSlider(sliderBusiness, spinnerBusiness, businessMax, jframe);
		utils.changeSpinnerSlider(sliderEconomy, spinnerEconomy, economyMax, jframe);
		utils.changeSpinnerSlider(sliderCargoPrice, spinnerCargoPrice, cargoMax, jframe);
		utils.changeSpinnerSlider(sliderFreeLuggage, spinnerFreeLuggage, freeMax, jframe);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dao.setPrices(activeAirline, sliderFirst.getValue(), sliderBusiness.getValue(), sliderEconomy.getValue(), sliderCargoPrice.getValue(), sliderFreeLuggage.getValue());
				jframe.dispose();
			}
		});
		btnSave.setBounds(206, 253, 89, 23);
		contentPane.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				jframe.dispose();
			}
		});
		btnCancel.setBounds(306, 253, 89, 23);
		contentPane.add(btnCancel);
		
		JLabel lblPricesPerkm = new JLabel("Prices per 100km");
		lblPricesPerkm.setBounds(10, 11, 385, 14);
		contentPane.add(lblPricesPerkm);
		
		JLabel lblPassengers = new JLabel("Passengers");
		lblPassengers.setBounds(10, 36, 110, 14);
		contentPane.add(lblPassengers);
		
		JLabel lblLuggagecargo = new JLabel("Luggage/Cargo");
		lblLuggagecargo.setBounds(10, 149, 157, 14);
		contentPane.add(lblLuggagecargo);
		
		JLabel lblFreeLuggage = new JLabel("Free Luggage");
		lblFreeLuggage.setBounds(10, 177, 110, 14);
		contentPane.add(lblFreeLuggage);
		
		JLabel lblPricekg = new JLabel("Price/kg");
		lblPricekg.setBounds(10, 205, 110, 14);
		contentPane.add(lblPricekg);
	}
}
