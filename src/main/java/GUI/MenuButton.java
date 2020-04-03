package GUI;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.math.BigInteger;

public class MenuButton extends JButton {
    private static final Color color = new Color( 211, 84, 0 );
    private static final Font font = new Font("Spicy Rice", Font.PLAIN, 30);
    private static final Color foreGround = new Color(74, 35, 90  );
    public MenuButton(String title) {
        super(title);
        setBorder(new BevelBorder(BevelBorder.RAISED));
        setBackground(color);
        setHorizontalTextPosition(JButton.CENTER);
        setFont(font);
        setForeground(foreGround);
        setPreferredSize(new Dimension(300, 100));
    }
}
