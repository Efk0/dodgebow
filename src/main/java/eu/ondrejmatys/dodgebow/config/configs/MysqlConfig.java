package eu.ondrejmatys.dodgebow.config.configs;

import eu.ondrejmatys.dodgebow.DodgeBow;
import eu.ondrejmatys.dodgebow.config.SimpleConfig;

public class MysqlConfig {

    public static void InitConfig() {
        SimpleConfig config = DodgeBow.getInstance().configManager.getNewConfig("mysql.yml");
        config.setIfNot("enabled", false);

        config.setIfNot("host", "127.0.0.1");
        config.setIfNot("port", "3306");
        config.setIfNot("db", "dodgebow");
        config.setIfNot("user", "root");
        config.setIfNot("pass", "password");

        config.setIfNot("useSsl", false, "Does your MySQL use secure connection ? Enable it here");

        config.saveConfig();
    }
}
