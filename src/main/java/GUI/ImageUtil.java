package GUI;

import java.awt.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

public class ImageUtil {
    public static Image getTransparent(Image img, Color color) {
        ImageFilter filter = new RGBImageFilter() {
            public int markerRGB = color.getRGB() | 0xFF000000;

            @Override
            public final int filterRGB(final int x, final int y, final int rgb) {
                if ((rgb | 0xFF000000) == this.markerRGB) {
                    return 0x00FFFFFF & rgb;
                } else {
                    return rgb;
                }
            }
        };

        ImageProducer ip = new FilteredImageSource(img.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    public static Image getWhiteTransparent(Image img) {
        return getTransparent(img, new Color(255, 255, 255));
    }

    public static Image getWhiteTransparent(String path) {
        return getWhiteTransparent(getImage(path));
    }

    public static Image getImage(String path) {
        return Toolkit.getDefaultToolkit().getImage("./Data/images/" + path);
    }


}
