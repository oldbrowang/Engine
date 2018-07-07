package oldbro.fight;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.util.Duration;
import oldbro.Component;
import oldbro.Scenes;
import oldbro.Statistics;
import oldbro.util.GifPlayer;

import java.util.concurrent.ExecutorService;

public class FightComponent extends Component
{
    static String fightFloorPng;

    static ImagePattern floorPattern;

    static String pinganMp3;

    static MediaPlayer musicPlayer;

    static boolean isFightStopped;

    static {
        fightFloorPng = "resource/floor.jpg";
        pinganMp3 = "resource/pingan.mp3";
    }

    public static void preload()
    {
        ImageView animationView = new ImageView();
        preLoadGifFrames(Tinman.monsterGif, animationView);
        preLoadGifFrames(Zombie.monsterGif, animationView);
        preLoadGifFrames(Devil.monsterGif, animationView);
        preLoadGifFrames(Ghosts.monsterGif, animationView);
        preLoadGifFrames(Goblin.monsterGif, animationView);
        preLoadGifFrames(Mob.monsterGif, animationView);
        preLoadGifFrames(Mummy.monsterGif, animationView);
        preLoadGifFrames(Phantom.monsterGif, animationView);
    }

    public static void loadScene()
    {
        Image floorImg = new Image(getInputStreamByFileName(fightFloorPng));
        floorPattern = new ImagePattern(floorImg, 0, 0, 1000, 400, false);
        musicPlayer = new MediaPlayer(new Media(getFileUriStringByFileName(pinganMp3)));
        musicPlayer.setCycleCount(10);
        //musicPlayer.setStartTime(Duration.millis(72000));
        //musicPlayer.setStopTime(Duration.millis(95000));
        Component.loadScene(floorPattern);
    }

    public static void proceed(EventHandler<ActionEvent> next)
    {
        Component.next = e -> {
            musicPlayer.stop();
            musicPlayer.dispose();
            setTimeout(500, next);
        };
        isFightStopped = false;
        musicPlayer.play();
        Tutorials.load();
        Swordman.load();
        Indicators.load();
        MonsterDispatcher.load();
        Tutorials.play(e -> {
            Swordman.fight();
            setTimeout(5000, e2 -> MonsterDispatcher.dispatch());
        });



        //Swordman.fight();
        //setTimeout(5000, e -> MonsterDispatcher.dispatch());


    }

    public static void stopAllComponents()
    {
        MonsterDispatcher.stopDispatcher();
        MonsterDispatcher.stopAllMonsters();
        Swordman.stopAllSlides();
        Swordman.unbindKeys();
    }

    public static void stopFight()
    {
        isFightStopped = true;
        musicPlayer.pause();
        stopAllComponents();
        Blinds.load();
        setTimeout(1000, e -> Blinds.show(e2 -> CountDown.begin()));
    }

    public static void resumeFight()
    {
        CountDown.clear();
        Swordman.setSwordmanToRootTop();
        Blinds.hide(e -> {
            Swordman.doYanfan(e2 -> {
                Swordman.resumeFight();
                MonsterDispatcher.makeAllMonstersMove();
                isFightStopped = false;
                musicPlayer.play();
                setTimeout(10000, e3 -> {
                    MonsterDispatcher.dispatch();
                });
            });
        });
    }

    public static void checkAndCloseFight()
    {
        System.out.println(MonsterDispatcher.getCurrentMonsterNumber());
        if (MonsterDispatcher.dispatchDone && MonsterDispatcher.getCurrentMonsterNumber() == 0) {
            double delay;
            if (Swordman.inCombo) {
                delay = 12000;
                System.out.println(delay);
            } else {
                delay = 1000;
                System.out.println(delay);
            }
            setTimeout(delay, e -> {
                Swordman.showFinishPose(null);
                Swordman.unbindKeys();
                setTimeout(3000, e2 -> {
                    Swordman.clear();
                    Swordman.showFollowerYell();
                    Swordman.showEscape();
                });
            });
        }
    }
}