package eu.ondrejmatys.dodgebow.menus;

import eu.ondrejmatys.dodgebow.messages.Message;
import org.bukkit.entity.Player;

public class BaseMenu {
    public static void BaseMenu(Player player) {
        player.sendMessage(Message.colorCodes("&e/dodgebow join <arena> &a- join to arena"));
        player.sendMessage(Message.colorCodes("&e/dodgebow leave &a- leave arena"));
        player.sendMessage(Message.colorCodes("&e/dodgebow list &a- list all arenas"));
        player.sendMessage(Message.colorCodes("&e/dodgebow stats [player] &a- get yours or others statistics"));
    }
}
