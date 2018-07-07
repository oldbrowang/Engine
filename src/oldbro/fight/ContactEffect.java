package oldbro.fight;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import oldbro.util.Random;


public class ContactEffect extends FightComponent
{
    static String[] textStrSelections;

    static int textFontSize;

    static int upLeftOffset;

    static int lineLength;

    static int centerRadius;

    static AudioClip hitPlayer;

    static {
        textStrSelections = new String[]{"啪", "噼", "哗", "咚", "嗒", "唰", "嘣", "咔", "啷"};
        textFontSize = 45;
        lineLength = 100;
        upLeftOffset = lineLength / 2;
        centerRadius = 30;
        hitPlayer = new AudioClip(getFileUriStringByFileName("resource/hit.wav"));
    }

    static Text getText(Color textColor)
    {
        String str;
        Text text;
        str = textStrSelections[Random.getRandomInteger(0, textStrSelections.length - 1)];
        text = new Text(str);
        text.setFont(Font.font("KaiTi", textFontSize));
        text.setFill(textColor);
        return text;
    }

    static Shape getStar()
    {
        Shape star;
        double lineStrokeWidth = 2;
        Line line1 = new Line(0, lineLength / 2, lineLength, lineLength / 2);
        line1.setStrokeWidth(lineStrokeWidth);
        Line line2 = new Line(lineLength / 2, 0, lineLength / 2, lineLength);
        line2.setStrokeWidth(lineStrokeWidth);
        Line line3 = new Line(0, 0, lineLength, lineLength);
        line3.setStrokeWidth(lineStrokeWidth);
        Line line4 = new Line(lineLength, 0, 0, lineLength);
        line4.setStrokeWidth(lineStrokeWidth);
        Circle circle1 = new Circle(lineLength / 2, lineLength / 2, lineLength / 2);
        Circle circle2 = new Circle(lineLength / 2, lineLength / 2, centerRadius);
        star = Shape.subtract(Shape.intersect(Shape.union(Shape.union(Shape.union(line1, line2), line3), line4), circle1), circle2);
        star.setFill(Color.WHITE);
        return star;
    }

    static void createOne(double centerX, double centerY, Color textColor, double duration, double delay)
    {
        setTimeout(delay, e -> {
            StackPane pane = new StackPane();
            //pane.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));
            pane.setLayoutX(centerX - upLeftOffset);
            pane.setLayoutY(centerY - upLeftOffset);
            Shape star = getStar();
            //star.setOpacity(1);
            Text text = getText(textColor);
            //text.setOpacity(1);
            pane.getChildren().addAll(star, text);
            addToRoot(pane);
            KeyFrame keyFrame1 = new KeyFrame(
                    Duration.millis(duration / 3),
                    new KeyValue(star.opacityProperty(), 0)
            );
            KeyFrame keyFrame2 = new KeyFrame(
                    Duration.millis(duration),
                    new KeyValue(text.opacityProperty(), 0.3),
                    new KeyValue(pane.scaleXProperty(), 4),
                    new KeyValue(pane.scaleYProperty(), 4)
            );
            Timeline timeline = new Timeline();
            timeline.setCycleCount(1);
            timeline.setAutoReverse(false);
            timeline.getKeyFrames().addAll(keyFrame1, keyFrame2);
            timeline.setOnFinished(e2 -> {
                removeFromRoot(pane);
            });
            timeline.play();
            //playSimpleSound(hitMedia);
            hitPlayer.play();
        });
    }
}