package frogger.model.bonuses;

import frogger.model.Model;
import frogger.model.bonuses.BonusTypeEnum;
import frogger.utilClasses.Constants;

public class SpringShoes extends Bonus {

	private int jumpsLeft = 0;

	public SpringShoes(int x, int y) {
		super(x, y, BonusTypeEnum.SPRING_SHOES);
	}

	public int getShieldDuration() {
		return jumpsLeft;
	}

	public void setShieldDuration(int shieldDuration) {
		this.jumpsLeft = shieldDuration;
	}

	@Override
	public void activateBonus(Model model) {
		this.jumpsLeft = Constants.SPRING_SHOES_JUMPS_COUNT;

		super.activateBonus(model);
	}

	public void doodlerJump() {
		this.getDoodler().doodlerJumping(Constants.DOODLER_VERTICAL_SPEED * 2);
		this.jumpsLeft--;
	}

	@Override
	public void run() {

		while (this.isObjectAlive()) {

			this.move();

			if (this.isActive() && this.jumpsLeft == 0) {
				this.getDoodler().setActiveBonus(null);

				this.setObjectAlive(false);
			}

			try {
				Thread.sleep(Constants.THREAD_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}
