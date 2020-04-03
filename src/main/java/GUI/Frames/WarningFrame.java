package GUI.Frames;

import GUI.Sound;

import javax.swing.*;
import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;

public class WarningFrame extends JFrame {
    private static WarningFrame instance;
    private JLabel label = new JLabel();
    private static Sound sound = new Sound("Error");
    public static void print(String massage) {
        sound.clip.setMicrosecondPosition(0);
        sound.clip.start();

        if (instance == null)
            instance = new WarningFrame();
        instance.label.setText(massage);
        instance.repaint();
        instance.setVisible(true);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                instance.setVisible(false);
            }
        }, 500);
    }

    private WarningFrame() {
        setSize(700, 200);
        setContentPane(new JPanel());
        getContentPane().setBackground(new Color(44, 62, 80));
        setLocationRelativeTo(null);
        label.setBounds(0, 0, 700, 500);
        label.setFont(new Font("arial", Font.BOLD, 40));
        label.setForeground(Color.yellow);
        getContentPane().add(label);
    }
}
