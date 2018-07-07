package oldbro.fight;

import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import oldbro.ComponentPosition;
import oldbro.Scenes;
import oldbro.Statistics;
import oldbro.util.GifPlayer;
import oldbro.util.Random;

class Monster extends FightComponent
{
    int upLeftOffsetX;

    int upLeftOffsetY;

    int gifCycleDistance;

    int gifOriginalDirection;

    ComponentPosition currentPosition;

    int speed;

    ImageView monsterView;

    GifPlayer framePlayer;

    State state; //1.moving 2.stopped 3.flying

    Monster thisMonster;

    Timeline moveTimeline;

    AnimationTimer flyTimer;

    RotateTransition rotateTransition;

    boolean isFree;

    enum State
    {
        Moving, Stopped, Flying
    }

    Monster(String monsterPng, int monsterWidth, int monsterHeight, int upLeftOffsetX, int upLeftOffsetY, int gifCycleDistance, int gifOriginalDirection, int centerX, int centerY, int speed, boolean isFree)
    {
        monsterView = new ImageView();
        monsterView.setFitWidth(monsterWidth);
        monsterView.setFitHeight(monsterHeight);
        framePlayer = new GifPlayer(monsterPng, monsterView);
        this.upLeftOffsetX = upLeftOffsetX;
        this.upLeftOffsetY = upLeftOffsetY;
        this.gifCycleDistance = gifCycleDistance;
        this.gifOriginalDirection = gifOriginalDirection;
        this.speed = speed;
        this.isFree = isFree;
        currentPosition = new ComponentPosition(centerX, centerY);
        setPosition(centerX, centerY);
        thisMonster = this;
        bindProperties();
    }

    void setPosition(double x, double y)
    {
        monsterView.setLayoutX(getUpLeftX(x));
        monsterView.setLayoutY(getUpLeftY(y));
    }

    double getUpLeftX(double x)
    {
        return x - upLeftOffsetX;
    }

    double getUpLeftY(double y)
    {
        return y - upLeftOffsetY;
    }

    double getCenterXByUpLeftX(double upLeftX)
    {
        return upLeftX + upLeftOffsetX;
    }

    double getCenterYByUpleftY(double upleftY)
    {
        return upleftY + upLeftOffsetY;
    }

