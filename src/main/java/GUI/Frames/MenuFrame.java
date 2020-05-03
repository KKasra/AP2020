package GUI.Frames;

import DB.UserDB;
import GUI.MenuPanels.Loginpanel;
import GUI.MenuPanels.MenuPanel;
import GUI.Sound;
import DB.components.User;

import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MenuFrame extends JFrame {
    public static final int width = 1800;
    public static final int height = 1000;
    private static final int repaintPeriodMS = 30;
    private static final Sound theme = new Sound("AntMarsh");
    private boolean isFocused;

    private JPanel panel;
    private JMenuItem backButton;
    private ArrayList<JPanel> menuStackTrace = new ArrayList<JPanel>();
    private Timer timer;

    private MenuFrame() {

        //panel
        setPanel(Loginpanel.getInstance());

        // menu bar
        backButton = new JMenuItem("Back");
        backButton.setBorder(new EtchedBorder(EtchedBorder.RAISED));


        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (menuStackTrace.size() == 1)
                    return;
                menuStackTrace.remove(menuStackTrace.size() - 1);
                JPanel now = menuStackTrace.get(menuStackTrace.size() - 1);
                menuStackTrace.remove(menuStackTrace.size() - 1);
                setPanel(now);

            }
        });
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(backButton);
        setJMenuBar(menuBar);

        //settings
        setSize(new Dimension(width, height));
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                theme.clip.loop(Clip.LOOP_CONTINUOUSLY);
            }

            @Override
            public void windowClosing(WindowEvent e) {
                if (User.user != null) {
                    User.user.getLog().writeEvent("sign_out", "");
                    UserDB.saveChanges(User.user);
                    theme.clip.stop();
                }
            }
        });
        setTitle("HeartStroke");
        FramePainter.paint(this);
    }

    public void setPanel(JPanel panel) {
        this.panel = panel;
        setContentPane(panel);

        menuStackTrace.add(panel);

    }
    public JPanel getPanel() {
        return this.panel;

    }

    private static MenuFrame instance;
    public static MenuFrame getInstance() {
        if (instance == null)
            instance = new MenuFrame();
        return instance;
    }

    public static Sound getTheme() {
        return theme;
    }
}
