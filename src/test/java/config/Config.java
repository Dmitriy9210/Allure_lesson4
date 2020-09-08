package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static class Init {
        private static final Config conf = new Config();
    }

    private Config() {}

    public static Config config() {
        return Init.conf;
    }

    public String getAssignee() {
        return getProperty("assignee");
    }

    public String getLogin() {
        return getProperty("login");
    }

    public String getPassword() {
        return getProperty("password");
    }

    public String getToken() {
        return getProperty("token");
    }

    public String getTitle() {
        return getProperty("title");
    }

    public String getRepository() {
        return getProperty("repository");
    }

    public String getLoginFormUrl() {
        return getProperty("url");
    }

    private String getProperty(String key) {
        String property = "";
        InputStream is = getClass().getClassLoader().getResourceAsStream("config.properties");
        Properties properties = new Properties();
        try {
            properties.load(is);
            property = properties.getProperty(key);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return property;
    }

}
