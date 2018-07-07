package oldbro.fight;

import javafx.animation.AnimationTimer;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import oldbro.ComponentPosition;
import oldbro.Statistics;
import oldbro.util.Random;

class Wave extends FightComponent
{
    Shape shape;

    double radius;

    double gap;

    double xSpeed;

    double ySpeed;

    ComponentPosition currentPosition;

    double scale;

    double opacity;

    AnimationTimer timer;

    boolean isSuper;

    Color color;

    Color baseColor;

    Wave(double x, double y, boolean isSuper)
    {
        currentPosition = new ComponentPosition(x, y);
        this.isSuper = isSuper;
        //Statistics.waveCount++;
    }

    void init()
    {
        radius = 30 + Random.getRandomFloat(0.0, 10.0);
        if (isSuper) {
            gap = 5 + Random.getRandomFloat(10.0, 30.0);
        } else {
            gap = 5 + Random.getRandomFloat(0.0, 20.0);
        }
        if (isSuper) {
            xSpeed = 0 + Random.getRandomFloat(-8.5, 8.5);
        } else {
            xSpeed = 0 + Random.getRandomFloat(-1.5, 1.5);
        }
        ySpeed = -6 + Random.getRandomFloat(-4.0, 0.0);
        scale = 1;
        opacity = 1;
        color = Color.rgb(Random.getRandomInteger(100, 255), Random.getRandomInteger(100, 255), Random.getRandomInteger(100, 255));
        if (isSuper) {
            baseColor = Color.WHITE;
        } else {
            baseColor = Color.RED;
        }
    }

    void load()
    {
        init();
        Circle circle1 = new Circle(radius);
        circle1.setCenterX(radius);
        circle1.setCenterY(radius);
        Circle circle2 = new Circle(radius);
        circle2.setCenterX(radius + gap * xSpeed / ySpeed);
        circle2.setCenterY(radius + gap);
        shape = Shape.subtract(circle1, circle2);
        setPosition();
        shape.setFill(getGradient());
        shape.setOpacity(opacity);
        addToRoot(shape);
    }

    void setPosition()
    {
        shape.setLayoutX(currentPosition.x - radius);
        shape.setLayoutY(currentPosition.y - radius);
    }

    void move()
    {
        timer = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                currentPosition.x += xSpeed;
                currentPosition.y += ySpeed;
                enlarge();
                fade();
                setPosition();
                boolean isHit = checkAndHit();
                boolean isOutOfBoundary = checkOutOfBoundary();
                if (isHit || isOutOfBoundary) {
                    timer.stop();
                    removeFromRoot(shape);
                }
            }
        };
        timer.start();
    }

    boolean checkOutOfBoundary()
    {
        return currentPosition.y < 20;
    }

    boolean checkAndHit()
    {
        for (Monster monster : MonsterDispatcher.monsters) {
            if (monster.state == Monster.State.Moving && currentPosition.getDistance(monster.currentPosition) < radius * 1.5) {
                monster.stopMoving();
                monster.sendFly(xSpeed, ySpeed, 100, Random.getDirection());
                Color effectColor;
                if (isSuper) {
                    effectColor = Color.WHITE;
                } else {
                    effectColor = Color.RED;
                }
                ContactEffect.createOne(monster.currentPosition.x, monster.currentPosition.y, color, 800, 100);
                return true;
            }
        }
        return false;
    }

    void enlarge()
    {
        if (isSuper) {
            scale *= 1.008;
            shape.setScaleY(scale);
        } else {
            scale *= 1.005;
            shape.setScaleX(scale);
        }
    }

    void fade()
    {
        opacity -= 0.01;
        shape.setOpacity(opacity);
    }

    RadialGradient getGradient()
    {
        Stop[] stops = new Stop[]{
                new Stop(0, color),
                new Stop(1, baseColor)
        };

        RadialGradient radialGradient =
                new RadialGradient(0, 0, radius, radius, radius * 1.2, false, CycleMethod.NO_CYCLE, stops);
        return radialGradient;
    }
}