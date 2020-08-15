package eu.ondrejmatys.dodgebow.menus;

import eu.ondrejmatys.dodgebow.messages.Message;
import org.bukkit.entity.Player;

public class AdminMenu extends Message {
    public static void AdminMenu(Player player) {
        player.sendMessage(Message.colorCodes("&e/dodgeadmin reload &a- plugin reload"));
        player.sendMessage(Message.colorCodes("&e/dodgeadmin forcestart &a- force arena to start"));
        player.sendMessage(Message.colorCodes("&e/dodgeadmin arena create <name> &a- creates arena with name"));
        player.sendMessage(Message.colorCodes("&e/dodgeadmin arena min <arena> <count> &a- set minimum players"));
        player.sendMessage(Message.colorCodes("&e/dodgeadmin arena max <arena> <count> &a- set maximum players"));
        player.sendMessage(Message.colorCodes("&e/dodgeadmin arena spawnpoint <arena> &a- add spawnpoint"));
        player.sendMessage(Message.colorCodes("&e/dodgeadmin arena spawnpoint <arena> reset &a- reset spawnpoints"));
        player.sendMessage(Message.colorCodes("&e/dodgeadmin arena remove <arena> &a- removes arena"));
    }
}
