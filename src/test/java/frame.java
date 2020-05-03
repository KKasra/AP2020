import GUI.ImageUtil;

import javax.swing.*;
import java.awt.*;


public class frame extends JFrame {
    public frame() throws Exception {
add( new JLabel(new ImageIcon(ImageUtil
        .getWhiteTransparent("gameComponents/deck.png")
        .getScaledInstance(200, 320, Image.SCALE_SMOOTH)
)));
        setVisible(true);
        setSize(new Dimension(800, 800));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }
}

