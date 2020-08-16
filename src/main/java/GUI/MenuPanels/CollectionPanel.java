package GUI.MenuPanels;


import DB.components.cards.Card;
import DB.components.cards.Deck;
import DB.components.heroes.Hero;
import GUI.CardShape;
import GUI.Frames.MenuFrame;
import GUI.Frames.WarningFrame;
import GUI.MenuButton;
import Network.Client;
import Network.Responses.CollectionData;


import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
public class CollectionPanel extends MenuPanel{
    private CardDisplayPanel cardDisplayPanel;
    private MouseAdapter pickACard = new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
            CardInfo.getInstance().selectCard(((CardShape)e.getSource()).card.getCardData());
        }
    };

    private CollectionData data;

    public void update(CollectionData data) {
        this.data = data;

        if (cardDisplayPanel.getCards().size() == 0)
            CardFilter.getInstance().filterCards();

        DeckInfo.getInstance().initDeck(data.getDecks().get(data.getChosenDeck()));

        DeckInfo.getInstance().refreshDeckList(data);

        int selectedHero = DeckInfo.getInstance().heroText.getSelectedIndex();
        DeckInfo.getInstance().heroText.removeAll();
        for (Hero hero : data.getHeroes()) {
            DeckInfo.getInstance().heroText.addItem(hero.getHeroName());
        }
        DeckInfo.getInstance().heroText.setSelectedIndex(selectedHero);

        selectedHero = addDeck.getInstance().heroField.getSelectedIndex();
        addDeck.getInstance().heroField.removeAll();
        for (Hero hero : data.getHeroes()) {
            addDeck.getInstance().heroField.addItem(hero.getHeroName());
        }
        addDeck.getInstance().heroField.setSelectedIndex(selectedHero);

        selectedHero = CardFilter.getInstance().heroField.getSelectedIndex();
        CardFilter.getInstance().heroField.removeAll();
        CardFilter.getInstance().heroField.addItem("Neutral");
        for (Hero hero : data.getHeroes()) {
            CardFilter.getInstance().heroField.addItem(hero.getHeroName());
        }
        CardFilter.getInstance().heroField.setSelectedIndex(selectedHero);

        if (CardInfo.getInstance().selectedCard != null) {
            Card card = null;
            for (Card dataCard : data.getCards()) {
                if (dataCard.getName().equals(CardInfo.getInstance().selectedCard.getName()))
                    card = dataCard;
            }
            CardInfo.getInstance().selectCard(card);
        }

    }


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
                    if (decksList.getItemCount() == CollectionPanel.getInstance().data.getDecks().size())
                        Client.getInstance().setCurrentDeck(
                                CollectionPanel.getInstance().data.getDecks().get(decksList.getSelectedIndex()));
                }
            }
        };


        public void refreshDeckList(CollectionData data) {
            int index = decksList.getSelectedIndex();
            decksList.removeAllItems();
            for (Deck deck : data.getDecks()) {
                decksList.addItem(deck.getHero().getHeroName() + ">" + deck.getName());
            }
            decksList.setSelectedIndex(index);
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
            decksLabel = new JLabel("Decks:", JLabel.RIGHT);
            decksList = new JComboBox();
            decksList.addItemListener(choseADeck);
            changeHero = new MenuButton("change", new Dimension(100, 30), 15);
            changeHero.addActionListener(e -> {
                Hero hero = CollectionPanel.getInstance().data.getHeroes().get(heroText.getSelectedIndex());
                Deck deck = CollectionPanel.getInstance().data.getDecks().get(
                        CollectionPanel.getInstance().data.getChosenDeck()
                );
                Client.getInstance().changeHeroOfDeck(deck, hero);

            });

            changeName = new MenuButton("change", new Dimension(100, 30), 15);
            changeName.addActionListener(e -> {
                Deck deck = CollectionPanel.getInstance().data.getDecks().get(
                        CollectionPanel.getInstance().data.getChosenDeck()
                );
                Client.getInstance().changeDeckName(deck, nameText.getText());
            });

            removeDeck = new MenuButton("remove", new Dimension(100, 30), 15);
            removeDeck.addActionListener(e -> {
                Deck deck = CollectionPanel.getInstance().data.getDecks().get(
                        CollectionPanel.getInstance().data.getChosenDeck()
                );
                Client.getInstance().removeDeck(deck);
            });


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
            label.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage("./Data/images/gameComponents/heroes/" +
                        deck.getHero().getHeroName() + ".jpg")
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
            nameField = new JTextField(15);
            heroLabel = new JLabel("Hero: ");
            heroField = new JComboBox();
            heroField.setPreferredSize(new Dimension(100, 25));
            addButton = new MenuButton("Add Deck", new Dimension(100, 50), 20);
            addButton.addActionListener(e -> {
                Hero hero = CollectionPanel.getInstance().data.getHeroes().get(heroField.getSelectedIndex());
                Client.getInstance().addDeck(nameField.getText(), hero);
                nameField.setText("");

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
            available.addActionListener(e -> {
                    Deck deck = CollectionPanel.getInstance().data.getDecks().get(
                            CollectionPanel.getInstance().data.getChosenDeck()
                    );
                    Client.getInstance().addCardToDeck(deck, selectedCard);
                    selectCard(selectedCard);

            });
            countField = new JTextField(10);
            heroField = new JTextField(10);
            remove = new MenuButton("remove", new Dimension(100, 25), 15);
            remove.addActionListener(e -> {
                if (selectedCard == null)
                    WarningFrame.print("select a card");
                Deck deck = CollectionPanel.getInstance().data.getDecks().get(
                        CollectionPanel.getInstance().data.getChosenDeck()
                );
                Client.getInstance().removeCardFromDeck(deck, selectedCard);
            });
            storeButton = new MenuButton("store",new Dimension(100, 25), 12);
            storeButton.addActionListener(e -> MenuFrame.getInstance().setPanel(StorePanel.getInstance()));
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
            Deck deck = CollectionPanel.getInstance().data.getDecks().get(
                    CollectionPanel.getInstance().data.getChosenDeck()
            );
            selectedCard = card;
            cardPanel.removeAll();
            cardPanel.add(new CardShape(card));
            countField.setText(deck.countCardInDeck(card) + "");
            heroField.setText(card.getHero() == null ?
                    "Neutral" : card.getHero().getHeroName());


            available.setText("add");

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
            searchByName.addActionListener(e -> filterCards());

            hero = new JCheckBox("Class: ", false);
            hero.addActionListener(e -> filterCards());

            Mana = new JCheckBox("Mana: ", false);
            Mana.addActionListener(e -> filterCards());

            sortBy = new JLabel("sortBy", JLabel.RIGHT);
            searchField = new JTextField();
            searchField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    filterCards();
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    filterCards();
                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    filterCards();
                }
            });
            heroField = new JComboBox();
            ManaField = new JComboBox();
            for (int i = 0; i <= 10; ++i)
                ManaField.addItem(i);

            sortByField = new JComboBox();
            for (String strategy : CardDisplayPanel.strategies) {
                sortByField.addItem(strategy);
            }
            sortByField.addActionListener(e -> filterCards());

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

        private void filterCards(){
            List<Card> cards = CollectionPanel.getInstance().data.getCards();

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
