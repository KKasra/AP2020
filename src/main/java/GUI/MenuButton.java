package GUI;

import DB.components.User;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuButton extends JButton {
    private static final Color color = new Color( 211, 84, 0 );
    private static final Font font = new Font("Spicy Rice", Font.PLAIN, 30);
    private static final Color foreGround = new Color(74, 35, 90  );
    public MenuButton(String title, Dimension dimension, int fontSize) {
        super(title);
        setBorder(new BevelBorder(BevelBorder.RAISED));
        setBackground(color);
        setHorizontalTextPosition(JButton.CENTER);
        setFont(new Font("Spicy Rice", Font.PLAIN, fontSize));
        setForeground(foreGround);
        setPreferredSize(dimension);

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (User.user != null)
                    User.user.getLog().writeEvent("Button_clicked",title);
            }
        });
    }

}
