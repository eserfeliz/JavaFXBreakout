import javafx.print.Collation;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Created by lphernandez on 4/2/17.
 */
public class Paddle extends Sprite {

    public Paddle(Layer layer, Vector2D location, Vector2D velocity, Vector2D acceleration, double width, double height) {
        super(layer, location, velocity, acceleration, width, height);
    }

    @Override
    public Node createView() {

        Rectangle rect = new Rectangle(this.width, this.height);

        rect.setStroke(Color.BLACK);
        rect.setFill(Color.BLACK);
        return rect;
    }

    @Override
    public void move() {
        //System.out.println("location: " + location.x + ", " + location.y);
        //System.out.println("velocity: " + velocity.x + ", " + velocity.y);
        //System.out.println("acceleration: " + acceleration.x + ", " + acceleration.y);
        this.acceleration.y = 0;
        velocity.add(acceleration);
        velocity.limit(maxSpeed);
        location.add(velocity);
        if (this.location.x <= this.width) {
            acceleration.multiply(0.1);
            if (this.location.x <= (this.width / 2)) {
                location.set((width / 2), location.y);
                acceleration.multiply(0.01);
            }
        }
        if (this.location.x >= this.width) {
            acceleration.multiply(0.1);
            if (location.x >= (Settings.SCENE_WIDTH - (width / 2))) {
                location.set(Settings.SCENE_WIDTH - (width / 2), location.y);
                acceleration.multiply(0.01);
            }
        }

        acceleration.multiply(0);
    }

    @Override
    public void track(Vector2D target) {
        Vector2D desired = Vector2D.subtract(target, location);

        // The distance is the magnitude of the vector pointing from location to target.

        double d = desired.magnitude();
        desired.normalize();

        // If we are closer than 100 pixels...
        if (d < Settings.SPRITE_SLOW_DOWN_DISTANCE) {

            // ...set the magnitude according to how close we are.
            double m = Utils.map(d, 0, Settings.SPRITE_SLOW_DOWN_DISTANCE, 0, maxSpeed);
            desired.multiply(m);

        }
        // Otherwise, proceed at maximum speed.
        else {
            desired.multiply(maxSpeed);
        }

        // The usual steering = desired - velocity
        Vector2D steer = Vector2D.subtract(desired, velocity);
        steer.limit(maxForce);

        applyForce(steer);
    }
}
