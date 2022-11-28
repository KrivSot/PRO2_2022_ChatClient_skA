package models.chatClients.api;

import com.google.gson.annotations.Expose;

public class SendMessageRequest {
    @Expose(serialize = true, deserialize = true)
    private String token;
    @Expose(serialize = true, deserialize = true)
    private String text;

    public SendMessageRequest(String token, String text){
        this.token = token;
        this.text = text;
    }
}
