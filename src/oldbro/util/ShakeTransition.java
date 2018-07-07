package oldbro.util;

import javafx.animation.Transition;
import javafx.scene.Node;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ShakeTransition extends Transition
{
    private Node node;

    private double initLayoutX;

    private double initLayoutY;

    private double fracStep;

    private int doneCount;

    private int extent;

    private boolean isFixed;

    public ShakeTransition(Node node, double duration, int count, int extent, boolean isFixed)
    {
        super();
        this.node = node;
        initLayoutX = node.getLayoutX();
        initLayoutY = node.getLayoutY();
        fracStep = 1.0 / count;
        doneCount = 0;
        this.extent = extent;
        this.isFixed = isFixed;
        setCycleDuration(Duration.millis(duration));
    }

    @Override
    protected void interpolate(double frac)
    {
        if (frac >= fracStep * (doneCount + 1)) {
            double baseLayoutX;
            double baseLayoutY;
            if (isFixed) {
                baseLayoutX = initLayoutX;
                baseLayoutY = initLayoutY;
            } else {
                baseLayoutX = node.getLayoutX();
                baseLayoutY = node.getLayoutY();
            }
            node.setLayoutX(baseLayoutX + Random.getRandomInteger(-extent, extent));
            node.setLayoutY(baseLayoutY + Random.getRandomInteger(-extent, extent));
            doneCount++;
        }
    }
}
