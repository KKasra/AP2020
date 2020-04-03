package GUI.Frames;

import Game.Gamestructure.Game;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {


    public static void launch(Game game) {
        getInstance().setVisible(true);
    }

    private static GameFrame instance;
    public static GameFrame getInstance() {
        if (instance == null)
            instance = new GameFrame();
        return instance;
    }
    private GameFrame() {
        //settings
        setSize(new Dimension(MenuFrame.width, MenuFrame.height));
        setResizable(false);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
}
