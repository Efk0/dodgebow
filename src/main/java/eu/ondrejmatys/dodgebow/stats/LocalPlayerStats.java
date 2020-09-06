package eu.ondrejmatys.dodgebow.stats;

import eu.ondrejmatys.dodgebow.DodgeBow;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.omg.CORBA.DynAnyPackage.Invalid;

import java.io.File;
import java.io.IOException;

public class LocalPlayerStats implements PlayerStats{

    private DodgeBow plugin = DodgeBow.getInstance();

    @Override
    public Object getValue(String playerName, String valueName) {
        File statsFolder = new File(plugin.getDataFolder() + "\\stats");
        File statsConfig = new File(statsFolder.getPath() + "\\" + playerName + ".yml");
        YamlConfiguration config = new YamlConfiguration();
        Player player = Bukkit.getPlayer(playerName);
        try {
            if (!statsFolder.exists()) {
                statsFolder.mkdir();
            }

            if (!statsConfig.exists()) {
                return 0;
            }

            config.load(statsConfig);

            return config.getInt(valueName);

        } catch (IOException ex) {
            ex.printStackTrace();;
        } catch (InvalidConfigurationException ex) {
            ex.printStackTrace();
        }

        return 0;
    }

    @Override
    public void setValue(String playerName, String valueName, String value) {
        File statsFolder = new File(plugin.getDataFolder() + "\\stats");
        File statsConfig = new File(statsFolder.getPath() + "\\" + playerName + ".yml");
        YamlConfiguration config = new YamlConfiguration();
        Player player = Bukkit.getPlayer(playerName);
        try {
            if (!statsFolder.exists()) {
                statsFolder.mkdir();
            }

            if (!statsConfig.exists()) {
                statsConfig.createNewFile();
            }

            config.load(statsConfig);

            config.set(valueName, value);

            config.save(statsConfig);

        } catch (IOException ex) {
            ex.printStackTrace();;
        } catch (InvalidConfigurationException ex) {
            ex.printStackTrace();
        }
    }
}
