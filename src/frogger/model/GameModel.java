package frogger.model;

import frogger.utilClasses.GameStaticValues;
import frogger.utilClasses.Observable;
import frogger.utilClasses.Observer;

import java.awt.geom.Line2D;
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

        this.frog = new Frog(GameStaticValues.DOODLER_START_X, GameStaticValues.DOODLER_START_Y);

        Thread thread = new Thread(frog);
        thread.start();
        frog.start();

        notifyObservers(new ModelChangeData(ModelChangeEvents.OBJECT_СREATE, this.frog));

        gameObjects = new ArrayList<GameObject>();

        formStartGround();
    }

    private void formStartGround() {
        for (int i = 1; i <= GameStaticValues.START_GROUND_ROWS; i++) {
            formRowOfGround(i);
        }
    }


    private void formRowOfGround(int rowNumber) {
        for (int i = 0; i < GameStaticValues.MAX_BLOCKS_COUNT_IN_ROW; i++) {

            GameObject ground = new GameObject(i * GameStaticValues.BLOCK_WIDTH,
                    GameStaticValues.GAME_WINDOW_SIZE.height - rowNumber * GameStaticValues.BLOCK_HEIGHT,
                    0, 0, ObjectTypes.GROUND);

            gameObjects.add(ground);

            ground.start();

            notifyObservers(new ModelChangeData(ModelChangeEvents.OBJECT_СREATE, ground));

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
        frog.jump(JumpDirections.LEFT);
    }

    public void frogJumpRight() {
        frog.jump(JumpDirections.RIGHT);
    }

    public void frogJumpUp() {
        frog.jump(JumpDirections.UP);
    }

    public void frogJumpDown() {
        frog.jump(JumpDirections.DOWN);
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

                if (frog.getObjectRectangle().getY() < doodlerHighestPosition) {
                    userScore += Math.abs(doodlerHighestPosition - frog.getObjectRectangle().getY());

                    doodlerHighestPosition = (long) frog.getObjectRectangle().getY();
                }

                notifyObservers(new ModelChangeData(ModelChangeEvents.SCORE_CHANGE, null, userScore));

                doodlerHighestPosition += GameStaticValues.GRAVITY;

                deleteDeadObjects();

                for (GameObject gameObject : gameObjects) {

                    notifyObservers(new ModelChangeData(ModelChangeEvents.OBJECT_MOVE, gameObject,
                            gameObjects.indexOf(gameObject)));

                    if (gameObject.getObjectRectangle()
                            .intersectsLine(new Line2D.Double(frog.getObjectRectangle().getX(),
                                    frog.getObjectRectangle().getMaxY(), frog.getObjectRectangle().getMaxX(),
                                    frog.getObjectRectangle().getMaxY()))) {


                        //  frog.jump();

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
                }

                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

            this.isGameActive = false;
            notifyObservers(new ModelChangeData(ModelChangeEvents.GAME_OVER, null));

        }
    }

}
