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
        //double l = (double) 1 / 3 * 1000;
        //System.out.println(l);

         //System.out.println(0.0 / (1.1 / 0));



        launch(args);
    }
}
