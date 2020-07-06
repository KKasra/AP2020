package GUI.MenuPanels;


import DB.Managment.HeroManager;
import DB.Managment.Market;
import DB.components.User;
import DB.components.cards.Card;
import DB.components.cards.Deck;
import DB.components.heroes.Hero;
import GUI.CardShape;
import GUI.Frames.MenuFrame;
import GUI.Frames.WarningFrame;
import GUI.MenuButton;


import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
public class CollectionPanel extends MenuPanel{
    //TODO why the frame freezes?!
    private CardDisplayPanel cardDisplayPanel;
    private MouseAdapter pickACard = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            CardInfo.getInstance().selectCard(((CardShape)e.getSource()).card.getCardData());
        }
    };

    private static class DeckInfo extends JPanel{
        private static final int imageWidth = 100;
        private static final int imageHeight = 180;
        private JPanel imagePanel;
        private JLabel nameLabel;
        private JTextField nameText;
        private JLabel heroLabel;
        private JComboBox heroText;
        private JLabel decksLabel;
        private JComboBox decksList;
        private MenuButton changeName;
        private MenuButton changeHero;
        private MenuButton removeDeck;

        private ItemListener choseADeck = new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if (decksList.getItemCount() == User.user.getDecks().size())
                        User.user.setCurrentDeck(decksList.getSelectedIndex());
                    initDeck(User.user.getCurrentDeck());
                }
            }
        };


        public void refreshDeckList() {
            decksList.removeAllItems();
            for (Deck deck : User.user.getDecks()) {
                decksList.addItem(deck.getHero().getHeroName() + ">" + deck.getName());
            }

            decksList.setSelectedIndex(User.user.getDecks().indexOf(User.user.getCurrentDeck()));
        }


        private DeckInfo() {
            //settings
            setPreferredSize(new Dimension(500, 200));
            setLayout(new GridBagLayout());


            initComponents();
            alignComponents();




        }
        private void initComponents(){
            imagePanel = new JPanel();
            imagePanel.setOpaque(false);
            nameLabel = new JLabel("Name:", JLabel.RIGHT);
            nameText = new JTextField();
            heroLabel = new JLabel("Hero:", JLabel.RIGHT);
            heroText = new JComboBox();
            for (Hero hero : HeroManager.getInstance().getHeroes()) {
                heroText.addItem(hero.getHeroName());
            }
            decksLabel = new JLabel("Decks:", JLabel.RIGHT);
            decksList = new JComboBox();
            refreshDeckList();
            decksList.addItemListener(choseADeck);
            changeHero = new MenuButton("change", new Dimension(100, 30), 15);
            changeHero.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    Hero hero = HeroManager.getInstance().getHeroes().get(heroText.getSelectedIndex());
                    try {
                        User.user.getCurrentDeck().setHero(hero);

                    } catch (Exception exception) {
                        WarningFrame.print(exception.getMessage());
                        heroText.setSelectedIndex(HeroManager.getInstance().getHeroes()
                                .indexOf(User.user.getCurrentDeck().getHero()));

                    }
                    initDeck(User.user.getCurrentDeck());
                    refreshDeckList();

                }
            });

            changeName = new MenuButton("change", new Dimension(100, 30), 15);
            changeName.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    User.user.getCurrentDeck().setName(nameText.getText());
                    refreshDeckList();
                    initDeck(User.user.getCurrentDeck());
                }
            });

            removeDeck = new MenuButton("remove", new Dimension(100, 30), 15);
            removeDeck.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    User.user.removeDeck(User.user.getCurrentDeck());
                    initDeck(User.user.getCurrentDeck());
                    refreshDeckList();
                }
            });

            initDeck(User.user.getCurrentDeck());
        }
        private void alignComponents(){
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 1;
            gbc.gridheight = 1;
            gbc.insets = new Insets(0, 0, 0, 0);

            gbc.gridheight = 3;
            add(imagePanel, gbc);

            gbc.gridheight = 1;

            gbc.gridx = 1;
            gbc.gridy = 0;
            add(nameLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 1;
            add(heroLabel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 2;
            add(decksLabel, gbc);

            gbc.gridx = 2;
            gbc.gridy = 0;
            add(nameText, gbc);

            gbc.gridx = 2;
            gbc.gridy = 1;
            add(heroText, gbc);

            gbc.gridx = 2;
            gbc.gridy = 2;
            add(decksList, gbc);

            gbc.gridx = 3;
            gbc.gridy = 0;
            add(changeName, gbc);

            gbc.gridx = 3;
            gbc.gridy = 1;
            add(changeHero, gbc);

            gbc.gridx = 3;
            gbc.gridy = 2;
            add(removeDeck, gbc);
        }


        private static DeckInfo instance;
        public static DeckInfo getInstance() {
            if (instance == null)
                instance = new DeckInfo();
            return instance;
        }
        private void initDeck(Deck deck){

            imagePanel.removeAll();
            JLabel label = new JLabel();
            label.setBorder(new LineBorder(new Color(0, 0, 0)));
            label.setIcon(new ImageIcon(HeroManager.getInstance()
                    .getHeroPicture(deck.getHero())
                    .getScaledInstance(imageWidth, imageHeight, Image.SCALE_SMOOTH)));
            imagePanel.add(label);

            nameText.setText(deck.getName());

            for (int i = 0; i < heroText.getItemCount(); i++)
                if (heroText.getItemAt(i).equals(deck.getHero().getHeroName()))
                    heroText.setSelectedIndex(i);

        }

    }
    private static class addDeck extends JPanel{

        private JLabel nameLabel;
        private JTextField nameField;
        private JLabel heroLabel;
        private JComboBox heroField;
        private MenuButton addButton;

        private addDeck(){
            //settings
            setPreferredSize(new Dimension(500, 80));
            setBorder(new TitledBorder("new Deck"));
            setLayout(new GridBagLayout());

            initComponents();
            alignComponents();
        }

        private void initComponents(){
            nameLabel = new JLabel("Name: ", JLabel.RIGHT);
            nameField = new JTextField();
            heroLabel = new JLabel("Hero: ");
            heroField = new JComboBox();
            heroField.setPreferredSize(new Dimension(100, 25));
            for (Hero hero : HeroManager.getInstance().getHeroes()) {
                heroField.addItem(hero.getHeroName());
            }

            addButton = new MenuButton("Add Deck", new Dimension(100, 50), 20);
            addButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        User.user.addDeck(new Deck(nameField.getText(),
                                HeroManager.getInstance().getHeroes().get(heroField.getSelectedIndex())));
                        DeckInfo.getInstance().refreshDeckList();
                        nameField.setText("");

                    } catch (Exception exception) {
                        WarningFrame.print(exception.getMessage());
                        nameField.setText("");
                    }
                }
            });
        }

        private void alignComponents(){
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridheight = 1;
            gbc.gridwidth = 1;

            add(nameLabel, gbc);

            ++gbc.gridy;
            add(heroLabel, gbc);

            ++gbc.gridx;
            gbc.gridy = 0;
            add(nameField, gbc);

            ++gbc.gridy;
            add(heroField, gbc);

            ++gbc.gridx;
            gbc.gridy = 0;
            gbc.gridheight = 2;
            add(addButton, gbc);

        }


        private static addDeck instance;
        public static addDeck getInstance(){
            if (instance == null)
                instance = new addDeck();
            return instance;
        }
    }
    private static class CardInfo extends JPanel{

        private JPanel cardPanel;
        private JLabel count;
        private JLabel hero;
        private MenuButton available;
        private MenuButton remove;
        private JTextField countField;
        private JTextField heroField;
        private MenuButton storeButton;

        private Card selectedCard;

        private CardInfo() {
            //settings
            setLayout(new GridBagLayout());
            setBorder(new TitledBorder("card info"));

            initComponents();
            alignComponents();

        }
        private void initComponents(){

            cardPanel = new JPanel();
            cardPanel.setPreferredSize(new Dimension(CardShape.WIDTH, CardShape.HEIGHT));
            cardPanel.setOpaque(false);
            count = new JLabel("count: ", JLabel.RIGHT);
            hero = new JLabel("hero: ", JLabel.RIGHT);
            available = new MenuButton("", new Dimension(100, 25), 12);
            available.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try {
                        User.user.getCurrentDeck().addCard(selectedCard);
                        selectCard(selectedCard);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                        WarningFrame.print(exception.getMessage());
                    }
                }
            });
            countField = new JTextField(10);
            heroField = new JTextField(10);
            remove = new MenuButton("remove", new Dimension(100, 25), 15);
            remove.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectedCard == null)
                        WarningFrame.print("select a card");
                    User.user.getCurrentDeck().removeCard(selectedCard);
                    selectCard(selectedCard);
                }
            });
            storeButton = new MenuButton("store",new Dimension(100, 25), 12);
            storeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    MenuFrame.getInstance().setPanel(StorePanel.getInstance());
                }
            });
        }
        private void alignComponents(){
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridheight = 1;
            gbc.gridwidth = 1;

            gbc.gridheight = 4;
            add(cardPanel, gbc);

            ++gbc.gridx;
            gbc.gridheight = 1;
            add(count, gbc);

            ++gbc.gridx;
            add(countField, gbc);

            gbc.gridx = 1;
            ++gbc.gridy;
            add(hero, gbc);

            ++gbc.gridx;
            add(heroField, gbc);

            gbc.gridx = 1;
            ++gbc.gridy;
            add(available, gbc);

            ++gbc.gridx;
            add(remove, gbc);

            ++gbc.gridy;
            add(storeButton, gbc);
        }

        public void selectCard(Card card){
            selectedCard = card;

            cardPanel.removeAll();
            cardPanel.add(new CardShape(card));

            countField.setText(User.user.getCurrentDeck().countCardInDeck(card) + "");
            System.out.println('o');
            System.out.println(card);
            System.out.println(card.getHero());
            System.out.println(heroField);
            try {
                heroField.setText(card.getHero() == null ?
                        "Neutral" : card.getHero().getHeroName());
            }
            catch (Exception e){
             e.printStackTrace();
                }
            System.out.println('c');

            available.setText(User.user.hasCard(card) ?
                    "Add" : "not Available");
            available.setEnabled(User.user.getCurrentDeck().countCardInDeck(card) < 2
                    && User.user.hasCard(card));

        }

        private static CardInfo instance;
        public static CardInfo getInstance(){
            if (instance == null)
                instance = new CardInfo();
            return instance;
        }
    }
    private static class CardFilter extends JPanel{

        private JCheckBox searchByName;
        private JTextField searchField;
        private JCheckBox hero;
        private JCheckBox Mana;
        private JComboBox heroField;
        private JComboBox ManaField;
        private JLabel sortBy;
        private JComboBox sortByField;

        private CardFilter(){
            //settings
            setBorder(new TitledBorder("Filters"));
            setLayout(new GridBagLayout());

            initComponents();
            alignComponents();

        }

        private void initComponents(){
            searchByName = new JCheckBox("search by name:", false);
            searchByName.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FilterCards();
                }
            });

            hero = new JCheckBox("Class: ", false);
            hero.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FilterCards();
                }
            });

            Mana = new JCheckBox("Mana: ", false);
            Mana.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FilterCards();
                }
            });

            sortBy = new JLabel("sortBy", JLabel.RIGHT);
            searchField = new JTextField();
            searchField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    FilterCards();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    FilterCards();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    FilterCards();
                }
            });
            heroField = new JComboBox();
            heroField.addItem("Neutral");
            for (Hero hero1 : HeroManager.getInstance().getHeroes()) {
                heroField.addItem(hero1.getHeroName());
            }

            ManaField = new JComboBox();
            for (int i = 0; i <= 10; ++i)
                ManaField.addItem(i);

            sortByField = new JComboBox();
            for (String strategy : CardDisplayPanel.strategies) {
                sortByField.addItem(strategy);
            }
            sortByField.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FilterCards();
                }
            });
        }
        private void alignComponents(){
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridheight = 1;
            gbc.gridwidth = 1;

            add(searchByName, gbc);

            ++gbc.gridx;
            add(searchField, gbc);

            ++gbc.gridy;
            gbc.gridx = 0;
            add(hero, gbc);

            ++gbc.gridx;
            add(heroField, gbc);

            gbc.gridy = 0;
            gbc.gridx = 2;
            add(Mana, gbc);

            ++gbc.gridx;
            add(ManaField, gbc);

            ++gbc.gridy;
            gbc.gridx = 2;
            add(sortBy, gbc);

            ++gbc.gridx;
            add(sortByField, gbc);
        }

        private void FilterCards(){
            List<Card> cards = Market.getInstance().getCardsForSale();

            if (searchByName.isSelected()) {
                List<Card> res = new ArrayList<Card>();
                String prefix = searchField.getText().toLowerCase();
                for (Card card : cards) {
                    if (card.getName().toLowerCase()
                            .substring(0, Math.min(card.getName().length(), prefix.length())).equals(prefix))
                        res.add(card);
                }
                cards = res;
            }

            if (hero.isSelected()) {
                List<Card> res = new ArrayList<Card>();
                for (Card card : cards) {
                    if (heroField.getSelectedItem().equals("Neutral") && card.getHero() == null)
                        res.add(card);
                    if (card.getHero() != null && heroField.getSelectedItem().equals(card.getHero().getHeroName()))
                        res.add(card);
                }
                cards = res;
            }
            if (Mana.isSelected()) {
                List<Card> res = new ArrayList<Card>();
                for (Card card : cards) {
                    if (card.getManaCost() == ManaField.getSelectedIndex())
                        res.add(card);
                }
                cards = res;
            }
            CollectionPanel.getInstance().cardDisplayPanel.setCards(cards);
            CollectionPanel.getInstance().cardDisplayPanel.initCardPanel(sortByField.getSelectedIndex());
        }

        private static CardFilter instance;
        public static CardFilter getInstance(){
            if (instance == null)
                instance = new CardFilter();
            return instance;
        }
    }
    private JPanel panel;


    private CollectionPanel() {
        //settings
        setPreferredSize(new Dimension(1500, 700));
        setLayout(new GridBagLayout());



        cardDisplayPanel = new CardDisplayPanel(1000, 700,pickACard);
        cardDisplayPanel.setCards(Market.getInstance().getCardsForSale());
        cardDisplayPanel.initCardPanel(0);
        initPanel();
        alignComponents();


    }

    private void alignComponents(){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.BOTH;

        add(cardDisplayPanel, gbc);
        ++gbc.gridx;
        add(panel, gbc);
    }
    private void initPanel(){
        panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 1;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        panel.add(DeckInfo.getInstance(), gbc);

        ++gbc.gridy;
        panel.add(addDeck.getInstance(), gbc);

        ++gbc.gridy;
        panel.add(CardInfo.getInstance(), gbc);

        ++gbc.gridy;
        panel.add(CardFilter.getInstance(), gbc);

        panel.setBackground(new Color(255, 138, 101));
        DeckInfo.getInstance().setOpaque(false);
        addDeck.getInstance().setOpaque(false);
        CardInfo.getInstance().setOpaque(false);
        CardFilter.getInstance().setOpaque(false);


    }

    private static CollectionPanel instance;
    public static CollectionPanel getInstance() {
        if (instance == null)
            instance = new CollectionPanel();
        return instance;
    }


}
