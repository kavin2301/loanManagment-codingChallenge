package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class DBConnUtil {

    public static Connection getConnection(String filename) {
        Connection connection = null;
        try {
            Properties props = DBPropertyUtil.getProperties(filename);

            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            if (url == null || username == null || password == null) {
                throw new RuntimeException("Missing db properties in file.");
            }

            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connection established!");
        } catch (Exception e) {
            System.out.println("Error establishing connection: " + e.getMessage());
            e.printStackTrace();
        }

        return connection;
    }
}
