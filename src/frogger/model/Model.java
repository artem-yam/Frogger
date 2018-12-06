package frogger.model;

import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import frogger.model.ModelChangeEventEnum;
import frogger.model.bonuses.Bonus;
import frogger.model.bonuses.BonusTypeEnum;
import frogger.model.bonuses.SpringShoes;
import frogger.model.platforms.BrokenPlatform;
import frogger.model.platforms.GhostPlatform;
import frogger.model.platforms.HorizontalMovingPlatform;
import frogger.model.platforms.Platform;
import frogger.model.platforms.PlatformTypeEnum;
import frogger.model.platforms.VerticalMovingPlatform;
import frogger.utilClasses.Constants;
import frogger.utilClasses.IObservable;
import frogger.utilClasses.IObserver;

public class Model extends Thread implements IObservable<Model> {

	private Doodler doodler;
	private List<GameObject> gameObjects = new CopyOnWriteArrayList<GameObject>();
	private final List<IObserver<Model>> subscribers = new ArrayList<IObserver<Model>>();

	private int gameGravity = 0;
	private int userScore = 0;

	private boolean isGameActive = false;

	public Model() {
		super();
	}

	public int getGameGravity() {
		return gameGravity;
	}

	public void setGameGravity(int gameGravity) {
		this.gameGravity = gameGravity;
	}

	public Doodler getDoodler() {
		return doodler;
	}

	public void setDoodler(Doodler doodler) {
		this.doodler = doodler;
	}

	public Collection<IObserver<Model>> getSubscribers() {
		return subscribers;
	}

	public boolean isGameActive() {
		return isGameActive;
	}

	public void setGameActive(boolean isGameActive) {
		this.isGameActive = isGameActive;
	}

	@Override
	public synchronized void notifySubscriber(IObserver<Model> observer, ModelChangeEventEnum changeType,
			ObjectTypeEnum objectType, int... values) {
		if (observer == null)
			throw new NullPointerException();
		if (!subscribers.contains(observer))
			throw new IllegalArgumentException(observer.toString());

		observer.observableChanged(new ModelChangeData(changeType, objectType, values));
	}

	@Override
	public synchronized void notifySubscribers(ModelChangeEventEnum changeType, ObjectTypeEnum objectType,
			int... values) {
		for (IObserver<Model> subscriber : subscribers)
			notifySubscriber(subscriber, changeType, objectType, values);
	}

	private void initializeVariables() {
		this.gameGravity = Constants.GRAVITY;
		this.userScore = 0;

		gameObjects = new ArrayList<GameObject>();

		isGameActive = false;

	}

	private void gameStart() {

		initializeVariables();

		this.doodler = new Doodler(Constants.DOODLER_START_X, Constants.DOODLER_START_Y);

		Thread thread = new Thread(doodler);
		thread.start();
		doodler.start();

		notifySubscribers(ModelChangeEventEnum.OBJECT_СREATE, ObjectTypeEnum.DOODLER, doodler.getObjectRectangle().x,
				doodler.getObjectRectangle().y, doodler.getObjectRectangle().width,
				doodler.getObjectRectangle().height);

		gameObjects = new ArrayList<GameObject>();

		Platform plat = new Platform(Constants.START_PLATFORM_X, Constants.START_PLATFORM_Y, true);
		gameObjects.add(plat);

		plat.start();

		notifySubscribers(ModelChangeEventEnum.OBJECT_СREATE, ObjectTypeEnum.PLATFORM,
				((GameObject) gameObjects.get(0)).getObjectRectangle().x,
				((GameObject) gameObjects.get(0)).getObjectRectangle().y,
				((Platform) gameObjects.get(0)).getType().ordinal());

		Platform plat2 = new Platform(Constants.START_PLATFORM_X,
				Constants.START_PLATFORM_Y - Constants.MAX_DISTANCE_BETWEEN_PLATFORMS, true);
		gameObjects.add(plat2);

		plat2.start();

		notifySubscribers(ModelChangeEventEnum.OBJECT_СREATE, ObjectTypeEnum.PLATFORM,
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
				case ENEMY:
					notifySubscribers(ModelChangeEventEnum.OBJECT_DESTROY, ObjectTypeEnum.ENEMY, index);
					break;
				case PLATFORM:
					notifySubscribers(ModelChangeEventEnum.OBJECT_DESTROY, ObjectTypeEnum.PLATFORM, index);
					break;
				case BULLET:
					doodler.getBullets().remove(doodler.getBullets().indexOf(gameObjects.get(index)));
					notifySubscribers(ModelChangeEventEnum.OBJECT_DESTROY, ObjectTypeEnum.BULLET, index);
					break;
				case BONUS:
					notifySubscribers(ModelChangeEventEnum.OBJECT_DESTROY, ObjectTypeEnum.BONUS, index);
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
				newPlatform = new GhostPlatform(platformX, platformY);
			} else if (rnd < 0.15 && !isRequired) {
				newPlatform = new BrokenPlatform(platformX, platformY);
			} else if (rnd < 0.30) {
				newPlatform = new HorizontalMovingPlatform(platformX, platformY);
			} else if (rnd < 0.45 && !isRequired) {
				newPlatform = new VerticalMovingPlatform(platformX, platformY);
			} else {
				newPlatform = new Platform(platformX, platformY);
			}
		} else {
			newPlatform = new Platform(platformX, platformY, true);
		}

