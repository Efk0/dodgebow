package eu.ondrejmatys.dodgebow.scoreboards;

import eu.ondrejmatys.dodgebow.arena.Arena;
import eu.ondrejmatys.dodgebow.players.DodgePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.ArrayList;

public class ArenaScoreboard {


    private Arena arena;

    public ArenaScoreboard(Arena arena) {
        this.arena = arena;

    }

    public void lobbyMenu() {
        for (DodgePlayer player : arena.getAllPlayers()) {
            Player p = player.player;

            if (ScoreHelper.hasScore(p)) {
                ScoreHelper.removeScore(p);
            }

            ScoreHelper helper = ScoreHelper.createScore(p);
            helper.setTitle("&6&lDODGEBOW");
            helper.setSlot(1, "&aOdpočet: &r" + arena.countdown);
            helper.setSlot(3, "&aHráči: &r" + arena.getAllPlayers().size() + "/" + arena.maxPlayers);
            helper.setSlot(5, "&aMapa: &r" + arena.name);
            helper.setSlot(7, "&7&m--------------------");
        }
    }

    public void gameMenu() {
        for (DodgePlayer player : arena.getAllPlayers()) {
            Player p = player.player;

            if (ScoreHelper.hasScore(p)) {
                ScoreHelper.removeScore(p);
            }

            ScoreHelper helper = ScoreHelper.createScore(p);
            helper.setTitle("&6&lDODGEBOW");
            helper.setSlot(1, "&aŽivoty: &r" + player.lives);
            helper.setSlot(3, "&aHráčů: &r" + arena.players.size() + "/" + arena.maxPlayers);
            helper.setSlot(5, "&aAréna: &r" + arena.name);
            helper.setSlot(7, "&7&m--------------------");
        }
    }
}
