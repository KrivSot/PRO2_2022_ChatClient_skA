package models.database;

import models.Message;

import java.util.List;

public interface DataBaseOperations {
    void addMessage(Message message);
    List<Message> getMessage();
}
