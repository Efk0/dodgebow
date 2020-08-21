package eu.ondrejmatys.dodgebow.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;


public class ConfigManager {
    private static ConfigManager single_inst = null;

    private ArrayList<FileConfiguration> configs = new ArrayList<FileConfiguration>();
    private String[] configNames = {"config.yml", "messages.yml"};
    private Plugin plugin = null;

    public void setPlugin(Plugin plugin) {
        this.plugin = plugin;
    }

    public FileConfiguration getConfig(String name){
        if (configs.size() > 0) {
            for (FileConfiguration conf : configs) {
                if (conf.getName().equalsIgnoreCase(name)) {
                    return conf;
                }
            }
        }

        return null;
    }

    public void checkAndLoadConfigs() {
        File dir = new File(String.valueOf(plugin.getDataFolder()));
        if (!dir.exists()) {
            dir.mkdir();
        }

        for (String name : configNames) {
            FileConfiguration newConfig;
            FileConfiguration oldConfig;
            File file = new File(plugin.getDataFolder(), name);
            if (!file.exists()) {
                plugin.saveResource(name, false);
                oldConfig = YamlConfiguration.loadConfiguration(file);
            } else {
                Reader newConfigStream;
                try {
                    newConfigStream = new InputStreamReader(plugin.getResource(name), "UTF8");
                } catch (UnsupportedEncodingException ex) {
                    plugin.getPluginLoader().disablePlugin(plugin);
                    return;
                }
                newConfig = YamlConfiguration.loadConfiguration(newConfigStream);
                oldConfig = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), name));
                for (String key : newConfig.getKeys(true)) {
                    if (oldConfig.get(key) != null) {
                        newConfig.set(key, oldConfig.get(key));
                    }
                }

                try {
                    newConfig.set("Moje", "Tvoje");
                    newConfig.save(name);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            configs.add(oldConfig);
        }
    }

    public String getString(FileConfiguration conf, String path){

        return conf.getString(path);
    }

    public String getString(String config, String path) {
        FileConfiguration conf = getConfig(config + ".yml");

        return conf.getString(path);
    }

    public int getInt(FileConfiguration conf, String path){

        return conf.getInt(path);
    }

    public double getDouble(FileConfiguration conf, String path){

        return conf.getDouble(path);
    }

    public boolean getBoolean(FileConfiguration conf, String path){

        return conf.getBoolean(path);
    }

    public List<?> getList(FileConfiguration conf, String path){

        return conf.getList(path);
    }

    public Location getLocation(FileConfiguration conf, String path){
        String worldName = getString(conf, String.format("%s.world", path));
        Bukkit.getServer().createWorld(new WorldCreator(worldName));

        World world = Bukkit.getWorld(worldName);
        int x = getInt(conf, String.format("%s.x", path));
        int y = getInt(conf, String.format("%s.y", path));
        int z = getInt(conf, String.format("%s.z", path));

        return new Location(world, x, y, z);
    }

    private ConfigManager() {
    }

    public static ConfigManager getInstance() {
        if (single_inst == null) {
            single_inst = new ConfigManager();
        }
        return single_inst;
    }
}
