package com.hit.dm;

import java.io.Serializable;
import java.util.LinkedList;



public class Country implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String countryName;
	private LinkedList<Flight> flights;

	public Country(String name) {
		//super(name);
		this.countryName = name;
		flights = new LinkedList<>();
	}

	public LinkedList<Flight> getFlights() {
		return flights;
	}

	public void setFlights(LinkedList<Flight> flights) {
		this.flights = flights;
	}
	
	public void addFlights(Flight flight) {
		for (Flight fli : flights) {
			if (flight.getSrc().equals(fli.getSrc()) && flight.getDest().equals(fli.getDest()))
			
			{
				fli.setPrice(flight.getPrice());
				return;
				// avoid adding duplicate edges so update the price to new one
			}
		}
		this.flights.add(flight);

	}

	public String getName() {
		return countryName;
	}

	public void setName(String name) {
		this.countryName = name;
	}
	@Override
	public String toString() {
		return countryName.toString();
	}

}
