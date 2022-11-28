package models.chatClients;

import models.Message;

import java.awt.event.ActionListener;
import java.util.List;

public interface ChatClient {
    void sendMessage(String text);
    void login(String userName);
    void logout();
    boolean isAuthenticated();
    List<String> getLoggedUsers();
    List<Message> getMessage();

    void addActionListenerLoggedUsersChanged(ActionListener toAdd);
    void addActionListenerMessagesChanged(ActionListener toAdd);
}
