package eu.ondrejmatys.dodgebow;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import eu.ondrejmatys.dodgebow.arena.Arena;
import eu.ondrejmatys.dodgebow.arena.LoadArenas;
import eu.ondrejmatys.dodgebow.commands.AdminCommands;
import eu.ondrejmatys.dodgebow.commands.AdminCompleter;
import eu.ondrejmatys.dodgebow.commands.BaseCommand;
import eu.ondrejmatys.dodgebow.commands.BaseCompleter;
import eu.ondrejmatys.dodgebow.config.SimpleConfig;
import eu.ondrejmatys.dodgebow.config.SimpleConfigManager;
import eu.ondrejmatys.dodgebow.config.configs.ConfigConfig;
import eu.ondrejmatys.dodgebow.config.configs.MessagesConfig;
import eu.ondrejmatys.dodgebow.events.EventsManager;
import eu.ondrejmatys.dodgebow.players.DodgePlayer;
import eu.ondrejmatys.dodgebow.players.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.HashMap;

public final class DodgeBow extends JavaPlugin implements PluginMessageListener {

    public static DodgeBow instance;

    public SimpleConfigManager configManager = new SimpleConfigManager(this);

    public SimpleConfig mainConfig = configManager.getNewConfig("config.yml");
    public SimpleConfig messagesConfig = configManager.getNewConfig("messages.yml");

    public ArrayList<Arena> arenas = new ArrayList<Arena>();
    public HashMap<Player, DodgePlayer> gamePlayers = new HashMap<Player, DodgePlayer>();

    @Override
    public void onEnable() {
        instance = this;
        System.out.println(ChatColor.translateAlternateColorCodes('&', "&eDodgebow &7>> &aSuccessfully started"));
        registerConfigs();
        registerEvents();
        registerCommands();
        LoadArenas.LoadArenas();
    }

    @Override
    public void onDisable() {
        for (DodgePlayer player : gamePlayers.values()) {
            PlayerManager.leavePlayerFromGame(player);
        }
        System.out.println(ChatColor.translateAlternateColorCodes('&', "&eDodgebow &7>> &aSuccessfully unloaded"));
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new EventsManager(), this);

        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
    }

    private void registerCommands() {
        getCommand("dodgebow").setExecutor(new BaseCommand());
        getCommand("dodgebow").setTabCompleter(new BaseCompleter());
        getCommand("dodgeadmin").setExecutor(new AdminCommands());
        getCommand("dodgeadmin").setTabCompleter(new AdminCompleter());
    }

    private void registerConfigs() {
        configManager = new SimpleConfigManager(this);

        ConfigConfig.InitConfig();
        MessagesConfig.InitConfig();
    }

    public static DodgeBow getInstance() {
        return instance;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subchannel = in.readUTF();
        if (subchannel.equals("SomeSubChannel")) {
            // Use the code sample in the 'Response' sections below to read
            // the data.
        }
    }
}
