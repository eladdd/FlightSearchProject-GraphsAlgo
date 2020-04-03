package com.hit.controller;


import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Set;


import com.hit.model.IModel;
import com.hit.model.MyModel;

import com.hit.view.IView;
import com.hit.view.MyView;

public class MyController implements IController {
	private IModel model;
	private IView view;

	public MyController(IModel model, IView view) {
		this.model = model;
		this.view = view;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable arg0, Object arg1) { 
		
		/*there is 2 instance the view and the model */
		if (arg0 instanceof MyView) { 
			String[] request = (String[]) arg1; //read request string
			
			switch (request[0]) {
			case "graphTypeDIJ":
			case "graphTypeBFS":
			case "BUILD GRAPH":
			case "LOAD":
			case "GETCOUNTRY":
				((MyModel)model).requestBuilder(request[0], null, null,null);
				break;

			case "SEARCH PATH":
				((MyModel)model).requestBuilder(request[0], request[1], request[2],null); //action,src,dest,price
				break;
				
			case "DELETE FLIGHT":
				((MyModel)model).requestBuilder(request[0], request[1], request[2],null);//action,src,dest,price
				break;

			case "ADD FLIGHT":
				((MyModel)model).requestBuilder(request[0], request[1], request[2],request[3]);//action,src,dest,price
				break;
			default:
				System.out.println("Something wrong");
				break;
			}
		}
		
		else if (arg0 instanceof MyModel)//response
		{
			Map<String, Object> res = (Map<String, Object>) arg1; //read response its come in map data structure
			
			switch ((String)res.get("ACTION")) {
			case "LOAD":
				((MyView)view).flightsData((LinkedList<String[]>)res.get("DATA"),(String)res.get("STATUS"));
				break;

			case "SEARCH PATH":
				((MyView)view).displaySearch((LinkedHashMap<String, Double>)res.get("DATA"), (String)res.get("STATUS"),(String)res.get("TYPE"));
				break;
				
			case "GETCOUNTRY":
				((MyView)view).countryData((Set<String>)res.get("DATA"), (String)res.get("STATUS"),(String)res.get("TYPE"));
				break;
				
			case "ADD FLIGHT":
			case "DELETE FLIGHT":
				((MyView)view).actionsFlight((String)res.get("STATUS"));
				break;
		
			default:
				System.out.println("Something wrong");
				break;
			}
		}
		else {
			System.out.println("something went wront in mycontroller");
		}

	}
}
