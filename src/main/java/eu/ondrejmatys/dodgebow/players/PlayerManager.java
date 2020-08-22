package eu.ondrejmatys.dodgebow.players;

import eu.ondrejmatys.dodgebow.DodgeBow;
import eu.ondrejmatys.dodgebow.arena.Arena;
import eu.ondrejmatys.dodgebow.arena.Status;
import eu.ondrejmatys.dodgebow.config.SimpleConfig;
import eu.ondrejmatys.dodgebow.messages.Message;
import eu.ondrejmatys.dodgebow.scoreboards.ScoreHelper;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

public class PlayerManager extends Message {

    public static void joinPlayerToGame(Player player, Arena arena) {
        DodgeBow plugin = DodgeBow.getInstance();
        if (plugin.gamePlayers.containsKey(player)) {
            Message("messages", "errors.ingame", player);
            return;
        }

        if (arena.status == Status.INGAME) {
            Message("messages", "errors.arenaingame", player);
            return;
        }

        if (arena.status == Status.UNDONE) {
            Message("messages", "errors.cannotjoin", player);
            return;
        }

        if (arena.players.size() > arena.maxPlayers) {
            Message("messages", "arena.arenafull", player);
            return;
        }

        DodgePlayer dodgePlayer = new DodgePlayer(arena, player);
        dodgePlayer.storeInventory();
        dodgePlayer.setLobbyInventory();
        arena.players.add(dodgePlayer);
        arena.alertJoin(player);

        plugin.gamePlayers.put(player, dodgePlayer);

        player.getInventory().clear();
        player.setGameMode(GameMode.ADVENTURE);
        player.setHealthScale(20);
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setExp(0);
        player.setLevel(0);

        player.teleport(arena.lobbyLoc);
    }

    public static void leavePlayerFromGame(DodgePlayer player) {
        DodgeBow plugin = DodgeBow.getInstance();
        plugin.getLogger().info("Odpojuji hráče " + player.player.getName());
        if (player == null) {
            return;
        }

        if (!plugin.gamePlayers.containsKey(player.player)) {
            Message("messages", "errors.notingame", player.player);
            return;
        }

        if (player.arena == null) {
            Message("messages", "errors.notingame", player.player);
            return;
        }

        if (ScoreHelper.hasScore(player.player)) {
            ScoreHelper.removeScore(player.player);
        }

        player.restoreInventory();
        player.player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
        player.arena.players.remove(player);
        player.arena.spectors.remove(player);
        player.arena.alertLeave(player.player);
        plugin.gamePlayers.remove(player.player);
    }
}
