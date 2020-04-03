package com.hit.server;

import java.io.Serializable;
import java.util.Map;


public class Response implements Serializable {
	/**
	 * response pattern that server send to client side
	 */
	private static final long serialVersionUID = 1L;
	
	private Map<String,String> res;
	public Response(Map<String, String> res) {
		this.res = res;
	}
	
	public Map<String, String> getRes() {
		return res;
	}
	public void setRes(Map<String, String> res) {
		this.res = res;
	}

}