    void bindProperties()
    {
        monsterView.layoutXProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                //System.out.println(newValue.doubleValue());
                currentPosition.x = getCenterXByUpLeftX(newValue.doubleValue());
                checkXAndRemove();
                checkCollisionWithSwordmanAndEndFight();
            }
        });
        monsterView.layoutYProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                currentPosition.y = getCenterYByUpleftY(newValue.doubleValue());
                checkYAndRemove();
                checkCollisionWithSwordmanAndEndFight();
            }
        });
    }

    void checkCollisionWithSwordmanAndEndFight()
    {
        if (!Swordman.collided && !Swordman.inCombo) {
            double distance = currentPosition.getDistance(Swordman.currentPosition);
            if (distance < 20) {
                Swordman.collided = true;
                stopFight();
            }
        }
    }

    void checkXAndRemove()
    {
        if (state == State.Flying) {
            if (currentPosition.x < -200 || currentPosition.x > Scenes.wWidth + 200 ) {
                stopFlying();
                MonsterDispatcher.remove(thisMonster);
            }
        }
    }

    void checkYAndRemove()
    {
        if (state == State.Flying) {
            if (currentPosition.y < -200 || currentPosition.y > Scenes.wHeight + 200 ) {
                stopFlying();
                MonsterDispatcher.remove(thisMonster);
            }
        } else if (state == State.Moving) {
            if (currentPosition.y > Scenes.wHeight + 200) {
                stopMoving();
                MonsterDispatcher.remove(thisMonster);
            }
        }
    }

    void playFrames(double duration, boolean frameHorizontallyReversed)
    {
        framePlayer.setIsFrameHorizontallyReversed(frameHorizontallyReversed);
        framePlayer.playAllFrames(duration, null);
    }

    void playFramesHorizontallyReversedFromLastCycle(double duration)
    {
        framePlayer.toggleHorizontalDirection();
        framePlayer.playAllFrames(duration, null);
    }

    void stopMoving()
    {
        if (moveTimeline != null) {
            moveTimeline.stop();
        }
        if (framePlayer != null) {
            framePlayer.stop();
        }
        state = State.Stopped;
    }

    void go()
    {
        state = State.Moving;
        move();
    }

    void move()
    {
        double duration = (double) gifCycleDistance / speed * 1000;
        ComponentPosition swordmanPosition = Swordman.readPosition();
        ComponentPosition destinationPosition = calculateNextDestination(swordmanPosition);
        int xDirection;
        if (destinationPosition == null) {
            destinationPosition = currentPosition;
            xDirection = currentPosition.calculateXDirection(swordmanPosition) * -1;
        } else {
            //xDirection = currentPosition.calculateXDirection(destinationPosition);
            xDirection = currentPosition.calculateXDirection(swordmanPosition);
        }
        boolean frameHorizontallyReversed = !(xDirection == gifOriginalDirection);
        playFrames(duration, frameHorizontallyReversed);
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(duration),
                new KeyValue(monsterView.layoutXProperty(), getUpLeftX(destinationPosition.x)),
                new KeyValue(monsterView.layoutYProperty(), getUpLeftY(destinationPosition.y))
        );
        moveTimeline = new Timeline();
        moveTimeline.setCycleCount(1);
        moveTimeline.setAutoReverse(false);
        moveTimeline.getKeyFrames().addAll(keyFrame);
        moveTimeline.setOnFinished(e -> {
            move();
        });
        moveTimeline.play();
    }

    ComponentPosition calculateNextDestination(ComponentPosition swordmanPosition)
    {
        double destinationX;
        double destinationY;
        double moveDistance = gifCycleDistance;
        if (isFree && currentPosition.y < Scenes.wHeight - 50) {
            double distanceFromSwordman = currentPosition.getDistance(swordmanPosition);
            double factor;
            if (Swordman.inCombo && distanceFromSwordman < 150) {
                return null;
                //destinationX = currentPosition.x;
                //destinationY = currentPosition.y;
            } else {
                factor = moveDistance / distanceFromSwordman;
                destinationX = (int) (currentPosition.x + (swordmanPosition.x - currentPosition.x) * factor);
                destinationY = (int) (currentPosition.y + (swordmanPosition.y - currentPosition.y) * factor);
                return new ComponentPosition(destinationX, destinationY);
            }
        } else {
            destinationX = currentPosition.x;
            destinationY = (int) (currentPosition.y + moveDistance);
            return new ComponentPosition(destinationX, destinationY);
        }
    }

    void rotate(int rotateDirection)
    {
        rotateTransition = new RotateTransition();
        rotateTransition.setByAngle(360 * rotateDirection);
        rotateTransition.setInterpolator(Interpolator.LINEAR);
        rotateTransition.setDuration(Duration.millis(Random.getRandomInteger(200, 800)));
        rotateTransition.setCycleCount(100);
        rotateTransition.setNode(monsterView);
        rotateTransition.play();
    }

    void stopFlying()
    {
        rotateTransition.stop();
        flyTimer.stop();
        state = State.Stopped;
    }

    void sendFly(double xSpeed, double ySpeed, double delay, int rotateDirection)
    {
        Statistics.monsterKillCount++;
        state = State.Flying;
        setTimeout(delay, e -> {
            flyTimer = new AnimationTimer()
            {
                @Override
                public void handle(long now)
                {
                    //currentPosition.x += xSpeed;
                    //currentPosition.y += ySpeed;
                    setPosition(currentPosition.x + xSpeed, currentPosition.y + ySpeed);
                }
            };
            flyTimer.start();
            rotate(rotateDirection);
            //state = State.Flying;
        });
    }

    void fadeThenRemove()
    {
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(1000),
                new KeyValue(monsterView.opacityProperty(), 0)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.setOnFinished(e -> MonsterDispatcher.remove(this));
        timeline.play();
    }
}
