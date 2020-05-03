package GUI.Frames;

import DB.components.cards.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class SummonFrame extends JFrame {

    public static final int WIDTH = 1000;
    public static final int HEIGHT = 500;

    private Container chosenItem;
    private Thread thread;

    public void chooseFrom(List<Container> list, ActionListener listener) {
        setContentPane(new JLayeredPane());
        Container pane = getContentPane();

        pane.removeAll();
        chosenItem = null;
        for (Container component : list) {
            component.setBounds(WIDTH * pane.getComponentCount() / list.size(), 0,
                    component.getPreferredSize().width, component.getPreferredSize().height);
            component.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    SummonFrame.getInstance().setVisible(false);
                    chosenItem = component;
                    synchronized (SummonFrame.getInstance()) {
                        SummonFrame.getInstance().notify();
                    }

                }
            });
            pane.add(component, pane.getComponentCount());
        }
        setVisible(true);

        thread = new Thread(new Runnable(){
            @Override
            public void run() {
                    try {
                        synchronized (SummonFrame.getInstance()) {
                            SummonFrame.getInstance().wait();
                            listener.actionPerformed(
                                    new ActionEvent(chosenItem, 0, "item selected"));
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


            }
        });
        thread.start();

    }

    private SummonFrame() {
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setBackground(new Color(44, 62, 80));
    }

    private static SummonFrame instance;
    public static SummonFrame getInstance(){
        if (instance == null)
            instance = new SummonFrame();
        return instance;
    }
}
