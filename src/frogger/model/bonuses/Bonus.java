package frogger.model.bonuses;

import frogger.model.Doodler;
import frogger.model.GameObject;
import frogger.model.Model;
import frogger.model.ObjectTypeEnum;
import frogger.model.bonuses.BonusTypeEnum;

public class Bonus extends GameObject {
	private BonusTypeEnum bonusType = null;
	private boolean isActive = false;
	private Doodler doodler = null;

	public Bonus(int x, int y) {
		super(x, y, 0, 0);

		this.setObjectType(ObjectTypeEnum.BONUS);

		this.setDy(this.getGravity());
	}

	public Bonus(int x, int y, BonusTypeEnum type) {
		this(x, y);

		this.bonusType = type;
	}

	public BonusTypeEnum getBonusType() {
		return bonusType;
	}

	public void setBonusType(BonusTypeEnum type) {
		this.bonusType = type;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public Doodler getDoodler() {
		return doodler;
	}

	public void setDoodler(Doodler doodler) {
		this.doodler = doodler;
	}

	public void activateBonus(Model model) {
		this.doodler = model.getDoodler();

		this.isActive = true;
		this.doodler.setActiveBonus(this);
	}

}
