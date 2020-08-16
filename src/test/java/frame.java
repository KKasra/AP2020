import DB.Managment.CardManager;
import GUI.Frames.GameFrame;
import GUI.ImageUtil;
import Game.GameStructure.Cards.CardFactory;

import javax.swing.*;
import java.awt.*;


public class frame extends JFrame {
    public frame() throws Exception {
        setContentPane(new JPanel());

        getContentPane().setPreferredSize(new Dimension(100, 160));

        getContentPane().add(new GameFrame.CardOnBoard(
                CardFactory.produce(null,
                        CardManager.getInstance().getCard("Goldshire Footman"), null), 0));
        setVisible(true);
        setSize(new Dimension(800, 800));
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }
}

