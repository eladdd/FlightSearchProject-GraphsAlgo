package com.hit.view;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;;


public class GlobalPanel extends JPanel {
/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel lblDate;

	/*
	 * create Date panel (bar that show the current date and time)
	 */
	public GlobalPanel() {
		lblDate = new JLabel();
		lblDate.setText(currentDate());
		add(lblDate);
		
		ActionListener clockListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				lblDate.setText(currentDate());
				
			}
		};
		new Timer(1000, clockListener).start(); // update in real time

	}
	
	
	//show the current date,time in the top of the jframe
	public String currentDate() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss:a          EEE, d MMM, yyyy",Locale.US);
		String formatDateTime = now.format(formatter);
		return formatDateTime;
	}

}
