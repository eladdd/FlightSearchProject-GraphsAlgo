package com.hit.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.EventQueue;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Observable;
import java.util.Set;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MyView extends Observable implements IView {

	private JFrame frame;
	private StartPanel startPanel;
	private SearchBFS bfsPanel;
	private SearchDIJ dijPanel;

	/**
	 * Launch the application.
	 * 
	 * @wbp.parser.entryPoint //used this to bypass the windows builder constructor structure
	 */
	public void start() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					initialize();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the frame.
	 * I use here card panel to allow smooth move between panels
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 670, 500);
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		JPanel mainPanel = new JPanel(new BorderLayout());
		CardLayout cardLayout = new CardLayout();
		GlobalPanel globalPanel = new GlobalPanel(); // *
		JPanel cardPanel = new JPanel(cardLayout);
		startPanel = new StartPanel(cardPanel, this);
		bfsPanel = new SearchBFS(cardPanel, this);
		dijPanel = new SearchDIJ(cardPanel, this);

		// add panels to cardPanel
		cardPanel.add(startPanel, "start");
		cardPanel.add(bfsPanel, "Shortest Flights");
		cardPanel.add(dijPanel, "Cheapest Flights");

		// instruct the layout which panel to show
		mainPanel.add(cardPanel);
		mainPanel.add(globalPanel, "North");
		frame.setTitle("Flight Search Project");
		frame.getContentPane().add(mainPanel); // *
		frame.setVisible(true);
		updateData(new String[] { "BUILD GRAPH", null, null }); // build graph require to load data table (build graph get also the db fro mserver)
	}

	/*
	 * helper function to call observable functions from each panel with generic data
	 * */
	public <T> void updateData(T t) {
		setChanged();
		notifyObservers(t);
	}

	/*
	 * boolean helper function to check the response from the controller
	 **/
	public Boolean checkResponse(String status) {
		if (status.toUpperCase().equals("FAILED")) {
			JOptionPane.showMessageDialog(null, "Action Failed");
			return false;
		}
		return true;
	}
	
	
	/* 
	 *  check is db is empty when user click "load data" , checking the status and calling the loaddatatable function in startpanel
	 */
	public void flightsData(LinkedList<String[]> flights, String status) {
		if (flights.isEmpty())
		{
			JOptionPane.showMessageDialog(null, "empty DB");
		}
		if(checkResponse(status))
		{
			startPanel.loadDataTableFlights(flights);
		}

	}

	/*this method called by controller after model units receive countries list from DB
	 *and initialize the current comboBox (from dij/bfs panel) this method avoid  duplicate code
	 * */
	 
	public void countryData(Set<String> countryList, String status,String type) {
		if (checkResponse(status))
		{
			JComboBox<String> comboBoxSrc;
			JComboBox<String> comboBoxDest;
			if(type!=null)
			{
				comboBoxSrc = type.equals("BFS")?bfsPanel.comboBoxSrc:dijPanel.comboBoxSrc;
				comboBoxDest = type.equals("BFS")?bfsPanel.comboBoxDest:dijPanel.comboBoxDest;
				comboBoxSrc.removeAllItems();
				comboBoxDest.removeAllItems();
				comboBoxSrc.addItem("n/a");
				comboBoxDest.addItem("n/a");
				for (String country:countryList)
				{
					comboBoxSrc.addItem(country);
					comboBoxDest.addItem(country);
				}
			}
		}

	}

	/*this method called from dij/bfs panel after user clicked on search button
	 *its check for current input and if its valid move on to the controller
	 *this function avoid duplicate code on bfs/dij panels
	 * */
	public void findMyPath(String src, String dest) {
		if (src.equals("n/a") || dest.equals("n/a")) {
			JOptionPane.showMessageDialog(null, "Make sure you choose source country and destination, try again");
		} else if (src.equals(dest)) {
			JOptionPane.showMessageDialog(null, "Oops.. cant fly to the same country src, try again");
		} else {
			updateData(new String[] {"SEARCH PATH",src,dest});
		}
	}
	
	
	

	/*
	 * helper function to navigate the current panel(Bfs/dij) function and check action status
	 */
	public void displaySearch(LinkedHashMap<String, Double> path, String status, String type) {
		if (status.equals("SUCCSES"))
		{
			if(type.equals("BFS"))
			{
				LinkedList<String> keys = new LinkedList<>(path.keySet());
				bfsPanel.showResult(keys);
			}
			else //DIJ
			{
				dijPanel.showResult(path);
			}
		}
		else {
			JOptionPane.showMessageDialog(null, "Your request failed,no such flight");
		}
		
		
	}
	
	/*
	 * check status if delete flight(admin premission) sucsses
	 */
	public void actionsFlight(String status) {
		if (status.equals("SUCCSES"))
		{
			JOptionPane.showMessageDialog(null, "Your Request succsesfully");
		}
		else {
			JOptionPane.showMessageDialog(null, "Your request failed");
		}
		
	}

}
