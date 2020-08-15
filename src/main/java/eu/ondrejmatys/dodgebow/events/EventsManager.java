package eu.ondrejmatys.dodgebow.events;

import eu.ondrejmatys.dodgebow.DodgeBow;
import eu.ondrejmatys.dodgebow.messages.Message;
import eu.ondrejmatys.dodgebow.players.DodgePlayer;
import eu.ondrejmatys.dodgebow.players.PlayerManager;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventsManager extends Message implements Listener {

    private DodgeBow plugin = DodgeBow.getInstance();

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.gamePlayers.containsKey(event.getPlayer())) {
            PlayerManager.leavePlayerFromGame(plugin.gamePlayers.get(event.getPlayer()));
        }
    }

    @EventHandler
    public void onBowHit(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Arrow) {
            Arrow hitArrow = (Arrow) event.getDamager();
            if (hitArrow.getShooter() instanceof Player) {
                if (plugin.gamePlayers.containsKey(hitArrow.getShooter()) && plugin.gamePlayers.containsKey(event.getEntity())) {
                    DodgePlayer killer = plugin.gamePlayers.get(hitArrow.getShooter());
                    DodgePlayer killed = plugin.gamePlayers.get(event.getEntity());

                    if (!killer.arena.immortality) {
                        killed.playerKilled(killer);
                        killer.playerKiller(killed);
                    }

                    event.setCancelled(true);
                }
            }
        } else {
            if (plugin.gamePlayers.containsKey(event.getDamager()) && plugin.gamePlayers.containsKey(event.getEntity())) {
                event.setCancelled(true);
            }
        }

        if (event.getDamager() instanceof Firework && event.getEntity() instanceof Player) {
            if (plugin.gamePlayers.containsKey(event.getEntity())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (plugin.gamePlayers.containsKey(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onHealthRegen(EntityRegainHealthEvent event) {
        if (plugin.gamePlayers.containsKey(event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDeathDrop(PlayerDeathEvent event) {
        if (plugin.gamePlayers.containsKey(event.getEntity())) {
            event.getDrops().clear();
        }
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        if (plugin.gamePlayers.containsKey(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (plugin.gamePlayers.containsKey(event.getPlayer())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (plugin.gamePlayers.containsKey(event.getWhoClicked())) {
            event.setCancelled(true);
        }
    }
}
