package com.hit.server;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import com.hit.service.MyController;

public class Server implements PropertyChangeListener,Runnable {

	private ServerSocket server;
	private int port;
	private boolean running = false;
	private String userInput;
	private Executor executor;
	private MyController controller;


	public Server(int port) {
		this.port = port;
		controller = new MyController();

	}

	public void startServer() {
		try {
			this.server = new ServerSocket(port);
			new Thread(this).start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void stopServer() {
		running = false;
		try {
			server.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		running = true;
		executor = Executors.newFixedThreadPool(4);
		while (running) {

			try {
				Socket socket = server.accept();
				//use executor for multiple thread
				executor.execute(new HandleRequest(socket, controller));
			} catch(SocketException e ) {}
			catch (IOException e) {

				e.printStackTrace();

			}

		}

		try {
			server.close();
			((ExecutorService) executor).shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		userInput = evt.getNewValue().toString();
		if (userInput.equals("START")) {
			if(running) {System.out.println("Server is already running");}
			else {
				System.out.println("Starting server...");
				startServer();
				System.out.println("Server ON");
			}

		} else if (userInput.equals("STOP")) {
			if (!running) {System.out.println("Server is already off");}	
			else {
				System.out.println("Server ShutDown...");
				stopServer();
			}

		}

	}

}
