package oldbro.fight;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import oldbro.Scenes;
import oldbro.util.Random;

class Blinds extends FightComponent
{
    static double blindMovingDuration;

    static int blindSectionCount;

    static Rectangle[] blinds;

    static int blindHeight;

    static int blindCount;

    static {
        blindMovingDuration = 300;
        blindSectionCount = 5;
        blindHeight = 2;
    }

    static void load()
    {
        blindCount = Scenes.wHeight / blindHeight;
        blinds = new Rectangle[blindCount];
        Rectangle blind;
        double delay;
        for (int i = 0; i < blindCount; i++) {
            blind = createBlind(i * blindHeight, blindHeight);
            blinds[i] = blind;
            addToRoot(blind);
        }
    }

    static void show(EventHandler<ActionEvent> next)
    {
        moveAllBlinds(0, next);
    }

    static void hide(EventHandler<ActionEvent> next)
    {
        moveAllBlinds(Scenes.wWidth, next);
    }

    static void moveAllBlinds(int detinationX, EventHandler<ActionEvent> next)
    {
        Rectangle blind;
        //int mod;
        double delay;
        for (int i = 0; i < blindCount; i++) {
            blind = blinds[i];
            //mod = i % blindSectionCount;
            //delay = mod * blindMovingDuration;
            delay = Random.getRandomInteger(100, 1500);
            moveBlind(blind, delay, detinationX);
        }
        setTimeout(blindMovingDuration * blindSectionCount, next);
    }

    static Rectangle createBlind(int y, int height)
    {
        Rectangle blind = new Rectangle(0, y, Scenes.wWidth, height);
        blind.setLayoutX(-Scenes.wWidth);
        return blind;
    }

    static void moveBlind(Rectangle blind, double delay, int detinationX)
    {
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(blindMovingDuration),
                new KeyValue(blind.layoutXProperty(), detinationX)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        //timeline.setOnFinished(next);
        timeline.setDelay(Duration.millis(delay));
        timeline.play();
    }
}
