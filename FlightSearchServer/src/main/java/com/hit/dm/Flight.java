package com.hit.dm;

import java.io.Serializable;



public class Flight implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String src;
	private String dest;
	private Double price;
	
	public Flight(Country src, Country dest, double price) {
		this.src = src.getName();
		this.dest = dest.getName();
		this.price = price;
		
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getDest() {
		return dest;
	}

	public void setDest(String dest) {
		this.dest = dest;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Override
	public String toString() {
		return "Flight [src=" + src + ", dest=" + dest + ", price=" + price + "]";
	}
	

}
