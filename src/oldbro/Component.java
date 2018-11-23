package oldbro;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;
import oldbro.util.GifPlayer;
import oldbro.util.BackJob;
import oldbro.util.Random;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class Component
{
    protected static EventHandler<ActionEvent> next;

    protected static Group root;

    protected static Scene scene;

    protected static void loadScene(Paint background)
    {
        root = new Group();
        scene = new Scene(root, Scenes.wWidth, Scenes.wHeight);
        scene.setFill(background);
        Scenes.stage.setScene(scene);
    }

    protected static InputStream getInputStreamByFileName(String fileName)
    {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(fileName);
        } catch (Exception e) {
            System.out.println(e);
        }
        return inputStream;
    }

    protected static String getFileUriStringByFileName(String fileName)
    {
        File file = new File(fileName);
        return file.toURI().toString();
    }

    protected static void addToRoot(Node node)
    {
        root.getChildren().add(node);
    }

    protected static void addToRootHead(Node node)
    {
        root.getChildren().add(0, node);
    }

    protected static void removeFromRoot(Node node)
    {
        root.getChildren().remove(node);
    }

    protected static void setToRootTop(Node node)
    {
        removeFromRoot(node);
        addToRoot(node);
    }

    protected static void removeAllFromRoot()
    {
        root.getChildren().clear();
    }

    protected static void allDone()
    {
        setTimeout(2000, next);
    }

    protected static Timeline setTimeout(double duration, EventHandler<ActionEvent> action)
    {
        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(duration), action));
        timeline.setCycleCount(1);
        timeline.play();
        return timeline;
    }

    protected static void nowLoading(EventHandler<ActionEvent> during, EventHandler<ActionEvent> after)
    {
        BackJob backJob = new BackJob(during);
        loadScene(Color.BLACK);
        ImageView animationView = new ImageView();
        animationView.setLayoutX(300);
        animationView.setLayoutY(170);
        animationView.setFitHeight(300);
        animationView.setPreserveRatio(true);
        addToRoot(animationView);
        Text text = new Text("NOW LOADING");
        text.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 60));
        text.setFill(Color.BLUE);
        text.setLayoutX(550);
        text.setLayoutY(700);
        addToRoot(text);
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(800), text);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.setCycleCount(20);
        fadeTransition.setAutoReverse(true);
        fadeTransition.play();
        GifPlayer player = new GifPlayer("resource/quickSword.gif", animationView, false);
        EventHandler<ActionEvent> finishHandler = new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                //System.out.println(backJob.isDone);
                if (backJob.isDone) {
                    fadeTransition.stop();
                    setTimeout(200, after);
                } else {
                    setTimeout(500, this);
                }
            }
        };
        player.playAllFrames(3000, finishHandler);
        setTimeout(1000, e -> backJob.start()); //parallel bug workaround
    }

    public static void preLoadGifFrames(String gif, ImageView animationView)
    {
        GifPlayer player = new GifPlayer(gif, animationView, false);
        player.renderAllFrame();
    }

    /*
    public static void playSimpleSound(String fileName)
    {
        MediaPlayer player = new MediaPlayer(new Media(getFileUriStringByFileName(fileName)));
        player.setCycleCount(1);
        player.play();
    }
    */

    public static void playSimpleSound(Media media)
    {
        MediaPlayer player = new MediaPlayer(media);
        player.setCycleCount(1);
        player.play();

        //setTimeout(Random.getRandomInteger(5000, 10000), e -> player.dispose());

    }

    public static boolean isWindowsOs()
    {
        String osStr = System.getProperty("os.name").toLowerCase();
        if (osStr.indexOf("windows") == -1) {
            return false;
        } else {
            return true;
        }
    }
}