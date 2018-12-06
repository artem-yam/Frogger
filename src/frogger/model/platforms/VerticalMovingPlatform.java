package frogger.model.platforms;

import frogger.utilClasses.Constants;

public class VerticalMovingPlatform extends Platform {

	private double topMovePosition;
	private double botMovePosition;

	public VerticalMovingPlatform(int x, int y) {
		super(x, y, PlatformTypeEnum.VERTICAL_MOVING);

		topMovePosition = Constants.PLATFORM_VERTICAL_MOVE_START_POINT;
		botMovePosition = Constants.PLATFORM_VERTICAL_MOVE_END_POINT;

		this.setDy(this.getDy() + Constants.MOVING_PLATFORM_SPEED);

		if (this.getBonus() != null) {
			this.getBonus().setDx(this.getDx());
			this.getBonus().setDy(this.getDy());
		}
	}

	public double getTopMovePosition() {
		return topMovePosition;
	}

	public void setTopMovePosition(double topMovePosition) {
		this.topMovePosition = topMovePosition;
	}

	public double getBotMovePosition() {
		return botMovePosition;
	}

	public void setBotMovePosition(double botMovePosition) {
		this.botMovePosition = botMovePosition;
	}

	@Override
	public void normalizeMovement(int gravity) {
		super.normalizeMovement(gravity);
		this.setDy(this.getGravity() + Constants.MOVING_PLATFORM_SPEED);
	}

	@Override
	public void move() {
		super.move();

		if (this.getObjectRectangle().getY() <= this.topMovePosition && this.getDy() < 0) {
			this.setDy(-this.getDy() + Constants.GRAVITY);

			if (this.getBonus() != null) {
				this.getBonus().setDy(this.getDy());
			}

		} else if (this.getObjectRectangle().getMaxY() >= this.botMovePosition && this.getDy() > 0) {
			this.setDy(-this.getDy() + Constants.GRAVITY);

			if (this.getBonus() != null) {
				this.getBonus().setDy(this.getDy());
			}
		}

		topMovePosition += this.getGravity();
		botMovePosition += this.getGravity();

		if (this.getObjectRectangle().y >= Constants.GAME_WINDOW_SIZE.getHeight()) {
			if (this.topMovePosition >= Constants.GAME_WINDOW_SIZE.getHeight())
				this.setObjectAlive(false);
			else
				this.setObjectAlive(true);
		}
	}

}
