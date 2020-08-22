package eu.ondrejmatys.dodgebow.arena;

import eu.ondrejmatys.dodgebow.DodgeBow;
import eu.ondrejmatys.dodgebow.messages.Message;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class LoadArenas extends Message {

    private static DodgeBow plugin = DodgeBow.getInstance();

    public static void LoadArenas() {
        FileConfiguration config = new YamlConfiguration();
        if (!new File(plugin.getDataFolder() + "\\arenas").exists()) {
            return;
        }
        File[] arenas = new File(plugin.getDataFolder() + "\\arenas").listFiles();
        for (File arena : arenas) {
            try {
                config.load(arena);
                Arena newArena = new Arena();
                newArena.name = config.getString("name");
                newArena.status = Status.valueOf(config.getString("status"));
                newArena.minPlayers = config.getInt("min");
                newArena.maxPlayers = config.getInt("max");
                newArena.lobbyLoc = (Location) config.get("lobby");
                newArena.spawnLocations = (ArrayList<Location>) config.get("spawnpoints");

                newArena.initScoreboard();

                plugin.getLogger().info(
                        ChatColor.translateAlternateColorCodes('&', String.format("&aArena &e%s &abyla úspěšně načtena!", newArena.name))
                );
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InvalidConfigurationException ex) {
                ex.printStackTrace();
            }
        }
    }
}
