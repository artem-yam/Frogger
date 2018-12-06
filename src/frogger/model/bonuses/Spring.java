package frogger.model.bonuses;

import frogger.model.Model;
import frogger.model.bonuses.BonusTypeEnum;
import frogger.utilClasses.Constants;

public class Spring extends Bonus {

	public Spring(int x, int y) {
		super(x, y, BonusTypeEnum.SPRING);
	}

	@Override
	public void activateBonus(Model model) {
		super.activateBonus(model);

		this.setActive(false);

		this.getDoodler().setActiveBonus(null);
		this.getDoodler().doodlerJumping(Constants.DOODLER_VERTICAL_SPEED * 2);

		this.setDoodler(null);

	}
}
