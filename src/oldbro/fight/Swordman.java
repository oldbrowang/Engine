package oldbro.fight;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import oldbro.Component;
import oldbro.ComponentPosition;
import oldbro.Scenes;
import oldbro.Statistics;
import oldbro.util.BackJob;
import oldbro.util.GifPlayer;
import oldbro.util.Random;
import oldbro.util.ShakeTransition;
import org.w3c.dom.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Swordman extends FightComponent
{
    static String readyPng;

    static String drawPng;

    static String superMoveUpGif;

    static String superMoveDownGif;

    static String followersGif;

    static String finishPoseGif;

    static String portraitPng;

    static String yanfanGif;

    static String escapeGif;

    static Image readyImg;

    static Image drawImg;

    static ImageView view;

    static ImageView viewShadow1;

    static ImageView viewShadow2;

    static Timeline slideTimerForSwordman;

    static Timeline slideTimerForShadow1;

    static Timeline slideTimerForShadow2;

    static ImageView portraitView;

    static boolean ready;

    static boolean inCombo;

    static int centerX;

    static int centerY;

    static ComponentPosition currentPosition;

    static ComponentPosition sceneCenterPosition;

    static int upLeftOffsetX;

    static int upLeftOffsetY;

    static int stepSpan;

    static int superDrawWaveCount;

    static int comboAreaOffset;

    static ImageView[] comboViews;

    static GifPlayer[] comboPlayers;

    static Slash[] slashes;

    static GifPlayer gifPlayer; //for test

    static Shape comboCircle;

    static EventHandler<KeyEvent> keyEventHandler;

    static boolean collided;

    static String followerYell;

    static String brandishWav;

    static Media brandishMedia;

    static AudioClip brandishPlayer;

    //static MediaPlayer brandishSoundPlayer;

    public static void fight()
    {
        gifPlayer = new GifPlayer("resource/prologue.gif", view, false); //for test
        showPortrait();
        setPosition();
        moveToPosotion(new ComponentPosition(490, 700));
        ready = true;
    }

    public static void resumeFight()
    {
        collided = false;
        bindKeys();
        getReady();
    }

    static
    {
        readyPng = "resource/ready.png";
        drawPng = "resource/draw.png";
        superMoveUpGif = "resource/superMoveUp.gif";
        superMoveDownGif = "resource/superMoveDown.gif";
        followersGif = "resource/followers.gif";
        finishPoseGif = "resource/finishPose.gif";
        portraitPng = "resource/portrait.png";
        yanfanGif = "resource/yanfan.gif";
        escapeGif = "resource/escape.gif";
        brandishWav = "resource/brandish.wav";
        followerYell = "UKYO-SAMA, WAIT FOR US!!!";
    }

    static void load()
    {
        sceneCenterPosition = new ComponentPosition(Scenes.wWidth / 2, Scenes.wHeight / 2);
        upLeftOffsetX = 40;
        upLeftOffsetY = 40;
        stepSpan = 150;
        superDrawWaveCount = 50;
        comboAreaOffset = 300;
        readyImg = new Image(getInputStreamByFileName(readyPng));
        drawImg = new Image(getInputStreamByFileName(drawPng));
        view = new ImageView();
        viewShadow1 = new ImageView();
        viewShadow1.setOpacity(0.7);
        viewShadow2 = new ImageView();
        viewShadow2.setOpacity(0.5);
        setToBeginningPosition();
        comboViews = new ImageView[5];
        comboPlayers = new GifPlayer[5];
        slashes = new Slash[4];
        renderGetReadyViewAll();
        ready = false;
        inCombo = false;
        addToRoot(viewShadow2);
        addToRoot(viewShadow1);
        addToRoot(view);
        comboCircle = createComboCircle();
        addToRoot(comboCircle);
        portraitView = createPortrait();
        addToRoot(portraitView);
        bindProperties();
        bindKeys();
        collided = false;
        //brandishMedia = new Media(getFileUriStringByFileName(brandishWav));
        brandishPlayer = new AudioClip(getFileUriStringByFileName(brandishWav));
    }

    static ImageView createPortrait()
    {
        ImageView portraitView = new ImageView(new Image(getInputStreamByFileName(portraitPng)));
        portraitView.setFitHeight(400);
        portraitView.setPreserveRatio(true);
        portraitView.setLayoutX(Scenes.wWidth);
        portraitView.setLayoutY(50);
        return portraitView;
    }

    static void showPortrait()
    {
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(700),
                new KeyValue(portraitView.layoutXProperty(), 250)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.setOnFinished(e -> {
            setTimeout(1000, e2 -> hideAndRemovePortrait());
        });
        timeline.play();
    }

    static void hideAndRemovePortrait()
    {
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(1000),
                new KeyValue(portraitView.layoutXProperty(), getUpLeftX(currentPosition.x)),
                new KeyValue(portraitView.layoutYProperty(), getUpLeftY(currentPosition.y)),
                new KeyValue(portraitView.scaleXProperty(), 0.25),
                new KeyValue(portraitView.scaleYProperty(), 0.25),
                new KeyValue(portraitView.translateXProperty(), -220),
                new KeyValue(portraitView.translateYProperty(), -165),
                new KeyValue(portraitView.opacityProperty(), 0)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.setOnFinished(e -> {
            removeFromRoot(portraitView);
        });
        timeline.play();
    }

    static Shape createComboCircle()
    {
        Shape circle;
        circle = new Circle(sceneCenterPosition.x, sceneCenterPosition.y, comboAreaOffset + 2);
        //Circle innerCircle = new Circle(sceneCenterPosition.x, sceneCenterPosition.y, comboAreaOffset - 2);
        //circle = Shape.subtract(outerCircle, innerCircle);
        //circle = outerCircle;
        circle.setFill(Color.RED);
        circle.setScaleX(0.01);
        circle.setScaleY(0.01);
        circle.setOpacity(0);
        return circle;
    }

    static void showComboCircle()
    {
        comboCircle.setOpacity(0.5);
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(500),
                //new KeyValue(comboCircle.opacityProperty(), 1),
                new KeyValue(comboCircle.scaleXProperty(), 1),
                new KeyValue(comboCircle.scaleYProperty(), 1),
                new KeyValue(comboCircle.fillProperty(), Color.YELLOW)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(2);
        timeline.setAutoReverse(true);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.setOnFinished(e -> {
            comboCircle.setOpacity(0);
            //comboCircle.setFill(Color.WHITE);
        });
        timeline.play();
    }

    static void hideComboCircle()
    {
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(100),
                //new KeyValue(comboCircle.opacityProperty(), 0),
                new KeyValue(comboCircle.scaleXProperty(), 0.01),
                new KeyValue(comboCircle.scaleYProperty(), 0.01)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.play();
    }

    static void setToBeginningPosition()
    {
        view.setLayoutX(Scenes.wWidth);
        view.setLayoutY(Scenes.wHeight);
        viewShadow1.setLayoutX(Scenes.wWidth);
        viewShadow1.setLayoutY(Scenes.wHeight);
        viewShadow2.setLayoutX(Scenes.wWidth);
        viewShadow2.setLayoutY(Scenes.wHeight);
        currentPosition = new ComponentPosition(getCenterX(Scenes.wWidth), getCenterY(Scenes.wHeight));
    }

    static void setPosition()
    {
        moveToPosotion(currentPosition);
    }

    static void moveToPosotion(ComponentPosition position)
    {
        double upLeftX = getUpLeftX(position.x);
        double upLeftY = getUpLeftY(position.y);
        slideTimerForSwordman = slide(view, upLeftX, upLeftY, 200, 0, null);
        slideTimerForShadow1 = slide(viewShadow1, upLeftX, upLeftY, 500, 0, null);
        slideTimerForShadow2 = slide(viewShadow2, upLeftX, upLeftY, 800, 0, null);
    }

    static void bindProperties()
    {
        view.layoutXProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                currentPosition.x = getCenterX(newValue.intValue());
            }
        });
        view.layoutYProperty().addListener(new ChangeListener<Number>()
        {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue)
            {
                currentPosition.y = getCenterY(newValue.intValue());
                //System.out.println("y" + currentPosition.y);
            }
        });
    }

    static ComponentPosition readPosition()
    {
        if (inCombo) {
            return sceneCenterPosition;
        } else {
            return currentPosition;
        }
    }

    static double getUpLeftX(double centerX)
    {
        return centerX - upLeftOffsetX;
    }

    static double getUpLeftY(double centerY)
    {
        return centerY - upLeftOffsetY;
    }

    static double getCenterX(double upLeftX)
    {
        return upLeftX + upLeftOffsetX;
    }

    static double getCenterY(double upLeftY)
    {
        return upLeftY + upLeftOffsetY;
    }

    static void setPositionForSuperCombo(double x, double y, EventHandler<ActionEvent> e)
    {
        double upLeftX = getUpLeftX(x);
        double upLeftY = getUpLeftY(y);
        slideTimerForSwordman = slide(view, upLeftX, upLeftY, 300, 0, null);
        slideTimerForShadow1 = slide(viewShadow1, upLeftX, upLeftY, 300, 100, null);
        slideTimerForShadow2 = slide(viewShadow2, upLeftX, upLeftY, 300, 200, e);
    }

    static ComponentPosition getNewCenterPosition(String moveDirection)
    {
        double newCenterX = 0;
        double newCenterY = 0;
        if (moveDirection == "Up") {
            newCenterY = currentPosition.y - stepSpan;
            if (newCenterY < 0) {
                currentPosition.y = newCenterY;
                newCenterY = 0;
            }
            newCenterX = currentPosition.x;
        } else if (moveDirection == "Down") {
            newCenterY = currentPosition.y + stepSpan;
            if (newCenterY > Scenes.wHeight) {
                newCenterY = Scenes.wHeight;
            }
            newCenterX = currentPosition.x;
        } else if (moveDirection == "Left") {
            newCenterX = currentPosition.x - stepSpan;
            if (newCenterX < 0) {
                newCenterX = 0;
            }
            newCenterY = currentPosition.y;
        } else if (moveDirection == "Right") {
            newCenterX = currentPosition.x + stepSpan;
            if (newCenterX > Scenes.wWidth) {
                newCenterX = Scenes.wWidth;
            }
            newCenterY = currentPosition.y;
        }
        return new ComponentPosition(newCenterX, newCenterY);
    }

    static void bindKeys()
    {
        keyEventHandler = new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event)
            {
                if (collided) {
                    return;
                }
                KeyCode keyCode = event.getCode();
                String keyName = keyCode.getName();
                //System.out.println(keyName);
                if (keyCode.isArrowKey()) {
                    moveToPosotion(getNewCenterPosition(keyName));
                } else if (keyCode.isWhitespaceKey()) {
                    if (ready) {
                        executeOrdinaryMove();
                        Indicators.addSlashVolumn(5);
                    }
                } else {
                    if (keyName == "B") {
                        if (ready) {
                            if (Indicators.ableToFlower()) {
                                executeSuperMove();
                                Indicators.addSlashVolumn(25);
                                Indicators.reduceFlower();
                            }
                        }
                    } else if (keyName == "V") {
                        if (ready) {
                            if (Indicators.ableToSlash()) {
                                prepareSuperCombo();
                                Indicators.drainVolumn();
                            }
                        }
                    } else if (keyName == "T") {
                        gif();
                        //doYanfan();
                        /*
                        MediaPlayer player = new MediaPlayer(new Media(getFileUriStringByFileName(brandishWav)));
                        player.setCycleCount(1);
                        player.play();
                        setTimeout(3000, e -> {
                            player.dispose();
                        });
                        */
                        //player.play();
                        //player.play();
                        //AudioClip player = new AudioClip(getFileUriStringByFileName(brandishWav));
                        //player.play();
                        //BackJob backJob = new BackJob(e -> System.out.println(123));
                        //backJob.start();
                    }
                }
            }
        };
        scene.addEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
    }

    static void unbindKeys()
    {
        scene.removeEventHandler(KeyEvent.KEY_PRESSED, keyEventHandler);
    }

    static void stopAllSlides()
    {
        if (slideTimerForSwordman != null) {
            //System.out.println("1");
            slideTimerForSwordman.stop();
        }
        if (slideTimerForShadow1 != null) {
            //System.out.println("2");
            slideTimerForShadow1.stop();
        }
        if (slideTimerForShadow2 != null) {
            //System.out.println("3");
            slideTimerForShadow2.stop();
        }
    }

    static Timeline slide(ImageView imageView, double toUpLeftX, double toUpLeftY, long duration, long delay, EventHandler<ActionEvent> next)
    {
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(duration),
                new KeyValue(imageView.layoutXProperty(), toUpLeftX),
                new KeyValue(imageView.layoutYProperty(), toUpLeftY)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.setDelay(Duration.millis(delay));
        timeline.play();
        if (next != null) {
            timeline.setOnFinished(next);
        }
        return timeline;
    }

    static void getReady()
    {
        renderGetReadyViewAll();
        ready = true;
        inCombo = false;
    }

    static void renderGetReadyViewAll()
    {
        renderGetReadyView(view);
        renderGetReadyView(viewShadow1);
        renderGetReadyView(viewShadow2);
    }

    static void renderGetReadyView(ImageView imageView)
    {
        imageView.setPreserveRatio(false);
        imageView.setImage(readyImg);
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        imageView.setTranslateY(0);
        imageView.setTranslateX(0);
    }

    static void drawSword(boolean isPoseIndefinitely, double delay)
    {
        renderDrawSwordViewAll();

        //playSimpleSound(brandishMedia);
        brandishPlayer.play();

        if (!isPoseIndefinitely) {
            setTimeout(delay, e -> getReady());
        }
    }

    static void renderDrawSwordViewAll()
    {
        renderDrawSwordView(view);
        renderDrawSwordView(viewShadow1);
        renderDrawSwordView(viewShadow2);
    }

    static void renderDrawSwordView(ImageView imageView)
    {
        imageView.setImage(drawImg);
        imageView.setFitWidth(125);
        imageView.setFitHeight(125);
        imageView.setTranslateY(-25);
        imageView.setTranslateX(0);
    }

    static void createWave(boolean isSuper)
    {
        Wave wave = new Wave(currentPosition.x, currentPosition.y - 50, isSuper);
        wave.load();
        wave.move();
    }

    static void executeOrdinaryMove()
    {
        executeMove(false);
        Statistics.yangYanCount++;
    }

    static void executeSuperMove()
    {
        executeMove(true);
        Statistics.daXueCount++;
    }

    static void executeMove(boolean isSuper)
    {
        int waveCount;
        if (isSuper) {
            waveCount = superDrawWaveCount;
        } else {
            waveCount = 1;
        }
        if (isSuper) {
            drawSword(false, 1000);
        } else {
            drawSword(false, 300);
        }

        for (int i = 0; i < waveCount; i++) {
            createWave(isSuper);
        }
        ready = false;
    }

    static void createSlash(int viewIndex)
    {
        int slashAngle;
        double slashBeginX = currentPosition.x;
        double slashBeginY = currentPosition.y;
        double slashEndX;
        double slashEndY;
        int slashOffset = comboAreaOffset / 2;
        switch (viewIndex) {
            case 0:
                slashAngle = 45;
                slashEndX = slashBeginX + slashOffset;
                slashEndY = slashBeginY - slashOffset;
                break;
            case 1:
                slashAngle = 135;
                slashEndX = slashBeginX + slashOffset;
                slashEndY = slashBeginY + slashOffset;
                break;
            case 2:
                slashAngle = -135;
                slashEndX = slashBeginX - slashOffset;
                slashEndY = slashBeginY + slashOffset;
                break;
            case 3:
                slashAngle = -45;
                slashEndX = slashBeginX - slashOffset;
                slashEndY = slashBeginY - slashOffset;
                break;
            default:
                slashAngle = 0;
                slashEndX = slashBeginX;
                slashEndY = slashBeginY;
                break;
        }
        Slash slash = new Slash(slashBeginX, slashBeginY, slashEndX, slashEndY, 20, slashAngle, 500);
        slash.load();
        slashes[viewIndex] = slash;
    }

    static void executeSlashes()
    {
        for (Slash slash : slashes) {
            slash.execute();
        }
        checkAndSendMonsterFly();
    }

    static void checkAndSendMonsterFly()
    {
        double distanceFromCenter;
        double directionFactor;
        int speed = 8;
        double speedX;
        double speedY;
        double delay;
        for (Monster monster : MonsterDispatcher.monsters) {
            distanceFromCenter = monster.currentPosition.getDistance(sceneCenterPosition);
            if (distanceFromCenter < comboAreaOffset) {
                speedX = speed * monster.currentPosition.caculateDirctionFactorX(sceneCenterPosition);
                speedY = speed * monster.currentPosition.caculateDirctionFactorY(sceneCenterPosition);
                if (speedX == 0 && speedY == 0) {
                    speedX = speed;
                    speedY = speed;
                }
                delay = Random.getRandomInteger(700, 2000);
                monster.stopMoving();
                monster.sendFly(speedX, speedY, delay, Random.getDirection());
                ContactEffect.createOne(monster.currentPosition.x, monster.currentPosition.y, Color.LIGHTBLUE, 2000, delay);
            }
        }
    }

    static void prepareSuperCombo()
    {
        ready = false;
        inCombo = true;
        darkenScene();
        double originalCenterX = currentPosition.x;
        double originalCenterY = currentPosition.y;
        int sceneCenterX = Scenes.wWidth / 2;
        int sceneCenterY = Scenes.wHeight / 2;
        //currentPosition.x = sceneCenterX - comboAreaOffset;
        //currentPosition.y = sceneCenterY + comboAreaOffset;
        setPositionForSuperCombo(sceneCenterX - comboAreaOffset, sceneCenterY + comboAreaOffset, event1 -> {
            createSuperMoveView(0);
            //currentPosition.x = sceneCenterX - comboAreaOffset;
            //currentPosition.y = sceneCenterY - comboAreaOffset;
            setPositionForSuperCombo(sceneCenterX - comboAreaOffset, sceneCenterY - comboAreaOffset, event2 -> {
                createSuperMoveView(1);
                //currentPosition.x = sceneCenterX + comboAreaOffset;
                //currentPosition.y = sceneCenterY - comboAreaOffset;
                setPositionForSuperCombo(sceneCenterX + comboAreaOffset, sceneCenterY - comboAreaOffset, event3 -> {
                    createSuperMoveView(2);
                    //currentPosition.x = sceneCenterX + comboAreaOffset;
                    //currentPosition.y = sceneCenterY + comboAreaOffset;
                    setPositionForSuperCombo(sceneCenterX + comboAreaOffset, sceneCenterY + comboAreaOffset, event4 -> {
                        createSuperMoveView(3);
                        //currentPosition.x = sceneCenterX;
                        //currentPosition.y = sceneCenterY;
                        setPositionForSuperCombo(sceneCenterX, sceneCenterY, event5 -> {
                            createSuperMoveView(4);
                            //currentPosition.x = originalCenterX;
                            //currentPosition.y = sceneCenterY + comboAreaOffset + 10;
                            setPositionForSuperCombo(originalCenterX, sceneCenterY + comboAreaOffset + 10, event6 -> {
                                renderComboViews();
                            });
                        });
                    });
                });
            });
        });
        Statistics.fangZhenCount++;
    }

    static void createSuperMoveView(int viewIndex)
    {
        ImageView comboView = new ImageView();
        comboView.setLayoutX(getUpLeftX(currentPosition.x));
        comboView.setLayoutY(getUpLeftY(currentPosition.y));
        GifPlayer comboPlayer;
        if (viewIndex < 4) {
            comboView.setFitWidth(240);
            comboView.setFitHeight(160);
            boolean isImageReversed;
            if (viewIndex == 0 || viewIndex == 1) {
                comboView.setTranslateX(-30);
                comboView.setTranslateY(-65);
                isImageReversed = false;
            } else {
                comboView.setTranslateY(-65);
                comboView.setTranslateX(-130);
                isImageReversed = true;
            }
            comboPlayer = new GifPlayer(superMoveUpGif, comboView, isImageReversed);
            comboPlayer.renderFrame(0);
            createSlash(viewIndex);
        } else {
            comboView.setFitWidth(210);
            comboView.setFitHeight(140);
            comboView.setTranslateX(-60);
            comboView.setTranslateY(-30);
            comboPlayer = new GifPlayer(superMoveDownGif, comboView, false);
            comboPlayer.renderFrame(3);
        }
        comboViews[viewIndex] = comboView;
        comboPlayers[viewIndex] = comboPlayer;
        addToRoot(comboView);
    }

    static void renderComboViews()
    {
        drawSword(true, 0);
        comboPlayers[0].playFrames(2, 5, 1000, null);
        comboPlayers[1].playFrames(2, 5, 1000, null);
        comboPlayers[2].playFrames(2, 5, 1000, null);
        comboPlayers[3].playFrames(2, 5, 1000, e1 -> {
            comboPlayers[4].playFrames(4, 15, 800, e -> {
                showComboCircle();
                removeFromRoot(comboViews[4]);
                executeSlashes();
                comboPlayers[0].playFrames(6, 19, 1000, null);
                comboPlayers[1].playFrames(6, 19, 1000, null);
                comboPlayers[2].playFrames(6, 19, 1000, null);
                comboPlayers[3].playFrames(6, 19, 1000, e2 -> {
                    removeFromRoot(comboViews[0]);
                    removeFromRoot(comboViews[1]);
                    removeFromRoot(comboViews[2]);
                    removeFromRoot(comboViews[3]);
                    hideComboCircle();
                    lightenScene();
                    showFinishPose(null);
                    showFollowers();
                    setTimeout(5000, e3 -> getReady());
                });
            });
        });
    }

    static List<KeyFrame> getSceneBlinkingFrames(long fromTime, int time)
    {
        ArrayList<KeyFrame> list = new ArrayList<>();
        KeyFrame whiteFrame;
        KeyFrame floorFrame;
        for (int i = 0; i < time; i++) {
            fromTime += 50;
            whiteFrame = new KeyFrame(
                    Duration.millis(fromTime),
                    new KeyValue(scene.fillProperty(), Color.WHITE)
            );
            list.add(whiteFrame);
            if (i == time - 1) {
                break;
            }
            fromTime += 50;
            floorFrame = new KeyFrame(
                    Duration.millis(fromTime),
                    new KeyValue(scene.fillProperty(), floorPattern)
            );
            list.add(floorFrame);
        }
        return list;
    }

    static void darkenScene()
    {
        List<KeyFrame> frames;
        frames = getSceneBlinkingFrames(0, 5);
        KeyFrame darkFrame = new KeyFrame(
                Duration.millis(3000),
                new KeyValue(scene.fillProperty(), Color.BLACK)
        );
        frames.add(darkFrame);
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(frames);
        timeline.play();
    }

    static void lightenScene()
    {
        KeyFrame lightFrame = new KeyFrame(
                Duration.millis(1800),
                new KeyValue(scene.fillProperty(), Color.WHITE)
        );
        KeyFrame floorFrame = new KeyFrame(
                Duration.millis(1850),
                new KeyValue(scene.fillProperty(), floorPattern)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(lightFrame, floorFrame);
        timeline.play();
    }

    static void showFollowers()
    {
        double upLeftX;
        double upLeftY;
        int imageWidth = 200;
        boolean imageReversed;
        if (currentPosition.x < Scenes.wWidth / 2) {
            upLeftX = Scenes.wWidth - imageWidth;
            imageReversed = false;
        } else {
            upLeftX = 0;
            imageReversed = true;
        }
        upLeftY = getUpLeftY(currentPosition.y);
        ImageView followersView = new ImageView();
        followersView.setFitWidth(imageWidth);
        followersView.setPreserveRatio(true);
        followersView.setLayoutX(upLeftX);
        followersView.setLayoutY(upLeftY);
        followersView.setTranslateY(-18);
        addToRoot(followersView);
        GifPlayer player = new GifPlayer(followersGif, followersView, imageReversed);
        player.playAllFrames(3000, e ->
                setTimeout(1000, e2 ->
                        removeFromRoot(followersView)));
    }

    static void showEscape()
    {
        double upLeftX;
        double upLeftY;
        int imageWidth = Scenes.wWidth;
        upLeftX = 0;
        upLeftY = 400;
        ImageView escapeView = new ImageView();
        escapeView.setFitWidth(imageWidth);
        escapeView.setPreserveRatio(true);
        escapeView.setLayoutX(upLeftX);
        escapeView.setLayoutY(upLeftY);
        //escapeView.setTranslateY(-200);
        addToRoot(escapeView);
        GifPlayer player = new GifPlayer(escapeGif, escapeView, false);
        player.playAllFrames(6000, Component.next);
    }

    static void showFinishPose(EventHandler<ActionEvent> next)
    {
        view.setPreserveRatio(true);
        view.setFitWidth(250);
        view.setFitHeight(125);
        view.setTranslateX(-35);
        view.setTranslateY(-40);
        hideShadows();
        GifPlayer player = new GifPlayer(finishPoseGif, view, false);
        player.playAllFrames(1000, next);
    }

    static void hideShadows()
    {
        viewShadow1.setImage(null);
        viewShadow2.setImage(null);
    }

    static void gif()
    {
        gifPlayer.renderNextFrame();
    }

    static void clear()
    {
        removeFromRoot(view);
        removeFromRoot(viewShadow1);
        removeFromRoot(viewShadow2);
    }

    static void doYanfan(EventHandler<ActionEvent> next)
    {
        moveToPosotion(currentPosition);
        hideShadows();
        adjustViewForYanfan();
        doYanfanStart(e -> {
            sendWheels();
            doYanfanFinish(next);
        });
        Statistics.yanFanCount++;
    }

    static void adjustViewForYanfan()
    {
        view.setFitWidth(260);
        view.setFitHeight(250);
        view.setTranslateY(-150);
        view.setTranslateX(-75);
    }

    static void doYanfanStart(EventHandler<ActionEvent> next)
    {
        GifPlayer player = new GifPlayer(yanfanGif, view, false);
        player.playFrames(0, 10, 1500, next);
    }

    static void doYanfanFinish(EventHandler<ActionEvent> next)
    {
        GifPlayer player = new GifPlayer(yanfanGif, view, false);
        player.playFrames(11, 19, 2000, e -> {
            setTimeout(1000, e2 -> player.playFrames(20, 21, 300, next));
        });
    }

    static void sendWheels()
    {
        int speed = 5;
        Wheel wheel;
        double speedX;
        double speedY;
        for (Monster monster : MonsterDispatcher.monsters) {
            double distance = currentPosition.getDistance(monster.currentPosition);
            if (distance > 250) {
                continue;
            }
            speedX  = speed * currentPosition.caculateDirctionFactorX(monster.currentPosition);
            speedY = speed * currentPosition.caculateDirctionFactorY(monster.currentPosition);
            if (speedX == 0 && speedY == 0) {
                speedX = speed;
                speedY = speed;
            }
            wheel = new Wheel(currentPosition.x, currentPosition.y, speedX, speedY, monster);
            wheel.load();
            wheel.go(700);
        }
    }

    static void setSwordmanToRootTop()
    {
        setToRootTop(view);
    }

    static void showFollowerYell()
    {
        Text yellText = new Text(followerYell);
        yellText.setFont(Font.font("Brush Script MT", FontWeight.EXTRA_BOLD, 70));
        yellText.setFill(Color.WHITE);
        yellText.setOpacity(0);
        TextFlow textFlow = new TextFlow(yellText);
        textFlow.setLayoutX(30);
        textFlow.setLayoutY(170);
        addToRoot(textFlow);
        ShakeTransition shakeTransition = new ShakeTransition(textFlow, 6000, 100, 2, false);
        shakeTransition.play();
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(2000),
                new KeyValue(yellText.opacityProperty(), 1)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.play();
    }
}