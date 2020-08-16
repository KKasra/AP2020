package GUI.MenuPanels;

import DB.components.User;
import DB.components.cards.Card;
import DB.components.cards.Deck;
import DB.components.cards.DeckInfo;
import Network.Client;
import Network.Responses.StatusData;


import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.*;
import java.util.List;

public class StatusPanel extends MenuPanel{

    @Override
    public void notify(MenuPanel nextPanel) {
        super.notify(nextPanel);
    }

    public void update(StatusData response) {
        updateDecks(response.getDecks());
        response.getUsers().sort((a , b) -> - Integer.compare(a.getWins() - a.getLoses(), b.getWins() - b.getLoses()));
        top10.show(response.getUsers().subList(0, Math.min(10, response.getUsers().size())));
        int index = 0;
        while(!response.getUsers().get(index).getUserName().equals(Client.getInstance().getname()))
            index++;

        Me.show(response.getUsers().subList(Math.max(0, index - 5), Math.min(response.getUsers().size(), index + 5)));
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
            Collections.sort(res1, (o1, o2) -> {
                for (int i = 2; i <= 5; i++) {
                    int c = ((Comparable)o1[i]).compareTo(o2[i]);
                    if (c != 0)
                        return c;
                }
                return 0;
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
    private static class UserListPanel extends JPanel{
        public UserListPanel(String name) {
            setBorder(new TitledBorder(name));
            setPreferredSize(new Dimension(700, 500));
            setLayout(new GridBagLayout());
        }
        public void show(List<User> users) {
            removeAll();
            users.add(0, null);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridy = 0;
            for (User user : users) {
                add(new UserItem(user), gbc);
                ++gbc.gridy;
            }
            users.remove(null);
        }

        private static class UserItem extends JPanel{
            private JLabel nameLabel;
            private JLabel winCountLabel;
            private JLabel loseCountLabel;
            UserItem(User user) {
                nameLabel = new JLabel(user == null ? "Name :" : user.getUserName());
                winCountLabel = new JLabel(user == null ? "Wins :" : user.getWins() + "");
                loseCountLabel = new JLabel(user == null ? "Loses :" : user.getLoses() + "");

                setPreferredSize(new Dimension(650, 45));
                nameLabel.setPreferredSize(new Dimension(300, 45));
                winCountLabel.setPreferredSize(new Dimension(200, 45));
                loseCountLabel.setPreferredSize(new Dimension(150, 45));

                setLayout(new GridBagLayout());
                GridBagConstraints gbc = new GridBagConstraints();

                gbc.gridx = 0;
                add(nameLabel, gbc);

                ++gbc.gridx;
                add(winCountLabel, gbc);

                ++gbc.gridx;
                add(loseCountLabel, gbc);
            }
        }
    }

    private DeckTable table;
    private JScrollPane tableScroller;
    private UserListPanel top10;
    private UserListPanel Me;

    private void updateDecks(List<Deck> decks) {
        table = new DeckTable(DeckTable.sortedTable(decks), DeckTable.args);
        tableScroller.setViewportView(table);
    }

    private StatusPanel() {
        tableScroller = new JScrollPane();
        tableScroller.setPreferredSize(new Dimension(1500, 200));
        tableScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        top10 = new UserListPanel("TOP 10");
        Me = new UserListPanel("MY RANK");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(tableScroller, gbc);

        ++gbc.gridy;
        gbc.gridwidth = 1;
        add(top10, gbc);

        ++gbc.gridx;
        add(Me, gbc);

    }
    private static StatusPanel instance;
    public static StatusPanel getInstance() {
        if(instance == null)
            instance = new StatusPanel();
        return instance;
    }
}
