package GUI;



import DB.components.cards.Rarity;
import Game.GameStructure.Cards.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseListener;
import java.util.Map;
import java.util.TreeMap;

public class CardShape extends JPanel {



    public static final int WIDTH = 200;
    public static final int HEIGHT = 320;
    public static final Color LABEL_COLOR = new Color(211, 47, 47);
    public static final Font LABEL_FONT = new Font("Spicy Rice",Font.PLAIN,20);
    public final Color color = new Color(52, 73, 94, 255);
    public static final String imagesPath = "./Data/images/gameComponents/cards/";


    private static JLabel BASE_LABEL(){
        return getImageLabel("paper.jpg",0,0,1, 1);
    }
    private static JLabel MINION_LABEL() {
        return getImageLabel("minion.png",(float)1 / 40,(float)7 / 64,
                (float)20 / 40,(float)24 / 64);
    }
    private static JLabel SPELL_LABEL(){
        return getImageLabel("spell.png",(float)1 / 40,(float)7 / 64,
                (float)20 / 40,(float)24 / 64);
    }
    private static JLabel WEAPON_LABEL(){
        return getImageLabel("weapon.png",(float)1 / 40,(float)7 / 64,
                (float)20 / 40,(float)24 / 64);
    }
    private static JLabel ATTACK_LABEL() {
        return getImageLabel("attack.png",(float)1 / 40, (float)56 / 64,
                (float)7 / 40,(float)7 / 64);
    }
    private static JLabel HEALTH_LABEL(){
        return getImageLabel("health.png",(float)30 / 40, (float)56 / 64,
                (float)7 / 40, (float)7 /  64);
    }
    private static JLabel RARITY_LABEL(){
        return  getImageLabel("rarity.png",(float)12 / 40, (float)56 / 64,
                (float)15 / 40, (float)7 / 64);
    }
    private static JLabel MANA_LABEL() {
        return getImageLabel("crystal.png",(float)32 / 40, 0,
                (float)7 / 40, (float)7 / 64);
    }


    private static Map<String, Image> images = new TreeMap<>();
    private static JLabel getImageLabel(String name, float x, float y, float width, float height) {
        JLabel res = new JLabel(new ImageIcon(getImage(name, Math.round(WIDTH * width), Math.round(HEIGHT * height))));
        res.setBounds(new Rectangle(Math.round(WIDTH * x), Math.round(HEIGHT * y),
                Math.round(WIDTH * width), Math.round(HEIGHT * height)));
        return res;
    }
    private static Image getImage(String imageName, int width, int height) {
        String query = imageName + "@" + width  + "/" + height;
        if (images.containsKey(query))
            return images.get(query);
        Image res = Toolkit.getDefaultToolkit().getImage(imagesPath + imageName);
        res = res.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        res = ImageUtil.getWhiteTransparent(res);
        images.put(query, res);
        return res;
    }

    private static class CardLabel extends JLabel {
        public CardLabel(String text, Icon icon, int horizontalAlignment) {
            super(text, icon, horizontalAlignment);
            setFont(LABEL_FONT);
            setForeground(LABEL_COLOR);
        }

        public CardLabel(String text, int horizontalAlignment) {
            super(text, horizontalAlignment);
            setFont(LABEL_FONT);
            setForeground(LABEL_COLOR);
        }

        public CardLabel(String text) {
            super(text);
            setFont(LABEL_FONT);
            setForeground(LABEL_COLOR);
        }

        public CardLabel() {
            setFont(LABEL_FONT);
            setForeground(LABEL_COLOR);
        }
    }

    private CardLabel attack;
    private CardLabel health;
    private JTextArea description;
    private CardLabel cost;
    private CardLabel rarity;
    private JLabel name;

    public Card card;

    public CardShape(Card card) {
        this.card = card;
        //layout
        setPreferredSize(new Dimension(WIDTH + 10, HEIGHT + 10));
        this.setLayout(null);
        this.setOpaque(false);





        //add components
        addName(card.getName());
        addRarity(card.getRarity());
        addDescription(card.getDescription());
        addCost(card.getManaCost());

        if (card instanceof SpellCard)
            initSpellCard((SpellCard) card);
        if (card instanceof WeaponCard)
            initWeaponCard((WeaponCard) card);
        if (card instanceof MinionCard)
            initMinionCard((MinionCard) card);
        add(BASE_LABEL());

    }
    public CardShape(DB.components.cards.Card card) {
        this.card = CardFactory.produce(null, card, null);
        //layout
        setPreferredSize(new Dimension(WIDTH + 10, HEIGHT + 10));
        this.setLayout(null);
        this.setOpaque(false);





        //add components
        addName(card.getName());
        addRarity(card.getRarity());
        addDescription(card.getDescription());
        addCost(card.getManaCost());

        if (this.card instanceof SpellCard)
            initSpellCard((SpellCard) this.card);
        if (this.card instanceof WeaponCard)
            initWeaponCard((WeaponCard) this.card);
        if (this.card instanceof MinionCard)
            initMinionCard((MinionCard) this.card);
        add(BASE_LABEL());

    }

    private void initSpellCard(SpellCard card) {
        add(SPELL_LABEL());
    }
    private void initWeaponCard(WeaponCard card) {
        add(WEAPON_LABEL());
        addAttack(card.getAttack());
        addHealth(card.getDurability());
    }
    private void initMinionCard(MinionCard card) {
        add(MINION_LABEL());
        addAttack(card.getAttack());
        addHealth(card.getHealth());
    }

    public void addName(String name) {
        this.name = new JLabel(name, JLabel.CENTER);
        this.name.setFont(new Font("Spicy Rice", Font.PLAIN, 15));
        this.name.setBounds(WIDTH / 5, 0,
                WIDTH * 3 / 5, HEIGHT * 7 / 40);
        add(this.name);
    }
    public void addAttack(int attack) {
        this.attack = new CardLabel(attack + "", JLabel.CENTER);
        this.attack.setBounds(ATTACK_LABEL().getBounds());
        add(this.attack);
        add(ATTACK_LABEL());
    }
    public void addHealth(int health) {
        this.health = new CardLabel(health + "", JLabel.CENTER);
        this.health.setBounds(HEALTH_LABEL().getBounds());
        add(this.health);
        add(HEALTH_LABEL());
    }
    public void addDescription(String description) {
        this.description = new JTextArea(description);
        this.description.setLineWrap(true);
        this.description.setWrapStyleWord(true);
        this.description.setEditable(false);
        this.setForeground(LABEL_COLOR);
        this.description.setFont(LABEL_FONT);
        this.description.setOpaque(false);
        this.description.setBounds(10, HEIGHT / 2, WIDTH * 9 / 10, HEIGHT / 3);
        add(this.description);
    }
    public void addCost(int cost) {
        this.cost = new CardLabel(cost + "", JLabel.CENTER);
        this.cost.setBounds(MANA_LABEL().getBounds());
        add(this.cost);
        add(MANA_LABEL());

    }
    public void addRarity(Rarity rarity) {
        this.rarity = new CardLabel(rarity.toString().charAt(0) + "", JLabel.CENTER);
        this.rarity.setBounds(RARITY_LABEL().getBounds());
        add(this.rarity);
        add(RARITY_LABEL());
    }




    @Override
    public synchronized void addMouseListener(MouseListener l) {
        super.addMouseListener(l);
//        description.addMouseListener(l);
        //TODO mouseClick doesn't work on TextArea
    }
}