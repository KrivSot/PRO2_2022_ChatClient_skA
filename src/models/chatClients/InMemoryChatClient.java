package models.chatClients;

import models.Message;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class InMemoryChatClient implements ChatClient {

    private String loggedUser;
    private List<Message> messages;
    private List<String> loggedUsers;

    private List<ActionListener> listenersLoggedUsersChanged = new ArrayList<>();
    private List<ActionListener> listenerMessageChanged = new ArrayList<>();

    public InMemoryChatClient(){
        messages = new ArrayList<>();
        loggedUsers = new ArrayList<>();
    }

    @Override
    public Boolean isAuthenticated() {
        return loggedUser!=null;
    }

    @Override
    public void login(String userName) {
        loggedUser = userName;
        loggedUsers.add(userName);
        addSystemMessage(Message.USER_LOGGED_IN, userName);
        raiseEventLoggedUsersChanged();
    }

    @Override
    public void logout() {
        addSystemMessage(Message.USER_LOGGED_OUT, loggedUser);
        loggedUsers.remove(loggedUser);
        loggedUser=null;
        raiseEventLoggedUsersChanged();
    }

    @Override
    public void sendMessage(String text) {
        messages.add(new Message(loggedUser, text));
        raiseEventMessagesChanged();
    }

    @Override
    public List<String> getLoggedUsers() {
        return loggedUsers;
    }

    @Override
    public List<Message> getMessages() {
        return messages;
    }

    @Override
    public void addActionListenerLoggedUsersChanged(ActionListener toAdd) {
        listenersLoggedUsersChanged.add(toAdd);
    }
    @Override
    public void addActionListenerMessagesChanged(ActionListener toAdd){
        listenerMessageChanged.add(toAdd);
    }

    private void raiseEventLoggedUsersChanged(){
        for(ActionListener al:listenersLoggedUsersChanged){
            al.actionPerformed(new ActionEvent(this, 1, "userChanged"));
        }
    }

    private void raiseEventMessagesChanged(){
        for(ActionListener al:listenerMessageChanged){
            al.actionPerformed(new ActionEvent(this, 1, "messagesChanged"));
        }
    }

    private void addSystemMessage(int type, String userName){
        messages.add(new Message(type, userName));
        raiseEventMessagesChanged();
    }
}
