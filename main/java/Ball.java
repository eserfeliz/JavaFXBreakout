import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.transform.Transform;

import java.util.function.Consumer;

/**
 * Created by lphernandez on 4/2/17.
 */
public class Ball extends Sprite {

    final double ballMaxSpeed = Settings.BALL_MAX_SPEED;

    boolean leftSideDetected = false, rightSideDetected = false;

    public Ball(Layer layer, Vector2D location, Vector2D velocity, Vector2D acceleration, double width, double height) {
        super(layer, location, velocity, acceleration, width, height);
    }

    @Override
    public Node createView() {
        Circle ball;
        return ball = new Circle(Settings.BALL_RADIUS);
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

    public void execute(Paddle p, Consumer<Paddle> c) {
        c.accept(p);
    }

    public void collisionWithPaddle(Paddle p) {
        Transform ballTransform = getLocalToParentTransform();

        Bounds paddleBounds = p.localToScreen(p.getBoundsInLocal());

        double ballTransformTx = ballTransform.getTx(), ballTransformTy = ballTransform.getTy();
        double paddleLayoutMinX = p.getLayoutX(), paddleLayoutMinY = p.getLayoutY(),
                paddleLayoutMaxX = (p.getLayoutX() + Settings.PADDLE_WIDTH), paddleLayoutMaxY = (p.getLayoutY() + Settings.PADDLE_HEIGHT);

        double closestXOnPaddle, closestYOnPaddle;

        if (ballTransformTx <= paddleLayoutMinX) {
            closestXOnPaddle = paddleLayoutMinX + 1;
        } else if (ballTransformTx >= paddleLayoutMaxX) {
            closestXOnPaddle = paddleLayoutMaxX - 1;
        } else {
            closestXOnPaddle = ballTransformTx;
        }

        if (ballTransformTy <= paddleLayoutMinY) {
            closestYOnPaddle = paddleLayoutMinY + 1;
        } else if (ballTransformTy >= paddleLayoutMaxY) {
            closestYOnPaddle = paddleLayoutMaxY - 1;
        } else {
            closestYOnPaddle = ballTransformTy;
        }

        double ballPaddleDistance = Utils.getDistance(ballTransformTx, ballTransformTy, closestXOnPaddle, closestYOnPaddle);
        //System.out.println(ballPaddleDistance);
        if (ballPaddleDistance <= Settings.BALL_RADIUS) {
            if (ballTransformTx <= (paddleLayoutMinX + 10)) {
                leftSideDetected = true;
                rightSideDetected = false;
                velocity.y = velocity.absY() * -1;
                velocity.x = velocity.absX() * -1;
            } else if (ballTransformTx >= (paddleLayoutMaxX - 10)) {
                leftSideDetected = false;
                rightSideDetected = true;
                velocity.y = velocity.absY() * -1;
                velocity.x = velocity.absX();
            } else {
                leftSideDetected = false;
                rightSideDetected = false;
                velocity.y = velocity.absY() * -1;
            }
            location.add(velocity);
        }
    }
}
