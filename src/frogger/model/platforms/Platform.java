package frogger.model.platforms;

import frogger.model.GameObject;
import frogger.model.ObjectTypeEnum;
import frogger.model.bonuses.Bonus;
import frogger.model.bonuses.Jetpack;
import frogger.model.bonuses.Shield;
import frogger.model.bonuses.Spring;
import frogger.model.bonuses.SpringShoes;
import frogger.utilClasses.Constants;

public class Platform extends GameObject {

	private Bonus bonus;
	private PlatformTypeEnum type = PlatformTypeEnum.NORMAL;

	public Platform(int x, int y) {
		super(x, y, 0, 0);

		this.setObjectType(ObjectTypeEnum.PLATFORM);

		double bonusChance = Constants.RND.nextDouble();

		if (bonusChance <= 0.2) {
			bonusChance = Constants.RND.nextDouble();

			if (bonusChance < 0.05) {
				this.bonus = new Jetpack(this.getObjectRectangle().x, this.getObjectRectangle().y);
			} else if (bonusChance < 0.20) {
				this.bonus = new SpringShoes(this.getObjectRectangle().x, this.getObjectRectangle().y);
			} else if (bonusChance < 0.4) {
				this.bonus = new Shield(this.getObjectRectangle().x, this.getObjectRectangle().y);
			} else {
				bonus = new Spring(this.getObjectRectangle().x, this.getObjectRectangle().y);
			}
		}
		this.setDy(this.getGravity());

	}

	public Platform(int x, int y, boolean noBonus) {
		this(x, y);

		if (noBonus) {
			this.bonus = null;
		}
	}

	public Platform(int x, int y, PlatformTypeEnum type) {
		this(x, y);

		this.type = type;

		if (this.bonus != null) {
			this.bonus.setDx(this.getDx());
			this.bonus.setDy(this.getDy());
		}
	}

	public Platform(int x, int y, PlatformTypeEnum type, boolean noBonus) {
		this(x, y, type);

		if (noBonus)
			this.bonus = null;
	}

	public Bonus getBonus() {
		return bonus;
	}

	public void setBonus(Bonus bonus) {
		this.bonus = bonus;
	}

	public PlatformTypeEnum getType() {
		return type;
	}

	public void setType(PlatformTypeEnum type) {
		this.type = type;
	}

	@Override
	public void run() {

		if (bonus != null) {
			bonus.start();
		}

		while (this.isObjectAlive()) {
			this.move();

			if (bonus != null && !bonus.isAlive()) {
				this.bonus = null;
			}

			try {
				Thread.sleep(Constants.THREAD_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
