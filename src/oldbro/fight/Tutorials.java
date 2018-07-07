package oldbro.fight;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;
import oldbro.Scenes;

public class Tutorials extends FightComponent
{
    static String message1;

    static String message2;

    static String message3;

    static String message4;

    static StackPane messagePane1;

    static StackPane messagePane2;

    static StackPane messagePane3;

    static StackPane messagePane4;

    static int paneWidth;

    static int paneHeight;

    static void init()
    {
        message1 = "←↑→↓        位移";
        message2 = "SPACE       秘剑阳炎";
        message3 = "B           秘剑暴雪";
        message4 = "V           残像四方阵";
        paneWidth = 800;
        paneHeight = 100;
    }

    static void load()
    {
        init();
        messagePane1 = createMessagePane(message1, 100);
        messagePane2 = createMessagePane(message2, 250);
        messagePane3 = createMessagePane(message3, 400);
        messagePane4 = createMessagePane(message4, 550);
        addToRoot(messagePane1);
        addToRoot(messagePane2);
        addToRoot(messagePane3);
        addToRoot(messagePane4);
    }

    static StackPane createMessagePane(String message, int yOffset)
    {
        StackPane messagePane = new StackPane();
        Rectangle rectangle = new Rectangle(paneWidth, paneHeight);
        rectangle.setFill(Color.PINK);
        rectangle.setOpacity(0.5);
        Text text = new Text(message);
        text.setFont(Font.font("Microsoft YaHei", 40));
        text.setFill(Color.WHITE);
        messagePane.getChildren().addAll(rectangle, text);
        messagePane.setLayoutX(-paneWidth);
        messagePane.setLayoutY(yOffset);
        return messagePane;
    }

    static void play(EventHandler<ActionEvent> next)
    {
        showMessagePane(messagePane1, 0, null);
        showMessagePane(messagePane2, 500, null);
        showMessagePane(messagePane3, 1000, null);
        showMessagePane(messagePane4, 1500, next);
    }

    static void showMessagePane(StackPane messagePane, double delay, EventHandler<ActionEvent> next)
    {
        int stopX = (Scenes.wWidth - paneWidth) / 2;
        KeyFrame keyFrame1 = new KeyFrame(
                Duration.millis(500),
                new KeyValue(messagePane.layoutXProperty(), stopX)
        );
        KeyFrame keyFrame2 = new KeyFrame(
                Duration.millis(3000),
                new KeyValue(messagePane.layoutXProperty(), stopX)
        );

        KeyFrame keyFrame3 = new KeyFrame(
                Duration.millis(3500),
                new KeyValue(messagePane.layoutXProperty(), Scenes.wWidth)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame1, keyFrame2, keyFrame3);
        timeline.setDelay(Duration.millis(delay));
        timeline.setOnFinished(next);
        timeline.play();
    }
}