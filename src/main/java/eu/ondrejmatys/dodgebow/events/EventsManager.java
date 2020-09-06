package eu.ondrejmatys.dodgebow.events;

import eu.ondrejmatys.dodgebow.DodgeBow;
import eu.ondrejmatys.dodgebow.arena.Arena;
import eu.ondrejmatys.dodgebow.arena.Status;
import eu.ondrejmatys.dodgebow.messages.Message;
import eu.ondrejmatys.dodgebow.players.DodgePlayer;
import eu.ondrejmatys.dodgebow.players.DodgeStats;
import eu.ondrejmatys.dodgebow.players.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;

public class EventsManager extends Message implements Listener {

    private DodgeBow plugin = DodgeBow.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.mainConfig.getBoolean("bungee.enabled")) {
            Arena arena = plugin.arenas.get(0);
            if (arena.players.size() >= arena.maxPlayers) {
                event.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', plugin.messagesConfig.getString("arena.arenafull")));
                return;
            }

            if (arena.status == Status.INGAME) {
                event.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', plugin.messagesConfig.getString("errors.arenaingame")));
                return;
            }

            if (plugin.arenas.get(0).status == Status.IDLE) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        event.getPlayer().teleport(arena.lobbyLoc);
                        PlayerManager.joinPlayerToGame(event.getPlayer(), arena);
                    }
                }.runTaskLater(plugin, 20L);
                return;
            }

            event.getPlayer().kickPlayer(ChatColor.translateAlternateColorCodes('&', plugin.messagesConfig.getString("errors.cannotjoin")));
        }

        plugin.playerXStats.put(event.getPlayer(), new DodgeStats(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (plugin.gamePlayers.containsKey(event.getPlayer())) {
            PlayerManager.leavePlayerFromGame(plugin.gamePlayers.get(event.getPlayer()));

            if (plugin.mainConfig.getBoolean("bungee.enabled")) {
                try {
                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(b);
                    try {
                        out.writeUTF("Connect");
                        out.writeUTF(plugin.mainConfig.getString("bungee.lobby"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    event.getPlayer().sendPluginMessage(plugin, "BungeeCord", b.toByteArray());
                } catch (org.bukkit.plugin.messaging.ChannelNotRegisteredException e) {
                    plugin.getLogger().warning(" ERROR - Usage of bungeecord connect effects is not possible. Your server is not having bungeecord support (Bungeecord channel is not registered in your minecraft server)!");
                }
            }
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
            List<?> allowedCmds = plugin.mainConfig.getList("allowedCommands");
            if (!allowedCmds.contains(event.getMessage())) {
                Message("messages.yml", "errors.cannotingame", event.getPlayer());
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (plugin.gamePlayers.containsKey(event.getWhoClicked())) {
            event.setCancelled(true);
        }
    }
}
