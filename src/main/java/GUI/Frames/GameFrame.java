package GUI.Frames;

//import DB.components.cards.*;
import DB.components.cards.Passive;
import GUI.CardShape;
import GUI.ImageUtil;
import GUI.MenuButton;
import GUI.Sound;
import Game.CommandAndResponse.*;
import Game.GameStructure.Attacker;
import Game.GameStructure.CardModels.CardModel;
import Game.GameStructure.CardModels.Loader;
import Game.GameStructure.CardModels.MinionModel;
import Game.GameStructure.Cards.Card;
import Game.GameStructure.CardModels.HeroPower;
import Game.GameStructure.Cards.MinionCard;
import Game.GameStructure.Cards.WeaponCard;
import Game.GameStructure.Game;
import Game.GameStructure.Heroes.Hero;
import Game.GameStructure.Player;


import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.List;

public class GameFrame extends JFrame {

    private GameProcessor gameProcessor;
    private Game game;

    private static Sound theme = new Sound("Antiphon");
    private static Sound wrongObject = new Sound("wrong");




    private List<Updatable> registeredUpdatables = new ArrayList<Updatable>();
    private void registerUpdatable(Updatable object) {
        new Thread(){
            @Override
            public void run() {
                synchronized (GameFrame.getInstance()) {
                    super.run();
                    registeredUpdatables.add(object);
                }
            }
        }.start();
    }
    private void unregisterUpdatable(Updatable object) {
        new Thread(){
            @Override
            public void run() {
                synchronized (GameFrame.getInstance()) {
                    super.run();
                    registeredUpdatables.remove(object);
                }
            }
        }.start();
    }
    public synchronized void updateComponents() {
        for (Updatable a : registeredUpdatables) {
            a.update();
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
    private static class CommandAndResponseHandler {
        private Image pickedCard = null;
        private Card pickedCardObject = null;
        private String CardSide = null;
        private static final Image selectionPointer = ImageUtil.getWhiteTransparent("selectionPointer.png").
                getScaledInstance(50, 50, Image.SCALE_SMOOTH);

        private List<BoardPanel.Box> registeredBoxes;
        public void registerBox(BoardPanel.Box box) {
            registeredBoxes.add(box);
        }

        private boolean selectionMode = false;

        private ObjectOutput commandOutput;
        private ObjectInput responseInput;

        private void writeCommand(Command command) {
            commandOutput.write(command);
        }

        private Timer timer  = new Timer(10, a -> {
            if (CommandAndResponseHandler.getInstance().pickedCard != null) {
                GameFrame.getInstance().getContentPane().getGraphics().
                        drawImage(CommandAndResponseHandler.getInstance().pickedCard,
                                MouseInfo.getPointerInfo().getLocation().x - 50,
                                MouseInfo.getPointerInfo().getLocation().y - 80, null);
            }
            if (CommandAndResponseHandler.getInstance().selectionMode) {
                GameFrame.getInstance().getContentPane().getGraphics().
                        drawImage(selectionPointer,
                                MouseInfo.getPointerInfo().getLocation().x - 30,
                                MouseInfo.getPointerInfo().getLocation().y - 50, null);
            }
        });
        private Thread responseReader = new Thread() {
            @Override
            public void run(){
                while (true) {
                    Response response = (Response) responseInput.read();
                    switch (response.getMessage()) {
                        case reject:
                            wrongObject.clip.setMicrosecondPosition(0);
                            wrongObject.clip.start();
                            break;
                        case selectATarget:
                            selectionMode = true;
                            break;
                        case endOfGame:
                            //TODO show the winner
                            GameFrame.getInstance().exitGame();
                            break;
                        case targetIsValid:
                            selectionMode = false;
                            break;

                    }
                    GameFrame.getInstance().updateComponents();
                }
            }
        };
        public static abstract class CardMouseAdapter extends MouseAdapter{
            public abstract Card getCard();
            public abstract String cardSide();
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                if (CommandAndResponseHandler.getInstance().selectionMode)
                    return;
                CommandAndResponseHandler.getInstance().pickedCardObject = getCard();

                BufferedImage bi = new BufferedImage(100, 160, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = bi.createGraphics();
                ((JComponent)e.getSource()).paint(g);
                CommandAndResponseHandler.getInstance().pickedCard = bi;
                CommandAndResponseHandler.getInstance().CardSide = cardSide();
                ((JComponent)e.getSource()).setVisible(false);
            }



            @Override
            public synchronized void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                Card card = getCard();
                int index = -1;
                String boxSide = null;
                for (BoardPanel.Box registeredBox : CommandAndResponseHandler.getInstance().registeredBoxes)
                    if (registeredBox.contains(SwingUtilities
                            .convertPoint(((JComponent)e.getSource()), e.getX(), e.getY(), registeredBox))){
                        index = registeredBox.index;
                        boxSide = registeredBox.playerName;
                    }


                CommandAndResponseHandler.getInstance().pickedCard = null;
                CommandAndResponseHandler.getInstance().pickedCardObject = null;
                if (index != -1)
                    if (CommandAndResponseHandler.getInstance().CardSide.equals(boxSide))
                        CommandAndResponseHandler.getInstance().commandOutput.
                                write(new PlayCommand(CommandAndResponseHandler.getInstance().CardSide, card, index));

                GameFrame.getInstance().updateComponents();
            }
        }
        public static abstract class TargetMouseAdapter extends MouseAdapter{
            public abstract Object getTarget();

            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (CommandAndResponseHandler.getInstance().selectionMode)
                    CommandAndResponseHandler.getInstance().writeCommand(new SelectionCommand(getTarget()));

            }
        }
        public static abstract class AttackerMouseAdapter extends MouseAdapter{
            public abstract Attacker getAttacker();
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                if (CommandAndResponseHandler.getInstance().selectionMode)
                    return;
                CommandAndResponseHandler.getInstance().commandOutput.write(new AttackCommand(getAttacker()));
            }
        }
        public static abstract class heroPowerMouserAdapter extends MouseAdapter{
            abstract HeroPower getHeroPower();
            abstract Player getPlayer();
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);

