import javafx.scene.Node;
import javafx.scene.shape.Circle;

import java.util.List;

/**
 * Created by lphernandez on 4/2/17.
 */
public class Ball extends Sprite {

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
            velocity.set(-0.75, -0.75);
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
            setVisible(false);
            velocity.set(0,0);
            acceleration.multiply(0);
            Main.subtractFromBallCount(this);
            Main.setBallsStopped();
            Main.setTurnComplete();
            Main.subtractLife();
        }
        if (location.y <= (Settings.BALL_RADIUS)) {
            velocity.y = velocity.absY();
        }
        location.add(velocity);
    }

    private void collisionWithPaddle(Paddle p, double d) {
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

    private void collisionWithBrick(Brick b, double d) {
        int combinationResult = 0;

        List<Vector2D> brickCollisionPoints = b.getBrickCollisionPoints();

        for (int i = 0; i < brickCollisionPoints.size(); i++) {
            if ((i == 0) && (Settings.BALL_RADIUS >= (brickCollisionPoints.get(i).x - location.x)) && ((brickCollisionPoints.get(i).x - location.x) >= 0)) {
                combinationResult += 1;
            }
            if ((i == 1) && (Settings.BALL_RADIUS >= (brickCollisionPoints.get(i).y - location.y)) && ((brickCollisionPoints.get(i).y - location.y) >= 0)) {
                combinationResult += 3;
            }
            if ((i == 2) && (Settings.BALL_RADIUS >= (location.x - brickCollisionPoints.get(i).x)) && ((location.x - brickCollisionPoints.get(i).x) >= 0)) {
                combinationResult += 6;
            }
            if ((i == 3) && (Settings.BALL_RADIUS >= (location.y - brickCollisionPoints.get(i).y)) && ((location.y - brickCollisionPoints.get(i).y) >= 0)) {
                combinationResult += 10;
            }
            switch (combinationResult) {
                case 0:
                    break;
                case 1:
                    velocity.x = (velocity.absX() * -1);
                    break;
                case 3:
                    velocity.y = (velocity.absY() * -1);
                    break;
                case 4:
                    velocity.x = (velocity.absX() * -1);
                    velocity.y = (velocity.absY() * -1);
                    break;
                case 6:
                    velocity.x = velocity.absX();
                    break;
                case 9:
                    velocity.x = velocity.absX();
                    velocity.y = (velocity.absY() * -1);
                    break;
                case 10:
                    velocity.y = velocity.absY();
                    break;
                case 11:
                    velocity.x = (velocity.absX() * -1);
                    velocity.y = velocity.absY();
                    break;
                case 16:
                    velocity.x = velocity.absX();
                    velocity.y = velocity.absY();
                    break;
                default:
                    break;
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
