package oldbro.story;


import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import oldbro.Scenes;
import oldbro.util.GifPlayer;

class PrologueShow extends StoryComponent
{
    static String prologueGif;

    static String prologueBackground;

    static ImageView view;

    static ImageView backgroundView;

    static {
        prologueGif = "resource/prologue.gif";
        prologueBackground = "resource/prologueBackground.jpg";
    }

    static void load()
    {
        backgroundView = new ImageView(new Image(getInputStreamByFileName(prologueBackground)));
        backgroundView.setPreserveRatio(true);
        backgroundView.setFitWidth(Scenes.wWidth);
        //addToRoot(backgroundView);
        //view = new ImageView(new Image(getInputStreamByFileName(prologueGif)));
        view = new ImageView();
        view.setPreserveRatio(true);
        view.setFitWidth(Scenes.wWidth);
        view.setLayoutY(320);
        //addToRoot(view);
    }

    static void play(EventHandler<ActionEvent> next)
    {
        addToRoot(backgroundView);
        addToRoot(view);
        GifPlayer player = new GifPlayer(prologueGif, view, false);
        player.playFrames(0, 7, 2000, e -> {
            setTimeout(2000, e2 -> {
                player.playFrames(9, 22, 3000, e3 -> {
                    player.playFrames(23, 47, 3500, e4 -> {
                        setTimeout(2000, e5 -> {
                            removeFromRoot(view);
                            removeFromRoot(backgroundView);
                            setTimeout(1500, next);
                        });
                    });
                });
            });
        });
    }
}