package frogger.model;

import java.awt.Rectangle;

import frogger.utilClasses.GameStaticValues;

public class GameObject extends Thread {
	private Rectangle objectRectangle = null;
	private double dx = 0;
	private double dy = 0;
	private int gravity = GameStaticValues.GRAVITY;

	private boolean isObjectAlive = true;
	private ObjectTypeEnum objectType;

	public GameObject(int x, int y, int width, int height, ObjectTypeEnum objectType, int dx, int dy) {
		this.objectRectangle = new Rectangle(x, y, width, height);
		this.dx = dx;
		this.dy = dy;
		this.objectType = objectType;
	}

	public GameObject(int x, int y, int width, int height, ObjectTypeEnum objectType) {
		this(x, y, width, height, objectType, 0, 0);
	}

	public Rectangle getObjectRectangle() {
		return objectRectangle;
	}

	public void setObjectRectangle(Rectangle objectRectangle) {
		this.objectRectangle = objectRectangle;
	}

	public double getDx() {
		return dx;
	}

	public void setDx(int dx) {
		this.dx = dx;
	}

	public int getGravity() {
		return gravity;
	}

	public void setGravity(int gravity) {
		this.gravity = gravity;
	}

	public void setDx(double dx) {
		this.dx = dx;
	}

	public double getDy() {
		return dy;
	}

	public void setDy(int dy) {
		this.dy = dy;
	}

	public void setDy(double dy) {
		this.dy = dy;
	}

	public boolean isObjectAlive() {
		return isObjectAlive;
	}

	public void setObjectAlive(boolean isAlive) {
		this.isObjectAlive = isAlive;
	}

	public ObjectTypeEnum getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectTypeEnum objectType) {
		this.objectType = objectType;
	}

	public void normalizeMovement(int gravity) {
		this.setGravity(gravity);
		this.setDy(gravity);
	}

	public void move() {
		this.objectRectangle.setLocation((int) (this.objectRectangle.x + dx), (int) (this.objectRectangle.y + dy));

		if (this.objectRectangle.y >= GameStaticValues.GAME_WINDOW_SIZE.getHeight()) {
			this.setObjectAlive(false);
		} else {
			if (this.getObjectRectangle().x >= GameStaticValues.GAME_WINDOW_SIZE.getWidth()) {
				this.getObjectRectangle().x = 0;
			} else if (this.getObjectRectangle().x <= 0) {
				this.getObjectRectangle().x = (int) GameStaticValues.GAME_WINDOW_SIZE.getWidth();
			}
		}

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
