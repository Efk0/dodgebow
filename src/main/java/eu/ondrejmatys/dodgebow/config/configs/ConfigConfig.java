package eu.ondrejmatys.dodgebow.config.configs;

import eu.ondrejmatys.dodgebow.DodgeBow;
import eu.ondrejmatys.dodgebow.config.SimpleConfig;

public class ConfigConfig {

    public static void InitConfig() {
        SimpleConfig config = DodgeBow.getInstance().configManager.getNewConfig("config.yml");
        config.setIfNot("allowedCommands", new String[] {"/dodgebow leave", "/db leave"}, new String[] {"Commands, that are allowed in game"});

        config.saveConfig();
    }
}
