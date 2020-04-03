package com.hit.model;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
	private int port;

	public Client(int port) {
		this.port = port;
	}

    public String send(String request){
        String response = "";
        Socket socket = null;
        try {
            socket = new Socket("localhost", port);
            try (PrintWriter out =new PrintWriter(new OutputStreamWriter(socket.getOutputStream()))) {
            	try (Scanner reader = new Scanner(new InputStreamReader(socket.getInputStream()))) {
            		out.println(request);
            		out.flush();
            		if(response!=null)
            		{
            			response = reader.nextLine();
            		}
            			
            	}
            }
            
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response;
    }
}
