package oldbro.opening;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import oldbro.Component;
import oldbro.Scenes;

public class OpeningComponent extends Component
{
    public static void loadScene()
    {
        Component.loadScene(Color.WHITE);
    }

    public static void proceed(EventHandler<ActionEvent> next)
    {
        Component.next = next;
        Sword.load();
        OpeningShow.load();
        StyleWords.load();
        Sword.moveToReadyPosition();
        StyleWords.showSchoolName(e -> {
            Sword.move(e2 -> {
                StyleWords.showMoveName(e3 -> {
                    OpeningShow.showOpening();
                });
            });
        });
    }

}