		gameObjects.add(newPlatform);

		notifySubscribers(ModelChangeEventEnum.OBJECT_СREATE, ObjectTypeEnum.PLATFORM, platformX, platformY,
				newPlatform.getType().ordinal());

		newPlatform.start();

		initializeBonusThread(newPlatform);

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

	private void generateNewEnemy() {
		int enemyX = Constants.RND.nextInt((int) Constants.GAME_WINDOW_SIZE.getWidth() - 40);

		Enemy enemy = new Enemy(enemyX, 0);

		gameObjects.add(enemy);

		notifySubscribers(ModelChangeEventEnum.OBJECT_СREATE, ObjectTypeEnum.ENEMY, enemyX);

		enemy.start();
	}

	private void generateNewObjects() {
		if (Constants.RND.nextDouble() < 0.05 || isNewPlatformRequired()) {

			generateNewPlatform(false, isNewPlatformRequired());

		}

		if (Constants.RND.nextDouble() < 0.01) {

			generateNewEnemy();

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

	public void doodlerShoot(boolean stopShotting) {

		if (doodler.getActiveBonus() != null) {
			if (doodler.getActiveBonus().getBonusType() == BonusTypeEnum.JETPACK) {
				doodler.setCanShoot(false);
			}
		}

		if (stopShotting) {
			doodler.setCanShoot(true);
		} else if (doodler.isCanShoot()) {

			doodler.setCanShoot(false);
			Bullet bullet = doodler.shoot();

			gameObjects.add(bullet);

			notifySubscribers(ModelChangeEventEnum.OBJECT_СREATE, ObjectTypeEnum.BULLET, bullet.getObjectRectangle().x,
					bullet.getObjectRectangle().y);

			bullet.start();

		}
	}

	public void normalizeObjectsMovement(int gravity) {
		this.gameGravity = gravity;

		for (GameObject gameObject : gameObjects) {
			gameObject.normalizeMovement(this.gameGravity);
		}

		doodler.doodlerJumping();
	}

	private void initializeBonusThread(Platform plat) {
		if (plat.getBonus() != null) {
			gameObjects.add(plat.getBonus());
			notifySubscribers(ModelChangeEventEnum.OBJECT_СREATE, ObjectTypeEnum.BONUS,
					plat.getBonus().getBonusType().ordinal(), gameObjects.indexOf(plat));
		}
	}

	@Override
	public void start() {

		while (true) {

			while (!isGameActive) {

			}

			notifySubscribers(ModelChangeEventEnum.GAME_START, null);

			gameStart();

			long doodlerHighestPosition = (long) doodler.getObjectRectangle().getY();

			while (doodler.isObjectAlive()) {

				generateNewObjects();

				if (doodler.getActiveBonus() == null) {
					notifySubscribers(ModelChangeEventEnum.OBJECT_MOVE, ObjectTypeEnum.DOODLER,
							doodler.getObjectRectangle().x, doodler.getObjectRectangle().y, (int) doodler.getDx(),
							doodler.isShooting() ? 1 : 0);
				} else {
					notifySubscribers(ModelChangeEventEnum.OBJECT_MOVE, ObjectTypeEnum.DOODLER,
							doodler.getObjectRectangle().x, doodler.getObjectRectangle().y, (int) doodler.getDx(),
							doodler.isShooting() ? 1 : 0, doodler.getActiveBonus().getBonusType().ordinal());

					if (doodler.getActiveBonus().getBonusType() == BonusTypeEnum.JETPACK) {
						gameGravity = Constants.GRAVITY * 5;

						for (GameObject gameObject : gameObjects) {
							gameObject.setGravity(gameGravity);
							gameObject.setDy(gameGravity);
						}
					}
				}

				if (doodler.getObjectRectangle().getY() < doodlerHighestPosition) {
					userScore += Math.abs(doodlerHighestPosition - doodler.getObjectRectangle().getY());

					doodlerHighestPosition = (long) doodler.getObjectRectangle().getY();
				}

				notifySubscribers(ModelChangeEventEnum.SCORE_CHANGE, ObjectTypeEnum.DOODLER, userScore);

				doodlerHighestPosition += this.gameGravity;

				deleteDeadObjects();

				for (GameObject gameObject : gameObjects) {

					switch (gameObject.getObjectType()) {
					case PLATFORM:
						if (((Platform) gameObject).getType().equals(PlatformTypeEnum.BROKEN)) {
							notifySubscribers(ModelChangeEventEnum.OBJECT_MOVE, ObjectTypeEnum.PLATFORM,
									gameObject.getObjectRectangle().x, gameObject.getObjectRectangle().y,
									gameObjects.indexOf(gameObject), ((BrokenPlatform) gameObject).isBroken() ? 1 : 0);
						} else {
							notifySubscribers(ModelChangeEventEnum.OBJECT_MOVE, ObjectTypeEnum.PLATFORM,
									gameObject.getObjectRectangle().x, gameObject.getObjectRectangle().y,
									gameObjects.indexOf(gameObject));

						}

						if (doodler.getActiveBonus() != null) {
							if (doodler.getActiveBonus().getBonusType() == BonusTypeEnum.JETPACK) {
								break;
							}
						}

						if (doodler.isFalling() && gameObject.getObjectRectangle()
								.intersectsLine(new Line2D.Double(doodler.getObjectRectangle().getX(),
										doodler.getObjectRectangle().getMaxY(), doodler.getObjectRectangle().getMaxX(),
										doodler.getObjectRectangle().getMaxY()))) {

							if (((Platform) gameObject).getType().equals(PlatformTypeEnum.BROKEN)) {
								((BrokenPlatform) gameObject).setBroken();
							} else {

								doodler.doodlerJumping();

								if (doodler.getActiveBonus() != null) {
									if (doodler.getActiveBonus().getBonusType() == BonusTypeEnum.SPRING_SHOES) {
										((SpringShoes) doodler.getActiveBonus()).doodlerJump();
									}
								}

								if (((Platform) gameObject).getType().equals(PlatformTypeEnum.GHOST)) {
									((GhostPlatform) gameObject).doodlerJumpedOn();
								}
							}

						}

						break;
					case BONUS:

						if (((Bonus) gameObject).getDoodler() != null && !((Bonus) gameObject).isActive()) {
							gameObject.setObjectAlive(false);
						} else {
							notifySubscribers(ModelChangeEventEnum.OBJECT_MOVE, ObjectTypeEnum.BONUS,
									gameObject.getObjectRectangle().x, gameObject.getObjectRectangle().y,
									gameObjects.indexOf(gameObject), ((Bonus) gameObject).isActive() ? 1 : 0);

							if (doodler.getActiveBonus() != null) {
								if ((doodler.getActiveBonus().getBonusType() == BonusTypeEnum.JETPACK)
										|| (((Bonus) gameObject).getBonusType() == BonusTypeEnum.SPRING) && doodler
												.getActiveBonus().getBonusType() == BonusTypeEnum.SPRING_SHOES) {
									break;
								}
							}

							if (gameObject.getObjectRectangle().intersects(doodler.getObjectRectangle())
									&& !((Bonus) gameObject).isActive()) {

								if (doodler.getActiveBonus() != null) {
									doodler.getActiveBonus().setObjectAlive(false);
								}

								if (((Bonus) gameObject).getBonusType() == BonusTypeEnum.SPRING) {
									if (doodler.isFalling()) {
										((Bonus) gameObject).activateBonus(this);
									}
								} else {
									((Bonus) gameObject).activateBonus(this);
									notifySubscribers(ModelChangeEventEnum.BONUS_ACTIVATION, ObjectTypeEnum.BONUS,
											gameObjects.indexOf(gameObject));
								}

							}
						}
						break;
					case ENEMY:
						notifySubscribers(ModelChangeEventEnum.OBJECT_MOVE, ObjectTypeEnum.ENEMY,
								gameObject.getObjectRectangle().x, gameObject.getObjectRectangle().y,
								gameObjects.indexOf(gameObject));

						if (doodler.getActiveBonus() != null) {
							if (doodler.getActiveBonus().getBonusType() == BonusTypeEnum.JETPACK) {
								break;
							}
						}

						if (gameObject.getObjectRectangle().intersects(doodler.getObjectRectangle())) {
							if (gameObject.getObjectRectangle().intersectsLine(new Line2D.Double(
									doodler.getObjectRectangle().getX(), doodler.getObjectRectangle().getMaxY(),
									doodler.getObjectRectangle().getMaxX(), doodler.getObjectRectangle().getMaxY()))
									&& doodler.isFalling()) {

								gameObject.setObjectAlive(false);

								if (doodler.getActiveBonus() != null) {
									if (doodler.getActiveBonus().getBonusType() == BonusTypeEnum.SPRING_SHOES) {
										((SpringShoes) doodler.getActiveBonus()).doodlerJump();
									}
								} else {
									doodler.doodlerJumping();
								}

								break;
							} else if (!doodler.isUnbreakable()) {
								doodler.setObjectAlive(false);
								break;
							}
						}

						for (Bullet bullet : doodler.getBullets()) {
							if (gameObject.getObjectRectangle()
									.intersects(new Rectangle(bullet.getObjectRectangle().x,
											bullet.getObjectRectangle().y, (int) bullet.getObjectRectangle().getWidth(),
											(int) bullet.getObjectRectangle().getHeight()))) {
								gameObject.setObjectAlive(false);
								bullet.setObjectAlive(false);
								break;
							}
						}

						break;
					case BULLET:
						notifySubscribers(ModelChangeEventEnum.OBJECT_MOVE, ObjectTypeEnum.BULLET,
								gameObject.getObjectRectangle().x, gameObject.getObjectRectangle().y,
								gameObjects.indexOf(gameObject));

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
			notifySubscribers(ModelChangeEventEnum.GAME_OVER, null);

		}
	}

}
