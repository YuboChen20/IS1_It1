package gui;

import java.text.DateFormat;
import java.util.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JCalendar;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import businessLogic.BLFacade;
import configuration.UtilDate;
import domain.Event;
import exceptions.EventFinished;
import exceptions.QuestionAlreadyExist;

public class CreateandFindQuestionGUI extends JFrame {
	private static final long serialVersionUID = 1L;

	private JComboBox<Event> jComboBoxEvents = new JComboBox<Event>();
	DefaultComboBoxModel<Event> modelEvents = new DefaultComboBoxModel<Event>();

	private JLabel jLabelListOfEvents = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("ListEvents"));
	private JLabel jLabelQuery = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("Query"));
	private JLabel jLabelMinBet = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("MinimumBetPrice"));
	private JLabel jLabelEventDate = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("EventDate"));

	private JTextField jTextFieldQuery = new JTextField();
	private JTextField jTextFieldPrice = new JTextField();
	private JCalendar jCalendar = new JCalendar();
	private Calendar calendarAct = null;
	private Calendar calendarAnt = null;

	private JScrollPane scrollPaneEvents = new JScrollPane();

	private JButton jButtonCreate = new JButton(ResourceBundle.getBundle("Etiquetas").getString("CreateQuery"));
	//Cambios
	private JButton jButtonLogout = new JButton(ResourceBundle.getBundle("Etiquetas").getString("Logout"));
	private JLabel jLabelMsg = new JLabel();
	private JLabel jLabelError = new JLabel();
	
	//Cosas Nuevas
	private final JLabel jLabelQueries = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("Queries")); 
	private JScrollPane scrollPaneQueries = new JScrollPane();
	private JTable tableQueries = new JTable();
	private DefaultTableModel tableModelQueries;
	private Event selectedEvent;
	
	
	private String[] columnNamesEvents = new String[] {
			ResourceBundle.getBundle("Etiquetas").getString("EventN"), 
			ResourceBundle.getBundle("Etiquetas").getString("Event"), 

	};
	private String[] columnNamesQueries = new String[] {
			ResourceBundle.getBundle("Etiquetas").getString("QueryN"), 
			ResourceBundle.getBundle("Etiquetas").getString("Query")

	};
	
	private Vector<Date> datesWithEventsCurrentMonth = new Vector<Date>();
	private JTextField description;

	public CreateandFindQuestionGUI(Vector<domain.Event> v) {
		try {
			jbInit(v);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit(Vector<domain.Event> v) throws Exception {

		this.getContentPane().setLayout(null);
		this.setSize(new Dimension(604, 370));
		this.setTitle(ResourceBundle.getBundle("Etiquetas").getString("CreateQuery"));

		jComboBoxEvents.setModel(modelEvents);
		jComboBoxEvents.setBounds(new Rectangle(303, 58, 250, 20));
		jLabelListOfEvents.setBounds(new Rectangle(303, 37, 277, 20));
		jLabelQuery.setBounds(new Rectangle(25, 262, 75, 20));
		jTextFieldQuery.setBounds(new Rectangle(95, 262, 352, 20));
		jLabelMinBet.setBounds(new Rectangle(25, 293, 75, 20));
		jTextFieldPrice.setBounds(new Rectangle(95, 293, 60, 20));

		jCalendar.setBounds(new Rectangle(25, 31, 240, 153));
		scrollPaneEvents.setBounds(new Rectangle(25, 44, 346, 116));

		jButtonCreate.setBounds(new Rectangle(450, 271, 130, 30));
		jButtonCreate.setEnabled(false);

		jButtonCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonCreate_actionPerformed(e);
			}
		});
		jButtonLogout.setFont(new Font("Tahoma", Font.PLAIN, 9));
		jButtonLogout.setBounds(new Rectangle(505, 6, 75, 20));
		jButtonLogout.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jButtonLogout_actionPerformed(e);
			}
		});

		jLabelMsg.setBounds(new Rectangle(287, 164, 305, 20));
		jLabelMsg.setForeground(Color.red);
		// jLabelMsg.setSize(new Dimension(305, 20));

		jLabelError.setBounds(new Rectangle(175, 240, 305, 20));
		jLabelError.setForeground(Color.red);

		this.getContentPane().add(jLabelMsg, null);
		this.getContentPane().add(jLabelError, null);

		this.getContentPane().add(jButtonLogout, null);
		this.getContentPane().add(jButtonCreate, null);
		this.getContentPane().add(jTextFieldQuery, null);
		this.getContentPane().add(jLabelQuery, null);
		this.getContentPane().add(jTextFieldPrice, null);

		this.getContentPane().add(jLabelMinBet, null);
		this.getContentPane().add(jLabelListOfEvents, null);
		this.getContentPane().add(jComboBoxEvents, null);

		this.getContentPane().add(jCalendar, null);
		
		
		BLFacade facade = MainGUI.getBusinessLogic();
		datesWithEventsCurrentMonth=facade.getEventsMonth(jCalendar.getDate());
		paintDaysWithEvents(jCalendar,datesWithEventsCurrentMonth);
		
		

		jLabelEventDate.setBounds(new Rectangle(40, 15, 140, 25));
		jLabelEventDate.setBounds(34, 11, 140, 25);
		getContentPane().add(jLabelEventDate);

		
		// Code for JCalendar
		this.jCalendar.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent propertychangeevent) {
//				this.jCalendar.addPropertyChangeListener(new PropertyChangeListener() {
//					public void propertyChange(PropertyChangeEvent propertychangeevent) {
				if (propertychangeevent.getPropertyName().equals("locale")) {
					jCalendar.setLocale((Locale) propertychangeevent.getNewValue());
				} else if (propertychangeevent.getPropertyName().equals("calendar")) {
					calendarAnt = (Calendar) propertychangeevent.getOldValue();
					calendarAct = (Calendar) propertychangeevent.getNewValue();
					System.out.println("calendarAnt: "+calendarAnt.getTime());
					System.out.println("calendarAct: "+calendarAct.getTime());
					DateFormat dateformat1 = DateFormat.getDateInstance(1, jCalendar.getLocale());
					
					int monthAnt = calendarAnt.get(Calendar.MONTH);
					int monthAct = calendarAct.get(Calendar.MONTH);
					if (monthAct!=monthAnt) {
						if (monthAct==monthAnt+2) { 
							// Si en JCalendar estÃƒÂ¡ 30 de enero y se avanza al mes siguiente, devolverÃƒÂ­a 2 de marzo (se toma como equivalente a 30 de febrero)
							// Con este cÃƒÂ³digo se dejarÃƒÂ¡ como 1 de febrero en el JCalendar
							calendarAct.set(Calendar.MONTH, monthAnt+1);
							calendarAct.set(Calendar.DAY_OF_MONTH, 1);
						}
						
						jCalendar.setCalendar(calendarAct);
						
						BLFacade facade = MainGUI.getBusinessLogic();

						datesWithEventsCurrentMonth=facade.getEventsMonth(jCalendar.getDate());
					}



					paintDaysWithEvents(jCalendar,datesWithEventsCurrentMonth);

					//	Date firstDay = UtilDate.trim(new Date(jCalendar.getCalendar().getTime().getTime()));
					Date firstDay = UtilDate.trim(calendarAct.getTime());

					try {
						BLFacade facade = MainGUI.getBusinessLogic();

						Vector<domain.Event> events = facade.getEvents(firstDay);

						if (events.isEmpty())
							jLabelListOfEvents.setText(ResourceBundle.getBundle("Etiquetas").getString("NoEvents")
									+ ": " + dateformat1.format(calendarAct.getTime()));
						else
							jLabelListOfEvents.setText(ResourceBundle.getBundle("Etiquetas").getString("Events") + ": "
									+ dateformat1.format(calendarAct.getTime()));
						jComboBoxEvents.removeAllItems();
						System.out.println("Events " + events);

						for (domain.Event ev : events)
							modelEvents.addElement(ev);
						jComboBoxEvents.repaint();
						

						if (events.size() == 0)
							jButtonCreate.setEnabled(false);
						else
							jButtonCreate.setEnabled(true);

					} catch (Exception e1) {

						jLabelError.setText(e1.getMessage());
					}

				}
			}
		});

		
		//Agregado nuevo
		scrollPaneQueries.setBounds(525, 100, -243, 102);
		jLabelQueries.setBounds(287, 89, 236, 14);
		this.getContentPane().add(jLabelQueries);
		
		jComboBoxEvents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(!(jComboBoxEvents.getSelectedItem()==null)) {
					selectedEvent = (Event) jComboBoxEvents.getSelectedItem();
					
					if(tableQueries.isEnabled()==false) {
						
						scrollPaneQueries.setEnabled(false);
						scrollPaneQueries.setToolTipText("No available offer for that room type.");
					}else {
						
						scrollPaneQueries.setEnabled(true);
						scrollPaneQueries.setToolTipText("Book this offer: "+selectedEvent);
					}
					
					

				}
			}
		});
		
		scrollPaneQueries.setViewportView(tableQueries);
		tableModelQueries = new DefaultTableModel(null, columnNamesQueries);
		
		tableQueries.setModel(tableModelQueries);
		tableQueries.getColumnModel().getColumn(0).setPreferredWidth(25);
		tableQueries.getColumnModel().getColumn(1).setPreferredWidth(268);
		
		this.getContentPane().add(scrollPaneQueries, null);
		
		JButton createEvent = new JButton(ResourceBundle.getBundle("Etiquetas").getString("CreateandFindQuestionGUI.btnNewButton.text")); 
		createEvent.setBounds(450, 206, 130, 30);
		getContentPane().add(createEvent);
		
		createEvent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		
				jLabelError.setText("");
				jLabelMsg.setText("");

				// Displays an exception if the query field is empty
				Date date = jCalendar.getDate();

				String des = description.getText();
			
				if(des!=null & date!=null) {
					// Obtain the business logic from a StartWindow class (local or remote)
					BLFacade facade = MainGUI.getBusinessLogic();

					boolean a=facade.createEvent(des,date);
					if(a==false)jLabelError.setText("Error al crear el evento");
				}
				description.setText("");
				
			}
		});
		
		
		
		description = new JTextField();
		description.setBounds(new Rectangle(83, 262, 364, 20));
		description.setBounds(95, 209, 352, 20);
		getContentPane().add(description);
		
		JLabel lblNewLabel = new JLabel(ResourceBundle.getBundle("Etiquetas").getString("CreateandFindQuestionGUI.lblNewLabel.text")); //$NON-NLS-1$ //$NON-NLS-2$
		lblNewLabel.setBounds(25, 214, 75, 14);
		getContentPane().add(lblNewLabel);
		
		
	
		
		
		
	}

	
