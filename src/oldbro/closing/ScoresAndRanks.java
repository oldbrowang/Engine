package oldbro.closing;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import oldbro.Player;
import oldbro.Scenes;
import oldbro.Statistics;
import oldbro.util.GifPlayer;
import oldbro.util.NumberAugmentTransition;
import oldbro.util.Random;
import java.util.Arrays;


public class ScoresAndRanks extends ClosingComponent
{
    static Text monsterKillCountText;

    static Text yangYanCountText;

    static Text daXueCountText;

    static Text fangZhenCountText;

    static Text yanFanCountText;

    static GridPane scoreItemPane;

    static GridPane rankListPane;

    static TextFlow scoreFlow;

    static Text scoreText;

    static int youRank;

    static String comboShowGif;

    static ImageView background;

    static Player[] players;

    static Font scoreItemFont;

    static Font rankItemFont;

    static {
        comboShowGif = "resource/scoreComboShow.gif";
    }

    static void load()
    {
        players = new Player[5];
        scoreItemFont = Font.font("KaiTi", 70);
        rankItemFont = Font.font("Microsoft YaHei", 80);
    }

    static void start(EventHandler<ActionEvent> next)
    {
        calculateScores();
        createBackground();
        createScoreItems();
        createYouScore();
        createRankList();
        playBackgroundFrames();
        showScoreItems();
        showScoreAugmentation(e -> {
            hideScoreItems();
            moveYouScore(e2 -> {
                showRankList(e3 -> {
                    hideYouScore();
                    throwApples(e4 -> {
                        showApples(e5 -> {
                            hideRankList();
                            setTimeout(1000, next);
                        });
                    });

                });
            });
        });
    }

    static void createYouScore()
    {
        scoreText = new Text("0");
        scoreText.setFont(rankItemFont);
        scoreText.setFill(Color.WHITE);
        scoreFlow = new TextFlow(scoreText);
        scoreFlow.setLayoutX(670);
        scoreFlow.setLayoutY(650);
        addToRoot(scoreFlow);
    }

    static void createBackground()
    {
        background = new ImageView();
        background.setPreserveRatio(true);
        background.setFitWidth(800);
        background.setOpacity(0.3);
        background.setLayoutX(100);
        background.setLayoutY(50);
        addToRoot(background);
    }

    static void playBackgroundFrames()
    {
        GifPlayer player = new GifPlayer(comboShowGif, background);
        player.playFrames(0, 75, 6500, e -> fadeBackground());
    }

    static void fadeBackground()
    {
        fadeThenRemove(background, 1000);
    }

    static void fadeThenRemove(Node node, double duration)
    {
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(duration),
                new KeyValue(node.opacityProperty(), 0)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.setOnFinished(e -> removeFromRoot(node));
        timeline.play();
    }

    static void createScoreItems()
    {
        scoreItemPane = new GridPane();
        scoreItemPane.setVgap(30);
        scoreItemPane.setPrefWidth(Scenes.wWidth);
        scoreItemPane.setAlignment(Pos.CENTER);
        monsterKillCountText = createScoreItemText(Statistics.monsterKillCountName, Statistics.monsterKillCount);
        monsterKillCountText.setOpacity(0);
        yangYanCountText = createScoreItemText(Statistics.yangYanCountName, Statistics.yangYanCount);
        yangYanCountText.setOpacity(0);
        daXueCountText = createScoreItemText(Statistics.daXueCountName, Statistics.daXueCount);
        daXueCountText.setOpacity(0);
        fangZhenCountText = createScoreItemText(Statistics.fangZhenCountName, Statistics.fangZhenCount);
        fangZhenCountText.setOpacity(0);
        yanFanCountText = createScoreItemText(Statistics.yanFanCountName, Statistics.yanFanCount);
        yanFanCountText.setOpacity(0);
        scoreItemPane.addRow(0, monsterKillCountText);
        scoreItemPane.addRow(1, yangYanCountText);
        scoreItemPane.addRow(2, daXueCountText);
        scoreItemPane.addRow(3, fangZhenCountText);
        scoreItemPane.addRow(4, yanFanCountText);
        scoreItemPane.setLayoutY(100);
        scoreItemPane.setOpacity(0);
        addToRoot(scoreItemPane);
    }

    static void moveYouScore(EventHandler<ActionEvent> next)
    {
        //1 50
        //2 186
        //3 322
        int layoutY = 50 + (youRank - 1) * 136;
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(1000),
                new KeyValue(scoreFlow.layoutXProperty(), 597),
                new KeyValue(scoreFlow.layoutYProperty(), layoutY)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.setOnFinished(next);
        timeline.play();
    }

