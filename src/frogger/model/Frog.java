package frogger.model;

import frogger.utilClasses.GameStaticValues;

public class Frog extends GameObject {

	private int remainingJumpDuration = 0;

	private boolean isUnbreakable = false;

	public Frog(int x, int y) {
		super(x, y, 0, 0, ObjectTypeEnum.FROG);
	}

	public int getRemainingJumpDuration() {
		return remainingJumpDuration;
	}

	public void setRemainingJumpDuration(int remainingJumpDuration) {
		this.remainingJumpDuration = remainingJumpDuration;
	}

	public boolean isUnbreakable() {
		return isUnbreakable;
	}

	public void setUnbreakable(boolean isUnbreakable) {
		this.isUnbreakable = isUnbreakable;
	}

	@Override
	public void move() {
		super.move();

		if (this.remainingJumpDuration > 0) {
			this.remainingJumpDuration--;
		}

	}

	public void jump() {
		jump(GameStaticValues.DOODLER_VERTICAL_SPEED);
	}

	public void jump(int jumpSpeed) {
		this.setDy(-jumpSpeed);
		remainingJumpDuration = GameStaticValues.DOODLER_JUMP_DURATION;
	}

	@Override
	public void run() {

		while (this.isObjectAlive()) {

			this.move();

			try {
				Thread.sleep(GameStaticValues.THREAD_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
