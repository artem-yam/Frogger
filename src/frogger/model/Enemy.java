package frogger.model;

import frogger.model.GameObject;
import frogger.model.ObjectTypeEnum;

public class Enemy extends GameObject {

	public Enemy(int x, int y) {
		super(x, y, 0, 0);

		this.setObjectType(ObjectTypeEnum.ENEMY);

		this.setDy(this.getGravity());
	}

}
