package oldbro.util;

import javafx.animation.Interpolator;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.image.*;
import org.w3c.dom.Node;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

public class GifPlayer
{
    public String fileName;

    public int gifWidth;

    public int gifHeight;

    public ImageReader reader;

    public ImageView view;

    public boolean frameHorizontallyReversed;

    public int currentFrameIndex;

    public GifTransition gifTransition;

    public WritableImage[] horizontallyUnreversedFrames;

    public WritableImage[] horizontallyReversedFrames;

    public int frameCount;

    public static Map<String, WritableImage[][]> frameCache;

    public boolean toTrancate;

    public int fromX;

    public int fromY;

    public int toX;

    public int toY;

    static {
        frameCache = new HashMap<>();
    }

    public GifPlayer(String fileName, ImageView view, boolean frameHorizontallyReversed)
    {
        this(fileName, view);
        setIsFrameHorizontallyReversed(frameHorizontallyReversed);
    }

    public GifPlayer(String fileName, ImageView view)
    {
        this.fileName = fileName;
        this.view = view;
        initReader();
        recordGifParams();
        currentFrameIndex = -1;
        frameCount = getFrameCount();
        if (frameCache.containsKey(fileName)) {
            horizontallyUnreversedFrames = frameCache.get(fileName)[0];
            horizontallyReversedFrames = frameCache.get(fileName)[1];
        } else {
            horizontallyUnreversedFrames = new WritableImage[frameCount];
            horizontallyReversedFrames = new WritableImage[frameCount];
            WritableImage[][] frames = new WritableImage[2][];
            frames[0] = horizontallyUnreversedFrames;
            frames[1] = horizontallyReversedFrames;
            frameCache.put(fileName, frames);
        }
    }

    public void setIsFrameHorizontallyReversed(boolean frameHorizontallyReversed)
    {
        this.frameHorizontallyReversed = frameHorizontallyReversed;
    }

    public void toggleHorizontalDirection()
    {
        this.frameHorizontallyReversed = !this.frameHorizontallyReversed;
    }

