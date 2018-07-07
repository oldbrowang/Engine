package oldbro.fight;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import oldbro.Scenes;
import oldbro.Statistics;
import oldbro.util.GifPlayer;

class CountDown extends FightComponent
{
    static String guardGif;

    static String explosionGif;

    static String counterGif;

    static String electricityGif;

    static String coughingGif;

    static int viewWidth;

    static ImageView electricityView;

    static ImageView guardView;

    static ImageView explosionView;

    static ImageView coughingView;

    static GifPlayer guardPlayer;

    static GifPlayer explosionPlayer;

    static Timeline guardFrameMovingTimeLine;

    static int currentCount;

    static Text number;

    static StackPane numberPane;

    static StackPane gameOverPane;

    static boolean toResume;

    static ImageView counterView;

    static {
        guardGif = "resource/guard.gif";
        explosionGif = "resource/explosion.gif";
        counterGif = "resource/fourChainSlashes.gif";
        electricityGif = "resource/electricity.gif";
        coughingGif = "resource/coughing.gif";
    }

    static void begin()
    {
        load();
        //playCounterFrames();
        cycle();
    }

    static void load ()
    {
        toResume = false;
        currentCount = 10;
        electricityView = new ImageView(new Image(getInputStreamByFileName(electricityGif)));
        electricityView.setFitWidth(1200);
        electricityView.setFitHeight(500);
        electricityView.setLayoutX(-90);
        electricityView.setLayoutY(430);
        electricityView.setOpacity(0);
        addToRoot(electricityView);
        coughingView = new ImageView(new Image(getInputStreamByFileName(coughingGif)));
        coughingView.setPreserveRatio(true);
        coughingView .setFitWidth(160);
        coughingView.setLayoutX(430);
        coughingView.setLayoutY(530);
        coughingView.setOpacity(0);
        addToRoot(coughingView);
        guardView = new ImageView();
        guardView.setFitWidth(300);
        guardView.setPreserveRatio(true);
        guardView.setLayoutX(820);
        guardView.setLayoutY(400);
        addToRoot(guardView);
        guardPlayer = new GifPlayer(guardGif, guardView);
        number = new Text("");
        number.setFill(Color.rgb(250, 78, 73));
        number.setStroke(Color.WHITE);
        number.setStrokeWidth(5);
        number.setFont(Font.font("Microsoft YaHei", FontWeight.EXTRA_BOLD, 400));
        numberPane = new StackPane(number);
        //numberPane.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));
        numberPane.setLayoutX(390);
        numberPane.setLayoutY(0);
        addToRoot(numberPane);
        explosionView = new ImageView();
        explosionView.setOpacity(0);
        explosionView.setFitWidth(300);
        explosionView.setFitWidth(420);
        explosionView.setLayoutX(300);
        explosionView.setLayoutY(90);
        addToRoot(explosionView);
        explosionPlayer = new GifPlayer(explosionGif, explosionView);
        Text gameOverText = new Text("GAME OVER");
        gameOverText.setFont(Font.font("Microsoft YaHei", FontWeight.EXTRA_BOLD, 150));
        gameOverText.setFill(Color.BLUE);
        gameOverText.setStroke(Color.RED);
        gameOverText.setStrokeWidth(10);
        gameOverPane = new StackPane(gameOverText);
        gameOverPane.setOpacity(0);
        gameOverPane.setLayoutX(35);
        gameOverPane.setLayoutY(150);
        addToRoot(gameOverPane);
        bindKeys();
        counterView = new ImageView();
        counterView.setFitWidth(410);
        //counterView.setFitHeight(250);
        counterView.setPreserveRatio(true);
        counterView.setTranslateX(-57);
        counterView.setTranslateY(62);
        addToRoot(counterView);
    }

    static void showElectricityView()
    {
        electricityView.setOpacity(1);
    }

    static void showCoughingView()
    {
        coughingView.setOpacity(1);
    }

    static void hideElectricityView()
    {
        electricityView.setOpacity(0);
    }

    static void renderNumber(EventHandler<ActionEvent> next)
    {
        explosionView.setOpacity(0);
        number.setText(currentCount + "");
        setTimeout(1500, next);
    }

    static void renderExplosion(EventHandler<ActionEvent> next)
    {
        explosionView.setOpacity(1);
        explosionPlayer.playAllFrames(500, next);
    }

    static void showGameOver()
    {
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(1500),
                new KeyValue(gameOverPane.opacityProperty(), 1)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.play();
    }

    static void bindKeys()
    {
        EventHandler<KeyEvent> keyEventHandler = new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                KeyCode keyCode = event.getCode();
                String keyName = keyCode.getName();
                if (keyCode.isWhitespaceKey() || keyName == "Enter") {
                    stopPlayingAndMovingGuardFrames();
                    //showElectricityView();
                    toResume = true;
                    //Statistics.resumeCount++;
                }
            }
        };
        scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
    }

    static void cycle()
    {
        if (toResume) {
            playCounterFrames();
        } else {
            currentCount--;
            if (currentCount >= 0) {
                moveGuardFrames();
                playGuardFrames();
                renderNumber(e -> renderExplosion(e2 -> cycle()));
            } else {
                musicPlayer.dispose();
                showGameOver();
                showCoughingView();
                setTimeout(5000, e -> Scenes.begin());
            }
        }
    }

    static void stopPlayingAndMovingGuardFrames()
    {
        guardPlayer.stop();
        guardFrameMovingTimeLine.stop();
    }

    static void playGuardFrames()
    {
        //System.out.println(guardPlayer.frameHorizontallyReversed);
        guardPlayer.playAllFrames(1500, null);
    }

    static void moveGuardFrames()
    {
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(1500),
                new KeyValue(guardView.layoutXProperty(), guardView.getLayoutX() - 100)
        );
        guardFrameMovingTimeLine = new Timeline();
        guardFrameMovingTimeLine.setCycleCount(1);
        guardFrameMovingTimeLine.setAutoReverse(false);
        guardFrameMovingTimeLine.getKeyFrames().addAll(keyFrame);
        guardFrameMovingTimeLine.play();
    }

    static void playCounterFrames()
    {
        showElectricityView();
        guardView.setOpacity(0);
        counterView.setLayoutX(guardView.getLayoutX());
        counterView.setLayoutY(guardView.getLayoutY());
        GifPlayer counterPlayer = new GifPlayer(counterGif, counterView, true);
        //counterPlayer.toTrancate = true;
        //counterPlayer.fromX = 100;
        //counterPlayer.fromY = 0;
        //counterPlayer.toX = 269;
        //counterPlayer.toY = 197;
        /*
        counterPlayer.playAllFrames(3000, e -> {
            hideElectricityView();
            resumeFight();
        });
        */
        counterPlayer.playFrames(0, 30, 2000, e -> {
            hideElectricityView();
            resumeFight();
        });
    }

    static void clear()
    {
        removeFromRoot(electricityView);
        removeFromRoot(guardView);
        removeFromRoot(counterView);
        removeFromRoot(explosionView);
        removeFromRoot(gameOverPane);
        removeFromRoot(numberPane);
    }
}
