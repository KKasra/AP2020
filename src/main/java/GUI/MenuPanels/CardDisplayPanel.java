package GUI.MenuPanels;

import DB.Managment.Market;
import DB.components.cards.Card;
import GUI.CardShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class CardDisplayPanel extends JScrollPane {
    private static final Color background = new Color(26, 188, 156);
    private static final int columns = 4;

    private static final int SORT_BY_TYPE = 0;
    private static final int SORT_BY_COIN_COST = 1;
    private static final int SORT_BY_NAME = 2;
    private static final int SORT_BY_MANA_COST = 3;
    private static final int SORT_BY_CLASS = 4;
    public static final String[] strategies = new String[]{"by type", "by coin cost", "by name", "by mana", "by class"};
   
    private JPanel cardDisplay;
    private MouseAdapter pickACard;
    private List<Card> cards = new ArrayList<>();

    public CardDisplayPanel(int width, int height, MouseAdapter pickACard){
        //scrollPane settings
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        getVerticalScrollBar().setUnitIncrement(20);
        setPreferredSize(new Dimension(width, height));
        cardDisplay = new JPanel();
        setViewportView(cardDisplay);

        //cardDisplay settings
        cardDisplay.setLayout(new GridBagLayout());
        cardDisplay.setBackground(background);
        cardDisplay.setPreferredSize(new Dimension(1000,
                40 / columns * (CardShape.HEIGHT + 200)));

        this.pickACard = pickACard;

    }

    public void initCardPanel(int sortType){
        cardDisplay.removeAll();


        GridBagConstraints cardDisplayGBC = new GridBagConstraints();
        cardDisplayGBC.insets = new Insets(20, 20, 20, 20);
        cardDisplayGBC.anchor = GridBagConstraints.FIRST_LINE_START;
        int cardIndex = 0;

        //sorting
        switch (sortType) {
            case SORT_BY_CLASS:
                Collections.sort(cards, (o1, o2) -> o1.getHero().toString().compareTo(o2.getHero().getHeroName()));
                break;
            case SORT_BY_COIN_COST:
                Collections.sort(cards, (o1, o2) -> {
                    Integer a = new Integer(o1.getCoinCost());
                    Integer b = new Integer(o2.getCoinCost());
                    return a.compareTo(b);
                });
                break;
            case SORT_BY_MANA_COST:
                Collections.sort(cards, (o1, o2) -> {
                    Integer a = new Integer(o1.getManaCost());
                    Integer b = new Integer(o2.getManaCost());
                    return a.compareTo(b);
                });
                break;
            case SORT_BY_NAME:
                Collections.sort(cards, (o1, o2) -> {
                    String a = o1.getName();
                    String b = o2.getName();
                    return a.compareTo(b);
                });
                break;
            case SORT_BY_TYPE:
                Collections.sort(cards, (o1, o2) -> {
                    String a = o1.getClass().getSimpleName();
                    String b = o2.getClass().getSimpleName();
                    return a.compareTo(b);
                });
        }

        for (Card card : cards) {
            CardShape cardShape = new CardShape(card);

            cardDisplayGBC.gridx = cardIndex % columns;
            cardDisplayGBC.gridy = cardIndex / columns;
            ++cardIndex;
            cardDisplay.add(cardShape, cardDisplayGBC);

            cardShape.addMouseListener(pickACard);
        }
    }

    public List<Card> getCards() {
        return cards;
    }

    public void setCards(List<Card> cards) {
        this.cards = cards;
    }

}