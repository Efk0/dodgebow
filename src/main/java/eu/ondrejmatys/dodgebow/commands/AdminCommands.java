package eu.ondrejmatys.dodgebow.commands;

import eu.ondrejmatys.dodgebow.DodgeBow;
import eu.ondrejmatys.dodgebow.arena.ArenaCreator;
import eu.ondrejmatys.dodgebow.config.ConfigManager;
import eu.ondrejmatys.dodgebow.menus.AdminMenu;
import eu.ondrejmatys.dodgebow.messages.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class AdminCommands extends Message implements CommandExecutor {

    private DodgeBow plugin = DodgeBow.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("dodgeadmin")) {
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("reload")) {
                    DodgeBow.getInstance().configManager = ConfigManager.getInstance();
                    Message("messages", "reload", (Player) sender);

                    return false;
                }

                if (args[0].equalsIgnoreCase("arena")) {
                    if (sender instanceof Player) {
                        if (args.length > 1) {
                            ArenaCreator.cmd(args[1], Arrays.copyOfRange(args, 2, args.length), (Player) sender);
                            return false;
                        }
                    }
                }

                if (args[0].equalsIgnoreCase("forcestart")) {
                    if (sender instanceof Player) {
                        if (plugin.gamePlayers.containsKey((Player) sender)) {
                            if (plugin.gamePlayers.get((Player) sender).arena != null) {
                                //plugin.gamePlayers.get((Player) sender).arena.forceStart();
                                return false;
                            }
                            Message("messages", "errors.notingame", (Player) sender);
                            return false;
                        }
                        Message("messages", "errors.notingame", (Player) sender);
                        return false;
                    }
                }
            }

            AdminMenu.AdminMenu((Player) sender);

            return false;
        }

        return false;
    }
}
