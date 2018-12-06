package frogger.model.platforms;

public class BrokenPlatform extends Platform {

	private boolean isBroken = false;
	private int remainingDisplayingTimeAfterBroke = 0;

	public BrokenPlatform(int x, int y) {
		super(x, y, PlatformTypeEnum.BROKEN, true);
	}

	public boolean isBroken() {
		return isBroken;
	}

	public void setBroken() {
		this.isBroken = true;
		this.remainingDisplayingTimeAfterBroke = 20;
	}

	@Override
	public void move() {
		super.move();

		this.remainingDisplayingTimeAfterBroke--;

		if (this.isBroken && this.remainingDisplayingTimeAfterBroke == 0) {
			this.setObjectAlive(false);
		}
	}

}
