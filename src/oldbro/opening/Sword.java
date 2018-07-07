package oldbro.opening;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import oldbro.Scenes;

import java.io.FileInputStream;

class Sword extends OpeningComponent
{
    static int radius;

    static int bladeLength;

    static Shape bladeCircle;

    static Arc showRegion;

    static double startAngle;

    static double endAngle;

    static double headAngleChangeStep;

    static double tailAngleChangeStep;

    static double tailAngle;

    static double headAngle;

    static Shape flash;

    static AnimationTimer timer;

    static double flashOpacity;

    static String swordPng;

    static ImageView swordView;

    static Rotate swordViewRotate;

    static void init()
    {
        radius = 300;
        bladeLength = 130;
        startAngle = 60;
        endAngle = -770;
        headAngleChangeStep = 10;
        tailAngleChangeStep = 7;
        headAngle = startAngle;
        tailAngle = startAngle;
        flashOpacity = 0.5;
        swordPng = "resource/sword.png";
    }

    static double getArcLength(double fromAngle, double toAngle)
    {
        return toAngle - fromAngle;
    }

    static void load()
    {
        init();
        Circle circle1 = new Circle(radius);
        circle1.setCenterX(radius);
        circle1.setCenterY(radius);
        Circle circle2 = new Circle(radius - bladeLength);
        circle2.setCenterX(radius);
        circle2.setCenterY(radius);
        bladeCircle = Shape.subtract(circle1, circle2);
        showRegion = new Arc(radius, radius, radius, radius, startAngle, 0);
        showRegion.setType(ArcType.ROUND);
        swordView = new ImageView(new Image(getInputStreamByFileName(swordPng)));
        swordView.setPreserveRatio(true);
        swordView.setFitWidth(500);
        swordView.setX(420);
        swordView.setY(250);
        swordViewRotate = new Rotate(90);
        swordView.setTranslateX(-200);
        swordView.setTranslateY(-350);
        swordViewRotate.setPivotX(radius);
        swordViewRotate.setPivotY(radius);
        swordView.getTransforms().add(swordViewRotate);
        teleportToProperPosition(swordView);
        addToRoot(swordView);
    }

    static void moveToReadyPosition()
    {
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(1000),
                new KeyValue(swordView.fitWidthProperty(), 180),
                new KeyValue(swordViewRotate.angleProperty(), -startAngle),
                new KeyValue(swordView.translateXProperty(), 0),
                new KeyValue(swordView.translateYProperty(), 0)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.setDelay(Duration.millis(1000));
        timeline.play();
    }

    static void teleportToProperPosition(Node node)
    {
        node.setLayoutX(350);
        node.setLayoutY(100);
    }

    static Shape createNewFlash()
    {
        Shape newFlash = Shape.intersect(bladeCircle, showRegion);
        newFlash.setFill(getGradient());
        newFlash.setOpacity(flashOpacity);
        newFlash.setStroke(Color.WHITE);
        newFlash.setStrokeWidth(5);
        return newFlash;
    }

    static void updateShowRegion()
    {
        showRegion.setStartAngle(tailAngle);
        showRegion.setLength(getArcLength(tailAngle, headAngle));
    }

    static boolean update()
    {
        boolean headAngleUpdated = updateHeadAngle();
        if (headAngleUpdated) {
            updateSword();
        }
        boolean tailAngleUpdated = updateTailAngle();
        if (tailAngleUpdated) {
            updateFlash();
        }
        if (headAngleUpdated || tailAngleUpdated) {
            return true;
        } else {
            return false;
        }
    }

    static void updateFlash()
    {
        updateShowRegion();
        removeFromRoot(flash);
        flash = createNewFlash();
        teleportToProperPosition(flash);
        addToRootHead(flash);
    }

    static void updateSword()
    {
        swordViewRotate.setAngle(-headAngle);
    }

    static boolean updateHeadAngle()
    {
        if (headAngle == endAngle) {
            return false;
        }
        double nextHeadAngle = headAngle - headAngleChangeStep;
        if (nextHeadAngle > endAngle) {
            headAngle = nextHeadAngle;
        } else {
            headAngle = endAngle;
        }
        return true;
    }

    static boolean updateTailAngle()
    {
        if (tailAngle == endAngle) {
            return false;
        }
        double nextTailAngle = tailAngle - tailAngleChangeStep;
        if (nextTailAngle > endAngle) {
            tailAngle = nextTailAngle;
        } else {
            tailAngle = endAngle;
        }
        return true;
    }

    static void move(EventHandler<ActionEvent> next)
    {
        timer = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                boolean updated = update();
                if (!updated) {
                    timer.stop();
                    setTimeout(500, next);
                }
            }
        };
        timer.start();
    }
    static RadialGradient getGradient()
    {
        Stop[] stops = new Stop[]{
                new Stop(0, Color.VIOLET),
                new Stop(0.1, Color.RED),
                new Stop(0.2, Color.ORANGE),
                new Stop(0.3, Color.YELLOW),
                new Stop(0.4, Color.GREEN),
                new Stop(0.5, Color.BLUE),
                new Stop(0.6, Color.INDIGO),
                new Stop(0.7, Color.VIOLET),
                new Stop(1, Color.WHITE),
        };
        RadialGradient radialGradient =
                new RadialGradient(0, 0, radius, radius, radius - bladeLength, false, CycleMethod.REPEAT, stops);
        return radialGradient;
    }
}