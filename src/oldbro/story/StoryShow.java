package oldbro.story;

import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import oldbro.Scenes;
import oldbro.util.TextTypingTransition;

public class StoryShow extends StoryComponent
{
    static String ukyoJpg;

    static String eatingJpg;

    static String yaoguaiJpg;

    static ImageView ukyoView;

    static ImageView picView;

    static String storyLineStr;

    static Text stroyLineText;

    static TextFlow storyLineFlow;

    static Rectangle cursor;

    static Shape cover;

    static Rectangle whiteMask;

    static void init()
    {
        ukyoJpg = "resource/ukyo.jpg";
        eatingJpg = "resource/eating.jpg";
        yaoguaiJpg = "resource/yaoguai.jpg";
        storyLineStr = "剑客橘右京去往魔界寻找究极之花。\n途经中国，发现当地人吃屎浑然不觉，妖魔横行，一派末日之景象!";
    }

    static void load()
    {
        init();
        ukyoView = new ImageView(new Image(getInputStreamByFileName(ukyoJpg)));
        ukyoView.setFitWidth(Scenes.wWidth);
        ukyoView.setFitHeight(Scenes.wHeight);
        ukyoView.setLayoutX(-200);
        addToRoot(ukyoView);
        Rectangle blackMask = new Rectangle(Scenes.wWidth / 2, Scenes.wHeight);
        blackMask.setFill(Color.BLACK);
        blackMask.setLayoutX(Scenes.wWidth / 2);
        addToRoot(blackMask);
        cursor = new Rectangle(42, 42);
        cursor.setTranslateX(10);
        cursor.setTranslateY(7);
        cursor.setFill(Color.WHITE);
        storyLineFlow = new TextFlow();
        //stroyLineText = new Text(storyLineStr);
        stroyLineText = new Text();
        stroyLineText.setFont(Font.font("Microsoft YaHei", 47));
        stroyLineText.setFill(Color.WHITE);
        storyLineFlow.getChildren().addAll(stroyLineText, cursor);
        storyLineFlow.setMaxSize(Scenes.wWidth / 2 - 40, Scenes.wHeight - 40);
        storyLineFlow.setLayoutX(Scenes.wWidth / 2 + 20);
        storyLineFlow.setLayoutY(Scenes.wHeight / 2 + 20);
        addToRoot(storyLineFlow);
        picView = new ImageView();
        picView.setFitWidth(Scenes.wWidth / 2 - 40);
        picView.setFitHeight(Scenes.wHeight / 2 - 40);
        picView.setLayoutX(Scenes.wWidth / 2 + 20);
        picView.setLayoutY(20);
        addToRoot(picView);
        whiteMask = new Rectangle(Scenes.wWidth, Scenes.wHeight);
        whiteMask.setFill(Color.WHITE);
        whiteMask.setOpacity(0);
        addToRoot(whiteMask);
        Rectangle coverBackground = new Rectangle(Scenes.wWidth, Scenes.wHeight);
        Text coverText = new Text("......");
        coverText.setFont(Font.font("Microsoft YaHei", 400));
        Font.getFontNames();
        coverText.setLayoutX(200);
        coverText.setLayoutY(420);
        cover = Shape.subtract(coverBackground, coverText);
        cover.setFill(Color.BLACK);
        cover.setScaleX(30);
        cover.setScaleY(30);
        cover.setOpacity(0.5);
        addToRoot(cover);
    }

    static void moveUkyoView(EventHandler<ActionEvent> next)
    {
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(14000),
                new KeyValue(ukyoView.layoutXProperty(), -50)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.setOnFinished(next);
        timeline.play();
    }

    static void flashCursor()
    {
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(500),
                new KeyValue(cursor.opacityProperty(), 0)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(50);
        timeline.setAutoReverse(true);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.play();
    }

    static void printStroy()
    {
        TextTypingTransition transition = new TextTypingTransition(stroyLineText, storyLineStr, 12000);
        transition.setInterpolator(Interpolator.EASE_IN);
        transition.play();
    }

    static void showEating()
    {
        setTimeout(5000, e -> {
            picView.setOpacity(0);
            picView.setImage(new Image(getInputStreamByFileName(eatingJpg)));
            showPicGradually();
        });
    }

    static void showYaoguai()
    {
        setTimeout(9500, e -> {
            picView.setOpacity(0);
            picView.setImage(new Image(getInputStreamByFileName(yaoguaiJpg)));
            showPicGradually();
        });
    }

    static void showPicGradually()
    {
        FadeTransition transition = new FadeTransition(Duration.millis(1000), picView);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.play();
    }

    static void coverUp()
    {
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(5000),
                new KeyValue(cover.scaleXProperty(), 1),
                new KeyValue(cover.scaleYProperty(), 1)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.setOnFinished(e -> setTimeout(2000, e2 -> allDone()));
        timeline.play();
    }

    static void showWhiteMask()
    {
        FadeTransition transition = new FadeTransition(Duration.millis(1000), whiteMask);
        transition.setFromValue(0);
        transition.setToValue(1);
        transition.setDelay(Duration.millis(4000));
        transition.play();
    }
}
