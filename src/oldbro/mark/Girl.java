package oldbro.mark;

import javafx.animation.TranslateTransition;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import oldbro.Scenes;

public class Girl extends MarkComponent
{
    public static String girlGif;

    public static Image img;

    public static ImageView girlView;

    public static Rectangle line;

    static {
        girlGif = "resource/girl.gif";
    }

    public static void load()
    {
        girlView = new ImageView(new Image(getInputStreamByFileName(girlGif)));
        addToRoot(girlView);
    }

    public static void walk()
    {
        TranslateTransition transition = new TranslateTransition(Duration.millis(5000), girlView);
        transition.setFromX(Scenes.wWidth - 250);
        transition.setToX(-640);
        transition.play();
        transition.setOnFinished(e -> {
            allDone();
        });
    }

    public static void drawline()
    {
        line = new Rectangle();
        line.setFill(Color.WHITE);
        line.setWidth(Scenes.wWidth);
        line.setHeight(10);
        line.setLayoutY(439);
        addToRoot(line);
        TranslateTransition transition = new TranslateTransition(Duration.millis(5000), line);
        transition.setFromX(Scenes.wWidth);
        transition.setToX(-Scenes.wWidth);
        transition.play();
    }
}
