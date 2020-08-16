package GUI.MenuPanels;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.ActionListener;

public class ChatPanel extends JPanel {
    private JScrollPane chatScrollPane;
    private JPanel chatPanel;
    private JTextField textField;
    private JButton sendButton;


    private static class ChatItem extends JPanel{
        private JLabel senderLabel;
        private JTextArea messageTextArea;
        ChatItem(String sender, String message){
            senderLabel = new JLabel(sender, JLabel.LEFT);
            senderLabel.setBorder(new EtchedBorder());
            senderLabel.setOpaque(true);
            senderLabel.setBackground(new Color(68, 144, 16));
            messageTextArea = new JTextArea(message);
            messageTextArea.setFont(new Font("arial",Font.BOLD, 20));
            messageTextArea.setBorder(new EtchedBorder());
            senderLabel.setPreferredSize(new Dimension(320, 25));
            messageTextArea.setPreferredSize(new Dimension(320, 25 * (message.length() / 28 + 1)));
            messageTextArea.setLineWrap(true);
            messageTextArea.setWrapStyleWord(true);
            messageTextArea.setEditable(false);

            setPreferredSize(new Dimension(320, 25 * (message.length() / 28 + 2)));
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridy = 0;
            add(senderLabel, gbc);

            ++gbc.gridy;
            add(messageTextArea, gbc);
        }
    }

    public void receiveNewMassage(String sender, String message) {
        ChatItem item = new ChatItem(sender, message);
        chatPanel.setSize(
                new Dimension(400,chatPanel.getPreferredSize().height + item.getPreferredSize().height));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridy = chatPanel.getComponentCount();
        chatPanel.add(item , gbc);
    }

    private ChatPanel() {
        initComponents();
        alignComponents();
    }

    private void initComponents() {
        chatPanel = new JPanel();
        chatScrollPane = new JScrollPane(chatPanel);
        textField = new JTextField();
        sendButton = new JButton("send");
        sendButton.setFont(new Font("Arial", Font.PLAIN, 20));
    }

    private void alignComponents() {
        setPreferredSize(new Dimension(400, 500));
        setLayout(new GridBagLayout());
        chatPanel.setLayout(new GridBagLayout());
        chatPanel.setSize(new Dimension(400, 0));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;

        gbc.gridwidth = 2;
        chatScrollPane.setPreferredSize(new Dimension(400, 500 - 25));
        add(chatScrollPane, gbc);

        ++gbc.gridy;
        gbc.gridwidth = 1;
        textField.setPreferredSize(new Dimension(300, 25));
        add(textField, gbc);

        ++gbc.gridx;
        sendButton.setPreferredSize(new Dimension(100,25));
        add(sendButton, gbc);
    }

    public void setSendAction(ActionListener action) {
        for (ActionListener actionListener : sendButton.getActionListeners()) {
            sendButton.removeActionListener(actionListener);
        }
        sendButton.addActionListener(action);
    }
    public String getMessage(){
        String message = textField.getText();
        textField.setText("");
        return message;
    }
    private static ChatPanel instance;
    public static ChatPanel getInstance() {
        if (instance == null)
            instance = new ChatPanel();
        return instance;
    }
}
