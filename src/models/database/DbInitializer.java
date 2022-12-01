package models.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DbInitializer {
    private final String driver;
    private final String url;

    public DbInitializer(String driver, String url){
        this.driver = driver;
        this.url = url;
    }

    public void init(){
        try {
            Class.forName(driver); // nacte jdb:derby driver
            Connection conn = DriverManager.getConnection(url); // otevre spojeni

            String sql =
                    "CREATE TABLE ChatMessages "
                    + "(id INT NOT NULL GENERATE ALWAYS AS IDENTITY "
                        + " CONSTRAINT ChatMessages_PK PRIMARY KEY,"
                    + "author varchar(50), "
                    + "text varchar(1000), "
                    + "created timestamp);";

            Statement statement = conn.createStatement();
            statement.executeUpdate(sql);
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
