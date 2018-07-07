package oldbro.util;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class BackJob
{
    public boolean isDone;

    EventHandler<ActionEvent> doing;

    public BackJob(EventHandler<ActionEvent> doing)
    {
        isDone = false;
        this.doing = doing;
    }

    public void start()
    {
        Service<Void> service = new Service<Void>()
        {
            @Override
            protected Task<Void> createTask()
            {
                Task<Void> task = new Task<Void>()
                {
                    @Override
                    protected Void call() throws Exception
                    {
                        doing.handle(new ActionEvent());
                        return null;
                    }
                };
                return task;
            }
        };
        service.start();
        //System.out.println("back job started");
        service.setOnSucceeded(e -> isDone = true);
    }
}
