package frogger.model.platforms;

import frogger.model.GameObject;
import frogger.model.ObjectTypeEnum;
import frogger.utilClasses.Constants;

public class Platform extends GameObject {

	private PlatformTypeEnum type = PlatformTypeEnum.NORMAL;

	public Platform(int x, int y) {
		super(x, y, 0, 0);

		this.setObjectType(ObjectTypeEnum.PLATFORM);

		this.setDy(this.getGravity());

	}

	public Platform(int x, int y, PlatformTypeEnum type) {
		this(x, y);

		this.type = type;
	}

	public PlatformTypeEnum getType() {
		return type;
	}

	public void setType(PlatformTypeEnum type) {
		this.type = type;
	}

	@Override
	public void run() {

		while (this.isObjectAlive()) {
			this.move();

			try {
				Thread.sleep(Constants.THREAD_SLEEP_TIME);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
