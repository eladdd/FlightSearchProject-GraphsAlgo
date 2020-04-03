package com.hit.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Set;

import com.hit.algorithm.BFSAlgoGraphImp;
import com.hit.algorithm.DijkstraAlgoGraphImp;
import com.hit.algorithm.IAlgoGraph;
import com.hit.algorithm.Node;
import com.hit.dao.DaoFileImpl;
import com.hit.dm.Country;
import com.hit.dm.Flight;
import com.hit.server.Request;

public class MyController {
	private MyService service;




	public MyController() {

		this.service = new MyService(new DijkstraAlgoGraphImp<>(),
				new DaoFileImpl("src/main/resources/DataSource.txt"));
	}
	
	
	public IAlgoGraph<Country> getGraphType() {
		return service.getFlightService();
	}
	
	public Boolean graphTypeDIJ() {
		return this.service.setFlightService(new DijkstraAlgoGraphImp<>());
	}

	public Boolean graphTypeBFS() {
		return this.service.setFlightService(new BFSAlgoGraphImp<>());
	}

	public Boolean addEntity(Request req) {
		System.out.println("controller --saving data");
		return this.service.addFlight(req.getBodySrc(), req.getBodyDest(), req.getBodyPrice());
	}
	

	public Country getEntity(Request req) {
		System.out.println("controller --retriving data");
		return service.find(req.getBodySrc());
	}

	public void deleteEntity(Request req) {
		System.out.println("controller --deleting");
		Country del = service.find(req.getBodySrc());
		this.service.delete(del);
	}

	public boolean deleteFlight(Request req) {
		System.out.println("controller --delete flight");
		return this.service.deleteFlight(req.getBodySrc(), req.getBodyDest());
	}
	
	public LinkedHashMap<Node<Country>, Double> searchFlight(Request req) {
		System.out.println("controller --get path");
		return service.searchPathGraph(req.getBodySrc(), req.getBodyDest());
	}

	public Boolean buildGraph() {
		System.out.println("controller --build graph");
		return this.service.buildGraphDB();
	}

	public LinkedHashMap<String, LinkedList<Flight>> getFlightsData() {
		System.out.println("controller --get flights db");
		return this.service.getFlightsDB();

	}

	public Set<Node<Country>> getCountryData() {
		System.out.println("controller --get country db");
		return this.service.getGraphNodes();
	}

}
