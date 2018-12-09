package frogger.model;

import frogger.utilClasses.GameStaticValues;
import frogger.utilClasses.Observable;
import frogger.utilClasses.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameModel extends Thread implements Observable {

    private final List<Observer> observers = new ArrayList<Observer>();
    private Frog frog;
    private List<GameObject> gameObjects = new CopyOnWriteArrayList<GameObject>();
    private int userScore = 0;

    private boolean isGameActive = false;

    public GameModel() {
        super();
    }

    public void setGameActive(boolean isGameActive) {
        this.isGameActive = isGameActive;
    }

    @Override
    public void addObserver(Observer observer) {
        if (this.observers.contains(observer))
            return;
        this.observers.add(observer);
    }

    @Override
    public void notifyObservers(ModelChangeData changeData) {
        for (Observer observer : observers)
            observer.handleEvent(changeData);
    }

    private void initializeVariables() {
        this.userScore = 0;
        gameObjects = new ArrayList<GameObject>();
        isGameActive = false;
    }

    private void gameStart() {

        initializeVariables();

        this.frog = new Frog(GameStaticValues.FROG_START_X, GameStaticValues.FROG_START_Y);

        Thread thread = new Thread(frog);
        thread.start();
        frog.start();

        notifyObservers(new ModelChangeData(ModelChangeEvents.OBJECT_СREATE, this.frog));

        gameObjects = new ArrayList<GameObject>();

        formStartBlocks();
    }

    private void formStartBlocks() {

        for (int i = 1; i <= GameStaticValues.START_GROUND_ROWS; i++) {
            formRowOfBlocks(i, ObjectTypes.GROUND);
        }

        for (int i = GameStaticValues.START_GROUND_ROWS + 1; i <= GameStaticValues.ROWS_COUNT; i++) {
            formRowOfBlocks(i, null);
        }
    }


    //TODO
    private void formRowOfBlocks(int rowNumber, ObjectTypes allBlocksType) {

        int blocksCount = 0;
        int blocksSpeed = 0;

//        if (allBlocksType == ObjectTypes.GROUND) {
//            blocksCount = GameStaticValues.MAX_BLOCKS_COUNT_IN_ROW;
//        } else {
//            blocksCount = GameStaticValues.MIN_BLOCKS_COUNT_IN_ROW + GameStaticValues.RND.nextInt(
//                    GameStaticValues.MAX_BLOCKS_COUNT_IN_ROW - GameStaticValues.MIN_BLOCKS_COUNT_IN_ROW + 1);
//        }

        if (allBlocksType != ObjectTypes.GROUND) {


            blocksSpeed = (GameStaticValues.RND.nextInt(2 * GameStaticValues.BLOCK_MAX_MOVE_SPEED + 1)
                    - GameStaticValues.BLOCK_MAX_MOVE_SPEED) * 2;
        }

        for (int i = 0; i < GameStaticValues.MAX_BLOCKS_COUNT_IN_ROW; i++) {

            if (blocksCount >= GameStaticValues.MAX_BLOCKS_COUNT_IN_ROW) {
                return;
            }

            ObjectTypes blockType = allBlocksType;


            if (blockType == null) {

                if (GameStaticValues.RND.nextDouble() > 0.5
                        || (GameStaticValues.MAX_BLOCKS_COUNT_IN_ROW - i + blocksCount)
                        > GameStaticValues.MAX_BLOCKS_COUNT_IN_ROW) {

                    blocksCount++;

                    if (GameStaticValues.RND.nextDouble() > 0.7) {
                        blockType = ObjectTypes.LEAF;
                    } else {
                        blockType = ObjectTypes.WOOD;
                    }

                } else {
                    continue;
                }

            } else {
                blocksCount++;
            }

            int rowsGap = 0;

            if (rowNumber > GameStaticValues.START_GROUND_ROWS) {
                rowsGap = rowNumber * 2;
            }

            GameObject block = new GameObject(1 + i * GameStaticValues.BLOCK_WIDTH,
                    GameStaticValues.GAME_WINDOW_SIZE.height - rowNumber * GameStaticValues.BLOCK_HEIGHT
                            - rowNumber * 2,
                    0, 0, blockType);

            block.setDx(blocksSpeed);

            gameObjects.add(block);

            block.start();

            notifyObservers(new ModelChangeData(ModelChangeEvents.OBJECT_СREATE, block, rowNumber));

        }
    }

    private void deleteDeadObjects() {

        for (int i = gameObjects.size() - 1; i >= 0; i--) {
            GameObject gameObject = gameObjects.get(i);

            if (!gameObject.isAlive()) {

                notifyObservers(new ModelChangeData(ModelChangeEvents.OBJECT_DESTROY, gameObject, i));

                gameObjects.remove(i);

            }
        }
    }

    // TODO
    private void generateNewBlock(boolean gameStart, boolean isRequired) {
        GameObject newBlock = null;

        int blockX = GameStaticValues.RND
                .nextInt((int) GameStaticValues.GAME_WINDOW_SIZE.getWidth() - GameStaticValues.BLOCK_WIDTH);

        int blockY = 0;
        if (gameStart) {

            blockY = GameStaticValues.RND.nextInt((int) GameStaticValues.GAME_WINDOW_SIZE.getHeight());

        }

        double rnd = GameStaticValues.RND.nextDouble();

        if (rnd < 0.3) {
            //       newBlock = new GameObject(blockX, blockY, width, height, ObjectTypes.LEAF);
        } else {
            //      newBlock = new GameObject(blockX, blockY, width, height, ObjectTypes.WOOD);
        }

        gameObjects.add(newBlock);

        //    notifyObservers(ModelChangeEvents.OBJECT_СREATE, newBlock.getObject(), blockX, blockY);

        newBlock.start();

    }

    // TODO
    private void generateNewObjects() {
        if (GameStaticValues.RND.nextDouble() < 0.05) {

            //       generateNewBlock(false, isNewPlatformRequired());

        }

    }

    public void frogJumpLeft() {
        frog.jump(MoveDirections.LEFT);
    }

    public void frogJumpRight() {
        frog.jump(MoveDirections.RIGHT);
    }

    public void frogJumpUp() {
        frog.jump(MoveDirections.UP);
    }

    public void frogJumpDown() {
        frog.jump(MoveDirections.DOWN);
    }

    public void changeObjectSize(GameObject object, int width, int height) {
        object.getObjectRectangle().setSize(width, height);
    }

    @Override
    public void start() {

        while (true) {

            while (!isGameActive) {

            }

            notifyObservers(new ModelChangeData(ModelChangeEvents.GAME_START, null));

            gameStart();

            long doodlerHighestPosition = (long) frog.getObjectRectangle().getY();

            while (frog.isObjectAlive()) {

                generateNewObjects();

                notifyObservers(new ModelChangeData(ModelChangeEvents.OBJECT_MOVE, frog));

                //    frog.getObjectRectangle().x, frog.getObjectRectangle().y, (int) frog.getDx());

                /*
                if (frog.getObjectRectangle().getY() < doodlerHighestPosition) {
                    userScore += Math.abs(doodlerHighestPosition - frog.getObjectRectangle().getY());

                    doodlerHighestPosition = (long) frog.getObjectRectangle().getY();
                }

                notifyObservers(new ModelChangeData(ModelChangeEvents.SCORE_CHANGE, null, userScore));

                doodlerHighestPosition += GameStaticValues.GRAVITY;
                */

                deleteDeadObjects();

                if (frog.getObjectRectangle().y <
                        GameStaticValues.GAME_WINDOW_SIZE.height / 2) {

                    // frog.getObjectRectangle().y += GameStaticValues.FROG_HEIGHT;
                    frog.setGravityActive(true);


                    /* notifyObservers(new ModelChangeData(ModelChangeEvents.OBJECT_MOVE, frog));*/

                    for (GameObject gameObject : gameObjects) {
                        // gameObject.getObjectRectangle().y += GameStaticValues.BLOCK_HEIGHT;
                        gameObject.setDy(GameStaticValues.GRAVITY);

                      /*  notifyObservers(new ModelChangeData(ModelChangeEvents.OBJECT_MOVE, gameObject,
                                gameObjects.indexOf(gameObject)));*/
                    }
                }


                for (GameObject gameObject : gameObjects) {

                    notifyObservers(new ModelChangeData(ModelChangeEvents.OBJECT_MOVE, gameObject,
                            gameObjects.indexOf(gameObject)));


                    if (gameObject.getObjectRectangle().intersects(frog.getObjectRectangle())
                            && frog.isCanJump()) {

                        frog.setDx(gameObject.getDx() / 2);

                        System.out.println("-------------------------");
                        System.out.println("Frog speed = " + frog.getDx());
                        System.out.println("Block speed = " + gameObject.getDx());
                        System.out.println("-------------------------");

                    }

                    switch (gameObject.getObjectType()) {
                        case LEAF:
                            this.userScore += 50;
                            break;
                        case WOOD:
                            this.userScore += 1;
                            break;
                        default:
                            break;
                    }

                }


                try {
                    Thread.sleep(GameStaticValues.THREAD_SLEEP_TIME);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            this.isGameActive = false;
            notifyObservers(new ModelChangeData(ModelChangeEvents.GAME_OVER, null));

        }
    }

}
