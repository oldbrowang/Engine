package oldbro.mark;

import javafx.animation.*;
import javafx.scene.effect.Reflection;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.transform.Shear;
import javafx.util.Duration;

public class Logo extends MarkComponent
{
    public static String name = "OLDBRO ENGINE";

    public static Text text;

    public static void load()
    {
        text = new Text(name);
        text.setFill(Color.WHITESMOKE);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 55));
        text.setTextAlignment(TextAlignment.CENTER);
        text.setLayoutY(650);
        text.setLayoutX(270);
        addToRoot(text);
    }

    public static void display()
    {
        FadeTransition ft = new FadeTransition(Duration.millis(5000), text);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.play();
        Reflection reflection = new Reflection();
        reflection.setBottomOpacity(0.1);
        reflection.setTopOpacity(0.2);
        reflection.setFraction(1);
        text.setEffect(reflection);
        Shear shear = new Shear();
        shear.setX(-0.3);
        text.getTransforms().add(shear);
        KeyFrame keyFrame1  = new KeyFrame(Duration.millis(2500), new KeyValue(shear.xProperty(), 0));
        KeyFrame keyFrame2  = new KeyFrame(Duration.millis(5000), new KeyValue(shear.xProperty(), 0.3));
        Timeline timeline  = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame1, keyFrame2);
        timeline.play();
    }
}
