package GUI.Frames;

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
                    DiscoverFrame.getInstance().setVisible(false);
                    chosenItem = component;
                    synchronized (DiscoverFrame.getInstance()) {
                        DiscoverFrame.getInstance().notify();
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
                        synchronized (DiscoverFrame.getInstance()) {
                            DiscoverFrame.getInstance().wait();
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

    private DiscoverFrame() {
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setBackground(new Color(44, 62, 80));
    }

    private static DiscoverFrame instance;
    public static DiscoverFrame getInstance(){
        if (instance == null)
            instance = new DiscoverFrame();
        return instance;
    }
}
