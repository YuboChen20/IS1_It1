package gui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class CreatePronosticoGUI extends JFrame {

	private JPanel contentPane;
	private JTextField textField;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreatePronosticoGUI frame = new CreatePronosticoGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public CreatePronosticoGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnClose = new JButton("Close");
		btnClose.setBounds(259, 229, 89, 23);
		contentPane.add(btnClose);
		
		JButton btnCreatePronostico = new JButton("Create");
		btnCreatePronostico.setBounds(99, 229, 89, 23);
		contentPane.add(btnCreatePronostico);
		
		textField = new JTextField();
		textField.setBounds(97, 192, 304, 20);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewPronostico = new JLabel("New Pronostico");
		lblNewPronostico.setBounds(10, 194, 83, 20);
		contentPane.add(lblNewPronostico);
		
		JPanel panel = new JPanel();
		panel.setBounds(147, 42, 254, 121);
		contentPane.add(panel);
		
		JLabel lblNewListaPronosticos = new JLabel("Lista Pronosticos:");
		lblNewListaPronosticos.setBounds(30, 96, 112, 14);
		contentPane.add(lblNewListaPronosticos);
		
		
	}
}
