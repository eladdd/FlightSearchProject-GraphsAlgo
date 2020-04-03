package com.hit.service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import com.hit.algorithm.IAlgoGraph;
import com.hit.algorithm.Node;
import com.hit.dao.DaoFileImpl;
import com.hit.dao.IDao;
import com.hit.dm.Country;
import com.hit.dm.Flight;

public class MyService {
	private IAlgoGraph<Country> flightService;
	private IDao<String, Country> service;

	public MyService(IAlgoGraph<Country> flightService, IDao<String, Country> dao) {
		this.flightService = flightService;
		this.service = dao;
	}

	
	
	public IAlgoGraph<Country> getFlightService() {
		return flightService;
	}

	public Set<Node<Country>> getGraphNodes() {
		return this.flightService.getGraphNodes();
	}

	public Node<Country> getNode(Country obj) {
		return flightService.getNode(obj);
	}


	public void setService(IDao<String, Country> service) {
		this.service = service;
	}

	public void save(Country entity) {
		this.service.save(entity);

	}

	public void delete(Country entity) {
		this.service.delete(entity);

	}
	
	public Country find(String name) {
		return this.service.find(name);
	}


	public Boolean buildGraphDB() {
		// build the graph and search
		try {
			Map<String, Country> daoMap = (Map<String, Country>) (((DaoFileImpl) this.service).getDaoMap());
			if (daoMap != null) {
				for (Country t : daoMap.values()) {
					for (Flight tt : t.getFlights()) {
						addEdgeGraph((service.find(tt.getSrc())), (service.find(tt.getDest())), tt.getPrice());
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;	
	}

	//part of building the graph, adding the db of flights as edge 
	public void addEdgeGraph(Country src, Country dest, double price) {

		Node<Country> a = this.flightService.getNode(src);
		Node<Country> b = this.flightService.getNode(dest);
		this.flightService.addEdge(a == null ? new Node<Country>(src) : a, b == null ? new Node<Country>(dest) : b,
				price);
	}

	public LinkedHashMap<Node<Country>, Double> searchPathGraph(String src, String dest) {

		LinkedHashMap<Node<Country>, Double> res = this.flightService.searchPath(
				flightService.getNode(this.service.find(src)), flightService.getNode(this.service.find(dest)));
		return res;
	}

	//add flight to DB
	public boolean addFlight(String src, String dest, double price) {
		try {
			Country a = this.service.find(src) == null ? new Country(src) : this.service.find(src);
			Country b = this.service.find(dest) == null ? new Country(dest) : this.service.find(dest);
			a.addFlights(new Flight(a, b, price));
			this.service.save(a);
			this.service.save(b);
		} catch (Exception e) {
			return false;
		}
		return true;

	}

	// change from bfs search to dij search
	public boolean setFlightService(IAlgoGraph<Country> flightService) {
		try {
			this.flightService = flightService;
			this.buildGraphDB(); // rebuild for the new service
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public boolean deleteFlight(String src, String dest) {
		try {
			this.service.deleteFlight(src, dest);
		} catch (Exception e) {
			return false;
		}
		return true;

	}


	public LinkedHashMap<String, LinkedList<Flight>> getFlightsDB() {
		Map<String, Country> daoMap = (Map<String, Country>) (((DaoFileImpl) this.service).getDaoMap());

		LinkedHashMap<String, LinkedList<Flight>> data = new LinkedHashMap<String, LinkedList<Flight>>();
		//in case no db
		try {
			for (String key : daoMap.keySet()) {
				// parse flight db for client, take it from the dao Map object.
				data.put(key, daoMap.get(key).getFlights());
			}
		} catch (NullPointerException e) {
			data=null;
		}


		return data;

	}

}
