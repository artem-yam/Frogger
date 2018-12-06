package frogger.controller;

import frogger.model.Model;
import frogger.model.ObjectTypeEnum;
import frogger.utilClasses.IObserver;
import frogger.utilClasses.ISubscribeHelper;

public class Controller implements ISubscribeHelper<Model> {
	private Model model;

	public Controller(Model model) {
		super();
		this.model = model;
	}

	@Override
	public void subscribeObserver(IObserver<Model> observer) {
		observer.subscribe(this.model);
	}

	public void changeDx(int value) {
		model.changeDoodlerDx(value);
	}

	public void changeObjectSize(ObjectTypeEnum objectType, int... values) {
		model.changeObjectSize(objectType, values);
	}

	public void changeObjectLocation(ObjectTypeEnum objectType, int... values) {
		model.changeObjectLocation(objectType, values);
	}

	public void doodlerShoot() {
		model.doodlerShoot(false);
	}

	public void doodlerStopShoot() {
		model.doodlerShoot(true);
	}

	public void startGame() {
		model.setGameActive(true);
	}

}
