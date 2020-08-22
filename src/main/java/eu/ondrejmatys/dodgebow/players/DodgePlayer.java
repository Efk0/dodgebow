package eu.ondrejmatys.dodgebow.players;

import eu.ondrejmatys.dodgebow.DodgeBow;
import eu.ondrejmatys.dodgebow.arena.Arena;
import eu.ondrejmatys.dodgebow.messages.Message;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import java.util.Random;

public class DodgePlayer extends Message {

    public Arena arena;
    public Player player;
    public StoredInventory inventory = new StoredInventory();
    public int lives = 3;

    private DodgeBow plugin = DodgeBow.getInstance();

    public DodgePlayer(Arena arena, Player player) {
        this.arena = arena;
        this.player = player;
    }

    public void setLobbyInventory() {
        // @TODO Lobby inventory for players
    }

    public void playerKilled(DodgePlayer killer) {
        lives--;
        player.teleport(arena.spawnLocations.get(new Random().nextInt(arena.spawnLocations.size())));
        if (lives < 1) {
            arena.playerToSpectator(this);
            return;
        }
        player.setHealth(lives * 2);
        arena.refreshScoreboard();
        alertDeath(killer.player);
    }

    public void playerKiller(DodgePlayer killed) {
    }

    private void alertDeath(Player killer) {
        String decodedMessage = String.format(plugin.messagesConfig.getString("arena.kill"), killer.getName(), player.getName(), lives);
        for (DodgePlayer player : arena.getAllPlayers()) {
            Message(decodedMessage, player.player);
            player.player.playSound(player.player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1F, 0.5F);
        }
    }

    public void storeInventory() {
        inventory.inventory = player.getInventory().getContents();
        inventory.armor = player.getInventory().getArmorContents();
        inventory.xp = player.getExp();
        inventory.effects = player.getActivePotionEffects();
        inventory.mode = player.getGameMode();
        inventory.leftLocation = player.getLocation();
        inventory.level = player.getLevel();
        inventory.listName = player.getPlayerListName();
        inventory.displayName = player.getDisplayName();
        inventory.foodLevel = player.getFoodLevel();
        inventory.healthLevel = player.getHealth();
    }

    public void restoreInventory() {
        player.getInventory().setContents(inventory.inventory);
        player.getInventory().setArmorContents(inventory.armor);
        player.setExp(inventory.xp);
        player.addPotionEffects(inventory.effects);
        player.setGameMode(inventory.mode);
        player.teleport(inventory.leftLocation);
        player.setLevel(inventory.level);
        player.setPlayerListName(inventory.listName);
        player.setDisplayName(inventory.displayName);
        player.setFoodLevel(inventory.foodLevel);
        player.setHealth(inventory.healthLevel);
    }
}
