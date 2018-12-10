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
    public static int START_GROUND_ROWS = 2;
    public static int ROWS_COUNT = GAME_WINDOW_SIZE.height / GameStaticValues.BLOCK_HEIGHT;
    public static int ROWS_GAP = 3;

    public static int BLOCK_MAX_MOVE_SPEED = 1;


    public static int FROG_WIDTH = 50;
    public static int FROG_HEIGHT = 50;

    public static int FROG_START_X = BLOCK_WIDTH * MAX_BLOCKS_COUNT_IN_ROW / 2;
    public static int FROG_START_Y = GAME_WINDOW_SIZE.height - FROG_HEIGHT -
            Math.abs(BLOCK_HEIGHT - FROG_HEIGHT) / 2 - 1;


    public static int FROG_JUMP_DURATION = 25;
    public static int FROG_VERTICAL_MOVE_SPEED = (int)((double)(BLOCK_HEIGHT + ROWS_GAP) / FROG_JUMP_DURATION);
    public static int FROG_HORIZONTAL_MOVE_SPEED = BLOCK_WIDTH / FROG_JUMP_DURATION;


    public static int THREAD_SLEEP_TIME = 20;

    public static double GRAVITY = 2;

}
