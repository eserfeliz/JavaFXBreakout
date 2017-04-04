import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

/**
 * Created by lphernandez on 4/2/17.
 */
public class MouseGestures {

    final MoveContext moveContext = new MoveContext();

    public void trackMouseMovement(final Scene scene) {

        //sprite.setOnMousePressed(onMousePressedEventHandler);
        //sprite.setOnMouseDragged(onMouseDraggedEventHandler);
        //sprite.setOnMouseReleased(onMouseReleasedEventHandler);
        scene.setOnMouseMoved(onMouseMovedEventHandler);

    }

    EventHandler<MouseEvent> onMouseMovedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent event) {
            //Sprite sprite = (Sprite) event.getSource();

            double offsetX = event.getSceneX() - moveContext.x;
            //double offsetY = event.getSceneY() - moveContext.y;

            //sprite.setLocationOffset(offsetX, offsetY);
            //sprite.setLayoutX(event.getX());

            moveContext.x = event.getSceneX();
            //moveContext.y = event.getSceneY();
        }
    };

    /*
    EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            dragContext.x = event.getSceneX();
            dragContext.y = event.getSceneY();

        }
    };

    EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

            Sprite sprite = (Sprite) event.getSource();

            double offsetX = event.getSceneX() - dragContext.x;
            double offsetY = event.getSceneY() - dragContext.y;

            sprite.setLocationOffset(offsetX, offsetY);

            dragContext.x = event.getSceneX();
            dragContext.y = event.getSceneY();

        }
    };

    EventHandler<MouseEvent> onMouseReleasedEventHandler = new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {

        }
    };

    class DragContext {

        double x;
        double y;

    }
    */

    class MoveContext {

        double x;
        double y;

    }
}
