import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

/**
 * Created by lphernandez on 4/2/17.
 */
public class MouseGestures {

    private final MoveContext moveContext = new MoveContext();

    public void trackMouseMovement(final Scene scene) {
        scene.setOnMouseMoved(onMouseMovedEventHandler);
    }

    private EventHandler<MouseEvent> onMouseMovedEventHandler = event -> moveContext.x = event.getX();

    private class MoveContext {
        double x, y;
    }
}
