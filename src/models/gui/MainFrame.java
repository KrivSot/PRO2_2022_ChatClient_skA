package models.gui;

import models.Message;
import models.chatClients.ChatClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {

    ChatClient chatClient;
    JButton btnLogin, btnSend;
    JTextArea txtAreaChat;
    JTextField txtInputName, txtInputMessage;
    JTextArea txtChat;

    public MainFrame(int width, int height, ChatClient chatClient){
        super("Titul");
        this.chatClient = chatClient;
        setSize(width, height);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        initGui();
        pack();
    }

    private void initGui(){
        txtInputName = new JTextField();
        JPanel panelMain = new JPanel(new BorderLayout());
        panelMain.add(initLoginPanel(), BorderLayout.NORTH);
        panelMain.add(initChatPanel(), BorderLayout.CENTER);
        panelMain.add(initLoggedUsersPanel(), BorderLayout.EAST);
        panelMain.add(initMessagePanel(), BorderLayout.SOUTH);

        add(panelMain);
    }

    private JPanel initLoginPanel(){
        JPanel panelLogin = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelLogin.add(new JLabel("username"));
        panelLogin.add(txtInputName);
        JButton btnLogin = new JButton();

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Button login clicked - " + txtInputName.getText());
                if(chatClient.isAuthenticated()){
                    chatClient.logout();
                    btnLogin.setText("Login");
                    txtInputName.setEditable(true);
                    txtAreaChat.setEnabled(false);
                }
                else{
                    String userName = txtInputName.getText();
                    if(userName.length()<1){
                        JOptionPane.showMessageDialog(null, "Enter your username", "Chyba", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    chatClient.login(userName);
                    btnLogin.setText("Logout");
                    txtInputName.setEditable(false);
                    txtAreaChat.setEnabled(true);
                }
            }
        });

        txtInputName = new JTextField("",30);
        panelLogin.add(btnLogin);

        return panelLogin;
    }

    private JPanel initChatPanel(){
        JPanel panelChat = new JPanel();
        panelChat.setLayout(new BoxLayout(panelChat, BoxLayout.X_AXIS));
        txtAreaChat = new JTextArea();
        txtAreaChat.setAutoscrolls(true);
        txtAreaChat.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtAreaChat);
        panelChat.add(scrollPane);
        return panelChat;
    }

    private JPanel initMessagePanel(){
        JPanel panelMessage = new JPanel();
        txtInputMessage = new JTextField("", 50);
        panelMessage.add(txtInputMessage);
        btnSend = new JButton("Pošli");
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String text = txtInputMessage.getText();
                if(text.length()==0){
                    return;
                }
                if(!chatClient.isAuthenticated()){
                    return;
                }
                chatClient.sendMessage(text);
                txtInputMessage.setText("");
                refreshMessages();
            }
        });
        panelMessage.add(btnSend);
        return panelMessage;
    }

    private JPanel initLoggedUsersPanel() {
        JPanel panel = new JPanel();

        /*Object[][] data = new Object [][]{
                {"1,1","1,2"},
                {"2,1","2,2"},
                {"safdsgd","fdhjh"},
        };*/

        JTable tblLoggedUsers = new JTable();

        LoggedUsersTableModel loggedUsersTableModel = new LoggedUsersTableModel(this.chatClient);
        tblLoggedUsers.setModel(loggedUsersTableModel);

        chatClient.addActionListenerLoggedUsersChanged(e -> {
            loggedUsersTableModel.fireTableDataChanged();
        });

        JScrollPane scrollPane = new JScrollPane(tblLoggedUsers);
        scrollPane.setPreferredSize(new Dimension(250,500));
        panel.add(scrollPane);
        return panel;
    }

    private void refreshMessages(){
        if(!chatClient.isAuthenticated()) return;

        txtAreaChat.setText("");

        for(Message msg: chatClient.getMessages()){
            txtAreaChat.append(msg.toString());
            txtAreaChat.append("\n");
        }
    }
}
