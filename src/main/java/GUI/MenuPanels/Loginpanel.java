package GUI.MenuPanels;

import GUI.*;
import GUI.Frames.MenuFrame;
import Network.Client;

import javax.swing.*;
import java.awt.*;

public class Loginpanel extends MenuPanel {
    private static final Font titleFont = new Font("Spicy Rice", Font.ITALIC, 70);
    private static final Font labelFont = new Font("arial", Font.BOLD, 30);

    private Loginpanel() {
        //back to menu shortKey
        withQuitAndExit(false);

        //layout
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 0);

        //Title
        JLabel title = new JLabel("Welcome to Heart Stroke!");
        title.setFont(titleFont);
        title.setForeground(Color.RED);
        title.setPreferredSize(new Dimension(MenuFrame.width * 2 / 3, MenuFrame.height / 4));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 5;
        gbc.gridheight = 1;
        add(title, gbc);

        //textFields
        JLabel user = new JLabel("username");
        user.setFont(labelFont);
        user.setForeground(Color.ORANGE);
        user.setPreferredSize(new Dimension(MenuFrame.width / 12, MenuFrame.height / 9));
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(user, gbc);

        JTextField name = new JTextField();
        name.setFont(labelFont);
        name.setPreferredSize(new Dimension(MenuFrame.width * 3 / 12, MenuFrame.height / 9));
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        add(name, gbc);


        JLabel pass = new JLabel("password");
        pass.setFont(labelFont);
        pass.setForeground(Color.ORANGE);
        pass.setPreferredSize(new Dimension(MenuFrame.width / 12, MenuFrame.height / 9));
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(pass, gbc);

        JPasswordField word = new JPasswordField();
        word.setFont(labelFont);
        word.setPreferredSize(new Dimension(MenuFrame.width * 3 / 12, MenuFrame.height / 9));
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        add(word, gbc);





        //buttons
        MenuButton loginButton = new MenuButton("Login", new Dimension(300, 100), 30);
        loginButton.setPreferredSize(new Dimension(MenuFrame.width * 2 / 12, MenuFrame.height / 9));
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        add(loginButton, gbc);

        MenuButton RegisterButton = new MenuButton("Register", new Dimension(300, 100), 30);
        RegisterButton.setPreferredSize(new Dimension(MenuFrame.width * 2 / 12, MenuFrame.height / 9));
        gbc.gridx = 3;
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        add(RegisterButton, gbc);


        //actionListeners
        loginButton.addActionListener(e -> login(name.getText(), new String(word.getPassword())));
        RegisterButton.addActionListener(e -> register(name.getText(), new String(word.getPassword())));
    }


    private void login(String userName, String passWord) {
        Client.getInstance().login(userName, passWord);
    }

    private void register(String userName, String password) {
        Client.getInstance().register(userName, password);
    }


    private static Loginpanel instance;
    public static Loginpanel getInstance() {
        if (instance == null)
            instance = new Loginpanel();
        return instance;
    }

}
