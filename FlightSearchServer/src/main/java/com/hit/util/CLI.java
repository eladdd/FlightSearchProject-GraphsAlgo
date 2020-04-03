package com.hit.util;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;



public class CLI implements Runnable {
	private Scanner scanner;
	private String userInput;
	private PropertyChangeSupport pcs;
	private Boolean serverIsRunning;

	public CLI(InputStream in, OutputStream out) {
		this.scanner = new Scanner(in);
		pcs = new PropertyChangeSupport(this);
		serverIsRunning = false;
	}

	@Override
	public void run() {
		userInput = "";
		System.out.println("Please enter your command START->to start , STOP - > to close(exit to quit):");
		while (!userInput.equalsIgnoreCase("EXIT")) {
			userInput = scanner.nextLine().trim().toUpperCase();
			if (userInput.equals("START")) {
				if (!serverIsRunning) {
					pcs.firePropertyChange(userInput, null, "START");
					serverIsRunning = true;
				} else {
					System.out.println("server is already running");
				}
			} else if (userInput.equals("STOP")) {

				if (serverIsRunning) {
					pcs.firePropertyChange(userInput, null, "STOP");
					serverIsRunning = false;
				} else {
					System.out.println("server is not running");
				}

			}
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener pcl) {
		this.pcs.addPropertyChangeListener(pcl);
	}

	public void removePropertyChangeListener(PropertyChangeListener pcl) {
		this.pcs.removePropertyChangeListener(pcl);
	}

}
