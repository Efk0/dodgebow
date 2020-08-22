package eu.ondrejmatys.dodgebow.scoreboards;

import eu.ondrejmatys.dodgebow.DodgeBow;
import eu.ondrejmatys.dodgebow.arena.Arena;
import eu.ondrejmatys.dodgebow.config.SimpleConfig;
import eu.ondrejmatys.dodgebow.players.DodgePlayer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import java.util.Collections;
import java.util.List;

public class ArenaScoreboard {


    private Arena arena;

    private DodgeBow plugin = DodgeBow.getInstance();

    public ArenaScoreboard(Arena arena) {
        this.arena = arena;

    }

    public void lobbyMenu() {
        for (DodgePlayer player : arena.getAllPlayers()) {
            Player p = player.player;

            if (ScoreHelper.hasScore(p)) {
                ScoreHelper.removeScore(p);
            }

            SimpleConfig messagesConfig = plugin.messagesConfig;
            Placeholders.loadPlaceholders(player.arena, player);

            ScoreHelper helper = ScoreHelper.createScore(p);
            helper.setTitle(Placeholders.translate(messagesConfig.getString("scoreboard.lobby.title")));
            List<?> lines = messagesConfig.getList("scoreboard.lobby.lines");

            Collections.reverse(lines);
            helper.setSlot(lines.size() + 1, "&7&m---------------------");
            for(int i = lines.size() - 1; i >= 0; i--) {
                String line = (String) lines.get(i);
                if (line.equalsIgnoreCase("")) {
                    helper.setSlot(i + 1, ChatColor.RESET.toString());
                    continue;
                }
                helper.setSlot(i + 1, Placeholders.translate(line));
            }
        }
    }

    public void gameMenu() {
        for (DodgePlayer player : arena.getAllPlayers()) {
            Player p = player.player;

            if (ScoreHelper.hasScore(p)) {
                ScoreHelper.removeScore(p);
            }

            SimpleConfig messagesConfig = plugin.messagesConfig;
            Placeholders.loadPlaceholders(player.arena, player);

            ScoreHelper helper = ScoreHelper.createScore(p);
            helper.setTitle(Placeholders.translate(messagesConfig.getString("scoreboard.ingame.title")));
            List<?> lines = messagesConfig.getList("scoreboard.ingame.lines");

            Collections.reverse(lines);
            helper.setSlot(lines.size() + 1, "&7&m---------------------");
            for(int i = lines.size() - 1; i >= 0; i--) {
                String line = (String) lines.get(i);
                if (line.equalsIgnoreCase("")) {
                    helper.setSlot(i + 1, ChatColor.RESET.toString());
                    continue;
                }
                helper.setSlot(i + 1, Placeholders.translate(line));
            }
        }
    }
}
