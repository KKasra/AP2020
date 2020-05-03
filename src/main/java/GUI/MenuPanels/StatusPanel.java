package GUI.MenuPanels;

import DB.components.User;
import DB.components.cards.Card;
import DB.components.cards.Deck;
import DB.components.cards.DeckInfo;


import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class StatusPanel extends MenuPanel{

    @Override
    public void notify(MenuPanel nextPanel) {
        super.notify(nextPanel);
        update();
    }

    private static class DeckTable extends JTable{
        private static String[] args = {"Name", "winning  Rate", "Wins", "Games Played",
                "average Mana cost", "Hero", "most played card"};
        public static Object[][] sortedTable(List<Deck> decks) {
            Object[][] res = new Object[decks.size()][args.length];
            for (int i = 0; i < decks.size(); i++) {
                res[i] = deckToTableItem(decks.get(i));
            }
            List<Object[]> res1 = Arrays.asList(res);
            Collections.sort(res1, new Comparator<Object[]>() {
                @Override
                public int compare(Object[] o1, Object[] o2) {
                    for (int i = 2; i <= 5; i++) {
                        int c = ((Comparable)o1[i]).compareTo(o2[i]);
                        if (c != 0)
                            return c;
                    }
                    return 0;
                }
            });
            for (int i = 0; i < res1.size(); i++) {
                res[i] = res1.get(i);
            }
            return res;
        }
        public static Object[] deckToTableItem(Deck deck) {
            DeckInfo info = deck.getInfo();


            ArrayList res = new ArrayList();
            res.add(deck.getName());
            if (info.getNumberOfPlayedGames() == 0) {
                res.add("0.0");
                res.add("0");
            }
            else {
                res.add(((double)info.getNumberOfWins()) / info.getNumberOfPlayedGames());
                res.add(info.getNumberOfWins());

            }
            res.add(info.getNumberOfPlayedGames());
            if (deck.getCards().size() == 0)
                res.add(0.0);
            else {
                double sum = 0;
                for (Card card : deck.getCards()) {
                    sum += card.getManaCost();
                }
                res.add(sum / deck.getCards().size());
            }
            res.add(deck.getHero().getHeroName());

            Card maxUsed = null;
            for (Card card : deck.getCards()) {
                if (maxUsed == null || info.getCardPlayedCount(card) > info.getCardPlayedCount(maxUsed))
                    maxUsed = card;
            }
            res.add(maxUsed == null ? "null" : maxUsed.getName());
            return res.toArray();
        }

        public DeckTable(Object[][] rowData, Object[] columnNames) {
            super(rowData, columnNames);
            setBackground(new Color(175, 122, 197, 150));
            setForeground(new Color(255, 111, 0));
            setFont(new Font("arial", Font.PLAIN, 24));

            setRowHeight(30);

        }

    }

    private DeckTable table;
    private JScrollPane tableScroller;

    private void update() {
        table = new DeckTable(DeckTable.sortedTable(User.user.getDecks()), DeckTable.args);
        tableScroller.setViewportView(table);
    }


    private StatusPanel() {
        tableScroller = new JScrollPane();
        tableScroller.setPreferredSize(new Dimension(1500, 500));
        tableScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(tableScroller);
        update();
    }
    private static StatusPanel instance;
    public static StatusPanel getInstance() {
        if(instance == null)
            instance = new StatusPanel();
        return instance;
    }
}
