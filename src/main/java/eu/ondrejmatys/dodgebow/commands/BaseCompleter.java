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

public class BaseCompleter implements TabCompleter {

    private DodgeBow plugin = DodgeBow.getInstance();

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> hints = new ArrayList<String>();
        if (command.getName().equalsIgnoreCase("dodgebow")) {
            if (args.length <= 1) {
                hints.add("join");
                hints.add("leave");
                hints.add("list");
                return hints;
            }
            if (args[0].equalsIgnoreCase("join")) {
                if (args.length <= 2) {
                    for(Arena arena : plugin.arenas) {
                        hints.add(arena.name);
                    }
                    return hints;
                }
                return hints;
            }
            return hints;
        }
        return hints;
    }

}
