package frogger.model;

import frogger.utilClasses.GameStaticValues;

import java.awt.*;

public class GameObject extends Thread {
    private Rectangle objectRectangle = null;
    private int dx = 0;
    private int dy = 0;

    private boolean isObjectAlive = true;
    private ObjectTypes objectType;

    public GameObject(int x, int y, int width, int height, ObjectTypes objectType, int dx, int dy) {
        this.objectRectangle = new Rectangle(x, y, width, height);
        this.dx = dx;
        this.dy = dy;
        this.objectType = objectType;
    }

    public GameObject(int x, int y, int width, int height, ObjectTypes objectType) {
        this(x, y, width, height, objectType, 0, GameStaticValues.GRAVITY);
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

    public int getDy() {
        return dy;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }

    public boolean isObjectAlive() {
        return isObjectAlive;
    }

    public void setObjectAlive(boolean isAlive) {
        this.isObjectAlive = isAlive;
    }

    public ObjectTypes getObjectType() {
        return objectType;
    }


    public void move() {
        this.objectRectangle.setLocation(this.objectRectangle.x + dx, this.objectRectangle.y + dy);

        if (this.objectRectangle.y >= GameStaticValues.GAME_WINDOW_SIZE.getHeight()) {
            //this.setObjectAlive(false);
            this.objectRectangle.y = 0;
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
