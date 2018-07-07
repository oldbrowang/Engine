package oldbro.opening;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.util.Duration;

class StyleWords extends OpeningComponent
{
    static String schoolNameStr;

    static TextFlow schoolNameFlow;

    static String moveNameStr;

    static TextFlow moveNameFlow;

    static Rectangle schoolNameCover;

    static void init()
    {
        schoolNameStr = "神梦想一刀流";
        moveNameStr = "燕返";
    }

    static void load()
    {
        init();
        //System.out.println(Font.getFamilies());
        Text schoolNameText = new Text(schoolNameStr);
        schoolNameText.setFont(Font.font("Microsoft YaHei", 80));
        schoolNameText.setStroke(Color.WHITE);
        schoolNameFlow = new TextFlow(schoolNameText);
        schoolNameFlow.setMaxWidth(90);
        schoolNameFlow.setLayoutX(200);
        schoolNameFlow.setLayoutY(100);
        //schoolNameFlow.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));
        addToRoot(schoolNameFlow);
        schoolNameCover = new Rectangle(90, 610);
        schoolNameCover.setFill(Color.WHITE);
        schoolNameCover.setLayoutX(200);
        schoolNameCover.setLayoutY(100);
        addToRoot(schoolNameCover);
        Text moveNameText = new Text(moveNameStr);
        moveNameText.setFont(Font.font("Microsoft YaHei", 75));
        moveNameText.setStroke(Color.WHITE);
        moveNameFlow = new TextFlow(moveNameText);
        moveNameFlow.setMaxWidth(100);
        moveNameFlow.setLayoutX(50);
        moveNameFlow.setLayoutY(215);
        moveNameFlow.setOpacity(0);
        addToRoot(moveNameFlow);
    }

    static void showSchoolName(EventHandler<ActionEvent> next)
    {
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(2500),
                new KeyValue(schoolNameCover.layoutYProperty(), 1000)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.setOnFinished(next);
        timeline.play();
    }

    static void showMoveName(EventHandler<ActionEvent> e)
    {
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(500),
                new KeyValue(moveNameFlow.opacityProperty(), 1)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.setOnFinished(e);
        timeline.play();
    }
}