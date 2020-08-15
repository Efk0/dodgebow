package eu.ondrejmatys.dodgebow.messages;

import eu.ondrejmatys.dodgebow.DodgeBow;
import eu.ondrejmatys.dodgebow.config.ConfigManager;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Message {

    public static void Message(String message, Player player) {
        String prefix = DodgeBow.getInstance().configManager.getString("messages", "prefix");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }

    public static void Message(String config, String path, Player player) {
        String message = DodgeBow.getInstance().configManager.getString(config, path);
        String prefix = DodgeBow.getInstance().configManager.getString("messages", "prefix");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }

    public static String colorCodes(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
