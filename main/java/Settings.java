/**
 * Created by lphernandez on 4/2/17.
 */
public class Settings {

    private Settings() {}

    public static final double SCENE_WIDTH = 400;
    public static final double SCENE_HEIGHT = 600;

    public static final int PADDLE_WIDTH = 60;
    public static final int PADDLE_HEIGHT = 10;

    public static int BALL_COUNT = 1;
    public static int PADDLE_COUNT = 1;

    public static double SPRITE_MAX_SPEED = 25;
    public static double SPRITE_MAX_FORCE = 2;

    // distance at which the sprite moves slower towards the target
    public static double SPRITE_SLOW_DOWN_DISTANCE = 100;

    public static final int PADDLE_Y_OFFSET = 550;

    public static final double BALL_MAX_SPEED = 2;

    /** Number of bricks per row */
    public static final int NBRICKS_PER_ROW = 10;

    /** Number of rows of bricks */
    public static final int NBRICK_ROWS = 10;

    /** Separation between bricks */
    public static final int BRICK_SEP = 4;

    /** Width of a brick */
    public static final int BRICK_WIDTH =
            ((int) SCENE_WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

    /** Height of a brick */
    public static final int BRICK_HEIGHT = 8;

    public static final int BRICK_Y_OFFSET = 70;

    public static final int BALL_RADIUS = (int) (BRICK_HEIGHT / 2);
}
