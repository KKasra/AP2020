package GUI.Frames;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FramePainter {
    public static final int delayMS = 50;
    private static  Timer timer;
    private static JFrame currentFrame;
    public static void paint(JFrame frame) {
        if (timer != null)
            timer.stop();
        timer = new Timer(delayMS, e -> {
            frame.revalidate();
            frame.repaint();
        });
        timer.start();
        if (currentFrame != null)
            currentFrame.setVisible(false);
        frame.setVisible(true);
        currentFrame = frame;
    }


}
