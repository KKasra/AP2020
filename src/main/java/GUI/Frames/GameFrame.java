package GUI.Frames;

import DB.components.cards.*;
import DB.components.heroes.Hero;
import GUI.CardShape;
import GUI.ImageUtil;
import GUI.MenuButton;
import GUI.Sound;
import Game.Gamestructure.Game;


import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class GameFrame extends JFrame {

    private Game game;

    private static final int periodTimeMS = 30;
    private static Sound theme = new Sound("Antiphon");





    private List<Updatable> registeredUpdatables = new ArrayList<Updatable>();
    private void registerUpdatable(Updatable object) {
        registeredUpdatables.add(object);
    }
    private void unregisterUpdatable(Updatable object) {
        registeredUpdatables.remove(object);
    }

    public synchronized void updateComponents() {
        for (Updatable registeredUpdatable : registeredUpdatables) {
            registeredUpdatable.update();
        }

    }

    public static abstract class GameMouseAdapter extends MouseAdapter{
        @Override
        public void mouseClicked(MouseEvent e) {
            super.mouseClicked(e);
            GameFrame.getInstance().updateComponents();
        }
    }
    public interface Updatable {
        void update();
    }




    private static class GameFramePanel extends JPanel{
        protected Game game;
        public GameFramePanel(){
            setOpaque(false);
            this.game = GameFrame.getInstance().game;
        }
    }

    private static class HandPanel extends JLayeredPane implements Updatable{
        private int width = 800;
        private int height = 140;
        private int cardsShift = width / 13;

        private List<Card> cards;

        @Override
        public void update() {
            removeAll();
            for (Card card1 : cards) {
                alignCard(new CardInHand(card1));
            }
        }

        private static class CardInHand extends JPanel{

            protected int width = 90;
            protected int height = (int)Math.round(1.6 * width);
            protected int lengthOfLabels = 20;


            private Card card;
            private JLabel background;
            private JLabel picture;
            private JLabel attack;
            private JLabel health;
            private JLabel manaCost;


            public CardInHand(Card card) {


                CardInfoPanel.getInstance().register(this, card);
                this.addMouseListener(new GameMouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            GameFrame.getInstance().game.getPlayer().drawCard(card);
                        } catch (Exception exception) {
                            WarningFrame.print(exception.getMessage());
                        }
                        super.mouseClicked(e);
                    }
                });

                this.card = card;

               setPreferredSize(new Dimension(width, height));

                initComponent();
                alignComponents();
            }

            private void initComponent() {
                String cardType = card.getClass().getSimpleName()
                        .substring(0, card.getClass().getSimpleName().length() - 4)
                        .toLowerCase();
                background = new JLabel(new ImageIcon(ImageUtil
                        .getWhiteTransparent("gameComponents/cards/cardInHand.png")
                        .getScaledInstance(width, height, Image.SCALE_SMOOTH)));

                picture = new JLabel(new ImageIcon(ImageUtil
                        .getWhiteTransparent("gameComponents/cards/" + cardType + ".png")
                        .getScaledInstance(width / 2, height / 2, Image.SCALE_SMOOTH)));

                attack = new JLabel();
                attack.setForeground(new Color(255, 50, 0));
                attack.setHorizontalAlignment(JLabel.CENTER);
                attack.setOpaque(false);
                health = new JLabel();
                health.setHorizontalAlignment(JLabel.CENTER);
                health.setForeground(new Color(0, 255, 200));
                health.setOpaque(false);
                if (card instanceof MinionCard) {
                    attack.setText(((MinionCard) card).getAttack() + "");
                    health.setText(((MinionCard) card).getHealth() + "");
                }

                if (card instanceof WeaponCard) {
                    attack.setText(((WeaponCard) card).getAttack() + "");
                    health.setText(((WeaponCard) card).getHealth() + "");
                }
                manaCost = new JLabel(card.getManaCost() + "");
                manaCost.setForeground(new Color(40, 53, 147));
            }

            private void alignComponents() {
                setLayout(null);
                JPanel panel = new JPanel();
                panel.setLayout(new GridBagLayout());

                panel.setBounds(0, 0, width, height);
                panel.setOpaque(false);
                add(panel);
                background.setBounds(0,0, width, height);
                add(background);

                GridBagConstraints gbc = new GridBagConstraints();

                gbc.gridx = 0;
                gbc.gridy = 0;
                panel.add(attack, gbc);

                gbc.gridx += 2;
                panel.add(manaCost, gbc);
                --gbc.gridx;
                ++gbc.gridy;
                panel.add(picture, gbc);

                ++gbc.gridx;
                ++gbc.gridy;
                panel.add(health, gbc);
            }

            @Override
            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            @Override
            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }
        }



        private void alignCard(CardInHand card) {
            card.setBounds( getComponentCount() * cardsShift, 0, card.getWidth(), card.getHeight());
            add(card, getComponentCount());
        }

        public HandPanel(List<Card> cards) {
            setPreferredSize(new Dimension(width, height));
            this.cards = cards;
            GameFrame.getInstance().registerUpdatable(this);
        }

    }
    private static class BoardPanel extends GameFramePanel implements Updatable{
        private int width = 1000;
        private int height = 160;
        private int cardsShift = width / 8;

        private List<Card> cards;

        @Override
        public void update() {
            removeAll();
            for (Card card1 : cards) {
                alignCard(new CardOnBoard(card1));
            }
        }

        private static class CardOnBoard extends JPanel{
            protected int width = 100;
            protected int height = (int)Math.round(1.6 * width);
            protected int lengthOfLabels = 30;


            private Card card;
            private JLabel background;
            private JLabel picture;
            private JLabel attack;
            private JLabel health;

            public CardOnBoard(Card card) {
                addMouseListener(new GameMouseAdapter(){});
                CardInfoPanel.getInstance().register(this, card);
                this.card = card;

                setPreferredSize(new Dimension(width, height));

                initComponent();
                alignComponents();
            }



            private void initComponent() {
                String cardType = card.getClass().getSimpleName().substring(0, card.getClass().getSimpleName().length() - 4);
                background = new JLabel(new ImageIcon(ImageUtil
                        .getWhiteTransparent("gameComponents/cards/cardOnBoard.png")
                        .getScaledInstance(width, height, Image.SCALE_SMOOTH)));

                picture = new JLabel(new ImageIcon(ImageUtil
                        .getWhiteTransparent("gameComponents/cards/" + cardType + ".png")
                        .getScaledInstance(width / 2, height / 2, Image.SCALE_SMOOTH)));

                attack = new JLabel();
                attack.setForeground(new Color(255, 50, 0));
                health = new JLabel();
                health.setForeground(new Color(0, 255, 200));
                attack.setText(((MinionCard) card).getAttack() + "");
                health.setText(((MinionCard) card).getHealth() + "");

            }

            private void alignComponents() {
                setLayout(null);
                JPanel panel = new JPanel();
                panel.setLayout(new GridBagLayout());
                background.setBounds(0, 0, width, height);
                panel.setBounds(0, 0, width, height);
                panel.setOpaque(false);
                add(panel);
                add(background);

                GridBagConstraints gbc = new GridBagConstraints();

                gbc.gridx = 0;
                gbc.gridy = 0;
                panel.add(attack, gbc);

                ++gbc.gridx;
                ++gbc.gridy;
                panel.add(picture, gbc);

                ++gbc.gridx;
                ++gbc.gridy;
                panel.add(health, gbc);
            }
        }



        private void alignCard(CardOnBoard card) {
            card.setBounds(0, getComponentCount() * cardsShift, card.getWidth(), card.getHeight());
            add(card);
        }

        public BoardPanel(List<Card> cards) {
            setPreferredSize(new Dimension(width, height));
            this.cards = cards;
            GameFrame.getInstance().registerUpdatable(this);
        }
    }
    private static class HeroPanel extends GameFramePanel implements Updatable{

        private Hero hero;
        private JLabel heroImage;
        private JLabel heroPowerImage;
        private JLabel HP;
        @Override
        public void update() {
            HP.setText(hero.getHp() + "");
        }

        private static class WeaponImage extends JPanel{
            private int width;
            private int height;
            private JLabel attack;
            private JLabel durability;
            private JLabel image;

            public WeaponImage(int width, int height) {
                this.width = width;
                this.height = height;
                //settings
                setOpaque(false);
                setLayout(null);
//                hideWeapon();
                addMouseListener(new GameMouseAdapter() {});

                initComponents();
                alignComponents();

            }

            public void initComponents() {
                attack = new JLabel();
                attack.setForeground(new Color(255, 0, 0));

                durability = new JLabel();
                durability.setForeground(new Color(200, 200, 0));

                image = new JLabel(new ImageIcon(ImageUtil
                        .getWhiteTransparent("gameComponents/weapon.png")
                        .getScaledInstance(width, height, Image.SCALE_SMOOTH)));


            }

            public void alignComponents() {
                setPreferredSize(new Dimension(width, height));

                int l = height/ 10;

                attack.setBounds(0, height - l, l, l);
                add(attack);

                durability.setBounds(width - l, height - l, l, l);
                add(durability);

                image.setBounds(0, 0, width, height);
                add(image);


            }

            public void addWeapon(WeaponCard weaponCard) {
                this.attack.setText(weaponCard.getAttack() + "");
                this.durability.setText(weaponCard.getHealth() + "");
                setVisible(true);
            }

            public void hideWeapon() {
                setVisible(false);
            }
        }

        private WeaponImage weaponImage;


        public HeroPanel(Hero hero) {
            this.hero = hero;

            setLayout(new GridBagLayout());

            setPreferredSize(new Dimension(240, 120));

           weaponImage = new WeaponImage(70, 120);

           JPanel panel = new JPanel(null);
           panel.setPreferredSize(new Dimension(70, 7 * 16));
           heroImage = new JLabel(new ImageIcon(ImageUtil
                   .getWhiteTransparent("gameComponents/heroes/" + hero.getHeroName() + ".jpg")
                    .getScaledInstance(70, 7 * 16, Image.SCALE_SMOOTH)));
           heroImage.setBounds(0, 0, 70, 7 * 16);
           HP = new JLabel();
           HP.setForeground(new Color(40, 180, 99));
           HP.setHorizontalAlignment(0);
           HP.setBounds(0, 0, 20,20);
           panel.add(HP);
           panel.add(heroImage);


           heroPowerImage = new JLabel(new ImageIcon(ImageUtil
                   .getWhiteTransparent("gameComponents/power.png")
                   .getScaledInstance(100, 100, Image.SCALE_SMOOTH)));




            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.SOUTH;
             add(weaponImage, gbc);
             ++gbc.gridx;
             add(panel, gbc);
             ++gbc.gridx;
             add(heroPowerImage, gbc);

            update();

        }


    }
    private static class EventPanel extends GameFramePanel{
        private int width = 150;
        private int height = 15 * 16;
        JTextArea logs;

        private EventPanel(){
            //settings

            logs = new JTextArea() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    logs.setText("");
                    for (String log : game.getLogs()) {
                        logs.append("\n" + log);
                    }

                }
            };
            logs.setBorder(new EtchedBorder(EtchedBorder.RAISED));
            logs.setForeground(Color.WHITE);
            logs.setOpaque(false);

            logs.setPreferredSize(new Dimension(width,  height));
            add(logs);

        }


        private static EventPanel instance;
        private static EventPanel getInstance() {
            if (instance == null)
                instance = new EventPanel();
            return instance;
        }
    }
    private static class DeckPanel extends GameFramePanel{

        public static final int WIDTH = 150;
        public static final int HEIGHT = 240;

        List<Card> deck;
        private JLabel image;
        private JLabel count;


        public DeckPanel(List<Card> deck) {
            this.deck = deck;
            //settings
            setLayout(null);
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            setBorder(new EtchedBorder(EtchedBorder.RAISED));

            image = new JLabel(new ImageIcon(ImageUtil
                    .getWhiteTransparent("gameComponents/deck.png")
                    .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH)
            ));



            count = new JLabel();
            count.setFont(new Font("Arial", Font.BOLD, 30));
            count.setForeground(new Color(255, 160, 0));
            count.setBounds(WIDTH /2, HEIGHT / 2, 30, 30);
            add(count);

            image.setBounds(0, 0, WIDTH, HEIGHT);
            add(image);





            addMouseListener(new GameMouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    count.setText(deck.size() + "");
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    count.setText("");
                }
            });
        }
    }
    private static class CardInfoPanel extends GameFramePanel{
        public void register(JComponent component, Card card) {
            component.addMouseListener(new GameMouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    CardInfoPanel.getInstance().showInfoOf(card);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    CardInfoPanel.getInstance().hideInfo();
                }
            });
        }

        public void showInfoOf(Card card){
            add(new CardShape(card));
        }
        public void hideInfo() {
            removeAll();
        }
        private CardInfoPanel() {
            setPreferredSize(new Dimension(CardShape.WIDTH, CardShape.HEIGHT));
        }

        private static CardInfoPanel instance;
        public static CardInfoPanel getInstance(){
            if (instance == null)
                instance = new CardInfoPanel();
            return instance;
        }
    }
    private static class ManaLabel extends JPanel implements Updatable{
        private int width = 30;
        private int height = 30;
        private Game.Player player;
        private JLabel image;
        private JLabel number;

        public ManaLabel(Game.Player player) {
            GameFrame.getInstance().registerUpdatable(this);
            this.player = player;

            setOpaque(false);
//            setPreferredSize(new Dimension(width, height));

            image = new JLabel(new ImageIcon(ImageUtil
            .getWhiteTransparent("gameComponents/cards/crystal.png")
            .getScaledInstance(width, height, Image.SCALE_SMOOTH)));
            number = new JLabel();
            number.setHorizontalAlignment(JLabel.CENTER);
            number.setForeground(new Color(244, 81, 30));
            number.setFont(new Font("Spicy rice",Font.PLAIN,20));
            image.setBounds(0, 0, width,height);
            number.setBounds(0, 0, width, height);

            add(number);
            add(image);
            update();

        }



        @Override
        public void update() {
            number.setText(player.getMana() + "");
        }
    }


    private MenuButton endTurnButton;
    private HandPanel playersHandPanel;
    private HandPanel opponentsHandPanel;
    private BoardPanel playersBoardPanel;
    private BoardPanel opponentsBoardPanel;
    private HeroPanel playersHeroPanel;
    private HeroPanel opponentsHeroPanel;
    private DeckPanel playersDeckPanel;
    private DeckPanel opponentsDeckPanel;
    private ManaLabel playersManaLabel;

    public static void launch(Game game) {
        getInstance().game = game;
        getInstance().setVisible(true);

        theme.clip.loop(Clip.LOOP_CONTINUOUSLY);

        getInstance().initComponent();
        getInstance().alignComponents();

        game.getPlayer().newTurn();
        getInstance().updateComponents();
        List<Container> passives = new ArrayList<>();
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < 3; ++i) {
            int indx = new Random().nextInt(5);
            if (indices.contains(indx))
                --i;
            else{
                indices.add(indx);
                passives.add(new Passive.PassiveShape(Passive.map.get(indx)));
            }
        }
        FramePainter.paint(getInstance());
        SummonFrame.getInstance().chooseFrom(passives, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Passive.PassiveShape res = (Passive.PassiveShape)e.getSource();
                game.getPlayer().setPassive(res.getPassive());
            }
        });



    }

    private GameFrame() {
        //settings
        setSize(new Dimension(MenuFrame.width, MenuFrame.height));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                FramePainter.paint(MenuFrame.getInstance());
                theme.clip.stop();
                MenuFrame.getTheme().clip.loop(Clip.LOOP_CONTINUOUSLY);
            }
        });



    }


    private void initComponent() {

        Container pane = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(ImageUtil.getImage("wall.png"),0, 0, null);
            }
        };
        setContentPane(pane);
        pane.setLayout(new GridBagLayout());
        endTurnButton = new MenuButton("end turn",new Dimension(160,100),20);
        endTurnButton.addMouseListener(new GameMouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                game.getPlayer().newTurn();
                super.mouseClicked(e);
            }
        });

        playersBoardPanel = new BoardPanel(game.getPlayer().getCardsOnBoard());
        opponentsBoardPanel = new BoardPanel(game.getOpponent().getCardsOnBoard());
        playersDeckPanel = new DeckPanel(game.getPlayer().getDeck());
        opponentsDeckPanel = new DeckPanel(game.getOpponent().getDeck());
        playersHandPanel = new HandPanel(game.getPlayer().getHand());
        opponentsHandPanel = new HandPanel(game.getOpponent().getHand());
        playersHeroPanel = new HeroPanel(game.getPlayer().getHero());
        opponentsHeroPanel = new HeroPanel(game.getOpponent().getHero());
        playersManaLabel = new ManaLabel(game.getPlayer());


    }

    private void alignComponents() {
        Container pane = getContentPane();
        pane.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridwidth = 1;
        gbc.gridheight = 1;
//        gbc.anchor = GridBagConstraints.CENTER;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridheight = 2;
        pane.add(opponentsDeckPanel, gbc);
        gbc.gridheight = 1;

        gbc.gridx = 1;
        gbc.gridy = 0;
        pane.add(opponentsHandPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        pane.add(opponentsHeroPanel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        pane.add(endTurnButton, gbc);



        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridheight = 2;
        pane.add(EventPanel.getInstance(), gbc);
        gbc.gridheight = 1;

        gbc.gridx = 1;
        gbc.gridy = 2;
        pane.add(opponentsBoardPanel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridheight = 2;
        pane.add(CardInfoPanel.getInstance(), gbc);
        gbc.gridheight = 1;

        gbc.gridx = 1;
        gbc.gridy = 3;
        pane.add(playersBoardPanel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        pane.add(playersHeroPanel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 4;
        gbc.gridheight = 2;
        pane.add(playersDeckPanel, gbc);
        gbc.gridheight = 1;

        gbc.gridx = 0;
        gbc.gridy = 5;
        pane.add(playersManaLabel, gbc);


        gbc.gridx = 1;
        gbc.gridy = 5;
        pane.add(playersHandPanel, gbc);

    }


    private static GameFrame instance;
    public static GameFrame getInstance() {
        if (instance == null)
            instance = new GameFrame();
        return instance;
    }

}
