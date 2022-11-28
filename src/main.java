import models.chatClients.ChatClient;
import models.chatClients.FileChatClient;
import models.chatClients.InMemoryChatClient;
import models.chatClients.api.ApiChatClient;
import models.chatClients.fileOperations.ChatFileOperations;
import models.chatClients.fileOperations.JsonChatFileOperations;
import models.database.DbInitializer;
import models.gui.MainFrame;

public class Main {
    public static void main(String[] args) {
        String databaseDriver = "org.apache.derby.jdbc.EmbeddedDriver";
        String databaseUrl = "jdbc:derby:ChatClientDb_skB";

        DbInitializer dbInitializer = new DbInitializer(databaseDriver, databaseUrl);
        dbInitializer.init(); // 2. run this mian

        ChatFileOperations chatFileOperations = new JsonChatFileOperations();
        ChatClient chatClient = new ApiChatClient();

        MainFrame window = new MainFrame(800,600, chatClient);

        //test();

    }

    private static void test() {
        ChatClient client = new InMemoryChatClient();

        client.login("vuca");

        client.sendMessage("Message 1");
        client.sendMessage("Hey");

        client.logout();

    }
}
