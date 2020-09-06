package eu.ondrejmatys.dodgebow.menus;

import eu.ondrejmatys.dodgebow.DodgeBow;
import eu.ondrejmatys.dodgebow.messages.Message;
import org.bukkit.entity.Player;

public class BaseStatsMenu {
    private static DodgeBow plugin = DodgeBow.getInstance();

    public static void BaseStatsMenu(Player player, String statsPlayer) {
        if (statsPlayer.equalsIgnoreCase(player.getName())) {
            player.sendMessage(Message.colorCodes("&e------ &aYour statistics &e------"));
        } else {
            player.sendMessage(Message.colorCodes(String.format("&e------ &a%s's statistics &e------", statsPlayer)));
        }

        player.sendMessage(Message.colorCodes("&eKills: &f" + plugin.statsManager.getValue(statsPlayer, "kills")));
        player.sendMessage(Message.colorCodes("&eWins: &f" + plugin.statsManager.getValue(statsPlayer, "wins")));
        player.sendMessage(Message.colorCodes("&ePlayed Games: &f" + plugin.statsManager.getValue(statsPlayer, "games_played")));
        player.sendMessage(Message.colorCodes("&eDeaths: &f" + plugin.statsManager.getValue(statsPlayer, "deaths")));
    }
}
