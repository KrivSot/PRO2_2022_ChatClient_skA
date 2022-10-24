import models.chatClients.ChatClient;
import models.chatClients.InMemoryChatClient;
import models.gui.MainFrame;

public class main {
    public static void main(String[] args) {
        ChatClient chatClient = new InMemoryChatClient();
        MainFrame mainFrame = new MainFrame(800, 600, chatClient);
        mainFrame.setVisible(true);
    }
}
