import javafx.scene.SnapshotParameters;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

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
     * @return ImageView
     */
    public static ImageView createBallImageView(double size) {
        return createBallImageView(size, size / 2.0, Color.BLUE, Color.BLUE.deriveColor(1, 1, 1, 0.3), 1);
    }

    /**
     * Create an imageview of a right facing arrow.
     * @param width The width.
     * @param height The height.
     * @return ImageView
     */
    private static ImageView createBallImageView(double width, double height, Paint stroke, Paint fill, double strokeWidth) {
        return new ImageView( createBallImage(width, height, stroke, fill, strokeWidth));
    }

    /**
     * Create an image of a right facing arrow.
     * @param width The width.
     * @param height The height.
     * @return Image
     */
    private static Image createBallImage(double width, double height, Paint stroke, Paint fill, double strokeWidth) {

        WritableImage wi;

        Circle ball = new Circle( 0, 0, width, Color.BLACK);
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
