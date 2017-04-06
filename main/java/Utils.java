import com.sun.javafx.geom.BaseBounds;
import com.sun.javafx.geom.transform.BaseTransform;
import com.sun.javafx.jmx.MXNodeAlgorithm;
import com.sun.javafx.jmx.MXNodeAlgorithmContext;
import com.sun.javafx.sg.prism.NGNode;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;

/**
 * Created by lphernandez on 4/2/17.
 */
public class Utils {

    public static double map(double value, double currentRangeStart, double currentRangeStop, double targetRangeStart, double targetRangeStop) {
        return targetRangeStart + (targetRangeStop - targetRangeStart) * ((value - currentRangeStart) / (currentRangeStop - currentRangeStart));
    }

    /**
     * Create an imageview of a right facing arrow.
     * @param size The width. The height is calculated as width / 2.0.
     * @param //height
     * @return
     */
    public static ImageView createBallImageView(double size) {
        return createBallImageView(size, size / 2.0, Color.BLUE, Color.BLUE.deriveColor(1, 1, 1, 0.3), 1);
    }

    /**
     * Create an imageview of a right facing arrow.
     * @param width
     * @param height
     * @return
     */
    public static ImageView createBallImageView(double width, double height, Paint stroke, Paint fill, double strokeWidth) {
        return new ImageView( createBallImage(width, height, stroke, fill, strokeWidth));
    }

    /**
     * Create an image of a right facing arrow.
     * @param width
     * @param height
     * @return
     */
    public static Image createBallImage(double width, double height, Paint stroke, Paint fill, double strokeWidth) {

        WritableImage wi;

        double arrowWidth = width - strokeWidth * 2;
        double arrowHeight = height - strokeWidth * 2;

        System.out.println("In creatBallImage()");

        Circle ball = new Circle( 0, 0, width, Color.BLACK); // left/right lines of the arrow
        ball.setStroke(stroke);
        ball.setFill(fill);
        ball.setStrokeWidth(strokeWidth);
        System.out.println(ball);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);

        int imageWidth = (int) width;
        int imageHeight = (int) height;

        wi = new WritableImage( imageWidth, imageHeight);
        ball.snapshot(parameters, wi);

        return wi;

    }

    public static double getDistance(double ax, double ay, double bx, double by) {
        double a = Math.abs(ax - bx);
        double b = Math.abs(ay - by);

        return Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }


}
