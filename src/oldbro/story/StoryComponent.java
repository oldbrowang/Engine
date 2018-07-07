package oldbro.story;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import oldbro.Component;

public class StoryComponent extends Component
{
    public static void loadScene()
    {
        Component.loadScene(Color.WHITE);
    }

    public static void proceed(EventHandler<ActionEvent> next)
    {
        Component.next = next;
        PrologueShow.load();
        PrologueShow.play(e -> {
            StoryShow.load();
            StoryShow.flashCursor();
            StoryShow.printStroy();
            StoryShow.showEating();
            StoryShow.showYaoguai();
            StoryShow.moveUkyoView(e2 -> {
                StoryShow.coverUp();
                StoryShow.showWhiteMask();
            });
        });
    }
}
