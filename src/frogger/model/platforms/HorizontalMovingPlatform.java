package frogger.model.platforms;

import frogger.utilClasses.Constants;

public class HorizontalMovingPlatform extends Platform {

	private double leftMovePosition;
	private double rightMovePosition;

	public HorizontalMovingPlatform(int x, int y) {
		super(x, y, PlatformTypeEnum.HORIZONTAL_MOVING);

		leftMovePosition = Constants.PLATFORM_HORISONTAL_MOVE_START_POINT;
		rightMovePosition = Constants.PLATFORM_HORISONTAL_MOVE_END_POINT;

		this.setDx(Constants.MOVING_PLATFORM_SPEED);

	}

	public double getLeftMovePosition() {
		return leftMovePosition;
	}

	public void setLeftMovePosition(double leftMovePosition) {
		this.leftMovePosition = leftMovePosition;
	}

	public double getRightMovePosition() {
		return rightMovePosition;
	}

	public void setRightMovePosition(double rightMovePosition) {
		this.rightMovePosition = rightMovePosition;
	}

	@Override
	public void move() {
		super.move();

		if (this.getObjectRectangle().getX() < Constants.PLATFORM_HORISONTAL_MOVE_START_POINT) {
			this.setDx(Math.abs(this.getDx()));
		} else if (this.getObjectRectangle().getMaxX() > Constants.PLATFORM_HORISONTAL_MOVE_END_POINT) {
			this.setDx(-Math.abs(this.getDx()));
		}
	}

}
