package GUI.MenuPanels;

import GUI.*;
import GUI.Frames.DeleteAccountFrame;
import GUI.Frames.MenuFrame;
import Network.Client;
import Network.Requests.DeleteAccount;
import Network.Requests.PlayRequest;
import Network.Requests.State;

import java.awt.*;

public class MainMenu extends MenuPanel {
    private static MainMenu instance;
    private MenuButton Play;
    private  MenuButton Store;
    private MenuButton Status;
    private MenuButton Collection;
    private ChatPanel chatPanel;
    private MenuButton deleteAccount;

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
        initDeleteAccount();
        chatPanel = ChatPanel.getInstance();

        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridheight = 1;

        gbc.gridy++;
        add(Play, gbc);

        gbc.gridy++;
        add(Store, gbc);

        gbc.gridy++;
        add(Status, gbc);

        gbc.gridy++;
        add(Collection, gbc);

        gbc.gridy++;
        add(deleteAccount, gbc);

        gbc.gridx++;
        gbc.gridy = 0;
        gbc.gridheight = 7;
        add(chatPanel, gbc);


    }

    private void initDeleteAccount() {
        deleteAccount = new MenuButton("delete Account", new Dimension(300, 100), 30);
        deleteAccount.addActionListener(a -> DeleteAccountFrame.getInstance().setVisible(true));
    }

    private void initPlay() {
        Play = new MenuButton("Play", new Dimension(300, 100), 30);
        Play.addActionListener(e -> {
            MenuFrame.getInstance().setPanel(PlayMenuPanel.getInstance());
//          Client.getInstance().sendPlayRequest(PlayRequest.PlayMode.deckReader);
        });
    }
    private void initStore() {
        Store = new MenuButton("Store", new Dimension(300, 100), 30);
        Store.addActionListener(e -> {
            Client.getInstance().setState(State.store);
            MenuFrame.getInstance().setPanel(StorePanel.getInstance());
        });
    }
    private void initStatus() {
        Status = new MenuButton("Status", new Dimension(300, 100), 30);
        Status.addActionListener(e -> {
            Client.getInstance().setState(State.status);
            MenuFrame.getInstance().setPanel(StatusPanel.getInstance());
        });
    }
    private void initCollection() {
        Collection = new MenuButton("Collection", new Dimension(300, 100), 30);
        Collection.addActionListener(e -> {
            Client.getInstance().setState(State.collection);
            MenuFrame.getInstance().setPanel(CollectionPanel.getInstance());
        });
    }
}
