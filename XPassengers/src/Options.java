import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class Options extends JFrame {

	private JPanel contentPane;
	private JTextField xplanePath;

	/**
	 * Create the frame.
	 */
	public Options() {
		Options jframe = this;
		setTitle("Options");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 345, 259);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblXplaneDirectory = new JLabel("X-Plane Directory");
		lblXplaneDirectory.setBounds(10, 11, 114, 14);
		contentPane.add(lblXplaneDirectory);
		
		xplanePath = new JTextField();
		xplanePath.setBounds(123, 8, 152, 20);
		contentPane.add(xplanePath);
		xplanePath.setColumns(10);
		
		String path = XPassengers.ini.get("options", "xplanePath");
		xplanePath.setText(path);
		
		JButton button = new JButton("...");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser xplanePathChooser = new JFileChooser();
				xplanePathChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int returnValue = xplanePathChooser.showOpenDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					File selectedFolder = xplanePathChooser.getSelectedFile();
					xplanePath.setText(selectedFolder.getAbsolutePath());
				}
			}
		});
		button.setBounds(274, 7, 29, 22);
		contentPane.add(button);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jframe.dispose();
			}
		});
		btnCancel.setBounds(230, 186, 89, 23);
		contentPane.add(btnCancel);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				XPassengers.ini.put("options","xplanePath",xplanePath.getText());
				try {
					XPassengers.ini.store();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				jframe.dispose();
			}
		});
		btnSave.setBounds(131, 186, 89, 23);
		contentPane.add(btnSave);
		
	}

}
