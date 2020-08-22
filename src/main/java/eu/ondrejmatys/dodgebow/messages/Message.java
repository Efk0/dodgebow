package eu.ondrejmatys.dodgebow.messages;

import eu.ondrejmatys.dodgebow.DodgeBow;
import eu.ondrejmatys.dodgebow.config.SimpleConfig;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Message {

    public static void Message(String message, Player player) {
        String prefix = DodgeBow.getInstance().messagesConfig.getString("prefix");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }

    public static void Message(String config, String path, Player player) {
        String message = DodgeBow.getInstance().messagesConfig.getString(path);
        String prefix = DodgeBow.getInstance().messagesConfig.getString("prefix");
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
    }

    public static String colorCodes(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }
}
