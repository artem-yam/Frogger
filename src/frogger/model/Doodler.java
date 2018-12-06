package frogger.model;

import java.util.ArrayList;
import java.util.List;

import frogger.model.bonuses.Bonus;
import frogger.model.bonuses.BonusTypeEnum;
import frogger.utilClasses.Constants;

public class Doodler extends GameObject {

	private int remainingJumpDuration = 0;
	private boolean isFalling = true;

	private boolean canShoot = true;
	private boolean isShooting = false;
	private int remainingShootingTime = 0;

	private Bonus activeBonus = null;
	private boolean isUnbreakable = false;

	private List<Bullet> bullets = new ArrayList<Bullet>();

	public Doodler(int x, int y) {
		super(x, y, 0, 0);

		this.setObjectType(ObjectTypeEnum.DOODLER);
	}

	public int getRemainingJumpDuration() {
		return remainingJumpDuration;
	}

	public void setRemainingJumpDuration(int remainingJumpDuration) {
		this.remainingJumpDuration = remainingJumpDuration;
	}

	public boolean isFalling() {
		return isFalling;
	}

	public void setFalling(boolean isFalling) {
		this.isFalling = isFalling;
	}

	public boolean isCanShoot() {
		return canShoot;
	}

	public void setCanShoot(boolean canShoot) {
		this.canShoot = canShoot;
	}

	public boolean isShooting() {
		return isShooting;
	}

	public void setShooting(boolean isShooting) {
		this.isShooting = isShooting;

		if (isShooting)
			this.remainingShootingTime = 20;
	}

	public List<Bullet> getBullets() {
		return bullets;
	}

	public void setBullets(List<Bullet> bullets) {
		this.bullets = bullets;
	}

	public Bonus getActiveBonus() {
		return activeBonus;
	}

	public void setActiveBonus(Bonus activeBonus) {
		this.activeBonus = activeBonus;
	}

	public boolean isUnbreakable() {
		return isUnbreakable;
	}

	public void setUnbreakable(boolean isUnbreakable) {
		this.isUnbreakable = isUnbreakable;
	}

	public Bullet shoot() {
		this.setShooting(true);

		Bullet bullet = new Bullet((int) this.getObjectRectangle().getCenterX(),
				(int) this.getObjectRectangle().getY());
		this.bullets.add(bullet);

		return bullet;
	}

	@Override
	public void move() {
		super.move();

		if (this.getObjectRectangle().x >= Constants.GAME_WINDOW_SIZE.getWidth()) {
			this.getObjectRectangle().x = 0;
		} else if (this.getObjectRectangle().x <= 0) {
			this.getObjectRectangle().x = (int) Constants.GAME_WINDOW_SIZE.getWidth();
		}

		if (this.remainingShootingTime > 0) {
			this.remainingShootingTime--;
		}

		if (this.remainingJumpDuration > 0) {
			this.remainingJumpDuration--;
		}

		if (this.isShooting && this.remainingShootingTime == 0) {
			this.setShooting(false);
		}
	}

	public void doodlerJumping() {
		doodlerJumping(Constants.DOODLER_VERTICAL_SPEED);
	}

	public void doodlerJumping(int jumpSpeed) {
		this.setDy(-jumpSpeed);
		remainingJumpDuration = Constants.DOODLER_JUMP_DURATION;
		isFalling = false;
	}

	public void doodlerFalling() {
		this.isFalling = true;
		this.setDy(Constants.DOODLER_VERTICAL_SPEED + Constants.GRAVITY);
	}

	@Override
	public void run() {

		while (this.isObjectAlive()) {

			this.move();

			if (activeBonus != null) {
				if (activeBonus.getBonusType() == BonusTypeEnum.JETPACK) {
					remainingJumpDuration = Constants.DOODLER_JUMP_DURATION;
				}
			}

			if (remainingJumpDuration == 0) {
				doodlerFalling();
			}

			try {
				Thread.sleep(Constants.THREAD_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
