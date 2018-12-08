package frogger.utilClasses;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public final class GameStaticValues {
    public static Random RND = new Random();
    public static Toolkit TK = Toolkit.getDefaultToolkit();
    public static Dimension GAME_WINDOW_SIZE = new Dimension(TK.getScreenSize().width / 3,
            TK.getScreenSize().height - TK.getScreenInsets(new JFrame().getGraphicsConfiguration()).bottom);


    public static int BLOCK_WIDTH = 50;
    public static int BLOCK_HEIGHT = 50;

    public static int MIN_BLOCKS_COUNT_IN_ROW = 3;
    public static int MAX_BLOCKS_COUNT_IN_ROW = (int) Math.ceil(GAME_WINDOW_SIZE.getWidth() / BLOCK_WIDTH);
    public static int START_GROUND_ROWS = 2;

    public static int DOODLER_WIDTH = 60;
    public static int DOODLER_HEIGHT = 60;

    public static int DOODLER_START_X = GAME_WINDOW_SIZE.width / 2 - DOODLER_WIDTH / 2;
    public static int DOODLER_START_Y = GAME_WINDOW_SIZE.height * 8 / 10 - DOODLER_HEIGHT - 50;

    public static int START_PLATFORMS_COUNT = 20;

    public static int THREAD_SLEEP_TIME = 10;
    public static int SPRING_SHOES_JUMPS_COUNT = 5;

    public static int GRAVITY = 1;

    public static int FROG_MOVE_SPEED = 1;
    public static int DOODLER_JUMP_DURATION = 120;
    public static int DOODLER_HORIZONTAL_SPEED = 2;

    public static int MOVING_PLATFORM_SPEED = 1;

}
