package frogger.model;

import frogger.utilClasses.GameStaticValues;

public class Frog extends GameObject {

    private int remainingJumpDuration = 0;
    private MoveDirections jumpDirection = null;
    private boolean isGravityActive = false;
 //   private int jumpPointCoordinate;

    private boolean canJump = true;

    //TODO скорость лягухи/2   (можно, если не пофикшу проблему)
/*    @Override
    public void setDx(double dx) {
        super.setDx(dx / 2);
    }

    @Override
    public void setDy(double dy) {
        super.setDy(dy / 2);
    }*/

    public Frog(int x, int y) {
        super(x, y, GameStaticValues.FROG_WIDTH, GameStaticValues.FROG_HEIGHT, ObjectTypes.FROG);
    }

    public boolean isCanJump() {
        return canJump;
    }

    public MoveDirections getJumpDirection() {
        return jumpDirection;
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


        if (this.remainingJumpDuration > 0) {
            this.remainingJumpDuration--;
        } else if (!this.canJump) {
            stopMovement();
        }
/*
        if (!this.canJump &&
                ((this.jumpDirection == MoveDirections.LEFT && this.getObjectRectangle().x <= jumpPointCoordinate)
                        || (this.jumpDirection == MoveDirections.RIGHT && this.getObjectRectangle().x >= jumpPointCoordinate)
                        || (this.jumpDirection == MoveDirections.UP && this.getObjectRectangle().y <= jumpPointCoordinate)
                        || (this.jumpDirection == MoveDirections.DOWN && this.getObjectRectangle().y >= jumpPointCoordinate))) {
            stopMovement();
        }*/



    /*    if (this.getObjectRectangle().y >= GameStaticValues.GAME_WINDOW_SIZE.getHeight()) {
           this.setObjectAlive(true);
            this.getObjectRectangle().y = 0;
        }*/


    }

    public void jump(MoveDirections direction) {

        if (!this.canJump) {
            return;
        }

        double dx = this.getDx();
        double dy = this.getDy();

        this.stopMovement();
        this.canJump = false;
        this.jumpDirection = direction;


        this.remainingJumpDuration = GameStaticValues.FROG_JUMP_DURATION;


        int gravityMoveInfluence = 0;
     //   int pointOffset = 0;

        if (this.isGravityActive) {
            gravityMoveInfluence = (int) (GameStaticValues.GRAVITY / 2);
        //    pointOffset = (int) (GameStaticValues.BLOCK_HEIGHT / GameStaticValues.FROG_MOVE_SPEED
        //            * GameStaticValues.GRAVITY);
        }


        //TODO настроить при активации гравитации
        switch (direction) {
            case LEFT:
            //    jumpPointCoordinate = this.getObjectRectangle().x - GameStaticValues.BLOCK_WIDTH;
                // this.setDx(getDx() - GameStaticValues.FROG_MOVE_SPEED);

                dx = getDx() - GameStaticValues.FROG_HORIZONTAL_MOVE_SPEED;
                dy = gravityMoveInfluence;

                break;
            case RIGHT:
           //     jumpPointCoordinate = this.getObjectRectangle().x + GameStaticValues.BLOCK_WIDTH;
                //  this.setDx(getDx() + GameStaticValues.FROG_MOVE_SPEED);

                dx = getDx() + GameStaticValues.FROG_HORIZONTAL_MOVE_SPEED;
                dy = gravityMoveInfluence;

                break;
            case UP:

             //   jumpPointCoordinate = this.getObjectRectangle().y - GameStaticValues.BLOCK_HEIGHT
             //           - GameStaticValues.ROWS_GAP + pointOffset;
                //    this.setDy(getDy() - GameStaticValues.FROG_MOVE_SPEED + gravityMoveInfluense);

            //    if (jumpPointCoordinate > this.getObjectRectangle().y) {
           //         jumpPointCoordinate = this.getObjectRectangle().y;
           //     }


                dx = 0;
                dy = getDy() - GameStaticValues.FROG_VERTICAL_MOVE_SPEED ;

                break;
            case DOWN:
            //    jumpPointCoordinate = this.getObjectRectangle().y + GameStaticValues.BLOCK_HEIGHT
            //            + GameStaticValues.ROWS_GAP + pointOffset;
                //    this.setDy(getDy() + GameStaticValues.FROG_MOVE_SPEED + gravityMoveInfluense);

                dx = 0;
                dy = getDy() + GameStaticValues.FROG_VERTICAL_MOVE_SPEED ;

                break;
        }

        this.setDx(dx);
        this.setDy(dy);

    }

}
