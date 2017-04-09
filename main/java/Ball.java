import javafx.scene.Node;
import javafx.scene.shape.Circle;

/**
 * Created by lphernandez on 4/2/17.
 */
public class Ball extends Sprite {

    double ballMaxSpeed = Settings.BALL_MAX_SPEED;

    public Ball(Layer layer, Vector2D location, Vector2D velocity, Vector2D acceleration, double width, double height) {
        super(layer, location, velocity, acceleration, width, height);
    }

    @Override
    public Node createView() {
        return new Circle(Settings.BALL_RADIUS);
    }

    @Override
    public void move() {
        if (!Main.isBallMoving()) {
            velocity.set(-0.5, -0.5);
            location.add(velocity);
        }
        velocity.limit(maxSpeed);
        location.add(velocity);
    }

    @Override
    public void track(Vector2D target) {}

    public void collisionAtBoundary() {
        if (location.x >= Settings.SCENE_WIDTH) {
            velocity.x = velocity.absX() * -1;
        }
        if (location.x <= (Settings.BALL_RADIUS)) {
            velocity.x = velocity.absX();
        }
        if (location.y >= Settings.SCENE_HEIGHT) {
            velocity.y = velocity.absY() * -1;
        }
        if (location.y <= (Settings.BALL_RADIUS)) {
            velocity.y = velocity.absY();
        }
        location.add(velocity);
    }

    public void collisionWithPaddle(Paddle p, double d) {
        if (Settings.BALL_RADIUS >= d) {
            if (location.x <= (p.location.x - ((int) (p.width / 2)) + 10)) {
                velocity.y = velocity.absY() * -1;
                velocity.x = velocity.absX() * -1;
            } else if (location.x >= (p.location.x + ((int) (p.width / 2)) - 10)) {
                velocity.y = velocity.absY() * -1;
                velocity.x = velocity.absX();
            } else {
                velocity.y = velocity.absY() * -1;
            }
            location.add(velocity);
        }
    }

    public void collisionWithBrick(Brick b, double d) {
        if ((Settings.BALL_RADIUS >= d) && b.isVisible()) {
            if (Settings.BALL_RADIUS >= Math.abs(((int) (b.location.x - (b.width / 2))) - location.x)) {
                velocity.x = (velocity.absX() * -1);
            } else if (Settings.BALL_RADIUS >= Math.abs(location.x - ((int) (b.location.x + (b.width / 2))))) {
                velocity.x = velocity.absX();
            }
            if (Settings.BALL_RADIUS >= Math.abs(location.y - ((int) (b.location.y + (b.height / 2))))) {
                velocity.y = velocity.absY();
            } else if (Settings.BALL_RADIUS >= Math.abs(((int) (b.location.y - (b.height / 2))) - location.y)) {
                velocity.y = (velocity.absY() * -1);
            }
            location.add(velocity);
            b.setVisible(false);
        }
    }

    public void collisionWithFieldObj(Sprite s) {

        double sprMinX = ((int) (s.location.x - s.width / 2)), sprMinY = ((int) (s.location.y - s.height / 2)),
                sprMaxX = ((int) (s.location.x + s.width / 2)), sprMaxY = ((int) (s.location.y + s.height / 2));

        double closestXOnSpr, closestYOnSpr;

        if (location.x <= sprMinX) {
            closestXOnSpr = sprMinX;
        } else if (location.x >= sprMaxX) {
            closestXOnSpr = sprMaxX;
        } else {
            closestXOnSpr = location.x;
        }

        if (location.y <= sprMinY) {
            closestYOnSpr = sprMinY;
        } else if (location.y >= sprMaxY) {
            closestYOnSpr = sprMaxY;
        } else {
            closestYOnSpr = location.y;
        }

        double ballSprDistance = Utils.getDistance(location.x, location.y, closestXOnSpr, closestYOnSpr);

        if (ballSprDistance <= Settings.BALL_RADIUS) {
            if (s instanceof Paddle) {
                collisionWithPaddle((Paddle) s, ballSprDistance);
            } else if (s instanceof Brick) {
                collisionWithBrick((Brick) s, ballSprDistance);
            }
        }
    }
}
