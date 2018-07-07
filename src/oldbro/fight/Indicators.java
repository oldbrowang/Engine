package oldbro.fight;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.util.Duration;

class Indicators extends FightComponent
{
    static int maxSlashVolumn;

    static int slashVolumn;

    static Rectangle slashVolumnRectangle;

    static Text slashVolumnText;

    static String snowFlowerJpg;

    static int maxFlowerCount;

    static int flowerCount;

    static HBox flowerBox;

    static void init()
    {
        maxSlashVolumn = 100;
        slashVolumn = 0;
        snowFlowerJpg = "resource/snowFlower.jpg";
        flowerCount = 7;
    }

    static void load()
    {
        init();
        slashVolumnRectangle = new Rectangle();
        slashVolumnRectangle.setHeight(20);
        slashVolumnRectangle.setWidth(0);
        slashVolumnRectangle.setOpacity(0.5);
        slashVolumnText = new Text("POW");
        slashVolumnText.setFont(Font.font("Courier New", FontWeight.EXTRA_BOLD, 30));
        slashVolumnText.setFill(Color.WHITE);
        slashVolumnText.setStroke(Color.BLACK);
        slashVolumnText.setOpacity(0.8);
        HBox slashBox = new HBox(10);
        slashBox.setLayoutX(30);
        slashBox.setLayoutY(30);
        slashBox.setAlignment(Pos.CENTER);
        slashBox.getChildren().addAll(slashVolumnRectangle, slashVolumnText);
        addToRoot(slashBox);
        flowerBox = new HBox(10);
        //flowerBox.setBackground(new Background(new BackgroundFill(Color.PINK, null, null)));
        flowerBox.setLayoutX(600);
        flowerBox.setLayoutY(30);
        flowerBox.setAlignment(Pos.CENTER);
        flowerBox.getChildren().addAll();
        flowerBox.setPrefWidth(300);
        addToRoot(flowerBox);
        drawFlowers();
    }

    static void addSlashVolumn(int step)
    {
        if (slashVolumn < maxSlashVolumn) {
            slashVolumn += step;
            if (slashVolumn > maxSlashVolumn) {
                slashVolumn = maxSlashVolumn;
            }
            drawVolumn();
        }
    }

    static void drawVolumn()
    {
        slashVolumnRectangle.setWidth(slashVolumn * 3);
        Color rectangleColor;
        if (slashVolumn < 30) {
            rectangleColor = Color.LIGHTBLUE;
        } else if (slashVolumn < 60) {
            rectangleColor = Color.BLUE;
        } else if (slashVolumn < 90) {
            rectangleColor = Color.YELLOW;
        } else if (slashVolumn < 100) {
            rectangleColor = Color.ORANGE;
        } else {
            rectangleColor = Color.RED;
            slashVolumnText.setText("GO");
            slashVolumnText.setFill(Color.GOLD);
        }
        slashVolumnRectangle.setFill(rectangleColor);
    }

    static boolean ableToSlash()
    {
        if (slashVolumn == maxSlashVolumn) {
            return true;
        } else {
            return false;
        }
    }

    static boolean ableToFlower()
    {
        if (flowerCount > 0) {
            return true;
        } else {
            return false;
        }
    }

    static void reduceFlower()
    {
        flowerCount--;
        drawFlowers();
    }

    static void drainVolumn()
    {
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(3000),
                new KeyValue(slashVolumnRectangle.widthProperty(), 0)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.setOnFinished(e -> {
            slashVolumn = 0;
            slashVolumnText.setText("POW");
            slashVolumnText.setFill(Color.WHITE);
        });
        timeline.play();
    }

    static void drawFlowers()
    {
        flowerBox.getChildren().clear();
        for (int i = 0; i < flowerCount; i++) {
            ImageView flower = new ImageView(new Image(getInputStreamByFileName(snowFlowerJpg)));
            flower.setFitWidth(35);
            flower.setPreserveRatio(true);
            flowerBox.getChildren().add(flower);
        }
    }
}
