import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

/**
 * Created by lphernandez on 4/2/17.
 */
public class MouseGestures {

    final MoveContext moveContext = new MoveContext();

    public void trackMouseMovement(final Scene scene) {

        scene.setOnMouseMoved(onMouseMovedEventHandler);

    }

    EventHandler<MouseEvent> onMouseMovedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {

            double offsetX = event.getSceneX() - moveContext.x;
            moveContext.x = event.getSceneX();
        }
    };

    class MoveContext {

        double x;
        double y;

    }
}
