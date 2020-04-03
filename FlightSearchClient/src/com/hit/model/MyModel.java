package com.hit.model;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

public class MyModel extends Observable implements IModel {

	private Client client;

	public MyModel(int port) {
		this.client = new Client(port); //create client
	}

	
	/*
	 * helper function to build request object as server expected, used by controller
	 */
	public void requestBuilder(String action, String src, String dest,String price) {
		JsonObject jobject = new JsonObject();

		JsonObject jobject2 = new JsonObject();
		jobject2.addProperty("action", action);
		
		// putting headers to JSONObject
		jobject.add("headers", new Gson().toJsonTree(jobject2));
		
		// body data //
		Map<Object, Object> mm = new LinkedHashMap<>(2);
		mm.put("src", src);
		mm.put("dest", dest);
		mm.put("price",price);
		
		// putting body to JSONObject
		jobject.add("body", new Gson().toJsonTree(mm));
		responseReader(jobject.toString());

	}

	/*
	 * helper function to read the response from the server
	 * TYPE= send type of graph data (dij/bfs)
	 */
	public void responseReader(String jobject) {
		String response = null;
		response = client.send(jobject); //SEND REQUEST FOR SERVER AND WAIT FOR RESPONSE
		Type ref = new TypeToken<Map<String, String>>() {}.getType();
		Map<String, String> res = new Gson().fromJson(response, ref);
		//check if the request action succeeded
		if (res.get("STATUS").equals("Succeeded")) {
			switch (res.get("ACTION").toUpperCase()) {
			case "GRAPHTYPEDIJ":
			case "GRAPHTYPEBFS":
			case "BUILD GRAPH":
				break;

			case "LOAD":
				readFlightsData(res.get("DATA"));
				break;

			case "SEARCH PATH":
				readJsonPath(res.get("DATA"),res.get("TYPE"));
				break;

			case "GETCOUNTRY":
				readCountryDB(res.get("DATA"),res.get("TYPE"));
				break;
				
			case "DELETE FLIGHT":
				responseBuilder("DELETE FLIGHT","SUCCSES",null,null);
				break;
				
			case "ADD FLIGHT":
				responseBuilder("ADD FLIGHT","SUCCSES",null,null);
				break;
				
			default:
				System.out.println("Something wrong");
				break;
			}
		} else {
			//failed of search path need special take care so i separated it
			responseBuilder(res.get("ACTION"),"Failed",null,null);
			System.out.println("Status probably failed , check: " + res.get("STATUS"));
		}

	}

	/*
	 * parse the response json object of country DB 
	 */
	private void readCountryDB(String json,String type) {
		Type ref = new TypeToken<Set<String>>() {}.getType();
		Set<String> countryDB =  new Gson().fromJson(json, ref);
		if(countryDB!=null)
		{
			responseBuilder("GETCOUNTRY","SUCCSES",countryDB,type);
		}
		else {
			responseBuilder("GETCOUNTRY","FAILED",null,null);//in case countrydb went wrong
		}
	}

	/*
	 * parse the response json object of flight DB 
	 */
	private void readFlightsData(String json) {
		Type ref = new TypeToken<LinkedHashMap<String, LinkedList<Flights>>>() {}.getType();
		LinkedHashMap<String, LinkedList<Flights>> flightsDB = new Gson().fromJson(json, ref);
		if (flightsDB != null) {
			LinkedList<String[]> flights = new LinkedList<>();
			for (String key : flightsDB.keySet()) {
				for (Flights data : flightsDB.get(key)) {
					flights.add(new String[] { data.getSrc(), data.getDest(), data.getPrice().toString() });
				}

			}
			responseBuilder("LOAD","SUCCSES",flights,null);

		} else {
			responseBuilder("LOAD","FAILED",null,null);//in case flights object build went wrong
		}
	}

	/*
	 * parse the response json object of path search
	 */
	private void readJsonPath(String json,String type) {
		Type ref = new TypeToken<LinkedHashMap<String, Double>>() {}.getType();
		LinkedHashMap<String, Double> path = new Gson().fromJson(json, ref); 
		if(path!=null)
		{
			responseBuilder("SEARCH PATH","SUCCSES",path,type);
		}
		else {
			responseBuilder("SEARCH PATH","FAILED",null,null);
			
		}
	}

	/*
	 * build response for controller 
	 */
	private void responseBuilder(String action , String status, Object data, Object type)
	{
		Map<String, Object> res = new HashMap<>();
		res.put("ACTION", action);
		res.put("STATUS", status);
		res.put("DATA", data);
		res.put("TYPE", type);
		setChanged();
		notifyObservers(res);
	}
	
	
	/*
	 *helper private class to help parse a flights db as needed
	 */
	private class Flights {
		String src;
		String dest;
		Double price;

		public String getSrc() {
			return src;
		}

		public String getDest() {
			return dest;
		}

		public Double getPrice() {
			return price;
		}

	}
}
