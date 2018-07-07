package oldbro.util;

import javafx.animation.Transition;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class NumberAugmentTransition  extends Transition
{
    private Text text;

    private int from;

    private int to;

    private int offset;

    public NumberAugmentTransition(Text text, int from, int to, double duration)
    {
        this.text = text;
        this.from = from;
        this.to = to;
        this.offset = to - from;
        setCycleDuration(Duration.millis(duration));
    }

    //@Override
    protected void interpolate(double frac)
    {
        int current = from + (int) (offset * frac);
        text.setText(current + "");
    }
}
