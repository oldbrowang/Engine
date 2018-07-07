package oldbro.closing;

import com.sun.xml.internal.ws.api.pipe.ServerTubeAssemblerContext;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.scene.transform.Shear;
import javafx.util.Duration;
import oldbro.Scenes;
import oldbro.Statistics;
import oldbro.util.GifPlayer;

class ClosingShow extends ClosingComponent
{
    static String lightningCloudsGif;

    static String closingComboShowGif;

    static String landingLightningGif;

    static ImageView closingComboShowView;

    static ImageView landingLightningView;

    static ImageView lightningCloudsView;

    static GifPlayer comboPlayer;

    static String titleStr;

    static String claimPart1Str;

    static String claimPart2Str;

    static String claimPart3Str;

    static TextFlow claimPart1Flow;

    static TextFlow claimPart2Flow;

    static TextFlow claimPart3Flow;

    static {
        lightningCloudsGif = "resource/lightningClouds.gif";
        closingComboShowGif = "resource/closingComboShow.gif";
        landingLightningGif = "resource/landingLightning.gif";
        titleStr = "Thank For Playing";
        claimPart1Str = "老哥";
        claimPart2Str = "迎娶";
        claimPart3Str = "白富美";
    }

    static void load()
    {
        lightningCloudsView = new ImageView();
        lightningCloudsView.setPreserveRatio(true);
        lightningCloudsView.setFitWidth(Scenes.wWidth);
        lightningCloudsView.setLayoutY(310);
        addToRoot(lightningCloudsView);
        landingLightningView = new ImageView();
        landingLightningView.setPreserveRatio(true);
        landingLightningView.setFitWidth(300);
        landingLightningView.setLayoutX(360);
        landingLightningView.setLayoutY(230);
        addToRoot(landingLightningView);
        Image closingComboShowImage = new Image(getInputStreamByFileName(closingComboShowGif));
        closingComboShowView = new ImageView();
        closingComboShowView.setOpacity(0);
        closingComboShowView.setPreserveRatio(true);
        closingComboShowView.setFitWidth(600);
        closingComboShowView.setLayoutX(250);
        closingComboShowView.setLayoutY(350);
        addToRoot(closingComboShowView);
        comboPlayer = new GifPlayer(closingComboShowGif, closingComboShowView, false);
        Text titleText = new Text(titleStr);
        titleText.setFont(Font.font("Brush Script MT", FontWeight.EXTRA_BOLD, 100));
        titleText.setFill(Color.YELLOW);
        TextFlow titleFlow = new TextFlow(titleText);
        titleFlow.setLayoutX(180);
        titleFlow.setLayoutY(80);
        addToRoot(titleFlow);
        Shear shear;
        Text claimPart1Text = new Text(claimPart1Str);
        claimPart1Text.setFont(Font.font("DengXian Light", 140));
        claimPart1Text.setFill(Color.WHITE);
        claimPart1Flow = new TextFlow(claimPart1Text);
        claimPart1Flow.setLayoutX(700);
        claimPart1Flow.setLayoutY(320);
        claimPart1Flow.setMaxWidth(100);
        shear = new Shear();
        shear.setX(0.1);
        claimPart1Text.getTransforms().add(shear);
        Text claimPart2Text = new Text(claimPart2Str);
        claimPart2Text.setFont(Font.font("DengXian Light", 130));
        claimPart2Text.setFill(Color.WHITE);
        claimPart2Flow = new TextFlow(claimPart2Text);
        claimPart2Flow.setLayoutX(495);
        claimPart2Flow.setLayoutY(350);
        claimPart2Flow.setMaxWidth(100);
        shear = new Shear();
        shear.setX(-0.1);
        claimPart2Text.getTransforms().add(shear);
        Text claimPart3Text = new Text(claimPart3Str);
        claimPart3Text.setFont(Font.font("DengXian Light", 110));
        claimPart3Text.setFill(Color.WHITE);
        claimPart3Flow = new TextFlow(claimPart3Text);
        claimPart3Flow.setLayoutX(185);
        claimPart3Flow.setLayoutY(350);
        claimPart3Flow.setMaxWidth(100);
        shear = new Shear();
        shear.setX(0.2);
        claimPart3Text.getTransforms().add(shear);
    }

    static void showClosing()
    {
        showSamurais(e -> {
            playLandingLightning(e2 -> {
                playLightningClouds();
                playComboFrontPart(e3 -> {
                    showClaim();
                    playComboMiddlePart(e4 -> {
                        playComboBackPart(e5 -> setTimeout(5000, next));
                    });
                });
            });
        });
    }

    static void showClaim()
    {
        setTimeout(500, e -> addToRoot(claimPart1Flow));
        setTimeout(1500, e -> addToRoot(claimPart2Flow));
        setTimeout(2500, e -> addToRoot(claimPart3Flow));
    }

    static void playLightningClouds()
    {
        GifPlayer player = new GifPlayer(lightningCloudsGif, lightningCloudsView, false);
        player.playAllFrames(2500, null);
    }

    static void playLandingLightning(EventHandler<ActionEvent> next)
    {
        GifPlayer player = new GifPlayer(landingLightningGif, landingLightningView, false);
        player.toTrancate = true;
        player.fromX = 0;
        player.fromY = 0;
        player.toX = 146;
        player.toY = 220;
        player.playAllFrames(800, e -> {
            removeFromRoot(landingLightningView);
            setTimeout(100, next);
        });
    }

    static void showSamurais(EventHandler<ActionEvent> next)
    {
        closingComboShowView.setScaleX(0.8);
        closingComboShowView.setScaleY(0.8);
        GifPlayer player = new GifPlayer(closingComboShowGif, closingComboShowView, false);
        player.renderFrame(0);
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(1500),
                new KeyValue(closingComboShowView.opacityProperty(), 1),
                new KeyValue(closingComboShowView.scaleXProperty(), 1),
                new KeyValue(closingComboShowView.scaleYProperty(), 1)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.setOnFinished(e -> setTimeout(500, next));
        timeline.play();
    }

    static void playComboFrontPart(EventHandler<ActionEvent> next)
    {
        comboPlayer.playFrames(0, 54, 2500, next);
    }

    static void playComboMiddlePart(EventHandler<ActionEvent> next)
    {
        setTimeout(500, e -> comboPlayer.playFrames(55, 59, 4000, next));
    }

    static void playComboBackPart(EventHandler<ActionEvent> next)
    {
        comboPlayer.playFrames(60, 71, 1500, next);
    }
}
