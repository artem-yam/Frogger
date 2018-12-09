package frogger.utilClasses;

import java.awt.*;
import java.util.Random;

public final class GameStaticValues {
    public static Random RND = new Random();
    //  public static Dimension GAME_WINDOW_SIZE = new Dimension(TK.getScreenSize().width / 3,
    //           TK.getScreenSize().height - TK.getScreenInsets(new JFrame().getGraphicsConfiguration()).bottom);
    public static Dimension GAME_WINDOW_SIZE = Toolkit.getDefaultToolkit().getScreenSize();


    public static int BLOCK_WIDTH = 50;
    public static int BLOCK_HEIGHT = 50;

    public static int MAX_BLOCKS_COUNT_IN_ROW = (int) Math.ceil(GAME_WINDOW_SIZE.getWidth() / BLOCK_WIDTH);
    public static int MIN_BLOCKS_COUNT_IN_ROW = MAX_BLOCKS_COUNT_IN_ROW / 10;
    public static int START_GROUND_ROWS = 2;
    public static int ROWS_COUNT = GAME_WINDOW_SIZE.height / GameStaticValues.BLOCK_HEIGHT;


    public static int BLOCK_MAX_MOVE_SPEED = 1;


    public static int FROG_WIDTH = 50;
    public static int FROG_HEIGHT = 50;

    public static int FROG_START_X = BLOCK_WIDTH * MAX_BLOCKS_COUNT_IN_ROW / 2;
    public static int FROG_START_Y = GAME_WINDOW_SIZE.height - FROG_HEIGHT -
            Math.abs(BLOCK_HEIGHT - FROG_HEIGHT) / 2;

    public static int FROG_MOVE_SPEED = 1;
    public static int FROG_JUMP_DURATION = 50;
    public static int DOODLER_HORIZONTAL_SPEED = 2;


    public static int THREAD_SLEEP_TIME = 10;

    public static int GRAVITY = 1;

}
