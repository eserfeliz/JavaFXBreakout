import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class Main extends Application {

    Layer gameWorld;

    List<Ball> balls = new ArrayList<Ball>();
    Paddle paddle;

    AnimationTimer gameTimer;

    Vector2D mouseLoc = new Vector2D(0, 0), paddleTarget = new Vector2D(0, 0), Vector2D, ballTarget = new Vector2D(0, 0);

    Scene scene;

    MouseGestures mouseGestures = new MouseGestures();

    private static boolean gameStarted = false;
    private static boolean ballMoving = false;

    public void start(Stage primaryStage) throws Exception{
        primaryStage.setTitle("Breakout");
        BorderPane root = new BorderPane();
        gameWorld = new Layer( Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
        Pane layerPane = new Pane();
        layerPane.getChildren().addAll(gameWorld);
        root.setCenter(layerPane);
        scene = new Scene(root, Settings.SCENE_WIDTH, Settings.SCENE_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.show();

        prepareGame();
        addListeners();
        startGame();
    }

    private void startGame() {

        // start game
        gameStarted = true;
        gameTimer = new AnimationTimer() {

            @Override
            public void handle(long now) {

                // seek attractor location, apply force to get towards it
                /*
                allVehicles.forEach(vehicle -> {

                    vehicle.seek( attractor.getLocation());

                });
                */
                paddleTarget = new Vector2D(mouseLoc.x, paddle.getLayoutY());

                paddle.track(paddleTarget);
                balls.forEach(Ball::checkEdges);

                // move sprite

                paddle.move();
                balls.forEach(Sprite::move);
                ballMoving = true;

                // check ball for collision with borders
                balls.forEach(Ball::checkEdges);

                // update in fx scene
                paddle.display();
                balls.forEach(Sprite::display);

            }
        };

        gameTimer.start();

    }

    private void prepareGame() {

        // add vehicles
        addBall();

        // add attractors
        addPaddle();
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
        scene.addEventFilter(MouseEvent.ANY, e -> {
            mouseLoc.set(e.getX(), e.getY());
            //System.out.println(e.getX() + " " + e.getY());
        });

        // move paddle via mouse
        mouseGestures.trackMouseMovement(scene);

    }

    public static boolean isGameStarted() {
        return gameStarted;
    }

    public static boolean isBallMoving() {
        return ballMoving;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
