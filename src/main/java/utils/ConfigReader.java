package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {
    private static ConfigReader instance;
    private final Properties properties;

    private ConfigReader() {
        String propertyFilePath = "src/main/resources/config.properties";
        properties = new Properties();
        try {
            FileInputStream fis = new FileInputStream(propertyFilePath);
            properties.load(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("config.properties file not found at " + propertyFilePath);
        }
    }

    public static synchronized ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    public String getProperty(String key) {
        String value = properties.getProperty(key);
        if (value != null) {
            return value;
        } else {
            throw new RuntimeException("Property '" + key + "' not specified in the config.properties file.");
        }
    }

    public String getBrowser() { return getProperty("browser"); }
    public String getBaseUrl() { return getProperty("baseUrl"); }
    public long getImplicitWait() { return Long.parseLong(getProperty("implicitWait")); }
    public long getExplicitWait() { return Long.parseLong(getProperty("explicitWait")); }
}
