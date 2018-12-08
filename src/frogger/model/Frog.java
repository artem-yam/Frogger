package frogger.model;

import frogger.utilClasses.GameStaticValues;

public class Frog extends GameObject {

    private int remainingJumpDuration = 0;

    private boolean isUnbreakable = false;

    public Frog(int x, int y) {
        super(x, y, 0, 0, ObjectTypes.FROG);
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
        } else {
            stopMovement();
        }


        //TODO
        if (this.getObjectRectangle().y >= GameStaticValues.GAME_WINDOW_SIZE.getHeight()) {
            this.setObjectAlive(true);
            this.getObjectRectangle().y = 0;
        }

    }

    private void stopMovement() {
        this.setDx(0);
        this.setDy(GameStaticValues.GRAVITY);
    }

    public void jump(JumpDirections direction) {
        if (remainingJumpDuration > 0) {
            return;
        }

        switch (direction) {
            case LEFT:
                setDx(-GameStaticValues.FROG_MOVE_SPEED);
                break;
            case RIGHT:
                setDx(GameStaticValues.FROG_MOVE_SPEED);
                break;
            case UP:
                setDy(-GameStaticValues.FROG_MOVE_SPEED);
                break;
            case DOWN:
                setDy(GameStaticValues.FROG_MOVE_SPEED);
                break;
        }

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
