package oldbro;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import oldbro.mark.MarkComponent;
import oldbro.closing.ClosingComponent;
import oldbro.opening.OpeningComponent;
import oldbro.fight.FightComponent;
import oldbro.story.StoryComponent;

public class Scenes
{
    public static Stage stage;

    public static final int wWidth = 1000;

    public static final int wHeight = 800;

    public static String title = "Bro";

    public static String iconPng = "resource/redApple.png";

    public static void begin()
    {
        stage.setTitle(title);
        stage.getIcons().add(new Image(Component.getInputStreamByFileName(iconPng)));
        stage.setResizable(false);
        showMark(e -> {
            showOpening(e2 -> {
                showStory(e3 -> {
                    showFight(e4 -> {
                        showClosing(e5 -> begin());
                    });
                });
            });
        });
        stage.show();
    }

    public static void showMark(EventHandler<ActionEvent> next)
    {
        MarkComponent.loadScene();
        MarkComponent.proceed(next);
    }

    public static void showOpening(EventHandler<ActionEvent> next)
    {
        OpeningComponent.loadScene();
        OpeningComponent.proceed(next);
    }

    public static void showStory(EventHandler<ActionEvent> next)
    {
        StoryComponent.loadScene();
        StoryComponent.proceed(next);
    }

    public static void showFight(EventHandler<ActionEvent> next)
    {

        Component.nowLoading(e -> FightComponent.preload(), e -> {
            FightComponent.loadScene();
            FightComponent.proceed(next);
        });



        //FightComponent.loadScene();
        //FightComponent.proceed(next);
    }

    public static void showClosing(EventHandler<ActionEvent> next)
    {


        Component.nowLoading(e -> ClosingComponent.preload(), e -> {
            ClosingComponent.loadScene();
            ClosingComponent.proceed(next);
        });
        //ClosingComponent.loadScene();
        //ClosingComponent.proceed(next);
    }
}
