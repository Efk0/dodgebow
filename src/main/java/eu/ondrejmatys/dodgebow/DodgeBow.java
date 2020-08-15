package eu.ondrejmatys.dodgebow;

import eu.ondrejmatys.dodgebow.arena.Arena;
import eu.ondrejmatys.dodgebow.arena.LoadArenas;
import eu.ondrejmatys.dodgebow.commands.AdminCommands;
import eu.ondrejmatys.dodgebow.commands.BaseCommand;
import eu.ondrejmatys.dodgebow.config.ConfigManager;
import eu.ondrejmatys.dodgebow.events.EventsManager;
import eu.ondrejmatys.dodgebow.messages.Message;
import eu.ondrejmatys.dodgebow.players.DodgePlayer;
import eu.ondrejmatys.dodgebow.players.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;

public final class DodgeBow extends JavaPlugin {

    public static DodgeBow instance;

    public ConfigManager configManager;
    public ArrayList<Arena> arenas = new ArrayList<Arena>();
    public HashMap<Player, DodgePlayer> gamePlayers = new HashMap<Player, DodgePlayer>();

    @Override
    public void onEnable() {
        instance = this;
        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&eDodgebow &7>> &aSuccessfully started"));
        registerEvents();
        registerCommands();
        registerConfigs();
        LoadArenas.LoadArenas();

        configManager = ConfigManager.getInstance();
    }

    @Override
    public void onDisable() {
        for (DodgePlayer player : gamePlayers.values()) {
            PlayerManager.leavePlayerFromGame(player);
        }
        getLogger().info(ChatColor.translateAlternateColorCodes('&', "&eDodgebow &7>> &aSuccessfully unloaded"));
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new EventsManager(), this);
    }

    private void registerCommands() {
        getCommand("dodgebow").setExecutor(new BaseCommand());
        getCommand("dodgeadmin").setExecutor(new AdminCommands());
    }

    private void registerConfigs() {
        ConfigManager.getInstance().setPlugin(this);
    }

    public static DodgeBow getInstance() {
        return instance;
    }
}