public static void paintDaysWithEvents(JCalendar jCalendar,Vector<Date> datesWithEventsCurrentMonth) {
		// For each day with events in current month, the background color for that day is changed.

		
		Calendar calendar = jCalendar.getCalendar();
		
		int month = calendar.get(Calendar.MONTH);
		int today=calendar.get(Calendar.DAY_OF_MONTH);
		int year=calendar.get(Calendar.YEAR);
		
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		int offset = calendar.get(Calendar.DAY_OF_WEEK);

		if (Locale.getDefault().equals(new Locale("es")))
			offset += 4;
		else
			offset += 5;
		
		
	 	for (Date d:datesWithEventsCurrentMonth){

	 		calendar.setTime(d);
	 		System.out.println(d);
	 		

			
			// Obtain the component of the day in the panel of the DayChooser of the
			// JCalendar.
			// The component is located after the decorator buttons of "Sun", "Mon",... or
			// "Lun", "Mar"...,
			// the empty days before day 1 of month, and all the days previous to each day.
			// That number of components is calculated with "offset" and is different in
			// English and Spanish
//			    		  Component o=(Component) jCalendar.getDayChooser().getDayPanel().getComponent(i+offset);; 
			Component o = (Component) jCalendar.getDayChooser().getDayPanel()
					.getComponent(calendar.get(Calendar.DAY_OF_MONTH) + offset);
			o.setBackground(Color.CYAN);
	 	}
	 	
 			calendar.set(Calendar.DAY_OF_MONTH, today);
	 		calendar.set(Calendar.MONTH, month);
	 		calendar.set(Calendar.YEAR, year);

	 	
	}
	



















	 
	private void jButtonCreate_actionPerformed(ActionEvent e) {
		domain.Event event = ((domain.Event) jComboBoxEvents.getSelectedItem());

		try {
			jLabelError.setText("");
			jLabelMsg.setText("");

			// Displays an exception if the query field is empty
			String inputQuery = jTextFieldQuery.getText();

			if (inputQuery.length() > 0) {

				// It could be to trigger an exception if the introduced string is not a number
				float inputPrice = Float.parseFloat(jTextFieldPrice.getText());

				if (inputPrice <= 0)
					jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorNumber"));
				else {

					// Obtain the business logic from a StartWindow class (local or remote)
					BLFacade facade = MainGUI.getBusinessLogic();

					facade.createQuestion(event, inputQuery, inputPrice);

					jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("QueryCreated"));
				}
			} else
				jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorQuery"));
		} catch (EventFinished e1) {
			jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorEventHasFinished") + ": "
					+ event.getDescription());
		} catch (QuestionAlreadyExist e1) {
			jLabelMsg.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorQueryAlreadyExist"));
		} catch (java.lang.NumberFormatException e1) {
			jLabelError.setText(ResourceBundle.getBundle("Etiquetas").getString("ErrorNumber"));
		} catch (Exception e1) {

			e1.printStackTrace();

		}
	}

	private void jButtonLogout_actionPerformed(ActionEvent e) {
		this.setVisible(false);
	}
}
