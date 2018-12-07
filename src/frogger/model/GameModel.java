package frogger.model;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import frogger.model.ModelChangeEventEnum;
import frogger.model.platforms.HorizontalMovingPlatform;
import frogger.model.platforms.Platform;
import frogger.utilClasses.Constants;
import frogger.utilClasses.Observable;
import frogger.utilClasses.Observer;

public class GameModel extends Thread implements Observable {

	private Frog doodler;
	private List<GameObject> gameObjects = new CopyOnWriteArrayList<GameObject>();
	private final List<Observer> observers = new ArrayList<Observer>();

	private int gameGravity = 0;
	private int userScore = 0;

	private boolean isGameActive = false;

	public GameModel() {
		super();
	}

	public int getGameGravity() {
		return gameGravity;
	}

	public void setGameGravity(int gameGravity) {
		this.gameGravity = gameGravity;
	}

	public Frog getDoodler() {
		return doodler;
	}

	public void setDoodler(Frog doodler) {
		this.doodler = doodler;
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
	public synchronized void notifyObservers(ModelChangeEventEnum changeType, ObjectTypeEnum objectType,
			int... values) {
		for (Observer observer : observers)
			observer.handleEvent(new ModelChangeData(changeType, objectType, values));
	}

	private void initializeVariables() {
		this.gameGravity = Constants.GRAVITY;
		this.userScore = 0;

		gameObjects = new ArrayList<GameObject>();

		isGameActive = false;

	}

	private void gameStart() {

		initializeVariables();

		this.doodler = new Frog(Constants.DOODLER_START_X, Constants.DOODLER_START_Y);

		Thread thread = new Thread(doodler);
		thread.start();
		doodler.start();

		notifyObservers(ModelChangeEventEnum.OBJECT_СREATE, ObjectTypeEnum.DOODLER, doodler.getObjectRectangle().x,
				doodler.getObjectRectangle().y, doodler.getObjectRectangle().width,
				doodler.getObjectRectangle().height);

		gameObjects = new ArrayList<GameObject>();

		Platform plat = new Platform(Constants.START_PLATFORM_X, Constants.START_PLATFORM_Y);
		gameObjects.add(plat);

		plat.start();

		notifyObservers(ModelChangeEventEnum.OBJECT_СREATE, ObjectTypeEnum.PLATFORM,
				((GameObject) gameObjects.get(0)).getObjectRectangle().x,
				((GameObject) gameObjects.get(0)).getObjectRectangle().y,
				((Platform) gameObjects.get(0)).getType().ordinal());

		Platform plat2 = new Platform(Constants.START_PLATFORM_X,
				Constants.START_PLATFORM_Y - Constants.MAX_DISTANCE_BETWEEN_PLATFORMS);
		gameObjects.add(plat2);

		plat2.start();

		notifyObservers(ModelChangeEventEnum.OBJECT_СREATE, ObjectTypeEnum.PLATFORM,
				((GameObject) gameObjects.get(1)).getObjectRectangle().x,
				((GameObject) gameObjects.get(1)).getObjectRectangle().y,
				((Platform) gameObjects.get(1)).getType().ordinal());

		for (int i = 0; i < Constants.START_PLATFORMS_COUNT - gameObjects.size(); i++) {

			generateNewPlatform(true, true);

		}

	}

	private void deleteDeadObjects() {

		for (int index = gameObjects.size() - 1; index >= 0; index--) {
			GameObject gameObject = gameObjects.get(index);

			if (!gameObject.isAlive()) {

				switch (gameObject.getObjectType()) {
				case PLATFORM:
					notifyObservers(ModelChangeEventEnum.OBJECT_DESTROY, ObjectTypeEnum.PLATFORM, index);
					break;
				default:
					break;
				}

				gameObjects.remove(index);

			}
		}
	}

	private int highestPlatformPosition() {
		GameObject highestPlatform = null;

		for (GameObject object : gameObjects) {

			if (object.getObjectType().equals(ObjectTypeEnum.PLATFORM)) {

				if (highestPlatform == null) {
					highestPlatform = object;
				} else if (object.getObjectRectangle().y < highestPlatform.getObjectRectangle().y) {
					highestPlatform = object;
				}
			}
		}

		return highestPlatform.getObjectRectangle().y;
	}

	private void generateNewPlatform(boolean gameStart, boolean isRequired) {
		Platform newPlatform = null;

		int platformX = Constants.RND.nextInt((int) Constants.GAME_WINDOW_SIZE.getWidth() - Constants.PLATFORM_WIDTH);

		int platformY = 0;
		if (gameStart) {

			if (this.isNewPlatformRequired()) {
				platformY = highestPlatformPosition() - Constants.MAX_DISTANCE_BETWEEN_PLATFORMS;
			} else {
				platformY = Constants.RND.nextInt((int) Constants.GAME_WINDOW_SIZE.getHeight());
			}
		}

		double rnd = Constants.RND.nextDouble();

		if (!gameStart) {
			if (rnd < 0.05) {
				newPlatform = new HorizontalMovingPlatform(platformX, platformY);
			} else {
				newPlatform = new Platform(platformX, platformY);
			}
		} else {
			newPlatform = new Platform(platformX, platformY);
		}

		gameObjects.add(newPlatform);

		notifyObservers(ModelChangeEventEnum.OBJECT_СREATE, ObjectTypeEnum.PLATFORM, platformX, platformY,
				newPlatform.getType().ordinal());

		newPlatform.start();

	}

	private boolean isNewPlatformRequired() {
		double highestPlatformY = Constants.GAME_WINDOW_SIZE.height;

		for (GameObject object : gameObjects) {

			if (object.getObjectType().equals(ObjectTypeEnum.PLATFORM)
					&& object.getObjectRectangle().y < highestPlatformY)
				highestPlatformY = object.getObjectRectangle().y;

		}

		if (highestPlatformY > Constants.MAX_DISTANCE_BETWEEN_PLATFORMS)
			return true;
		else
			return false;
	}

	private void generateNewObjects() {
		if (Constants.RND.nextDouble() < 0.05 || isNewPlatformRequired()) {

			generateNewPlatform(false, isNewPlatformRequired());

		}

	}

	public void changeDoodlerDx(int dx) {
		doodler.setDx(dx);
	}

	public void changeObjectSize(ObjectTypeEnum objectType, int... values) {
		if (objectType == ObjectTypeEnum.DOODLER) {
			doodler.getObjectRectangle().setSize(values[0], values[1]);
		} else {
			gameObjects.get(values[2]).getObjectRectangle().setSize(values[0], values[1]);
		}
	}

	public void changeObjectLocation(ObjectTypeEnum objectType, int... values) {
		if (objectType == ObjectTypeEnum.DOODLER) {
			doodler.getObjectRectangle().setLocation(values[0], values[1]);
		} else {
			gameObjects.get(values[2]).getObjectRectangle().setLocation(values[0], values[1]);
		}
	}

	public void normalizeObjectsMovement(int gravity) {
		this.gameGravity = gravity;

		for (GameObject gameObject : gameObjects) {
			gameObject.normalizeMovement(this.gameGravity);
		}

		doodler.doodlerJumping();
	}

	@Override
	public void start() {

		while (true) {

			while (!isGameActive) {

			}

			notifyObservers(ModelChangeEventEnum.GAME_START, null);

			gameStart();

			long doodlerHighestPosition = (long) doodler.getObjectRectangle().getY();

			while (doodler.isObjectAlive()) {

				generateNewObjects();

				notifyObservers(ModelChangeEventEnum.OBJECT_MOVE, ObjectTypeEnum.DOODLER,
						doodler.getObjectRectangle().x, doodler.getObjectRectangle().y, (int) doodler.getDx());

				if (doodler.getObjectRectangle().getY() < doodlerHighestPosition) {
					userScore += Math.abs(doodlerHighestPosition - doodler.getObjectRectangle().getY());

					doodlerHighestPosition = (long) doodler.getObjectRectangle().getY();
				}

				notifyObservers(ModelChangeEventEnum.SCORE_CHANGE, ObjectTypeEnum.DOODLER, userScore);

				doodlerHighestPosition += this.gameGravity;

				deleteDeadObjects();

				for (GameObject gameObject : gameObjects) {

					switch (gameObject.getObjectType()) {
					case PLATFORM:
						notifyObservers(ModelChangeEventEnum.OBJECT_MOVE, ObjectTypeEnum.PLATFORM,
								gameObject.getObjectRectangle().x, gameObject.getObjectRectangle().y,
								gameObjects.indexOf(gameObject));

						if (doodler.isFalling() && gameObject.getObjectRectangle()
								.intersectsLine(new Line2D.Double(doodler.getObjectRectangle().getX(),
										doodler.getObjectRectangle().getMaxY(), doodler.getObjectRectangle().getMaxX(),
										doodler.getObjectRectangle().getMaxY()))) {

							doodler.doodlerJumping();

						}

						break;
					default:
						break;
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
