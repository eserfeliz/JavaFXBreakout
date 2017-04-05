import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Created by lphernandez on 4/2/17.
 */
public class Ball extends Sprite {

    final double ballMaxSpeed = Settings.BALL_MAX_SPEED;

    public Ball(Layer layer, Vector2D location, Vector2D velocity, Vector2D acceleration, double width, double height) {
        super(layer, location, velocity, acceleration, width, height);
    }

    @Override
    public Node createView() {
        Circle ball = new Circle(Settings.BALL_RADIUS);
        ball.setTranslateY(Settings.BALL_RADIUS * -1);
        System.out.println(ball);

        return ball;
    }

    @Override
    public void move() {
        if (!Main.isBallMoving()) {
            velocity.set(0.5, -0.5);
            location.add(velocity);
        }
        location.add(velocity);

    }

    @Override
    public void track(Vector2D target) {}

    public void checkEdges() {
        if (location.x >= Settings.SCENE_WIDTH) {
            velocity.x = velocity.absX() * -1;
        }
        if (location.x <= (0 + Settings.BALL_RADIUS)) {
            velocity.x = velocity.absX();
        }
        if (location.y >= Settings.SCENE_HEIGHT) {
            velocity.y = velocity.absY() * -1;
        }
        if (location.y <= (0 + Settings.BALL_RADIUS)) {
            velocity.y = velocity.absY();
        }
        location.add(velocity);
    }

    public void checkPaddle() {

    }
}
