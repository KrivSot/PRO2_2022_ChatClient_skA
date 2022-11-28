package models.chatClients.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Message;
import models.chatClients.ChatClient;
import models.chatClients.LocalDateTimeDeserializer;
import models.chatClients.LocalDateTimeSerializer;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
//http://fimuhkpro22021.aspifyhost.cz/swagger/index.html
public class ApiChatClient implements ChatClient {
    private String loggedUser;
    private List<String> loggedUsers;
    private List<Message> messages;

    private List<ActionListener> listenersLoggedusersChanged = new ArrayList<>();
    private List<ActionListener> listenersMessagesChanged = new ArrayList<>();

    private final String BASE_URL = "http://fimuhkpro22021.aspifyhost.cz/";
    private String token;
    private Gson gson;

    public ApiChatClient() {
        loggedUsers = new ArrayList<>();
        messages = new ArrayList<>();
        gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .setPrettyPrinting() // set user friendly file writing
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeDeserializer())
                .create();
    }

    @Override
    public void sendMessage(String text) {
        messages.add(new Message(loggedUser, text));
        System.out.println("new message - " + text);
        raiseEventMessagesChanged();
    }

    @Override
    public void login(String userName) {
        try{
            String url = BASE_URL + "/api/Chat/Login";
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity("\""+userName+"\"","utf-8");
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            if(response.getStatusLine().getStatusCode() == 200){
                token = response.getEntity().toString();
                token = token.replace("\"", "").trim();

                loggedUser = userName;
                raiseEventLoggedUsersChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        loggedUser = userName;
        loggedUsers.add(userName);
        addSystemMessages(Message.USER_LOGGED_IN, loggedUser);
        System.out.println("user logged in: " + loggedUser);
        raiseEventLoggedUsersChanged();
    }

    @Override
    public void logout() {
        try{
            String url = BASE_URL + "/api/Chat/Login";
            HttpPost post = new HttpPost(url);
            StringEntity body = new StringEntity("\""+token+"\"","utf-8");
            body.setContentType("application/json");
            post.setEntity(body);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = httpClient.execute(post);

            if(response.getStatusLine().getStatusCode() == 204){
               token = null;
               loggedUser = null;

               loggedUsers = new ArrayList<>();
               raiseEventLoggedUsersChanged();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
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
        raiseEventMessagesChanged();
    }
}
