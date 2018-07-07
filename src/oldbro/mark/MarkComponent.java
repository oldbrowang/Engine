package oldbro.mark;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import oldbro.Component;

public class MarkComponent extends Component
{
    public static void loadScene()
    {
        Component.loadScene(Color.BLACK);
    }

    public static void proceed(EventHandler<ActionEvent> next)
    {
        Component.next = next;
        Girl.load();
        Logo.load();
        Girl.drawline();
        Logo.display();
        Girl.walk();
    }
}
