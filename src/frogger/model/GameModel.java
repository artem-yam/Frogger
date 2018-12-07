package frogger.model;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import frogger.model.ModelChangeEventEnum;
import frogger.utilClasses.GameStaticValues;
import frogger.utilClasses.Observable;
import frogger.utilClasses.Observer;

public class GameModel extends Thread implements Observable {

	private Frog frog;
	private List<GameObject> gameObjects = new CopyOnWriteArrayList<GameObject>();
	private final List<Observer> observers = new ArrayList<Observer>();

	private int gameGravity = 0;
	private int userScore = 0;

	private boolean isGameActive = false;

	public GameModel() {
		super();
	}

	public Frog getDoodler() {
		return frog;
	}

	public void setDoodler(Frog doodler) {
		this.frog = doodler;
	}

	public Collection<Observer> getSubscribers() {
		return observers;
	}

	public boolean isGameActive() {
		return isGameActive;
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
	public void removeObserver(Observer observer) {
		if (!this.observers.contains(observer))
			return;
		this.observers.remove(observer);
	}

	@Override
	public void notifyObservers(ModelChangeEventEnum changeType, ObjectTypeEnum objectType, int... values) {
		for (Observer observer : observers)
			observer.handleEvent(new ModelChangeData(changeType, objectType, values));
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

		notifyObservers(ModelChangeEventEnum.OBJECT_СREATE, ObjectTypeEnum.FROG, frog.getObjectRectangle().x,
				frog.getObjectRectangle().y, frog.getObjectRectangle().width, frog.getObjectRectangle().height);

		gameObjects = new ArrayList<GameObject>();

	}

	// TODO
	private void formStartGround() {
		for (int i = 0; i < GameStaticValues.START_PLATFORMS_COUNT - gameObjects.size(); i++) {

			GameObject plat = new GameObject(x, y, width, height, ObjectTypeEnum);
			gameObjects.add(plat);

			plat.start();

			notifyObservers(ModelChangeEventEnum.OBJECT_СREATE, ObjectTypeEnum.PLATFORM,
					((GameObject) gameObjects.get(0)).getObjectRectangle().x,
					((GameObject) gameObjects.get(0)).getObjectRectangle().y,
					((Block) gameObjects.get(0)).getType().ordinal());

			generateNewBlock(true, true);

		}
	}

	private void deleteDeadObjects() {

		for (int index = gameObjects.size() - 1; index >= 0; index--) {
			GameObject gameObject = gameObjects.get(index);

			if (!gameObject.isAlive()) {

				notifyObservers(ModelChangeEventEnum.OBJECT_DESTROY, gameObject.getObjectType(), index);

				gameObjects.remove(index);

			}
		}
	}

	// TODO
	private void generateNewBlock(boolean gameStart, boolean isRequired) {
		GameObject newBlock = null;

		int blockX = GameStaticValues.RND
				.nextInt((int) GameStaticValues.GAME_WINDOW_SIZE.getWidth() - GameStaticValues.PLATFORM_WIDTH);

		int blockY = 0;
		if (gameStart) {

			blockY = GameStaticValues.RND.nextInt((int) GameStaticValues.GAME_WINDOW_SIZE.getHeight());

		}

		double rnd = GameStaticValues.RND.nextDouble();

		if (rnd < 0.3) {
			newBlock = new GameObject(blockX, blockY, width, height, ObjectTypeEnum.LEAF);
		} else {
			newBlock = new GameObject(blockX, blockY, width, height, ObjectTypeEnum.WOOD);
		}

		gameObjects.add(newBlock);

		notifyObservers(ModelChangeEventEnum.OBJECT_СREATE, newBlock.getObjectType(), blockX, blockY);

		newBlock.start();

	}

	// TODO
	private void generateNewObjects() {
		if (GameStaticValues.RND.nextDouble() < 0.05) {

			generateNewBlock(false, isNewPlatformRequired());

		}

	}

	public void changeFrogHorizontalSpeed(int dx) {
		frog.setDx(dx);
	}

	public void changeObjectSize(ObjectTypeEnum objectType, int... values) {
		if (objectType == ObjectTypeEnum.FROG) {
			frog.getObjectRectangle().setSize(values[0], values[1]);
		} else {
			gameObjects.get(values[2]).getObjectRectangle().setSize(values[0], values[1]);
		}
	}

	public void changeObjectLocation(ObjectTypeEnum objectType, int... values) {
		if (objectType == ObjectTypeEnum.FROG) {
			frog.getObjectRectangle().setLocation(values[0], values[1]);
		} else {
			gameObjects.get(values[2]).getObjectRectangle().setLocation(values[0], values[1]);
		}
	}

	@Override
	public void start() {

		while (true) {

			while (!isGameActive) {

			}

			notifyObservers(ModelChangeEventEnum.GAME_START, null);

			gameStart();

			long doodlerHighestPosition = (long) frog.getObjectRectangle().getY();

			while (frog.isObjectAlive()) {

				generateNewObjects();

				notifyObservers(ModelChangeEventEnum.OBJECT_MOVE, ObjectTypeEnum.FROG, frog.getObjectRectangle().x,
						frog.getObjectRectangle().y, (int) frog.getDx());

				if (frog.getObjectRectangle().getY() < doodlerHighestPosition) {
					userScore += Math.abs(doodlerHighestPosition - frog.getObjectRectangle().getY());

					doodlerHighestPosition = (long) frog.getObjectRectangle().getY();
				}

				notifyObservers(ModelChangeEventEnum.SCORE_CHANGE, ObjectTypeEnum.FROG, userScore);

				doodlerHighestPosition += this.gameGravity;

				deleteDeadObjects();

				for (GameObject gameObject : gameObjects) {

					notifyObservers(ModelChangeEventEnum.OBJECT_MOVE, gameObject.getObjectType(),
							gameObject.getObjectRectangle().x, gameObject.getObjectRectangle().y,
							gameObjects.indexOf(gameObject));

					if (gameObject.getObjectRectangle()
							.intersectsLine(new Line2D.Double(frog.getObjectRectangle().getX(),
									frog.getObjectRectangle().getMaxY(), frog.getObjectRectangle().getMaxX(),
									frog.getObjectRectangle().getMaxY()))) {

						frog.jump();

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
			notifyObservers(ModelChangeEventEnum.GAME_OVER, null);

		}
	}

}
