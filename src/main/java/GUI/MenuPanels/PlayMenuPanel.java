package GUI.MenuPanels;

import GUI.MenuButton;
import Network.Client;
import Network.Requests.PlayRequest;

import javax.swing.*;
import java.awt.*;

public class PlayMenuPanel extends MenuPanel {
    private MenuButton classicModeButton;
    private MenuButton deckReaderModeButton;
    private MenuButton tavernBrawlModeButton;
    private MenuButton trainingButton;

    private PlayMenuPanel() {
        initComponents();
        alignComponents();
    }

    private void initComponents() {
        classicModeButton = new MenuButton("Classic", new Dimension(300, 100), 20);
        deckReaderModeButton = new MenuButton("Deck Reader", new Dimension(300, 100), 20);
        tavernBrawlModeButton = new MenuButton("Tavern Brawl", new Dimension(300,100), 20);
        trainingButton = new MenuButton("Training", new Dimension(300,100), 20);
        classicModeButton.addActionListener(e ->
                Client.getInstance().sendPlayRequest(PlayRequest.PlayMode.classic));
        deckReaderModeButton.addActionListener(e ->
                Client.getInstance().sendPlayRequest(PlayRequest.PlayMode.deckReader));
        tavernBrawlModeButton.addActionListener(e ->
                Client.getInstance().sendPlayRequest(PlayRequest.PlayMode.tavernBrawl));
        trainingButton.addActionListener(e ->
                Client.getInstance().sendPlayRequest(PlayRequest.PlayMode.training));
    }

    private void alignComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridy = 0;
        add(classicModeButton, gbc);

        ++gbc.gridy;
        add(deckReaderModeButton, gbc);

        ++gbc.gridy;
        add(tavernBrawlModeButton, gbc);

        ++gbc.gridy;
        add(trainingButton, gbc);

    }

    private static PlayMenuPanel instance;
    public static PlayMenuPanel getInstance() {
        if (instance == null)
            instance = new PlayMenuPanel();
        return instance;
    }
}
