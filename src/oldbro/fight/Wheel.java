package oldbro.fight;

import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Shape;
import javafx.util.Duration;
import oldbro.ComponentPosition;
import oldbro.Scenes;
import oldbro.Statistics;
import oldbro.util.Random;
import sun.security.provider.SHA;

class Wheel extends FightComponent
{
    static int radius;

    static int scaleFactor;

    double xSpeed;

    double ySpeed;

    Shape shape;

    ComponentPosition currentPosition;

    Monster monster;

    AnimationTimer timer;

    RotateTransition rotateTransition;

    static {
        radius = 20;
        scaleFactor = 3;
    }

    Wheel(double x, double y, double xSpeed, double ySpeed, Monster monster)
    {
        currentPosition = new ComponentPosition(x, y);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        this.monster = monster;
        //Statistics.wheelCount++;
    }

    void load()
    {
        //System.out.println(currentPosition.x);
        //System.out.println(currentPosition.y);
        Circle circle = new Circle(radius, radius, radius);
        Ellipse ellipse1 = new Ellipse(radius, radius, radius, 5);
        Ellipse ellipse2 = new Ellipse(radius, radius, 5, radius);
        shape = Shape.subtract(circle, Shape.union(ellipse1, ellipse2));

        shape.setFill(Color.rgb(255, Random.getRandomInteger(0, 255), Random.getRandomInteger(0, 255)));
        shape.setOpacity(0.7);
        setPosition();
        addToRoot(shape);
        //System.out.println(circle.getLayoutX());
        //System.out.println(circle.getLayoutY());
    }

    void go(double delay)
    {
        rotate(Random.getDirection());
        timer = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                currentPosition.x += xSpeed;
                currentPosition.y += ySpeed;
                setPosition();
                if (currentPosition.getDistance(monster.currentPosition) < 10 && monster.state == Monster.State.Stopped) {
                    //System.out.println("aaa");
                    //ContactEffect.createOne(currentPosition.x, currentPosition.y, Color.ORANGE, 800, 10);
                    //circle.setOpacity(0.3);
                    shape.setScaleX(scaleFactor);
                    shape.setScaleY(scaleFactor);
                    monster.sendFly(xSpeed, ySpeed, 10, 0);
                }
                if (checkOutOfBoundary()) {
                    rotateTransition.stop();
                    timer.stop();
                    removeFromRoot(shape);
                }
            }
        };
        setTimeout(delay, e -> timer.start());
    }

    void rotate(int rotateDirection)
    {
        rotateTransition = new RotateTransition();
        rotateTransition.setByAngle(360 * rotateDirection);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.setDuration(Duration.millis(Random.getRandomInteger(300, 500)));
        rotateTransition.setCycleCount(100);
        rotateTransition.setNode(shape);
        rotateTransition.play();
    }

    boolean checkOutOfBoundary()
    {
        int boundaryLimit = -50;
        if (currentPosition.x < boundaryLimit || currentPosition.x > Scenes.wWidth - boundaryLimit ||
                currentPosition.y < boundaryLimit || currentPosition.y > Scenes.wHeight - boundaryLimit) {
            return true;
        } else {
            return false;
        }
    }

    void setPosition()
    {
        shape.setLayoutX(currentPosition.x - radius);
        shape.setLayoutY(currentPosition.y - radius);
    }
}
