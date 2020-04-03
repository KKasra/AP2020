package GUI.Frames;

import GUI.MenuPanels.Loginpanel;
import GUI.MenuPanels.MenuPanel;
import GUI.Sound;
import User.User;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Timer;
import java.util.TimerTask;

public class MenuFrame extends JFrame {
    public static final int width = 1800;
    public static final int height = 1000;
    private static final int repaintPeriodMS = 16;
    private static final Sound theme = new Sound("AntMarsh");
    private MenuPanel panel;
    private MenuFrame() {

        //panel
        setPanel(Loginpanel.getInstance());

        //settings
        setSize(new Dimension(width, height));
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
                theme.clip.loop(Clip.LOOP_CONTINUOUSLY);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                if (User.user != null)
                    User.user.getLog().writeEvent("sign_out", "");
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {
                theme.clip.loop(Clip.LOOP_CONTINUOUSLY);
            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        setTitle("HeartStroke");
        //repaint
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!isVisible()) {
                    theme.clip.stop();
                    return;
                }
                revalidate();
                repaint();
            }
        }, 0, repaintPeriodMS);


    }

    public void setPanel(MenuPanel panel) {
        this.panel = panel;
        setContentPane(panel);
        panel.requestFocus();

    }
    public MenuPanel getPanel() {
        return this.panel;

    }

    private static MenuFrame instance;
    public static MenuFrame getInstance() {
        if (instance == null)
            instance = new MenuFrame();
        return instance;
    }

}