                if (CommandAndResponseHandler.getInstance().selectionMode)
                    return;

                CommandAndResponseHandler.getInstance().writeCommand(new HeroPowerCommand(getHeroPower(), getPlayer()));
            }
        }



        private CommandAndResponseHandler() {
            registeredBoxes = new ArrayList<>();
        }
        private static CommandAndResponseHandler instance;
        public static CommandAndResponseHandler getInstance() {
            if (instance == null)
                instance = new CommandAndResponseHandler();
            return instance;
        }
        public void connectToProcessor(GameProcessor processor) {
            commandOutput = processor.getCommandOutput();
            responseInput = processor.getResponseInput();
            timer.start();
            responseReader.start();
        }


    }
    private static Image WALL =ImageUtil.getImage("wall.png");

    private static class HandPanel extends JLayeredPane implements Updatable{
        private int width = 800;
        private int height = 140;
        private int cardsShift = width / 13;

        private List<Card> cards;
        private String playerName;

        @Override
        public void update() {
            removeAll();
            for (Card card1 : cards) {
                alignCard(new CardInHand(card1, playerName));
            }
        }

        private static class CardInHand extends JPanel implements Updatable{

            protected static final int width = 90;
            protected static final int height = (int)Math.round(1.6 * width);
            protected static final int lengthOfLabels = 20;
            protected static final Image BACKGROUND = ImageUtil
                    .getWhiteTransparent("gameComponents/cards/cardInHand.png")
                    .getScaledInstance(width, height, Image.SCALE_SMOOTH);

            protected static final Map<String, Image> CardImages;
            static {
                CardImages = new HashMap<>();
                CardImages.put("minion", ImageUtil
                        .getWhiteTransparent("gameComponents/cards/" + "minion" + ".png")
                        .getScaledInstance(width / 2, height / 2, Image.SCALE_SMOOTH));
                CardImages.put("weapon", ImageUtil
                        .getWhiteTransparent("gameComponents/cards/" + "weapon" + ".png")
                        .getScaledInstance(width / 2, height / 2, Image.SCALE_SMOOTH));
                CardImages.put("spell", ImageUtil
                        .getWhiteTransparent("gameComponents/cards/" + "spell" + ".png")
                        .getScaledInstance(width / 2, height / 2, Image.SCALE_SMOOTH));

            }

            private Card card;
            private JLabel background;
            private JLabel picture;
            private JLabel attack;
            private JLabel health;
            private JLabel manaCost;


            public CardInHand(Card card, String playerName) {
                CardInfoPanel.getInstance().register(this, card);

                this.card = card;

               setPreferredSize(new Dimension(width, height));

                initComponent();
                alignComponents();

                addMouseListener(new CommandAndResponseHandler.CardMouseAdapter() {
                    @Override
                    public Card getCard() {
                        return card;
                    }

                    @Override
                    public String cardSide() {
                        return playerName;
                    }
                });
                addMouseMotionListener(new CommandAndResponseHandler.CardMouseAdapter() {
                    @Override
                    public Card getCard() {
                        return card;
                    }

                    @Override
                    public String cardSide() {
                        return playerName;
                    }
                });
            }

            private void initComponent() {
                String cardType = card.getClass().getSimpleName()
                        .substring(0, card.getClass().getSimpleName().length() - 4)
                        .toLowerCase();
                background = new JLabel(new ImageIcon(BACKGROUND));

                picture = new JLabel(new ImageIcon(CardImages.get(cardType)));

                attack = new JLabel();
                attack.setForeground(new Color(255, 50, 0));
                attack.setHorizontalAlignment(JLabel.CENTER);
                attack.setOpaque(false);
                health = new JLabel();
                health.setHorizontalAlignment(JLabel.CENTER);
                health.setForeground(new Color(0, 255, 200));
                health.setOpaque(false);
               update();
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

            @Override
            public int getHeight() {
                return height;
            }

            @Override
            public void update() {
                if (card instanceof MinionCard) {
                    attack.setText(((MinionCard) card).getAttack() + "");
                    health.setText(((MinionCard) card).getHealth() + "");
                }

                if (card instanceof WeaponCard) {
                    attack.setText(((WeaponCard) card).getAttack() + "");
                    health.setText(((WeaponCard) card).getDurability() + "");
                }
                manaCost = new JLabel(card.getManaCost() + "");
            }
        }



        private void alignCard(CardInHand card) {
            card.setBounds( getComponentCount() * cardsShift, 0, card.getWidth(), card.getHeight());
            add(card, getComponentCount());
        }

        public HandPanel(List<Card> cards, String playerName) {
            setPreferredSize(new Dimension(width, height));
            this.cards = cards;
            this.playerName = playerName;
            GameFrame.getInstance().registerUpdatable(this);
        }

    }
    private static class BoardPanel extends GameFramePanel implements Updatable{
        private int width = 1000;
        private int height = 160;
        private int cardsShift = width / 8;

        private List<Card> cards;
        private List<Box> boxes;
        private String playerName;
        @Override
        public void update() {
            for (int i = 0; i < 7; ++i) {
                if (cards.get(i) != null) {
                    CardOnBoard card = new CardOnBoard(cards.get(i));
                    boxes.get(i).setCard(card);
                    GameFrame.getInstance().registerUpdatable(card);
                }
                else {
                    GameFrame.getInstance().unregisterUpdatable(boxes.get(i).getCard());
                    boxes.get(i).clear();
                }
            }
        }



        public BoardPanel(List<Card> cards, String playerName) {
            setPreferredSize(new Dimension(width, height));
            this.cards = cards;
            this.playerName = playerName;
            GameFrame.getInstance().registerUpdatable(this);

            boxes = new ArrayList();
            for (int i = 0; i < 7; ++i) {
                Box box = new Box(i, playerName);
                box.setLocation(0, i * cardsShift);
                boxes.add(box);
                add(box);
            }
        }
        public static class Box extends JPanel {
            private static final Image BOX = ImageUtil.getWhiteTransparent("box.png").
                    getScaledInstance(100, 160, Image.SCALE_SMOOTH);
            public JLabel background;
            private int index;
            private String playerName;
            private CardOnBoard card;

            public void setCard(CardOnBoard card) {
                this.card = card;
                removeAll();
                add(card);
            }

            public void clear() {
                removeAll();
                add(background);
            }

            public CardOnBoard getCard() {
                return card;
            }

            public Box(int index, String playerName) {
                this.index = index;
                this.playerName = playerName;
                setPreferredSize(new Dimension(100, 160));
                setOpaque(false);
                background = new JLabel(new ImageIcon(BOX));
                background.setBounds(0, 0, 100, 160);

                CommandAndResponseHandler.getInstance().registerBox(this);
            }
        }
    }
    private static class HeroPanel extends GameFramePanel implements Updatable{

        private Hero hero;
        private JLabel heroImage;
        private JLabel heroPowerImage;
        private JLabel HP;
        private WeaponImage weaponImage;

        @Override
        public void update() {
            HP.setText(hero.getHp() + "");
            if (hero.getWeapon() != null)
                weaponImage.addMouseListener(new CommandAndResponseHandler.AttackerMouseAdapter() {
                    @Override
                    public Attacker getAttacker() {
                        return hero.getWeapon();
                    }
                });
            else {
                for (MouseListener a : Arrays.asList(weaponImage.getMouseListeners())) {
                    weaponImage.removeMouseListener(a);
                }
            }

            if (hero.getPower() instanceof Attacker)
                weaponImage.addMouseListener(new CommandAndResponseHandler.AttackerMouseAdapter() {
                    @Override
                    public Attacker getAttacker() {
                        return (Attacker)hero.getPower();
                    }
                });
            else {
                for (MouseListener a : Arrays.asList(heroPowerImage.getMouseListeners())) {
                    heroPowerImage.removeMouseListener(a);
                }
            }

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
                this.durability.setText(weaponCard.getDurability() + "");
                setVisible(true);
            }
            public void hideWeapon() {
                setVisible(false);
            }

        }


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

           heroPowerImage.addMouseListener(new CommandAndResponseHandler.heroPowerMouserAdapter() {

               @Override
               HeroPower getHeroPower() {
                   return hero.getPower();
               }

               @Override
               Player getPlayer() {
                   return hero.getPlayer();
               }
           });


            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.SOUTH;
             add(weaponImage, gbc);
             ++gbc.gridx;
             add(panel, gbc);
             ++gbc.gridx;
             add(heroPowerImage, gbc);


            addMouseListener(new CommandAndResponseHandler.TargetMouseAdapter() {
                @Override
                public Object getTarget() {
                    return hero;
                }
            });
            GameFrame.getInstance().registerUpdatable(this);
        }


    }
    private static class EventPanel extends GameFramePanel implements Updatable{
        private int width = 150;
        private int height = 15 * 16;
        JTextArea logs;

        private EventPanel(){
            //settings

            logs = new JTextArea();
            logs.setBorder(new EtchedBorder(EtchedBorder.RAISED));
            logs.setForeground(Color.WHITE);
            logs.setOpaque(false);

            logs.setPreferredSize(new Dimension(width,  height));
            add(logs);
            GameFrame.getInstance().registerUpdatable(this);

        }


        private static EventPanel instance;
        private static EventPanel getInstance() {
            if (instance == null)
                instance = new EventPanel();
            return instance;
        }

        @Override
        public void update() {
            logs.setText("");
            for (String log : game.getLogs()) {
                logs.append("\n" + log);
            }
        }
    }
    private static class DeckPanel extends GameFramePanel{

        public static final int WIDTH = 150;
        public static final int HEIGHT = 240;
        public static final Image PICTURE = ImageUtil
                .getWhiteTransparent("gameComponents/deck.png")
                .getScaledInstance(WIDTH, HEIGHT, Image.SCALE_SMOOTH);

        List<Card> deck;
        private JLabel image;
        private JLabel count;


        public DeckPanel(List<Card> deck) {
            this.deck = deck;
            //settings
            setLayout(null);
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            setBorder(new EtchedBorder(EtchedBorder.RAISED));

            image = new JLabel(new ImageIcon(PICTURE));



            count = new JLabel();
            count.setFont(new Font("Arial", Font.BOLD, 25));
            count.setForeground(new Color(255, 160, 0));
            count.setBounds(WIDTH /2, HEIGHT / 2, 30, 30);
            add(count);

            image.setBounds(0, 0, WIDTH, HEIGHT);
            add(image);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    count.setText(deck.size() + "");
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    count.setText("");
                }
            });

        }

    }
    private static class CardInfoPanel extends GameFramePanel{
        public void register(JComponent component, Card card) {
            component.addMouseListener(new MouseAdapter() {
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
        private Player player;
        private JLabel image;
        private JLabel number;

        public ManaLabel(Player player) {
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
    private static class CardOnBoard extends JPanel implements Updatable{
        protected static final int width = 100;
        protected static final int height = (int)Math.round(1.6 * width);
        protected static final Image BACKGROUND = ImageUtil
                .getWhiteTransparent("gameComponents/cards/cardOnBoard.png")
                .getScaledInstance(width, height, Image.SCALE_SMOOTH);
        protected static final Map<String, Image> CardImages;
        static {
            CardImages = new HashMap<>();
            CardImages.put("minion", ImageUtil
                    .getWhiteTransparent("gameComponents/cards/" + "minion" + ".png")
                    .getScaledInstance(width / 2, height / 2, Image.SCALE_SMOOTH));
            CardImages.put("weapon", ImageUtil
                    .getWhiteTransparent("gameComponents/cards/" + "weapon" + ".png")
                    .getScaledInstance(width / 2, height / 2, Image.SCALE_SMOOTH));
            CardImages.put("spell", ImageUtil
                    .getWhiteTransparent("gameComponents/cards/" + "spell" + ".png")
                    .getScaledInstance(width / 2, height / 2, Image.SCALE_SMOOTH));

        }

        protected int lengthOfLabels = 30;


        private Card card;
        private JLabel background;
        private JLabel picture;
        private JLabel attack;
        private JLabel health;
        private JLabel divineShield;
        private JLabel tauntBackground;

        public CardOnBoard(Card card) {

            CardInfoPanel.getInstance().register(this, card);
            this.card = card;

            setPreferredSize(new Dimension(width, height));

            initComponent();
            alignComponents();

        }



        private void initComponent() {
            String cardType = card.getClass().getSimpleName().substring(0, card.getClass().getSimpleName().length() - 4);
            cardType = cardType.toLowerCase();
            background = new JLabel(new ImageIcon(BACKGROUND));


            picture = new JLabel(new ImageIcon(CardImages.get(cardType)));

            tauntBackground = new JLabel(new ImageIcon(ImageUtil
                    .getWhiteTransparent("gameComponents/cards/" + "taunt" + ".png")
                    .getScaledInstance(width , height , Image.SCALE_SMOOTH)));
            divineShield = new JLabel(new ImageIcon(ImageUtil
                    .getWhiteTransparent("gameComponents/cards/" + "divineShield" + ".png")
                    .getScaledInstance(width , height / 2, Image.SCALE_SMOOTH)));

            attack = new JLabel();
            attack.setForeground(new Color(255, 50, 0));
            health = new JLabel();
            health.setForeground(new Color(0, 255, 200));
            update();
            addMouseListener(new CommandAndResponseHandler.TargetMouseAdapter() {
                @Override
                public Object getTarget() {
                    return card;
                }
            });
            addMouseListener(new CommandAndResponseHandler.AttackerMouseAdapter() {
                @Override
                public Attacker getAttacker() {
                    return (MinionCard)card;
                }
            });

        }

        private void alignComponents() {
            setLayout(null);
            JPanel panel = new JPanel();
            panel.setLayout(new GridBagLayout());
            background.setBounds(0, 0, width, height);
            tauntBackground.setBounds(0, 0, width, height);
            divineShield.setBounds(0, 0, width, height / 2);

            panel.setBounds(0, 0, width, height);
            panel.setOpaque(false);


            if (card.getCardModel() instanceof MinionModel)
                if (((MinionModel) card.getCardModel()).isDivineShield())
                    add(divineShield);
            add(panel);
            if (card.getCardModel() instanceof MinionModel) {
                if (((MinionModel) card.getCardModel()).isTaunt())
                    add(tauntBackground);
                else
                    add(background);
            }
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

        @Override
        public void update() {
            if (card instanceof MinionCard)
                attack.setText(((MinionCard) card).getAttack() + "");
            if (card instanceof MinionCard)
                health.setText(((MinionCard) card).getHealth() + "");
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

    public static void launch(GameProcessor gameProcessor, int indexOfPlayer) {
        getInstance().gameProcessor = gameProcessor;


        getInstance().game = gameProcessor.getGame();


        getInstance().setVisible(true);

        theme.clip.loop(Clip.LOOP_CONTINUOUSLY);

        CommandAndResponseHandler.getInstance().registeredBoxes = new ArrayList<>();
        getInstance().initComponent(indexOfPlayer);
        getInstance().alignComponents();
        CommandAndResponseHandler.getInstance().connectToProcessor(gameProcessor);
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
        DiscoverFrame.getInstance().chooseFrom(passives, e -> {
            Passive.PassiveShape res = (Passive.PassiveShape)e.getSource();
            getInstance().game.getPlayers().get(indexOfPlayer).setPassive(res.getPassive());

            try {

                Player player = gameProcessor.getGame().getPlayers().get(0);
                DB.components.cards.Card myPassive = new DB.components.cards.Card();
                myPassive.setName(res.getPassive().name());
                Card myPassiveCard = new Card(myPassive, player){};
                Constructor passiveConstructor = Loader.getInstance()
                        .getModel(myPassiveCard);

                CardModel card = (CardModel)passiveConstructor
                        .newInstance(gameProcessor, player, myPassiveCard);

                card.play(new PlayCommand(player.getName(), null, 0));


            } catch (Exception ignore) {
                ignore.printStackTrace();
            }

        });


    }

    private void exitGame() {
        FramePainter.paint(MenuFrame.getInstance());
        theme.clip.stop();
        MenuFrame.getTheme().clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    private GameFrame() {
        //settings
        setSize(new Dimension(MenuFrame.width, MenuFrame.height));

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                exitGame();
            }
        });



    }


    private void initComponent(int indexOfPlayer) {

        Container pane = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(WALL,0, 0, null);
            }
        };
        setContentPane(pane);
        pane.setLayout(new GridBagLayout());
        endTurnButton = new MenuButton("end turn",new Dimension(160,100),20);
        endTurnButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                getInstance().gameProcessor.getCommandOutput().write(new EndTurnCommand(this));
            }
        });

        registeredUpdatables.clear();

        playersBoardPanel = new BoardPanel(game.getPlayers().get(indexOfPlayer).getCardsOnBoard(),  game.getPlayers().get(indexOfPlayer).getName());
        opponentsBoardPanel = new BoardPanel(game.getPlayers().get(1 - indexOfPlayer).getCardsOnBoard(), game.getPlayers().get(1 - indexOfPlayer).getName());
        playersDeckPanel = new DeckPanel(game.getPlayers().get(indexOfPlayer).getDeck());
        opponentsDeckPanel = new DeckPanel(game.getPlayers().get(1 - indexOfPlayer).getDeck());
        playersHandPanel = new HandPanel(game.getPlayers().get(indexOfPlayer).getHand(), game.getPlayers().get(indexOfPlayer).getName());
        opponentsHandPanel = new HandPanel(game.getPlayers().get(1 - indexOfPlayer).getHand(), game.getPlayers().get(1 - indexOfPlayer).getName());
        playersHeroPanel = new HeroPanel(game.getPlayers().get(indexOfPlayer).getHero());
        opponentsHeroPanel = new HeroPanel(game.getPlayers().get(1 - indexOfPlayer).getHero());
        playersManaLabel = new ManaLabel(game.getPlayers().get(indexOfPlayer));


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
