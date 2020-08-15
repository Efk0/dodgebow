package eu.ondrejmatys.dodgebow.commands;

import eu.ondrejmatys.dodgebow.DodgeBow;
import eu.ondrejmatys.dodgebow.arena.Arena;
import eu.ondrejmatys.dodgebow.arena.ArenaCreator;
import eu.ondrejmatys.dodgebow.players.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AdminCompleter implements TabCompleter {

    private DodgeBow plugin = DodgeBow.getInstance();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> hints = new ArrayList<String>();
        if (command.getName().equalsIgnoreCase("dodgeadmin")) {
            if (args.length <= 1) {
                hints.add("reload");
                hints.add("forcestart");
                hints.add("arena");
                return hints;
            }
            if (args[0].equalsIgnoreCase("arena")) {
                if (args.length <= 2) {
                    hints.add("create");
                    hints.add("min");
                    hints.add("max");
                    hints.add("spawnpoint");
                    hints.add("remove");
                    return hints;
                }
                if (args[1].equalsIgnoreCase("create")) {
                    return hints;
                }

                if (args[1].equalsIgnoreCase("min")) {
                    if (args.length <= 3) {
                        for(Arena arena : plugin.arenas) {
                            hints.add(arena.name);
                        }
                        return hints;
                    }
                }

                if (args[1].equalsIgnoreCase("max")) {
                    if (args.length <= 3) {
                        for(Arena arena : plugin.arenas) {
                            hints.add(arena.name);
                        }
                        return hints;
                    }
                }

                if (args[1].equalsIgnoreCase("spawnpoint")) {
                    if (args.length <= 3) {
                        for(Arena arena : plugin.arenas) {
                            hints.add(arena.name);
                        }
                        return hints;
                    }
                    if (args.length <= 4) {
                        hints.add("reset");
                        return hints;
                    }

                }

                if (args[1].equalsIgnoreCase("remove")) {
                    if (args.length <= 3) {
                        for(Arena arena : plugin.arenas) {
                            hints.add(arena.name);
                        }
                        return hints;
                    }
                }

                return hints;
            }
            return hints;
        }
        return hints;
    }

}
