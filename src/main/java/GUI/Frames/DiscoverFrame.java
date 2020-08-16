package GUI.Frames;

import DB.components.cards.Card;
import DB.components.cards.Passive;
import GUI.CardShape;
import Game.CommandAndResponse.SelectionCommand;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class DiscoverFrame extends JFrame {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 500;


    public synchronized void chooseFrom(List<Object> list) {
        getContentPane().removeAll();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        for (Object o : list) {
            Component component = null;
            if (o instanceof Passive)
                component = new Passive.PassiveShape((Passive) o);
            if (o instanceof Card)
                component = new CardShape((Card)o);

            component.addMouseListener(new GameFrame.CommandAndResponseHandler.DiscoverChoiceMouseAdapter() {
                @Override
                public Object object() {
                    return o;
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    DiscoverFrame.getInstance().setVisible(false);
                }
            });
            getContentPane().add(component, gbc);
            ++gbc.gridx;
        }

        setVisible(true);
    }

    private DiscoverFrame() {
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setBackground(new Color(44, 62, 80));

        JPanel panel = new JPanel(new GridBagLayout());
        setContentPane(panel);
    }

    private static DiscoverFrame instance;
    public static DiscoverFrame getInstance(){
        if (instance == null)
            instance = new DiscoverFrame();
        return instance;
    }
}
