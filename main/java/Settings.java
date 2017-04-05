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

    public static final int BALL_RADIUS = 6;
    public static final double BALL_MAX_SPEED = 4;
}
