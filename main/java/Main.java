import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.function.Consumer;

public class Main extends Application {

    private static Layer gameWorld;

    private static List<Ball> balls = new ArrayList<>();
    private static List<Ball> ballsToRemove = new ArrayList<>();
    private List<Brick> bricks = new ArrayList<>();
    private Paddle paddle;

    private AnimationTimer gameTimer;

    private Vector2D mouseLoc = new Vector2D(0, 0), paddleTarget = new Vector2D(0, 0);
    private boolean mouseClick = false;

    private Scene scene;

    private MouseGestures mouseGestures = new MouseGestures();

    private static boolean gameStarted = false;
    private static boolean ballMoving = false;
    private static boolean turnInProgress = false;

    private int bricksRemaining = 0;
    public static int livesRemaining = 2;
    public static int numOfBalls = 0;

    public void start(Stage primaryStage) throws Exception {
        if (!gameStarted) {
            prepareGame(primaryStage);
            addListeners();
            gameStarted = true;
        }
        startGame();
    }

    private void startGame() {

        // start game
        gameTimer = new AnimationTimer() {

            /**
             * Starts the {@code AnimationTimers}. Once it is started, the
             * handle(long) method of this {@code AnimationTimers} will be
             * called in every frame.
             *
             * The {@code AnimationTimers} can be stopped by calling {@link #stop()}.
             */

            @Override
            public void handle(long now) {
                paddleTarget = new Vector2D(mouseLoc.x, paddle.getLayoutY());
                paddle.track(paddleTarget);

                doHandle();

                scene.setOnMouseClicked(event -> {
                    if (!isTurnInProgress() && (livesRemaining >= 0)) {
                        try {
                            for (Ball ball : balls) {
                                ball.location.set(gameWorld.getWidth() / 2, Settings.PADDLE_Y_OFFSET - Settings.BALL_RADIUS);
                                ball.velocity = new Vector2D( 0,0);
                                ball.acceleration = new Vector2D( 0,0);
                                ball.setVisible(true);
                                numOfBalls++;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        setTurnInProgress();

                        try {
                            for (Ball ball : balls) {
                                ball.velocity.set(-0.75, -0.75);
                                ball.location.add(ball.velocity);
                            }
                            setBallsMoving();
                            balls.forEach(Sprite::move);
                        } catch (ConcurrentModificationException cme) {
                            cme.printStackTrace();
                        }

                        balls.forEach(Sprite::display);
                    }
                });
            }
        };
        gameTimer.start();
    }

    private void doHandle() {
        try {
            balls.forEach(Ball::collisionAtBoundary);
        } catch (ConcurrentModificationException cme) {
            cme.printStackTrace();
        }

        // move sprite
        paddle.move();
        try {
            balls.forEach(Ball::move);
            if (numOfBalls > 0) {
                setBallsMoving();
            } else {
                setBallsStopped();
            }
        } catch (ConcurrentModificationException cme) {
            cme.printStackTrace();
        }

        if (isBallMoving()) {
            setTurnInProgress();
        }

        // check ball for collision with paddle
        for (Ball ball:balls) {
            execute(paddle, ball::collisionWithFieldObj);
        }

        // check ball for collision with bricks

        for (Ball ball:balls) {
            for (int i = bricks.size() - 1; i >= 0; i--) {
                if (bricks.get(i).isVisible()) {
                    execute(bricks.get(i), ball::collisionWithFieldObj);
                }
            }
        }

        // update in fx scene
        paddle.display();
        balls.forEach(Sprite::display);
    }

    private void execute(Sprite sprite, Consumer<Sprite> s) { s.accept(sprite);}

    private void prepareGame(Stage primaryStage) {
        primaryStage.setTitle("Breakout");
        BorderPane root = new BorderPane();
        gameWorld = new Layer( Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
        Pane layerPane = new Pane();
        layerPane.getChildren().addAll(gameWorld);
        root.setCenter(layerPane);
        scene = new Scene(root, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();

        // add vehicles
        addBall();

        // add attractors
        addPaddle();

        // add Bricks
        addBricks();
    }

    private void addBricks() {
        int xPos = ((Settings.BRICK_SEP / 2) + (Settings.BRICK_WIDTH / 2));
        int yPos = Settings.BRICK_Y_OFFSET;

        for (int i = 1; i <= Settings.NBRICK_ROWS; i++) {
            for (int j = 1; j <= Settings.NBRICKS_PER_ROW; j++) {
                String brickColor = "";

                switch (i) {
                    case 1:
                    case 2:
                        brickColor = "red";
                        break;
                    case 3:
                    case 4:
                        brickColor = "orange";
                        break;
                    case 5:
                    case 6:
                        brickColor = "yellow";
                        break;
                    case 7:
                    case 8:
                        brickColor = "green";
                        break;
                    case 9:
                    case 10:
                        brickColor = "cyan";
                        break;
                    default:
                        break;
                }
                addOneBrick(xPos, yPos, brickColor);
                addOneBrickToCount();
                xPos = xPos + Settings.BRICK_SEP + Settings.BRICK_WIDTH;
            }
            xPos = ((Settings.BRICK_SEP / 2) + (Settings.BRICK_WIDTH / 2));
            yPos += Settings.BRICK_HEIGHT + Settings.BRICK_SEP;
        }
    }

    private void addOneBrick(double xPos, double yPos, String color) {

        Layer layer = gameWorld;

        double width = Settings.BRICK_WIDTH;
        double height = Settings.BRICK_HEIGHT;

        Vector2D location = new Vector2D(xPos,yPos);
        Vector2D velocity = new Vector2D( 0,0);
        Vector2D acceleration = new Vector2D( 0,0);

        Brick brick = new Brick(layer, location, velocity, acceleration, width, height, color);
        bricks.add(brick);
    }

    private void addPaddle() {

        Layer layer = gameWorld;

        // center paddle
        double x = layer.getWidth() / 2;
        double y = Settings.PADDLE_Y_OFFSET;

        // dimensions
        double width = Settings.PADDLE_WIDTH;
        double height = Settings.PADDLE_HEIGHT;

        // create paddle data
        Vector2D location = new Vector2D( x,y);
        Vector2D velocity = new Vector2D( 0,0);
        Vector2D acceleration = new Vector2D( 0,0);

        paddle = new Paddle( layer, location, velocity, acceleration, width, height);
    }

    private void addBall() {

        Layer layer = gameWorld;

        // random location
        double x = layer.getWidth() / 2;
        double y = Settings.PADDLE_Y_OFFSET - Settings.BALL_RADIUS;

        // dimensions
        double width = Settings.BALL_RADIUS;
        double height = Settings.BALL_RADIUS;

        // create vehicle data
        Vector2D location = new Vector2D( x,y);
        Vector2D velocity = new Vector2D( 0,0);
        Vector2D acceleration = new Vector2D( 0,0);

        // create sprite and add to layer
        Ball ball = new Ball( layer, location, velocity, acceleration, width, height);

        // register vehicle
        balls.add(ball);
        numOfBalls++;
    }

    private void addListeners() {

        // capture mouse position
        scene.addEventFilter(MouseEvent.MOUSE_MOVED, e -> mouseLoc.set(e.getX(), e.getY()));

        // move paddle via mouse
        mouseGestures.trackMouseMovement(scene);
    }

    public static boolean isGameStarted() {
        return gameStarted;
    }

    public static boolean isBallMoving() {
        return ballMoving;
    }

    public int getBricksRemaining() { return bricksRemaining; }

    public static void setBallsStopped() { ballMoving = false; }

    public static void setTurnComplete() { turnInProgress = false; }

    public static void removeBall(Ball ball) {
        numOfBalls--;
    }

    public static void subtractLife() {
        if (livesRemaining >= 0) {
            livesRemaining--;
        }
    }

    private void addOneBrickToCount() { bricksRemaining++; }

    private static void setBallsMoving() { ballMoving = true; }

    private static void setTurnInProgress() { turnInProgress = true; }

    private static boolean isTurnInProgress() { return turnInProgress; }






    public static void main(String[] args) {
        launch(args);
    }

}
