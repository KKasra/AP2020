package DB.components.cards;

import GUI.CardShape;
import GUI.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

public enum Passive {
    TwiceDraw, OffCards, Nurse, FreePower, ManaJump;

    public static Map<Integer, Passive>  map = new TreeMap<>();
    static {
        map.put(0, TwiceDraw);
        map.put(1, Nurse);
        map.put(2, OffCards);
        map.put(3, FreePower);
        map.put(4, ManaJump);
    }



    public static class PassiveShape extends JPanel{
        private static int width = 200;
        private static int height = 320;
        private static String path = "gameComponents/passive/";
        private static Font font = new Font("Spicy Rice",Font.PLAIN,20);

        private static Image BACKGROUND =ImageUtil.getWhiteTransparent(path + "wall.png")
                                                  .getScaledInstance(width, height,Image.SCALE_SMOOTH);

        private Passive passive;

        JLabel background;
        JLabel picture;
        JTextArea description;
        JLabel name;
        public PassiveShape(Passive item) {
            this.passive = item;
            setPreferredSize(new Dimension(CardShape.WIDTH, CardShape.HEIGHT));

            initComponents();
            alignComponents();

        }

        private void initComponents() {

            background = new JLabel(new ImageIcon(BACKGROUND));

            picture = new JLabel(new ImageIcon(ImageUtil.
                    getWhiteTransparent(path + passive + ".png")
                    .getScaledInstance(width, height / 2,Image.SCALE_SMOOTH)));
            picture.setOpaque(false);

            description = new JTextArea();
            description.setOpaque(false);
            description.setEnabled(false);
            description.setForeground(new Color(255, 112, 67));
            description.setFont(font);

            name =  new JLabel(passive.toString());
            name.setForeground(new Color(255, 112, 67));
            name.setFont(font);
            name.setOpaque(false);
        }

        private void alignComponents() {
            setLayout(null);
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setOpaque(false);
            panel.setBounds(0, 0, width, height);
            add(panel);

            background.setBounds(0, 0, width, height);
            add(background);



            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            panel.add(picture, gbc);

            ++gbc.gridy;
            panel.add(name, gbc);

            ++gbc.gridy;
            panel.add(description, gbc);
        }

        public Passive getPassive() {
            return passive;
        }
    }

}
