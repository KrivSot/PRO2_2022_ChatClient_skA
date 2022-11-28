import models.chatClients.ChatClient;
import models.chatClients.FileChatClient;
import models.chatClients.InMemoryChatClient;
import models.chatClients.api.ApiChatClient;
import models.chatClients.fileOperations.ChatFileOperations;
import models.chatClients.fileOperations.JsonChatFileOperations;
import models.database.DbInitializer;
import models.gui.MainFrame;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String databaseDriver = "org.apache.derby.jdbc.EmbeddedDriver";
        String databaseUrl = "jdbc:derby:ChatClientDb_skB";

        DbInitializer dbInitializer = new DbInitializer(databaseDriver, databaseUrl);
        dbInitializer.init(); // 2. run this mian

        ChatFileOperations chatFileOperations = new JsonChatFileOperations();
        ChatClient chatClient = new ApiChatClient();

        Class<ApiChatClient> reflectionExample = ApiChatClient.class;
        List<Field> field  = getAllField(reflectionExample);

        System.out.println("Class name: "+reflectionExample.getSimpleName()+ " | "+reflectionExample.getName());

        for(Field f : field){
            System.out.println(f.getName()+" : "+f.getType());
        }

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

    private static List<Field> getAllField(Class<?> cls){
        List<Field> fieldList = new ArrayList<>();
        for(Field f : cls.getDeclaredFields()){
            fieldList.add(f);
        }
        return fieldList;
    }
}
