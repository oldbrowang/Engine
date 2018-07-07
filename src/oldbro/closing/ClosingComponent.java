package oldbro.closing;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import oldbro.Component;

public class ClosingComponent extends Component
{
    public static void preload()
    {
        //System.out.println("preload begun");
        ImageView animationView = new ImageView();
        preLoadGifFrames(ScoresAndRanks.comboShowGif, animationView);
        preLoadGifFrames(ClosingShow.closingComboShowGif, animationView);
        preLoadGifFrames(ClosingShow.landingLightningGif, animationView);
        preLoadGifFrames(ClosingShow.lightningCloudsGif, animationView);
        //System.out.println("preload done");
    }

    public static void loadScene()
    {
        Component.loadScene(Color.BLACK);
    }

    public static void proceed(EventHandler<ActionEvent> next)
    {
        Component.next = next;

        ScoresAndRanks.load();
        ScoresAndRanks.start(e -> {
            ClosingShow.load();
            ClosingShow.showClosing();
        });

        //ScoresAndRanks.throwApples();
    }
}
