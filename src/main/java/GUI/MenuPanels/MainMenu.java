package GUI.MenuPanels;

import GUI.*;
import GUI.Frames.GameFrame;
import GUI.Frames.MenuFrame;
import GUI.Frames.WarningFrame;
import Game.Gamestructure.Game;
import DB.components.User;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends MenuPanel {
    private static MainMenu instance;

    public static MainMenu getInstance() {
        if (instance == null)
            instance = new MainMenu();
        return instance;
    }

    private MainMenu() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        initPlay();
        initStatus();
        initStore();
        initCollection();


        gbc.gridy= 0;
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridy++;
        add(Play, gbc);

        gbc.gridy++;
        add(Store, gbc);

        gbc.gridy++;
        add(Status, gbc);

        gbc.gridy++;
        add(Collection, gbc);

    }

    private MenuButton Play;
    private void initPlay() {
        Play = new MenuButton("Play", new Dimension(300, 100), 30);
        Play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User.user.getLog().writeEvent("Game", "launch");
                try {
                    GameFrame.launch(new Game());
                    MenuFrame.getTheme().clip.stop();
                }catch (Exception exception) {
                    exception.printStackTrace();
                    WarningFrame.print(exception.getMessage());
                    MenuFrame.getInstance().setPanel(CollectionPanel.getInstance());
                }
            }
        });
    }
    private  MenuButton Store;
    private void initStore() {
        Store = new MenuButton("Store", new Dimension(300, 100), 30);
        Store.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                User.user.getLog().writeEvent("Navigate", "Store");
                MenuFrame.getInstance().setPanel(StorePanel.getInstance());
            }
        });
    }
    private MenuButton Status;
    private void initStatus() {
        Status = new MenuButton("Status", new Dimension(300, 100), 30);
        Status.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User.user.getLog().writeEvent("Navigate", "Status");
                MenuFrame.getInstance().setPanel(StatusPanel.getInstance());
            }
        });
    }
    private MenuButton Collection;
    private void initCollection() {
        Collection = new MenuButton("Collection", new Dimension(300, 100), 30);
        Collection.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                User.user.getLog().writeEvent("Navigate", "Collection");
                MenuFrame.getInstance().setPanel(CollectionPanel.getInstance());
            }
        });
    }
}
