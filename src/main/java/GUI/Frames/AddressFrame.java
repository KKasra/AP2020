package GUI.Frames;

import GUI.MenuButton;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class AddressFrame extends JFrame {

    private JLabel domainLabel;
    private JTextField domainField;
    private JLabel portLabel;
    private JTextField portField;
    private MenuButton connectButton;

    private ActionListener connectionListener;

    private AddressFrame() {
        initComponents();
        alignComponents();
    }

    private void initComponents() {
        setSize(500, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        domainLabel = new JLabel("domain :", JLabel.RIGHT);
        domainField = new JTextField(20);
        portLabel = new JLabel("IP :", JLabel.RIGHT);
        portField = new JTextField(20);
        connectButton = new MenuButton("Connect", new Dimension(300, 100), 20);
        connectButton.addActionListener(a -> connectionListener.actionPerformed(a));
    }

    private void alignComponents() {
        JPanel panel = new JPanel();
        setContentPane(panel);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;

        panel.add(domainLabel, gbc);

        ++gbc.gridx;
        panel.add(domainField, gbc);

        ++gbc.gridy;
        gbc.gridx = 0;
        panel.add(portLabel, gbc);

        ++gbc.gridx;
        panel.add(portField, gbc);

        ++gbc.gridy;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(connectButton, gbc);


    }

    public void setConnectionListener(ActionListener connectionListener) {
        this.connectionListener = connectionListener;
    }

    public String getDomain() {
        return domainField.getText();
    }

    public int getPort() {
        return Integer.parseInt(portField.getText());
    }

    private static AddressFrame instance;
    public static AddressFrame getInstance() {
        if (instance == null)
            instance = new AddressFrame();
        return instance;
    }
}
