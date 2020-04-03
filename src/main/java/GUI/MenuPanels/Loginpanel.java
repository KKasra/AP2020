package GUI.MenuPanels;

import GUI.*;
import GUI.Frames.MenuFrame;
import GUI.Frames.WarningFrame;
import User.User;
import User.UserDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Loginpanel extends MenuPanel {
    private static final Font titleFont = new Font("Spicy Rice", Font.ITALIC, 70);
    private static final Font labelFont = new Font("arial", Font.BOLD, 30);

    private Loginpanel() {
        //back to menu shortKey
        withQuitAndExit(false);

        //layout
        setLayout(null);

        //Title
        JLabel title = new JLabel("Welcome to Heart Stroke!");
        title.setFont(titleFont);
        title.setForeground(Color.RED);
        title.setBounds(MenuFrame.width / 6, 0,
                MenuFrame.width * 2 / 3, MenuFrame.height / 3);
        add(title);

        //textFields
        JLabel user = new JLabel("username");
        user.setBounds(MenuFrame.width / 3, MenuFrame.height / 3,
                MenuFrame.width / 12, MenuFrame.height / 12);
        user.setFont(labelFont);
        user.setForeground(Color.ORANGE);
        JTextField name = new JTextField();
        name.setBounds(MenuFrame.width * 5 / 12, MenuFrame.height / 3,
                MenuFrame.width * 3 / 12, MenuFrame.height / 12);
        name.setFont(labelFont);
        add(user);
        add(name);

        JLabel pass = new JLabel("password");
        pass.setBounds(MenuFrame.width / 3, MenuFrame.height * 5 / 12,
                MenuFrame.width / 12, MenuFrame.height / 12);
        pass.setFont(labelFont);
        pass.setForeground(Color.ORANGE);
        JPasswordField word = new JPasswordField();
        word.setBounds(MenuFrame.width * 5 / 12, MenuFrame.height * 5 / 12,
                MenuFrame.width * 3 / 12, MenuFrame.height / 12);
        word.setFont(labelFont);
        add(pass);
        add(word);

        //buttons
        MenuButton loginButton = new MenuButton("Login");
        loginButton.setBounds(MenuFrame.width / 3, MenuFrame.height * 3/ 6,
                MenuFrame.width / 6, MenuFrame.height / 12);
        add(loginButton);

        MenuButton RegisterButton = new MenuButton("Register");
        RegisterButton.setBounds(MenuFrame.width * 3 / 6, MenuFrame.height * 3/ 6,
                MenuFrame.width / 6, MenuFrame.height / 12);
        add(RegisterButton);


        //actionListeners
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login(name.getText(), new String(word.getPassword()));
            }
        });
        RegisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register(name.getText(), new String(word.getPassword()));
            }
        });
    }

    private void login(String userName, String passWord) {
        try {
            User.user = UserDB.getUser(userName, passWord);
            User.user.getLog().writeEvent("sign_in", "");
            MenuFrame.getInstance().setPanel(MainMenu.getInstance());

        } catch (Exception e) {
            e.printStackTrace();
            WarningFrame.print(e.getMessage());
        }
    }

    private void register(String userName, String password) {
        try{
            User.user = new User(userName, password);
            UserDB.addUser(User.user);
            User.user.getLog().writeEvent("sign_in", "");
            MenuFrame.getInstance().setPanel(MainMenu.getInstance());
        }catch (Exception e) {
            e.printStackTrace();
            WarningFrame.print(e.getMessage());
        }
    }


    private static Loginpanel instance;
    public static Loginpanel getInstance() {
        if (instance == null)
            instance = new Loginpanel();
        return instance;
    }

}
