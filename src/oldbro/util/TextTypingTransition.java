package oldbro.util;

import javafx.animation.Transition;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class TextTypingTransition extends Transition
{
    private Text text;

    private String fullTextLine;

    private int letterCount;

    public TextTypingTransition(Text text, String fullTextLine, double duration)
    {
        super();
        this.text = text;
        this.fullTextLine = fullTextLine;
        letterCount = fullTextLine.length();
        setCycleDuration(Duration.millis(duration));
    }

    @Override
    protected void interpolate(double frac)
    {
        int from = 0;
        int to = (int) (letterCount * frac);
        text.setText(fullTextLine.substring(from, to));
    }
}
