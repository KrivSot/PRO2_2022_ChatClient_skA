package models.gui;

import models.chatClients.ChatClient;

import javax.swing.table.AbstractTableModel;

public class LoggedUsersTableModel extends AbstractTableModel {

    ChatClient chatClient;

    public LoggedUsersTableModel(ChatClient chatClient){
        this.chatClient = chatClient;
    }

    @Override
    public int getRowCount() {
        return chatClient.getLoggedUsers().size();
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return chatClient.getLoggedUsers().get(columnIndex);
    }

     @Override
    public String getColumnName(int column){
        if(column == 0) return "Coll A";
        else return "Coll B";
     }
}
