package oldbro.util;

import javafx.animation.Transition;
import javafx.util.Duration;

public class GifTransition extends Transition
{
    public int fromIndex;

    public int toIndex;

    public int frameCount;

    public GifPlayer player;

    public int currentIndex;

    public GifTransition(GifPlayer player, int fromIndex, int toIndex, double duration)
    {
        super();
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
        this.player = player;
        frameCount = toIndex - fromIndex + 1;
        setCycleDuration(Duration.millis(duration));
        currentIndex = -1;
    }

    @Override
    protected void interpolate(double frac)
    {
        int indexOfframeToRender;
        if (frac == 1.0) {
            indexOfframeToRender = toIndex;
        } else {
            indexOfframeToRender = (int) (frameCount * frac) + fromIndex;
        }
        if (indexOfframeToRender != currentIndex) {
            player.renderFrame(indexOfframeToRender);
            currentIndex = indexOfframeToRender;
        }
    }
}
