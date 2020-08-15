package eu.ondrejmatys.dodgebow.commands;

import eu.ondrejmatys.dodgebow.DodgeBow;
import eu.ondrejmatys.dodgebow.arena.Arena;
import eu.ondrejmatys.dodgebow.arena.ArenaCreator;
import eu.ondrejmatys.dodgebow.messages.Message;
import eu.ondrejmatys.dodgebow.players.PlayerManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BaseCommand extends Message implements CommandExecutor {

    private DodgeBow plugin = DodgeBow.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("dodgebow")) {
            if (args.length > 0) {
                if (sender instanceof Player) {
                    if (args[0].equalsIgnoreCase("list")) {
                        int inGame = 0;
                        int idle = 0;
                        int undone = 0;
                        String arenasList = "";
                        for (Arena arena : plugin.arenas) {
                            arenasList += "&a" + arena.name + "&7 - ";
                            String status = "";
                            switch (arena.status) {
                                case IDLE:
                                    idle++;
                                    status = "&aČEKÁNÍ";
                                    break;
                                case STARTING:
                                    idle++;
                                    status = "&eSTARTUJE";
                                    break;
                                case INGAME:
                                    status = "&cVE HŘE";
                                    inGame++;
                                    break;
                                case UNDONE:
                                    undone++;
                                    status = "&7NEFUNGUJE";
                                    break;
                            }
                            arenasList += status + "\n";
                        }
                        String arenasCounter = String.format("&a%s&f/&c%s&f/&7%s", idle, inGame, undone);
                        Message("&aArény: " + arenasCounter, (Player) sender);
                        Message(arenasList, (Player) sender);
                        return false;
                    }

                    if (args[0].equalsIgnoreCase("join")) {
                        if (args.length > 1) {
                            Arena arena = ArenaCreator.findArenaWithName(args[1]);
                            if (arena == null) {
                                Message("messages", "errors.arenanotexists", (Player) sender);
                                return false;
                            }

                            PlayerManager.joinPlayerToGame((Player) sender, arena);
                        }
                    }

                    if (args[0].equalsIgnoreCase("leave")) {
                        PlayerManager.leavePlayerFromGame(plugin.gamePlayers.get((Player) sender));
                    }

                    return false;
                }

                return false;
            }
            Message("&aVersion: &e" + DodgeBow.getInstance().getDescription().getVersion(), (Player) sender);
            Message("&aAuthor: &e" + DodgeBow.getInstance().getDescription().getAuthors(), (Player) sender);
            Message("&aWebsite: &e" + DodgeBow.getInstance().getDescription().getWebsite(), (Player) sender);
            return false;
        }

        return false;
    }
}
