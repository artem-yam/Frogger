package frogger;

import frogger.model.GameModel;
import frogger.model.ObjectTypeEnum;
import frogger.utilClasses.Observer;

public class GameController {
	private GameModel model;

	public GameController(GameModel model) {
		super();
		this.model = model;
	}

	public static void main(String[] args) {
		GameModel model = new GameModel();
		GameController controller = new GameController(model);
		GameView view = new GameView(controller);
		model.start();
	}

	public void addObserverToModel(Observer observer) {
		model.addObserver(observer);
	}

	public void changeDx(int value) {
		model.changeFrogHorizontalSpeed(value);
	}

	public void changeObjectSize(ObjectTypeEnum objectType, int... values) {
		model.changeObjectSize(objectType, values);
	}

	public void changeObjectLocation(ObjectTypeEnum objectType, int... values) {
		model.changeObjectLocation(objectType, values);
	}

	public void startGame() {
		model.setGameActive(true);
	}

}
