package GUI.Frames;

import GUI.MenuButton;
import Network.Client;

import javax.swing.*;
import java.awt.*;

public class DeleteAccountFrame extends JFrame {
    private JLabel passwordLabel;
    private JPasswordField passwordField;
    private MenuButton confirmButton;

    private DeleteAccountFrame(){
        initComponents();
        alignComponents();
    }
    private void initComponents() {
        passwordLabel = new JLabel("password :", JLabel.RIGHT);
        passwordLabel.setPreferredSize(new Dimension(100, 50));
        passwordField = new JPasswordField(20);
        passwordField.setPreferredSize(new Dimension(100,50));
        confirmButton = new MenuButton("Confirm", new Dimension(100, 50),10);
        confirmButton.addActionListener(a -> {
            Client.getInstance().deleteAccount(new String(passwordField.getPassword()));
            setVisible(false);
        });
    }

    private void alignComponents() {
        setSize(500, 200);
        setLocationRelativeTo(null);
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(220, 9, 39, 245));
        panel.setOpaque(true);
        setContentPane(panel);

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        panel.add(passwordLabel, gbc);

        ++gbc.gridx;
        panel.add(passwordField, gbc);

        ++gbc.gridx;
        panel.add(confirmButton, gbc);

    }


    private static DeleteAccountFrame instance;
    public static DeleteAccountFrame getInstance() {
        if (instance == null)
            instance = new DeleteAccountFrame();
        return instance;
    }
}
