package oldbro;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application
{
    @Override
    public void start(Stage primaryStage) throws Exception
    {
        Scenes.stage = primaryStage;
        Scenes.begin();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}
