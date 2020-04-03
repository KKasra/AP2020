package GUI.MenuPanels;

import GUI.Frames.MenuFrame;
import User.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class MenuPanel extends JPanel {
    private static final Image backGround = Toolkit.getDefaultToolkit().getImage("./Data/images/menu.jpg");
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
        withQuitAndExit(true);
        setFocusable(true);
    }
}