    public void initReader()
    {
        try {
            Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("gif");
            ImageReader reader = readers.next();
            ImageInputStream inputStream = ImageIO.createImageInputStream(new File(fileName));
            reader.setInput(inputStream);
            this.reader = reader;
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void recordGifParams()
    {
        try {
            BufferedImage originalGif = ImageIO.read(new File(fileName));
            gifWidth = originalGif.getWidth();
            gifHeight = originalGif.getHeight();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public int getFrameCount()
    {
        int count = 0;
        try {
            count = reader.getNumImages(true);
        } catch (Exception e) {
            System.out.println(e);
        }
        return count;
    }

    public void renderFrame(int frameIndex)
    {
        //System.out.println(frameIndex);
        try {
            if (horizontallyUnreversedFrames[frameIndex] == null) {
                BufferedImage bufferedImage = reader.read(frameIndex);
                Node imageDescriptor = reader.getImageMetadata(frameIndex).getAsTree("javax_imageio_gif_image_1.0").getFirstChild();
                int offsetX = Integer.parseInt(imageDescriptor.getAttributes().getNamedItem("imageLeftPosition").getNodeValue());
                int offsetY = Integer.parseInt(imageDescriptor.getAttributes().getNamedItem("imageTopPosition").getNodeValue());
                WritableImage frame = SwingFXUtils.toFXImage(bufferedImage, null);
                frame = fillFrame(frame, offsetX, offsetY);
                horizontallyUnreversedFrames[frameIndex] = frame;
                horizontallyReversedFrames[frameIndex] = reverseFrameHorizontally(frame);
            }
            WritableImage frameToRender;
            if (frameHorizontallyReversed) {
                frameToRender = horizontallyReversedFrames[frameIndex];
            } else {
                frameToRender = horizontallyUnreversedFrames[frameIndex];
            }
            if (toTrancate) {
                frameToRender = truncateFrame(frameToRender);
            }
            view.setImage(frameToRender);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void renderAllFrame()
    {
        for (int i = 0; i < frameCount; i++) {
            renderFrame(i);
        }
    }

    public void renderNextFrame()
    {
        currentFrameIndex++;
        //System.out.println(currentFrameIndex);
        renderFrame(currentFrameIndex);
    }

    public void playFrames(int fromIndex, int toIndex, double duration, EventHandler<ActionEvent> next)
    {
        gifTransition = new GifTransition(this, fromIndex, toIndex, duration);
        gifTransition.setCycleCount(1);
        gifTransition.setInterpolator(Interpolator.LINEAR);
        gifTransition.play();
        gifTransition.setOnFinished(next);
    }

    public void playAllFrames(double duration, EventHandler<ActionEvent> finishCallBack)
    {
        playFrames(0, frameCount - 1, duration, finishCallBack);
    }

    public WritableImage reverseFrameHorizontally(WritableImage orginal)
    {
        int width = (int) (orginal.getWidth());
        int height = (int) (orginal.getHeight());
        WritableImage reversed = new WritableImage(width, height);
        PixelReader reader = orginal.getPixelReader();
        PixelWriter writer = reversed.getPixelWriter();
        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                int rgb = reader.getArgb(w, h);
                writer.setArgb(width - w - 1, h, rgb);
            }
        }
        return reversed;
    }

    public WritableImage truncateFrame(WritableImage orginal)
    {
        int width = (int) (orginal.getWidth());
        int height = (int) (orginal.getHeight());
        WritableImage trancated = new WritableImage(toX - fromX + 1, toY - fromY + 1);
        PixelReader reader = orginal.getPixelReader();
        PixelWriter writer = trancated.getPixelWriter();
        for (int h = 0; h < height; h++) {
            if (h < fromY || h > toY) {
                continue;
            }
            for (int w = 0; w < width; w++) {
                if (w < fromX || w > toX) {
                    continue;
                }
                int rgb = reader.getArgb(w, h);
                //System.out.println(h);
                writer.setArgb(w - fromX, h - fromY, rgb);
            }
        }
        return trancated;
    }

    public WritableImage fillFrame0(WritableImage orginal)
    {
        int originalWidth = (int) (orginal.getWidth());
        int originalHeight = (int) (orginal.getHeight());
        WritableImage fullImage = new WritableImage(gifWidth, gifHeight);
        PixelReader reader = orginal.getPixelReader();
        PixelWriter writer = fullImage.getPixelWriter();
        int wOffset = (int) Math.ceil(((double) gifWidth - originalWidth) / 2);
        int hOffset = (int) Math.ceil(((double) gifHeight - originalHeight) / 2);
        for (int h = 0; h < gifHeight; h++) {
            for (int w = 0; w < gifWidth; w++) {
                if (h >= hOffset && h < gifHeight - hOffset && w >= wOffset && w < gifWidth - wOffset) {
                    int rgb = reader.getArgb(w - wOffset, h - hOffset);
                    writer.setArgb(w, h, rgb);
                } else {
                    writer.setArgb(w, h, 0);
                    //writer.setArgb(w, h, 255 << 24 | 0 << 16 | 0 << 8 | 0);
                }
            }
        }
        return fullImage;
    }

    public WritableImage fillFrame(WritableImage orginal, int offsetX, int offsetY)
    {
        int originalWidth = (int) (orginal.getWidth());
        int originalHeight = (int) (orginal.getHeight());
        WritableImage fullImage = new WritableImage(gifWidth, gifHeight);
        PixelReader reader = orginal.getPixelReader();
        PixelWriter writer = fullImage.getPixelWriter();
        for (int h = 0; h < gifHeight; h++) {
            for (int w = 0; w < gifWidth; w++) {
                if (h >= offsetY && h < offsetY + originalHeight && w >= offsetX && w < offsetX + originalWidth) {
                    int rgb = reader.getArgb(w - offsetX, h - offsetY);
                    writer.setArgb(w, h, rgb);
                } else {
                    writer.setArgb(w, h, 0);
                    //writer.setArgb(w, h, 255 << 24 | 0 << 16 | 0 << 8 | 0);
                }
            }
        }
        return fullImage;
    }

    public void stop()
    {
        if (gifTransition != null) {
            gifTransition.stop();
        }
    }
}