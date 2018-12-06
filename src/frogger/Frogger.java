package frogger;

import frogger.controller.Controller;
import frogger.model.Model;
import frogger.view.View;

public class Frogger {

	public static void main(String[] args) {
		Model model = new Model();
		Controller controller = new Controller(model);
		View view = new View(controller);
		model.start();
	}

}
