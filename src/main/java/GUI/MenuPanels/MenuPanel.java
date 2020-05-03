package GUI.MenuPanels;

import GUI.ChangingPageNotifier;
import GUI.Frames.MenuFrame;
import DB.components.User;
import GUI.Frames.WarningFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MenuPanel extends JPanel {
    private static final Image backGround = Toolkit.getDefaultToolkit().getImage("./Data/images/menu.jpg")
            .getScaledInstance(MenuFrame.width - 200, MenuFrame.height - 100, Image.SCALE_DEFAULT);
    private static KeyListener QuitAndExit = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                MenuFrame.getInstance().setPanel(MainMenu.getInstance());
                User.user.getLog().writeEvent("Navigate", "MainMenu");
            }

        }

        @Override
        public void keyReleased(KeyEvent e) {

        }
    };
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backGround, 0, 0, null);
    }
    public void withQuitAndExit(boolean b) {
        if (b)
            addKeyListener(QuitAndExit);
        else
            removeKeyListener(QuitAndExit);
    }

    public MenuPanel() {
        setPreferredSize(new Dimension(MenuFrame.width, MenuFrame.height));
        withQuitAndExit(true);
        setFocusable(true);
        ChangingPageNotifier.getInstance().register(this);
    }
    public void notify(MenuPanel nextPanel){

    }
}
