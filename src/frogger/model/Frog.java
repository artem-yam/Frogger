package frogger.model;

import frogger.utilClasses.GameStaticValues;

public class Frog extends GameObject {

    private int remainingJumpDuration = 0;
    private MoveDirections jumpDirection = null;
    private boolean isGravityActive = false;

    private boolean canJump = true;

    public Frog(int x, int y) {
        super(x, y, GameStaticValues.FROG_WIDTH, GameStaticValues.FROG_HEIGHT, ObjectTypes.FROG);
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

    public boolean isGravityActive() {
        return isGravityActive;
    }

    public void setGravityActive(boolean gravityActive) {
        this.isGravityActive = gravityActive;
        this.setDy(this.getDy() + GameStaticValues.GRAVITY / 2);
    }

    private void stopMovement() {
        this.setDx(0);

        if (this.isGravityActive) {
            this.setDy(GameStaticValues.GRAVITY / 2);
        } else {
            this.setDy(0);
        }

        this.canJump = true;

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void move() {
        super.move();

        if (/*!this.getObjectRectangle().getLocation().equals(this.jumpDestinationPoint)
             &&*/ this.remainingJumpDuration > 0) {
            this.remainingJumpDuration--;
        } else if (!this.canJump) {
            stopMovement();
        }


    /*    if (this.getObjectRectangle().y >= GameStaticValues.GAME_WINDOW_SIZE.getHeight()) {
           this.setObjectAlive(true);
            this.getObjectRectangle().y = 0;
        }*/


    }

    public void jump(MoveDirections direction) {

        if (!this.canJump) {
            return;
        }

        int dx = (int) this.getDx();
        int dy = (int) this.getDy();

        this.stopMovement();
        this.canJump = false;
        this.remainingJumpDuration = GameStaticValues.FROG_JUMP_DURATION;
        this.jumpDirection = direction;

        //TODO находить конечную координату после прыжка, проверять по ней

        int gravityMoveInfluense = 0;

        if (this.isGravityActive) {
            gravityMoveInfluense = (int) (GameStaticValues.GRAVITY / 2);
        }

        switch (direction) {
            case LEFT:
                this.setDx(getDx() - GameStaticValues.FROG_MOVE_SPEED);
                break;
            case RIGHT:
                this.setDx(getDx() + GameStaticValues.FROG_MOVE_SPEED);
                break;
            case UP:
                this.setDy(getDy() - GameStaticValues.FROG_MOVE_SPEED);
                break;
            case DOWN:
                this.setDy(getDy() + GameStaticValues.FROG_MOVE_SPEED);
                break;
        }

    }

}