    static void hideYouScore()
    {
        fadeThenRemove(scoreFlow, 1000);
    }

    static void hideRankList()
    {
        fadeThenRemove(rankListPane, 1000);
    }

    static void showScoreItems()
    {
        scoreItemPane.setOpacity(1);
        setTimeout(500, e -> monsterKillCountText.setOpacity(1));
        setTimeout(1000, e -> yangYanCountText.setOpacity(1));
        setTimeout(1500, e -> daXueCountText.setOpacity(1));
        setTimeout(2000, e -> fangZhenCountText.setOpacity(1));
        setTimeout(2500, e -> yanFanCountText.setOpacity(1));
    }

    static void hideScoreItems()
    {
        fadeThenRemove(scoreItemPane, 3000);
    }

    static void showScoreAugmentation(EventHandler<ActionEvent> next)
    {
        NumberAugmentTransition transition = new NumberAugmentTransition(scoreText, 0, Statistics.score, 3000);
        transition.setOnFinished(next);
        transition.setDelay(Duration.millis(1000));
        transition.play();
    }

    static void calculateScores()
    {
        Statistics.calculate();
        int youScore = Statistics.score;
        players[0] = new Player(Player.youName, youScore);
        players[1] = new Player(Player.gatesName, getRandomScore(youScore));
        players[2] = new Player(Player.tysonName, getRandomScore(youScore));
        players[3] = new Player(Player.luhanName, getRandomScore(youScore));
        players[4] = new Player(Player.oldbroName, getRandomScore(youScore));
        Arrays.sort(players);
        for (int i = 0; i < players.length; i++) {
            players[i].rank = i + 1;
        }
    }

    static void createRankList()
    {
        rankListPane = new GridPane();
        rankListPane.setVgap(30);
        rankListPane.setPrefWidth(Scenes.wWidth);
        rankListPane.setAlignment(Pos.CENTER);
        rankListPane.setLayoutY(50);
        for (int i = 0; i < players.length; i++) {
            Player player = players[i];
            if (player.name == Player.youName) {
                youRank = player.rank;
            }
            rankListPane.addRow(i, createRankItemText(player));
        }
        rankListPane.setOpacity(0);
        addToRoot(rankListPane);
    }

    static void showRankList(EventHandler<ActionEvent> next)
    {
        KeyFrame keyFrame = new KeyFrame(
                Duration.millis(1500),
                new KeyValue(rankListPane.opacityProperty(), 1)
        );
        Timeline timeline = new Timeline();
        timeline.setCycleCount(1);
        timeline.setAutoReverse(false);
        timeline.getKeyFrames().addAll(keyFrame);
        timeline.setOnFinished(next);
        timeline.setDelay(Duration.millis(2500));
        timeline.play();
    }

    static int getRandomScore(int youScore)
    {
        return Random.getRandomInteger(youScore - 1000, youScore + 1000);
    }

    static Text createScoreItemText(String name, int score)
    {
        Text text = new Text(name + "\t" + score);
        text.setFill(Color.WHITE);
        text.setFont(scoreItemFont);
        return text;
    }

    static Text createRankItemText(Player player)
    {
        Text text = new Text(player.name + "\t\t" + player.score);
        //text.setFill(Color.WHITE);
        boolean hasOutline;
        if (player.rank == 1) {
            text.setFill(Color.GOLD);
            hasOutline = true;
        } else if (player.rank == 2) {
            text.setFill(Color.SILVER);
            hasOutline = true;
        } else if (player.rank == 3) {
            text.setFill(Color.BROWN);
            hasOutline = true;
        } else {
            text.setFill(Color.GRAY);
            hasOutline = false;
        }
        if (hasOutline) {
            text.setStrokeWidth(2);
            text.setStroke(Color.WHITE);
        }
        text.setFont(rankItemFont);
        return text;
    }

    static void showApples(EventHandler<ActionEvent> next)
    {
        for (int i = 0; i < 100; i++) {
            setTimeout(Random.getRandomInteger(100, 3000), e -> {
                Apple apple = new Apple();
                apple.move();
            });
        }
        setTimeout(3000, next);
    }

    static void throwApples(EventHandler<ActionEvent> next)
    {
        Apple apple1 = new Apple(-1, 80, 330, 550);
        apple1.toThrow(null);
        Apple apple2 = new Apple(1, 85, 300, 600);
        apple2.toThrow(null);
        Apple apple3 = new Apple(-1, 90, 265, 650);
        apple3.toThrow(null);
        Apple apple4 = new Apple(1, 95, 240, 710);
        apple4.toThrow(null);
        Apple apple5 = new Apple(-1, 100, 225, 770);
        apple5.toThrow(next);
    }
}