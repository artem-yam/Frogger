package frogger.model.platforms;

public class GhostPlatform extends Platform {

	public GhostPlatform(int x, int y) {
		super(x, y, PlatformTypeEnum.GHOST, true);
	}

	public void doodlerJumpedOn() {
		this.setObjectAlive(false);
	}

}
