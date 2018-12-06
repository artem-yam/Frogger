package frogger.model;

import frogger.model.GameObject;
import frogger.model.ObjectTypeEnum;
import frogger.utilClasses.Constants;

public class Bullet extends GameObject {

	private int endPosition;

	public Bullet(int x, int y) {
		super(x, y, 0, 0);

		this.setObjectType(ObjectTypeEnum.BULLET);

		this.endPosition = this.getObjectRectangle().y - Constants.GAME_WINDOW_SIZE.height / 3;

		this.setDy(-this.getGravity() * 5);
	}

	public int getEndPosition() {
		return endPosition;
	}

	public void setEndPosition(int endPosition) {
		this.endPosition = endPosition;
	}

	@Override
	public void move() {
		super.move();

		if (this.getObjectRectangle().y < this.endPosition || this.getObjectRectangle().y < 0) {
			this.setObjectAlive(false);
		}
	}

}
