package util;

import java.io.FileInputStream;
import java.util.Properties;

public class DBPropertyUtil {

    public static Properties getProperties(String filename) throws Exception {
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream(filename);
        props.load(fis);
        return props;
    }
}