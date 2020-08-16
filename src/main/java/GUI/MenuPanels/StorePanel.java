package GUI.MenuPanels;

import DB.components.cards.Card;
import GUI.CardShape;
import GUI.Frames.WarningFrame;
import GUI.MenuButton;
import Network.Client;
import Network.Responses.StoreData;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class StorePanel extends MenuPanel {

    private static final List<String> strategies =
            Arrays.asList(new String[]{"by type", "by coin cost", "by name", "by mana", "by class"});
    private static int columns = 6;
    private static final Image COIN_IMAGE = Toolkit.getDefaultToolkit().getImage(
            "./Data/images/coins.jpg"
    ).getScaledInstance(70, 70, Image.SCALE_SMOOTH);

    private CardDisplayPanel cardsPanel;
    private Card chosenCard;
    private int sortStrategy;

    private StoreData data;

    public void update(StoreData data) {
        this.data = data;
        ButtonsPanel.getInstance().coins.setText(data.getUserCoins() + "");
        initCardPanel(sortStrategy, data.getCardList());
        if (chosenCard != null){
            for (Card card : data.getCardList()) {
                if (card.getName().equals(chosenCard.getName()))
                    chosenCard = card;
            }
            setChosenCard(chosenCard);
        }
    }

    private static class ButtonsPanel extends JPanel{

        private JLabel cost;
        private JButton buy;
        private JPanel chosenCardPanel;
        private JPanel searchPanel;
        private JLabel coins;



        private ButtonsPanel() {
            //settings
            setBackground(new Color(93, 109, 126));
            setPreferredSize(new Dimension(500, 700));
            setLayout(new GridBagLayout());

            initComponents();
            alignComponents();

        }
        private void initComponents() {
            initSearchPanel();

            chosenCardPanel = new JPanel();
            chosenCardPanel.setPreferredSize(new Dimension(CardShape.WIDTH + 10, CardShape.HEIGHT + 10));
            chosenCardPanel.setOpaque(false);

            buy = new MenuButton("Buy it", new Dimension(150, 70), 20);
            buy.addActionListener(e -> {
                if (StorePanel.getInstance().chosenCard == null) {
                    WarningFrame.print("you didn't choose a card!");
                    return;
                }
                Client.getInstance().sendBuyRequest(StorePanel.getInstance().chosenCard.getName());
            });

            cost = new JLabel("---");
            cost.setPreferredSize(new Dimension(100,70));
            cost.setBackground(new Color(252, 243, 207, 200));
            cost.setHorizontalAlignment(SwingConstants.CENTER);
            cost.setOpaque(true);


            coins = new JLabel(){
                @Override
                protected void paintComponent(Graphics g) {
                    g.drawImage(COIN_IMAGE, 0, 0,null);
                    super.paintComponent(g);

                }
            };
            coins.setHorizontalAlignment(JLabel.CENTER);
            coins.setPreferredSize(new Dimension(70, 70));
            coins.setFont(new Font("arial", Font.BOLD, 20));
        }
        private void alignComponents() {

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 3;
            gbc.gridheight = 1;
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.anchor = GridBagConstraints.CENTER;


            add(searchPanel, gbc);


            ++gbc.gridy;
            add(chosenCardPanel, gbc);


            gbc.gridwidth = 1;
            ++gbc.gridy;
            add(buy, gbc);

            ++gbc.gridx;
            add(cost, gbc);

            ++gbc.gridx;
            add(coins, gbc);
        }


        static ButtonsPanel instance;
        static ButtonsPanel getInstance(){
            if (instance == null)
                instance = new ButtonsPanel();
            return instance;
        }
        private void initSearchPanel() {
            searchPanel = new JPanel(new GridBagLayout());
            searchPanel.setBorder(new TitledBorder("Filters"));


            JComboBox sortStrategy = new JComboBox(strategies.toArray());
            sortStrategy.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    StorePanel.getInstance().sortStrategy = strategies.indexOf(e.getItem());
                }
            });

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;

            searchPanel.add(new JLabel("sort by:", JLabel.RIGHT), gbc);

            ++gbc.gridx;
            searchPanel.add(sortStrategy, gbc);


        }

    }



    private StorePanel() {
        //settings
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;

        //add cardsPanel
        cardsPanel = new CardDisplayPanel(1000, 700, new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                setChosenCard(((CardShape)e.getSource()).card.getCardData());
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(cardsPanel, gbc);

        //add ButtonsPanel

        gbc.gridx = 1;
        add(ButtonsPanel.getInstance());


    }

    private void initCardPanel(int sortType, List<Card> cards){
        cardsPanel.setCards(cards);
        cardsPanel.initCardPanel(sortType);
    }

    public void setChosenCard(Card chosenCard) {
        this.chosenCard = chosenCard;
        ButtonsPanel.instance.chosenCardPanel.removeAll();
        ButtonsPanel.instance.chosenCardPanel.add(new CardShape(chosenCard));
        ButtonsPanel.instance.cost.setText(chosenCard.getCoinCost() + "");
        ButtonsPanel.getInstance().buy.setText(data.hasCard(chosenCard) ?
                "Sell it" : "Buy it");
    }

    private static StorePanel instance;
    public static StorePanel getInstance() {
        if (instance == null)
            instance = new StorePanel();
        return instance;
    }


}
