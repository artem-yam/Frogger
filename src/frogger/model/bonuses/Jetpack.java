package frogger.model.bonuses;

import frogger.model.Model;
import frogger.model.bonuses.BonusTypeEnum;
import frogger.utilClasses.Constants;

public class Jetpack extends Bonus {

	private int jetpackDuration = 0;
	private Model gameModel;

	public Jetpack(int x, int y) {
		super(x, y, BonusTypeEnum.JETPACK);
	}

	public int getJetpackDuration() {
		return jetpackDuration;
	}

	public void setJetpackDuration(int jetpackDuration) {
		this.jetpackDuration = jetpackDuration;
	}

	public Model getGameModel() {
		return gameModel;
	}

	public void setGameModel(Model model) {
		this.gameModel = model;
	}

	@Override
	public void activateBonus(Model model) {
		this.jetpackDuration = Constants.JETPACK_DURATION_TIME;

		super.activateBonus(model);
		this.gameModel = model;

		this.getDoodler().setUnbreakable(true);
		this.getDoodler().setCanShoot(false);

		this.getDoodler().setDy(0);
	}

	@Override
	public void run() {

		while (this.isObjectAlive()) {

			this.move();

			if (this.isActive()) {
				if (this.jetpackDuration > 0) {
					this.jetpackDuration--;
				} else {
					this.getDoodler().setUnbreakable(false);
					this.getDoodler().setActiveBonus(null);

					gameModel.normalizeObjectsMovement(Constants.GRAVITY);
					this.getDoodler().doodlerJumping();

					this.setObjectAlive(false);
				}
			}

			try {
				Thread.sleep(Constants.THREAD_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
