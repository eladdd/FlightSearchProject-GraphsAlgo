package com.hit.server;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.hit.algorithm.DijkstraAlgoGraphImp;
import com.hit.algorithm.Node;
import com.hit.dm.Country;
import com.hit.dm.Flight;
import com.hit.service.MyController;

public class HandleRequest implements Runnable {
	private Socket socket;
	private MyController controller;

	public HandleRequest(Socket socket, MyController controller) {
		this.socket = socket;
		this.controller = controller;
	}

	@Override
	public void run() {
		try {

			Scanner reader = new Scanner(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			Map<String, String> res = readAction(readRequest(reader));


			out.println(new Gson().toJson(res));
			out.flush();

			reader.close();
			out.close();

		} catch (JsonSyntaxException | JsonIOException | IOException e) {
			e.printStackTrace();
		} finally {

			try {
				this.socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println("Connection closed");
		}

	}

	/*
	 * parse the request object from json
	 */
	public Request readRequest(Scanner reader) {
		String json = reader.nextLine();
		Type ref = new TypeToken<Request>(){}.getType();
		return new Gson().fromJson(json.toString(), ref);// instead ref can just request.class
	}

	/*
	 * read the request object from the client and navigate the controller, waiting for the response and send it the response builder
	 */
	public Map<String, String> readAction(Request req) {
		Map<String, String> res = new HashMap<>();
		String status;


		switch (req.getHeaders().get("action")) {
		case "graphTypeDIJ":
			status = controller.graphTypeDIJ() ? "Succeeded" : "Failed";
			ResponseBuilder(res,"graphTypeDIJ",status, null,null);
			break;

		case "graphTypeBFS":
			status = controller.graphTypeBFS() ? "Succeeded" : "Failed";
			ResponseBuilder(res,"graphTypeBFS",status, null,null);
			break;
			
		case "DELETE FLIGHT":
			status = controller.deleteFlight(req)  ? "Succeeded" : "Failed";
			ResponseBuilder(res,"DELETE FLIGHT",status, null,null);
			break;
			
		case "UPDATE":
		case "ADD FLIGHT":
			status = controller.addEntity(req) ? "Succeeded" : "Failed";
			ResponseBuilder(res,"ADD FLIGHT",status, null,null);
			break;
					
		case "DELETE":
			controller.deleteEntity(req);
			break;

		case "SEARCH PATH":
			LinkedHashMap<Node<Country>, Double> path = controller.searchFlight(req);
			if (path != null) {
				String type = controller.getGraphType().getClass().equals(DijkstraAlgoGraphImp.class) ? "DIJ" : "BFS";
				ResponseBuilder(res,"SEARCH PATH","Succeeded", new Gson().toJson(path),type);

			} else {
				ResponseBuilder(res,"SEARCH PATH","Failed", null,null);
			}

			break;

		case "BUILD GRAPH":
			status = controller.buildGraph() ? "Succeeded" : "Failed";
			ResponseBuilder(res,"Build Graph",status,null,null);

			break;

		case "LOAD":
			LinkedHashMap<String, LinkedList<Flight>> flightsDB = controller.getFlightsData();
			if (flightsDB != null) {
				ResponseBuilder(res,"LOAD","Succeeded",new Gson().toJson(flightsDB),null);
			} else {
				ResponseBuilder(res,"LOAD","Failed",null,null);
			}
			break;

		case "GETCOUNTRY":
			Set<Node<Country>> c = controller.getCountryData();

			if (c != null) {
				Set<String> CountryDB = new HashSet<>();
				for (Node<Country> node : c) {
					CountryDB.add(node.getName().getName());
				}
				String type = controller.getGraphType().getClass().equals(DijkstraAlgoGraphImp.class) ? "DIJ" : "BFS";
				ResponseBuilder(res,"GETCOUNTRY","Succeeded",new Gson().toJson(CountryDB),type);
			} else {
				ResponseBuilder(res,"GETCOUNTRY","Failed",null,null);
			}

			break;
		default:
			break;
		}

		return res;

	}
	
	/*
	 * avoid duplicate code, its build the response Map with requires variables, 
	 *	type used to know if its response to bfs/dij panel on client
	 */
	private void ResponseBuilder(Map<String, String> res,String action, String status,String data,String type) {
		res.put("ACTION",action);
		res.put("STATUS",status);
		res.put("DATA",data);
		res.put("TYPE", type);
		
	}
}
