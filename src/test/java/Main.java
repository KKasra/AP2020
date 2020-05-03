import DB.UserDB;
import DB.components.User;
import GUI.Frames.GameFrame;
import Game.Gamestructure.Game;

import java.awt.*;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;

public class Main {
    public static Image applyAlphaChannel(final Image img, final Color color) {
        if (color == null || img == null) {
            return img;
        }

        final ImageFilter filter = new RGBImageFilter() {

            // the color we are looking for... Alpha bits are set to opaque
            public final int markerRGB = color.getRGB() | 0xFF000000;

            @Override
            public final int filterRGB(final int x, final int y, final int rgb) {
                if ((rgb | 0xFF000000) == this.markerRGB) {
                    // Mark the alpha bits as zero - transparent
                    return 0x00FFFFFF & rgb;
                } else {
                    // nothing to do
                    return rgb;
                }
            }
        };

        final ImageProducer ip = new FilteredImageSource(img.getSource(), filter);
        return Toolkit.getDefaultToolkit().createImage(ip);
    }

    public static void main(String[] args) throws Exception {
        User.user = UserDB.getUser("","");
        GameFrame.launch(new Game());
//new frame();
    }
}
