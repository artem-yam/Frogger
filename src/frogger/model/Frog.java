package frogger.model;

import frogger.utilClasses.GameStaticValues;

import java.awt.*;

public class Frog extends GameObject {

    private int remainingJumpDuration = 0;
    private MoveDirections jumpDirection = null;
    private Point jumpDestinationPoint = null;

    private boolean canJump = true;

    public Frog(int x, int y) {
        super(x, y, 0, 0, ObjectTypes.FROG);
    }

    public int getRemainingJumpDuration() {
        return remainingJumpDuration;
    }

    public void setRemainingJumpDuration(int remainingJumpDuration) {
        this.remainingJumpDuration = remainingJumpDuration;
    }

    public boolean isCanJump() {
        return canJump;
    }

    public void setCanJump(boolean canJump) {
        this.canJump = canJump;
    }

    public MoveDirections getJumpDirection() {
        return jumpDirection;
    }

    public void setJumpDirection(MoveDirections jumpDirection) {
        this.jumpDirection = jumpDirection;
    }

    @Override
    public void move() {
        super.move();

        if (!this.getObjectRectangle().getLocation().equals(this.jumpDestinationPoint)
            /* && this.remainingJumpDuration > 0*/) {
            this.remainingJumpDuration--;
        } else if (!this.canJump) {
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
        // this.setDy(GameStaticValues.GRAVITY);
        this.setDy(0);

        this.canJump = true;

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void jump(MoveDirections direction) {

        if (!this.canJump) {
            return;
        }

        int dx = (int) this.getDx();
        int dy = (int) this.getDy();

        this.stopMovement();
        this.canJump = false;
        //this.remainingJumpDuration = GameStaticValues.FROG_JUMP_DURATION;
        this.jumpDirection = direction;


        switch (direction) {
            case LEFT:
                this.jumpDestinationPoint = new Point(
                        this.getObjectRectangle().x - GameStaticValues.BLOCK_WIDTH +
                                GameStaticValues.BLOCK_WIDTH / GameStaticValues.THREAD_SLEEP_TIME * dx,
                        this.getObjectRectangle().y);
                this.setDx(this.getDx() - GameStaticValues.FROG_MOVE_SPEED);
                break;
            case RIGHT:
                this.jumpDestinationPoint = new Point(
                        this.getObjectRectangle().x + GameStaticValues.BLOCK_WIDTH -
                                GameStaticValues.BLOCK_WIDTH / GameStaticValues.THREAD_SLEEP_TIME * dx,
                        this.getObjectRectangle().y);
                this.setDx(this.getDx() + GameStaticValues.FROG_MOVE_SPEED);
                break;
            case UP:
                this.jumpDestinationPoint = new Point(
                        this.getObjectRectangle().x, this.getObjectRectangle().y - GameStaticValues.BLOCK_HEIGHT);
                this.setDy(dy - GameStaticValues.FROG_MOVE_SPEED);
                break;
            case DOWN:
                this.jumpDestinationPoint = new Point(
                        this.getObjectRectangle().x, this.getObjectRectangle().y + GameStaticValues.BLOCK_HEIGHT);
                this.setDy(dy + GameStaticValues.FROG_MOVE_SPEED);
                break;
        }

    }

}
