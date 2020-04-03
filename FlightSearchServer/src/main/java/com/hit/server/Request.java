package com.hit.server;

import java.io.Serializable;
import java.util.Map;



public class Request implements Serializable {
	/**
	 * Request pattern from client side
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	private Map<String, String> headers;
	private Body body;
	
	
	public Request(Map<String, String> headers,String src, String dest,double price) {
		this.headers = headers;
		this.body = new Body(src, dest,price);
	}
	
	public double getBodyPrice() {
		return body.getPrice();
	}
	
	public Body getBody() {
		return body;
	}
	
	public String getBodySrc() {
		return body.getSrc();
	}
	
	public String getBodyDest() {
		return body.getDest();
	}

	public void setBody(Body body) {
		this.body = body;
	}


	public Map<String, String> getHeaders() {
		return headers;
	}


	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}


	@Override
	public String toString() {
		return "Request [headers=" + headers + ", body=" + body + "]";
	}


	private class Body{
		private String src;
		private String dest;
		private double price;
		
		public Body(String src, String dest,double price) {
			this.src = src;
			this.dest = dest;
			this.price = price;
		}
		
		public double getPrice() {
			return price;
		}


		public String getSrc() {
			return src;
		}

		public String getDest() {
			return dest;
		}

		@Override
		public String toString() {
			return "Body [src=" + src + ", dest=" + dest + "]";
		}
		
		
		
	}
	


}

