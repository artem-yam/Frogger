package frogger.model;

import java.util.ArrayList;
import java.util.List;

import frogger.utilClasses.Constants;

public class Frog extends GameObject {

	private int remainingJumpDuration = 0;
	private boolean isFalling = true;

	private boolean isUnbreakable = false;

	public Frog(int x, int y) {
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

	public boolean isUnbreakable() {
		return isUnbreakable;
	}

	public void setUnbreakable(boolean isUnbreakable) {
		this.isUnbreakable = isUnbreakable;
	}

	@Override
	public void move() {
		super.move();

		if (this.getObjectRectangle().x >= Constants.GAME_WINDOW_SIZE.getWidth()) {
			this.getObjectRectangle().x = 0;
		} else if (this.getObjectRectangle().x <= 0) {
			this.getObjectRectangle().x = (int) Constants.GAME_WINDOW_SIZE.getWidth();
		}

		if (this.remainingJumpDuration > 0) {
			this.remainingJumpDuration--;
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
