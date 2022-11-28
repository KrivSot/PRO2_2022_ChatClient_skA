package models.chatClients;

import models.Message;
import models.chatClients.fileOperations.ChatFileOperations;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class FileChatClient implements  ChatClient{
    private String loggedUser;
    private List<String> loggedUsers;
    private List<Message> messages;

    private List<ActionListener> listenersLoggedusersChanged = new ArrayList<>();
    private List<ActionListener> listenersMessagesChanged = new ArrayList<>();

    ChatFileOperations chatFileOperations;

    public FileChatClient(ChatFileOperations chatFileOperations) {
        loggedUsers = new ArrayList<>();
        this.chatFileOperations = chatFileOperations;
        messages = chatFileOperations.readMessages();
    }

    @Override
    public void sendMessage(String text) {
        messages.add(new Message(loggedUser, text));
        System.out.println("new message - " + text);
        chatFileOperations.writeMessages(messages);
        raiseEventMessagesChanged();
    }

    @Override
    public void login(String userName) {
        loggedUser = userName;
        loggedUsers.add(userName);
        addSystemMessages(Message.USER_LOGGED_IN, loggedUser);
        System.out.println("user logged in: " + loggedUser);
        raiseEventLoggedUsersChanged();
    }

    @Override
    public void logout() {
        loggedUsers.remove(loggedUser);
        loggedUser = null;
        addSystemMessages(Message.USER_LOGGED_OUT, loggedUser);
        System.out.println("user: " + loggedUser + " is logged out");
        raiseEventLoggedUsersChanged();
    }

    @Override
    public boolean isAuthenticated() {
        System.out.println("is authenticated: " + (loggedUser != null));
        return loggedUser != null;
    }

    @Override
    public List<String> getLoggedUsers() {
        return loggedUsers;
    }

    @Override
    public List<Message> getMessage() {
        return messages;
    }

    @Override
    public void addActionListenerLoggedUsersChanged(ActionListener toAdd) {
        listenersLoggedusersChanged.add(toAdd);
    }

    @Override
    public void addActionListenerMessagesChanged(ActionListener toAdd) {
        listenersMessagesChanged.add(toAdd);
    }

    private void raiseEventLoggedUsersChanged(){
        for (ActionListener al:
                listenersLoggedusersChanged) {
            al.actionPerformed(new ActionEvent(this,1, "usersChanged"));
        }
    }

    private void raiseEventMessagesChanged(){
        for (ActionListener al:
                listenersMessagesChanged) {
            al.actionPerformed(new ActionEvent(this,1, "messagesChanged"));
        }
    }

    private void addSystemMessages(int type, String author){
        messages.add(new Message(type,author));
        chatFileOperations.writeMessages(messages);
        raiseEventMessagesChanged();
    }
}
