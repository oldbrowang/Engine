package oldbro.opening;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import oldbro.Scenes;
import oldbro.util.GifPlayer;

class OpeningShow extends OpeningComponent
{
    static String backgroundGif;

    static ImageView backgroundView;

    static String openingComboShowGif;

    static ImageView openingComboShowView;

    static GifPlayer player;

    static String alertStr;

    static TextFlow alertContainer;

    static String copyrightStr;

    static TextFlow copyrightContainer;

    static Timeline alertNotPressedTimer;

    static boolean enterPressable;

    static void init()
    {
        backgroundGif = "resource/openingBackground.gif";
        openingComboShowGif = "resource/openingComboShow.gif";
        alertStr = "PRESS ENTER";
        copyrightStr = "Copyright © 2017 OLDBRO. All Rights Reserved.";
        enterPressable = false;
    }

    static void load()
    {
        init();
        bindKeys();
        backgroundView = new ImageView(new Image(getInputStreamByFileName(backgroundGif)));
        backgroundView.setFitHeight(Scenes.wHeight);
        backgroundView.setPreserveRatio(true);
        backgroundView.setLayoutX(-17);
        backgroundView.setOpacity(0);
        addToRoot(backgroundView);
        openingComboShowView = new ImageView();
        openingComboShowView.setFitHeight(500);
        openingComboShowView.setPreserveRatio(true);
        openingComboShowView.setLayoutX(300);
        openingComboShowView.setLayoutY(260);
        openingComboShowView.setOpacity(0);
        addToRoot(openingComboShowView);
        player = new GifPlayer(openingComboShowGif, openingComboShowView, false);
        player.renderFrame(0);
        Text alertText = new Text(alertStr);
        alertText.setFont(Font.font(50));
        alertText.setFill(Color.WHITE);
        alertContainer = new TextFlow(alertText);
        alertContainer.setLayoutX(500);
        alertContainer.setLayoutY(350);
        alertContainer.setOpacity(0);
        addToRoot(alertContainer);
        Text copyrightText = new Text(copyrightStr);
        copyrightText.setFont(Font.font("Courier New", FontWeight.BOLD, 20));
        copyrightText.setFill(Color.BLACK);
        copyrightContainer = new TextFlow(copyrightText);
        copyrightContainer.setLayoutX(450);
        copyrightContainer.setLayoutY(770);
        copyrightContainer.setOpacity(1);
        addToRoot(copyrightContainer);
    }

    static void showOpening()
    {
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(2000),
                new KeyValue(backgroundView.opacityProperty(), 1),
                new KeyValue(openingComboShowView.opacityProperty(), 1)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.setOnFinished(e2 -> OpeningShow.showAlertNotPressed());
        timeline.play();
    }

    static void showAlertNotPressed()
    {
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(1000),
                new KeyValue(alertContainer.opacityProperty(), 1)
        );
        alertNotPressedTimer = new Timeline();
        alertNotPressedTimer.setCycleCount(999);
        alertNotPressedTimer.setAutoReverse(true);
        alertNotPressedTimer.getKeyFrames().addAll(keyFrame);
        alertNotPressedTimer.play();
        enterPressable = true;
    }

    static void showAlertPressed()
    {
        alertContainer.setOpacity(0);
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(300),
                new KeyValue(alertContainer.opacityProperty(), 1)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(3);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.play();
        timeline.setOnFinished(e -> {
            alertContainer.setOpacity(0);
            showCombo();
        });
    }

    static void bindKeys()
    {
        scene.addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            KeyCode keyCode = e.getCode();
            String keyName = keyCode.getName();
            if (keyName == "Enter" && enterPressable) {
                enterPressable = false;
                alertNotPressedTimer.stop();
                showAlertPressed();
            }
        });
    }

    static void showCombo()
    {
        player.playAllFrames(5000, e -> allDone());
    }
}