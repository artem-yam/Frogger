package frogger.model.bonuses;

import frogger.model.Model;
import frogger.model.bonuses.BonusTypeEnum;
import frogger.utilClasses.Constants;

public class Shield extends Bonus {

	private int shieldDuration = 0;

	public Shield(int x, int y) {
		super(x, y, BonusTypeEnum.SHIELD);
	}

	public int getShieldDuration() {
		return shieldDuration;
	}

	public void setShieldDuration(int shieldDuration) {
		this.shieldDuration = shieldDuration;
	}

	@Override
	public void activateBonus(Model model) {
		this.shieldDuration = Constants.SHIELD_DURATION_TIME;

		super.activateBonus(model);

		this.getDoodler().setUnbreakable(true);
	}

	@Override
	public void run() {

		while (this.isObjectAlive()) {

			this.move();

			if (this.isActive()) {
				if (this.shieldDuration > 0) {
					this.shieldDuration--;
				} else {
					this.getDoodler().setUnbreakable(false);
					this.getDoodler().setActiveBonus(null);

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
