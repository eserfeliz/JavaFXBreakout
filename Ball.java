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
        //return Utils.createBallImageView( (int) width);
        //Circle ball = new Circle((Settings.SCENE_WIDTH / 2.0), (Settings.SCENE_HEIGHT - Settings.PADDLE_Y_OFFSET - Settings.BALL_RADIUS), Settings.BALL_RADIUS, Color.web("black", 0.85));
        Circle ball = new Circle(Settings.BALL_RADIUS);
        ball.setTranslateY(Settings.BALL_RADIUS * -1);
        System.out.println(ball);

        return ball;
    }

    @Override
    public void move() {
        System.out.println("location: " + location.x + ", " + location.y);
        System.out.println("velocity: " + velocity.x + ", " + velocity.y);
        System.out.println("acceleration: " + acceleration.x + ", " + acceleration.y);

        //System.out.println(Main.isBallMoving());
        if (!Main.isBallMoving()) {
            //System.out.println("Here");
            Vector2D dir = Vector2D.subtract(new Vector2D(0.0, 344.0), location);
            dir.normalize();
            dir.multiply(ballMaxSpeed);
            acceleration = dir;
        } else {
            // set velocity depending on acceleration
            velocity.add(acceleration);

            // limit velocity to max speed
            velocity.limit(ballMaxSpeed);

            // change location depending on velocity
            location.add(velocity);

            // angle: towards velocity (ie target)
            // angle = velocity.heading2D();

            // clear acceleration
            //acceleration.multiply(0);
        }

    }

    @Override
    public void track(Vector2D target) {}

    public void checkEdges() {
        if (this.location.x <= (2 * Settings.BALL_RADIUS)) {
            //System.out.println("We SHOULD be turning around here...");
            acceleration.absX();
            velocity.add(acceleration);
            //location.add(velocity);
        }

        if (this.location.x >= (Settings.SCENE_WIDTH - Settings.BALL_RADIUS)) {
            System.out.println(Settings.SCENE_WIDTH - Settings.BALL_RADIUS);
            //velocity.limit(0.0001);
            acceleration.add(new Vector2D(-1.0, 0));
            velocity.add(acceleration);
            //location.add(velocity);
        }

        if (this.location.y <= (2 * Settings.BALL_RADIUS)) {
            //System.out.println("TURNING AROUND AT y = 0!");
            acceleration.absY();
            velocity.add(acceleration);
            //location.add(velocity);
        }
    }

    public void checkPaddle() {

    }
}
