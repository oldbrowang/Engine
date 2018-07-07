package oldbro.fight;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;
import oldbro.Scenes;
import oldbro.Statistics;

class Slash extends FightComponent
{
    double beginX;

    double beginY;

    double endX;

    double endY;

    int radius;

    int angle;

    Shape shape;

    long duration;

    Slash(double beginX, double beginY, double endX, double endY, int radius, int angle, long duration)
    {
        this.beginX = beginX;
        this.beginY = beginY;
        this.endX = endX;
        this.endY = endY;
        this.radius = radius;
        this.angle = angle;
        this.duration = duration;
        //Statistics.slashCount++;
    }

    void load()
    {
        Circle circle1 = new Circle(radius);
        circle1.setCenterX(radius);
        circle1.setCenterY(radius);
        Circle circle2 = new Circle(radius);
        circle2.setCenterX(radius);
        circle2.setCenterY(radius + radius * 0.8);
        shape = Shape.subtract(circle1, circle2);
        Rotate rotate = new Rotate();
        rotate.setAngle(angle);
        rotate.setPivotX(radius);
        rotate.setPivotY(radius);
        shape.getTransforms().add(rotate);
        setPosition(beginX, beginY);
        shape.setFill(getGradient());
        shape.setOpacity(1);
    }

    void execute()
    {
        addToRoot(shape);
        KeyFrame endFrame = new KeyFrame(
                Duration.millis(duration),
                new KeyValue(shape.layoutXProperty(), computeLayoutX(endX)),
                new KeyValue(shape.layoutYProperty(), computeLayoutY(endY)),
                new KeyValue(shape.scaleXProperty(), 10),
                new KeyValue(shape.scaleYProperty(), 10),
                new KeyValue(shape.opacityProperty(), 0.8)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(endFrame);
        timeline.play();
        timeline.setDelay(Duration.millis(200));
        timeline.setOnFinished(e -> {
            setTimeout(100, e2 -> removeFromRoot(shape));
        });
    }

    double computeLayoutX(double centerX)
    {
        return centerX - radius;
    }

    double computeLayoutY(double centerY)
    {
        return centerY - radius - 50;
    }

    void setPosition(double centerX, double centerY)
    {
        shape.setLayoutX(computeLayoutX(centerX));
        shape.setLayoutY(computeLayoutY(centerY));
    }

    RadialGradient getGradient()
    {
        Stop[] stops = new Stop[]{
                new Stop(0, Color.rgb(100, 100, 250)),
                new Stop(1, Color.WHITE)
        };
        RadialGradient radialGradient =
                new RadialGradient(0, 0, radius, radius, radius * 0.8, false, CycleMethod.NO_CYCLE, stops);
        return radialGradient;
    }
}
