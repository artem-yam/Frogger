package frogger.utilClasses;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.JFrame;

public final class GameStaticValues {
	public static Random RND = new Random();
	public static Toolkit TK = Toolkit.getDefaultToolkit();
	public static Dimension GAME_WINDOW_SIZE = new Dimension(TK.getScreenSize().width / 3,
			TK.getScreenSize().height - TK.getScreenInsets(new JFrame().getGraphicsConfiguration()).bottom);

	public static int DOODLER_WIDTH = 60;
	public static int DOODLER_HEIGHT = 60;

	public static int DOODLER_START_X = GAME_WINDOW_SIZE.width / 2 - DOODLER_WIDTH / 2;
	public static int DOODLER_START_Y = GAME_WINDOW_SIZE.height * 8 / 10 - DOODLER_HEIGHT - 50;

	public static int PLATFORM_WIDTH = 70;
	public static int PLATFORM_HEIGHT = 20;

	public static int START_PLATFORMS_COUNT = 20;

	public static int THREAD_SLEEP_TIME = 10;
	public static int SPRING_SHOES_JUMPS_COUNT = 5;

	public static int GRAVITY = 1;

	public static int DOODLER_VERTICAL_SPEED = 1;
	public static int DOODLER_JUMP_DURATION = 120;
	public static int DOODLER_HORIZONTAL_SPEED = 2;

	public static int MOVING_PLATFORM_SPEED = 1;

}
