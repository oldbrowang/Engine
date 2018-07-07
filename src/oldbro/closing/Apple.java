package oldbro.closing;

import javafx.animation.AnimationTimer;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import oldbro.Scenes;
import oldbro.util.Random;

class Apple extends ClosingComponent
{
    static String redApplePng;

    static String greenApplePng;

    static final double g = 350;

    ImageView view;

    double speed;

    double xSpeed;

    double ySpeed;

    AnimationTimer timer;

    long beginTime;

    double beginX;

    double beginY;

    static {
        redApplePng = "resource/redApple.png";
        greenApplePng = "resource/greenApple.png";
    }

    Apple()
    {
        String png;
        if (Random.getDirection() == 1) {
            png = redApplePng;
        } else {
            png = greenApplePng;
        }
        view = new ImageView(new Image(getInputStreamByFileName(png)));
        view.setLayoutY(Scenes.wHeight + 100);
        view.setLayoutX(Random.getRandomFloat(-20, Scenes.wWidth - 20));
        view.setPreserveRatio(true);
        view.setFitWidth(Random.getRandomFloat(30, 150));
        view.setOpacity(0.8);
        speed = Random.getRandomInteger(30, 50);
        addToRoot(view);
    }

    Apple(int type, int width, double xSpeed, double ySpeed)
    {
        String png;
        if (type == 1) {
            png = redApplePng;
        } else {
            png = greenApplePng;
        }
        beginX = -50;
        beginY = Scenes.wHeight + 50;
        view = new ImageView(new Image(getInputStreamByFileName(png)));
        view.setLayoutX(beginX);
        view.setLayoutY(beginY);
        view.setPreserveRatio(true);
        view.setFitWidth(width);
        view.setOpacity(1);
        this.xSpeed = xSpeed;
        this.ySpeed = ySpeed;
        addToRoot(view);
    }

    void move()
    {
        timer = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                double newLayoutY = view.getLayoutY() - speed;
                if (view.getLayoutY() < -100) {
                    timer.stop();
                    removeFromRoot(view);
                } else {
                    view.setLayoutY(view.getLayoutY() - speed);
                }
            }
        };
        timer.start();
    }

    void toThrow(EventHandler<ActionEvent> next)
    {
        beginTime = System.nanoTime();
        timer = new AnimationTimer()
        {
            @Override
            public void handle(long now)
            {
                double timeOffset = (double) (now - beginTime) / 1000000000;
                if (timeOffset < 0) {
                    return;
                }
                //System.out.println(timeOffset);
                double xOffset = xSpeed * timeOffset;
                double yOffset = ySpeed * timeOffset - 0.5 * g * timeOffset * timeOffset;
                view.setLayoutX(beginX + xOffset);
                view.setLayoutY(beginY - yOffset);
                if (yOffset < 0) {
                    timer.stop();
                    removeFromRoot(view);
                    setTimeout(100, next);
                }
            }
        };
        timer.start();
    }
}
