package GUI.MenuPanels;

import DB.components.User;
import DB.components.cards.Card;
import GUI.CardShape;
import GUI.Frames.WarningFrame;
import GUI.MenuButton;
import DB.Managment.Market;

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
    private Market market = Market.getInstance();
    private Card chosenCard;
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
            buy.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (StorePanel.getInstance().chosenCard == null) {
                        WarningFrame.print("you didn't choose a card!");
                        return;
                    }
                    try {
                        if (User.user.hasCard(StorePanel.getInstance().chosenCard))
                            Market.getInstance().sell(StorePanel.getInstance().chosenCard);
                        else
                            Market.getInstance().buy(StorePanel.getInstance().chosenCard);
                        StorePanel.getInstance().setChosenCard(StorePanel.getInstance().chosenCard);
                    } catch (Exception ex) {
                        WarningFrame.print(ex.getMessage());
                    }
                    coins.setText(User.user.getCoins() + "");
                }
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
            coins.setText(User.user.getCoins() + "");
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

        public JLabel getCost() {
            return cost;
        }

        public void setCost(JLabel cost) {
            this.cost = cost;
        }

        public JPanel getChosenCardPanel() {
            return chosenCardPanel;
        }

        public void setChosenCardPanel(JPanel chosenCardPanel) {
            this.chosenCardPanel = chosenCardPanel;
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
            sortStrategy.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {

                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        System.out.println(strategies.indexOf(e.getItem()));
                        StorePanel.getInstance().initCardPanel(strategies.indexOf(e.getItem()));
                    }
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
        initCardPanel(0);

        gbc.gridx = 0;
        gbc.gridy = 0;
        add(cardsPanel, gbc);

        //add ButtonsPanel

        gbc.gridx = 1;
        add(ButtonsPanel.getInstance());


    }

    private void initCardPanel(int sortType){
        if (cardsPanel.getCards() == null || cardsPanel.getCards().size() == 0)
            cardsPanel.setCards(market.getCardsForSale());
        cardsPanel.initCardPanel(sortType);
    }

    public void setChosenCard(Card chosenCard) {
        this.chosenCard = chosenCard;
        ButtonsPanel.instance.chosenCardPanel.removeAll();
        ButtonsPanel.instance.chosenCardPanel.add(new CardShape(chosenCard));
        ButtonsPanel.instance.cost.setText(chosenCard.getCoinCost() + "");
        ButtonsPanel.getInstance().buy.setText(User.user.hasCard(chosenCard) ?
                "Sell it" : "Buy it");
    }

    private static StorePanel instance;
    public static StorePanel getInstance() {
        if (instance == null)
            instance = new StorePanel();
        return instance;
    }


}
