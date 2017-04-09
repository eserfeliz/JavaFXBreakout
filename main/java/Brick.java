import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by lphernandez on 4/6/17.
 */
public class Brick extends Sprite {

    public Brick(Layer layer, Vector2D location, Vector2D velocity, Vector2D acceleration, double width, double height, String brickColor) {
        super(layer, location, velocity, acceleration, width, height, brickColor);
    }

    @Override
    public Node createView() {
        Rectangle brick = new Rectangle(((int) (location.x - width / 2)), ((int) (location.y - height / 2)), Settings.BRICK_WIDTH, Settings.BRICK_HEIGHT);
        brick.setFill(Color.web(color, 0.50));

        return brick;
    }

    @Override
    public void move() {

    }

    @Override
    public void track(Vector2D target) {

    }
}
