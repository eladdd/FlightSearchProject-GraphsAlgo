package com.hit.driver;


import com.hit.controller.IController;
import com.hit.controller.MyController;
import com.hit.model.IModel;
import com.hit.model.MyModel;
import com.hit.view.IView;
import com.hit.view.MyView;

public class MVCDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		IView view = new MyView();
		IModel model = new MyModel(34567);
		IController controller = new MyController(model,view);
		((MyModel)model).addObserver(controller);
		((MyView)view).addObserver(controller);
		view.start();
	}

}
