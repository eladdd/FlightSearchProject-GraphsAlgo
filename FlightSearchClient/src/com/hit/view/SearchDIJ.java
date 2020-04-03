package com.hit.view;

import java.awt.CardLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;


public class SearchDIJ extends JPanel {

	/**
	 * this jpanel used for search flights by price, cheapest flights
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Create the panel.
	 */
	
	protected JComboBox<String> comboBoxSrc; //protected to allow myview use it for update country list
	protected JComboBox<String> comboBoxDest;
	private JTextArea textArea;
	private JLabel lblResultBFS;
	private JLabel lblTotalFlights;
	
	public SearchDIJ(JPanel cardPanel, MyView myview) {
		setLayout(null);
		this.setBounds(100, 100, 670, 500);

		JButton btnBack = new JButton("");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				//reset comboBox select
				comboBoxDest.setSelectedIndex(0);
				comboBoxSrc.setSelectedIndex(0);
				//set the visibility of the result to false(display preferences)
				textArea.setVisible(false);
				lblResultBFS.setVisible(false);
				lblTotalFlights.setVisible(false);
				((CardLayout) cardPanel.getLayout()).show(cardPanel, "start");
				
			}
		});
		btnBack.setIcon(new ImageIcon(SearchDIJ.class.getResource("/com/hit/resources/back.png")));
		btnBack.setBounds(543, 378, 100, 41);
		add(btnBack);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				myview.findMyPath(comboBoxSrc.getSelectedItem().toString(),comboBoxDest.getSelectedItem().toString());
			}

		});
		btnSearch.setIcon(new ImageIcon(SearchDIJ.class.getResource("/com/hit/resources/search.png")));
		btnSearch.setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.ITALIC, 14));
		btnSearch.setBounds(385, 90, 102, 27);
		add(btnSearch);
		
		
		//labels//
		JLabel lblSrc = new JLabel("Source Country");
		lblSrc.setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.PLAIN, 15));
		lblSrc.setBounds(55, 55, 119, 20);
		add(lblSrc);
		
		JLabel lblDest = new JLabel("Destination Country");
		lblDest.setFont(new Font("Tw Cen MT Condensed Extra Bold", Font.PLAIN, 15));
		lblDest.setBounds(234, 55, 119, 20);
		add(lblDest);
		
		//combox-windows builder use old java so to use generic (for see it on design tab) i create my own combo box model//
		DefaultComboBoxModel<String> comboModel = new DefaultComboBoxModel<String>(new String[] { "n/a" });
		DefaultComboBoxModel<String> comboModel2 = new DefaultComboBoxModel<String>(new String[] { "n/a" });
		
		comboBoxSrc = new JComboBox<String>(comboModel);
		comboBoxSrc.setBounds(55, 94, 119, 20);
		add(comboBoxSrc);
		
		comboBoxDest = new JComboBox<String>(comboModel2);
		comboBoxDest.setBounds(234, 94, 119, 20);
		add(comboBoxDest);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Courier New", Font.BOLD, 14));
		textArea.setBounds(55, 229, 396, 149);
		textArea.setOpaque(false);
		textArea.setVisible(false);	
		add(textArea);
		
		lblResultBFS = new JLabel("Results:");
		lblResultBFS.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblResultBFS.setBounds(55, 125, 69, 35);
		lblResultBFS.setVisible(false);
		add(lblResultBFS);
		
		lblTotalFlights = new JLabel();
		lblTotalFlights.setFont(new Font("Times New Roman", Font.BOLD, 18));
		lblTotalFlights.setBounds(55, 161, 432, 35);
		lblTotalFlights.setVisible(false);
		add(lblTotalFlights);
		
		JLabel lblBackGround = new JLabel("");
		lblBackGround.setIcon(new ImageIcon(StartPanel.class.getResource("/com/hit/resources/background1.jpg")));
		lblBackGround.setBounds(0, 0, 670, 500);
		add(lblBackGround);
	}
	
	/*
	 * Function to show up the result of the path user search, its show the quantity of flights with total price
	 *  and the flights themselves with their price
	 */
	public void showResult(LinkedHashMap<String, Double> path) {
		StringBuilder flights = new StringBuilder(); // use string builder to allow me change the string
		int count = 0; //count flights
		Double price=0.0;
		Double totalPrice = 0.0;
		String lastCountry="";
		for(String key:path.keySet())
		{
			if(count>=1) // start counting after src country
			{
				price=path.get(key)-price;
				totalPrice+=price;
				flights.append(lastCountry+" -> "+key+ " Price: "+price+ "\n");
			}
			lastCountry = key;
			count++;
		}
		textArea.setText(flights.toString());
		lblTotalFlights.setText("Number Of Flights: "+ (count-1) + "  Total Price: " +totalPrice);
		//set the visibility of the results to true
		textArea.setVisible(true);
		lblResultBFS.setVisible(true);
		lblTotalFlights.setVisible(true);
	}
	


}
