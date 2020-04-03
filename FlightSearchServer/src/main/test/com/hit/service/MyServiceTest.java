package com.hit.service;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.hit.algorithm.BFSAlgoGraphImp;
import com.hit.algorithm.DijkstraAlgoGraphImp;
import com.hit.algorithm.Node;
import com.hit.dao.DaoFileImpl;
import com.hit.dm.Country;
import com.hit.dm.Flight;

public class MyServiceTest {
	@Test
	public void testGraph() {
		System.out.println("trying test by build the db file");
		MyService testGraph = new MyService(new BFSAlgoGraphImp<>(),new DaoFileImpl("src/main/resources/DataSource.txt"));

		
		
		Country a = (new Country("israel"));
		Country b = (new Country("brazil"));
		Country c = (new Country("greece"));
		Country d = (new Country("germany"));
		Country e = (new Country("united state"));
		Country f = (new Country("united kingdom"));
		Country g = (new Country("egypt"));
		Country h = (new Country("russia"));
		Country i = (new Country("norway"));
		testGraph.addFlight(a.getName(), b.getName(), 4.0);
		testGraph.addFlight(b.getName(), c.getName(), 11.0);
		testGraph.addFlight(b.getName(), d.getName(), 9.0);
		testGraph.addFlight(c.getName(), a.getName(), 8.0);
		testGraph.addFlight(d.getName(), c.getName(), 7.0);
		testGraph.addFlight(d.getName(), e.getName(), 2.0);
		testGraph.addFlight(d.getName(), f.getName(), 6.0);
		testGraph.addFlight(e.getName(), b.getName(), 8.0);
		testGraph.addFlight(e.getName(), g.getName(), 7.0);
		testGraph.addFlight(e.getName(), h.getName(), 4.0);
		testGraph.addFlight(f.getName(), c.getName(), 1.0);
		testGraph.addFlight(f.getName(), e.getName(), 5.0);
		testGraph.addFlight(g.getName(), h.getName(), 14.0);
		testGraph.addFlight(g.getName(), i.getName(), 9.0);
		testGraph.addFlight(h.getName(), f.getName(), 2.0);
		testGraph.addFlight(h.getName(), i.getName(), 10.0);


		ArrayList<Node<Country>> excpected = new ArrayList<>();
		// [i, g, e, d, b, a]
		excpected.add(new Node<>(a));
		excpected.add(new Node<>(b));
		excpected.add(new Node<>(d));
		excpected.add(new Node<>(e));
		excpected.add(new Node<>(g));
		excpected.add(new Node<>(i));

		// seachTest bfs
		testGraph.buildGraphDB();

		LinkedHashMap<Node<Country>, Double> res = testGraph.searchPathGraph(a.getName(), i.getName());
		
		Set<Node<Country>> result = res.keySet();

		System.out.println("result path: " + result);
		System.out.println("excpected path: " + excpected);
		
		assertTrue(result.toString().equals(excpected.toString()));
		assertEquals(result.toString(),(excpected.toString()));



	}

	@Test
	public void testDaoMethods(){
		MyService testGraph = new MyService(new BFSAlgoGraphImp<Country>(),new DaoFileImpl("src/main/resources/DataSource.txt"));

		Country a = new Country("china");
		Country b = new Country("israel");
		a.addFlights(new Flight(a, b, 4.0));
		testGraph.addEdgeGraph(a, b, 4.0);

		testGraph.save(a);

		assertEquals(testGraph.find("china").getName(), a.getName());//when you read from file its a new object
		assertEquals(testGraph.find("china").getFlights().toString(), a.getFlights().toString());
		assertEquals(testGraph.find("china").getFlights().get(0).getDest(), "israel");

		b = testGraph.find("china");
		assertEquals(b.getName(), a.getName());
		
		testGraph.deleteFlight("china", "israel");
		assertTrue(testGraph.find("china").getFlights().isEmpty());
		
		testGraph.delete(a);
		assertEquals(testGraph.find("china"), null);

		
	}
	
	@Test
	public void testGraphWithDB() {
		System.out.println("trying test from DB file");
		MyService testGraph = new MyService(new BFSAlgoGraphImp<>(),new DaoFileImpl("src/main/resources/DataSource.txt"));


		ArrayList<String> excpected = new ArrayList<>();
		excpected.add("israel");
		excpected.add("brazil");
		excpected.add("germany");
		excpected.add("united state");
		excpected.add("egypt");
		excpected.add("norway");
		
		// seachTest bfs
		testGraph.buildGraphDB();
		LinkedHashMap<Node<Country>, Double> res = testGraph.searchPathGraph("israel", "norway");
		
		Set<Node<Country>> result = res.keySet();

		System.out.println("result path: " + result);
		System.out.println("excpected path: " + excpected);
		
		assertTrue(result.toString().equals(excpected.toString()));
		assertEquals(result.toString(),(excpected.toString()));
		
		//try DIJ
		testGraph.setFlightService(new DijkstraAlgoGraphImp<>());
		LinkedHashMap<Node<Country>, Double> res2 = testGraph.searchPathGraph("israel", "norway");
		
		
		Map<String,Double> excpected2 = new LinkedHashMap<>();
		excpected2.put("israel", 0.0);
		excpected2.put("brazil", 4.0);
		excpected2.put("germany", 13.0);
		excpected2.put("united state", 15.0);
		excpected2.put("russia", 19.0);
		excpected2.put("norway", 29.0);
		System.out.println("dij path:\n" + "" +res2.toString());
		System.out.println(excpected2);
		assertEquals(excpected2.toString(), res2.toString());			
		
	}
	
	
}
