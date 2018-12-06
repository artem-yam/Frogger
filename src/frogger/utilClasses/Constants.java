package frogger.utilClasses;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Random;

import javax.swing.JFrame;

public final class Constants {
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

	public static int START_PLATFORM_X = GAME_WINDOW_SIZE.width / 2 - PLATFORM_WIDTH / 2;
	public static int START_PLATFORM_Y = GAME_WINDOW_SIZE.height * 8 / 10 - PLATFORM_HEIGHT;

	public static int PLATFORM_HORISONTAL_MOVE_START_POINT = (int) GAME_WINDOW_SIZE.getWidth() / 5;
	public static int PLATFORM_HORISONTAL_MOVE_END_POINT = (int) GAME_WINDOW_SIZE.getWidth() * 4 / 5;

	public static int PLATFORM_VERTICAL_MOVE_START_POINT = (int) GAME_WINDOW_SIZE.getHeight() / 5;
	public static int PLATFORM_VERTICAL_MOVE_END_POINT = (int) GAME_WINDOW_SIZE.getHeight() * 4 / 5;

	public static int START_PLATFORMS_COUNT = 20;
	public static int MAX_DISTANCE_BETWEEN_PLATFORMS = 100;

	public static int THREAD_SLEEP_TIME = 10;
	public static int SHIELD_DURATION_TIME = (int) (5000 / Constants.THREAD_SLEEP_TIME);
	public static int JETPACK_DURATION_TIME = (int) (5000 / Constants.THREAD_SLEEP_TIME);
	public static int SPRING_SHOES_JUMPS_COUNT = 5;

	public static int GRAVITY = 1;

	public static int DOODLER_VERTICAL_SPEED = 1;
	public static int DOODLER_JUMP_DURATION = 120;
	public static int DOODLER_HORIZONTAL_SPEED = 2;

	public static int MOVING_PLATFORM_SPEED = 1;

}
