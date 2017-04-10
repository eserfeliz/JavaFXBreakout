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
    private List<Brick> bricks = new ArrayList<>();
    private Paddle paddle;

    private AnimationTimer gameTimer;

    private Vector2D mouseLoc = new Vector2D(0, 0), paddleTarget = new Vector2D(0, 0);
    private boolean mouseClick = false;

    private Scene scene;

    private MouseGestures mouseGestures = new MouseGestures();

    private static boolean gameStarted = false;
    private static boolean ballMoving = false;

    private int bricksRemaining = 0;
    private int livesRemaining = 2;

    public void start(Stage primaryStage) throws Exception{
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

            @Override
            public void handle(long now) {
                doHandle();
                scene.setOnMouseClicked(event -> {
                    if (!isBallMoving() && (livesRemaining > 0)) {
                        addBall();
                        gameTimer.start();
                        livesRemaining--;
                    }
                });
            }

            private void doHandle() {
                paddleTarget = new Vector2D(mouseLoc.x, paddle.getLayoutY());

                paddle.track(paddleTarget);

                // check ball for collision with borders
                try {
                    balls.forEach(Ball::collisionAtBoundary);
                } catch (ConcurrentModificationException cme) {
                    gameTimer.stop();
                }

                // move sprite
                paddle.move();
                balls.forEach(Sprite::move);
                setBallsMoving();

                if (balls.size() == 0) {
                    setBallsStopped();
                } else {

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
            }
            private void execute(Sprite sprite, Consumer<Sprite> s) { s.accept(sprite);}
        };
        gameTimer.start();
    }

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
                addBricks(xPos, yPos, brickColor);
                addOneBrickToCount();
                xPos = xPos + Settings.BRICK_SEP + Settings.BRICK_WIDTH;
            }
            xPos = ((Settings.BRICK_SEP / 2) + (Settings.BRICK_WIDTH / 2));
            yPos += Settings.BRICK_HEIGHT + Settings.BRICK_SEP;
        }
    }

    private void addBricks(double xPos, double yPos, String color) {

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

    private void addOneBrickToCount() { bricksRemaining++; }

    public int getBricksRemaining() { return bricksRemaining; }

    private static void setBallsStopped() { ballMoving = false; }

    private static void setBallsMoving() { ballMoving = true; }

    public static void main(String[] args) {
        launch(args);
    }

    public static void removeBall(Ball ball) {
        gameWorld.getChildren().remove(ball);
        balls.remove(ball);
    }
}
