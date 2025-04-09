package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;
import java.io.FileInputStream;

public class DBUtil {

    private static Connection connection = null;

    public static Connection getDBConn() {
        if (connection == null) {
            try {
                Properties props = new Properties();
                FileInputStream fis = new FileInputStream("db.properties");
                props.load(fis);

                String url = props.getProperty("db.url");
                String username = props.getProperty("db.username");
                String password = props.getProperty("db.password");

                if (url == null || username == null || password == null) {
                    throw new RuntimeException("One or more database properties (url, username, password) are missing in db.properties.");
                }

                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Database connection established.");
            } catch (Exception e) {
                System.out.println("Unexpected error while establishing DB connection.");
                e.printStackTrace();
            }
        }
        return connection;
    }
}
