package eu.ondrejmatys.dodgebow.arena;

import eu.ondrejmatys.dodgebow.DodgeBow;
import eu.ondrejmatys.dodgebow.config.ConfigManager;
import eu.ondrejmatys.dodgebow.messages.Message;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;


public class ArenaCreator extends Message {

    public static void cmd(String command, String[] args, Player sender) {

        if (findArenaWithName(args[0]) == null && !command.equalsIgnoreCase("create")) {
            Message("messages", "errors.arenanotexists", sender);
            return;
        }

        if (findArenaWithName(args[0]) != null && findArenaWithName(args[0]).status != Status.UNDONE && !command.equalsIgnoreCase("edit")) {
            Message("messages", "errors.cannotedit", sender);
            return;
        }

        if (command.equalsIgnoreCase("create")) {
            createNewArena(args[0], sender);
        }

        if (command.equalsIgnoreCase("min")) {
            Arena arena = findArenaWithName(args[0]);
            setMinPlayers(Integer.parseInt(args[1]), arena, sender);
        }

        if (command.equalsIgnoreCase("max")) {
            Arena arena = findArenaWithName(args[0]);
            setMaxPlayers(Integer.parseInt(args[1]), arena, sender);
        }

        if (command.equalsIgnoreCase("spawnpoint")) {
            Arena arena = findArenaWithName(args[0]);
            if (args.length > 1 && args[1].equalsIgnoreCase("reset")) {
                resetSpawnPoints(arena, sender);
                return;
            }
            addSpawnPoint(arena, sender);
        }

        if (command.equalsIgnoreCase("lobby")) {
            Arena arena = findArenaWithName(args[0]);
            setLobby(arena, sender);
        }

        if (command.equalsIgnoreCase("remove")) {
            Arena arena = findArenaWithName(args[0]);
            arenaRemove(arena, sender);
        }

        if (command.equalsIgnoreCase("save")) {
            Arena arena = findArenaWithName(args[0]);
            arenaSave(arena, sender);
        }

        if (command.equalsIgnoreCase("edit")) {
            Arena arena = findArenaWithName(args[0]);
            arena.editArena();
        }
    }

    private static void createNewArena(String name, Player sender) {
        DodgeBow plugin = DodgeBow.getInstance();
        if (findArenaWithName(name) != null) {
            Message("messages", "errors.arenaalreadyexists", sender);
            return;
        }

        Arena newArena = new Arena();
        newArena.name = name;
        String decoded = String.format(ConfigManager.getInstance().getString("messages", "creation.create"), name);
        Message(decoded, sender);
    }

    private static void setMinPlayers(int minPlayers, Arena arena, Player sender) {
        if (arena == null) {
            return;
        }
        arena.minPlayers = minPlayers;
        String decoded = String.format(ConfigManager.getInstance().getString("messages", "creation.newmin"), minPlayers);
        Message(decoded, sender);
    }

    private static void setMaxPlayers(int maxPlayers, Arena arena, Player sender) {
        if (arena == null) {
            return;
        }
        arena.maxPlayers = maxPlayers;
        String decoded = String.format(ConfigManager.getInstance().getString("messages", "creation.newmax"), maxPlayers);
        Message(decoded, sender);
    }

    private static void setLobbyLocation(Arena arena, Player sender) {
        arena.lobbyLoc = sender.getLocation();
    }

    private static void addSpawnPoint(Arena arena, Player sender) {
        if (arena.spawnLocations.size() >= arena.maxPlayers) {
            Message("messages", "errors.spawnpointsfull", sender);
            return;
        }
        arena.spawnLocations.add(sender.getLocation());
        String decoded = String.format(ConfigManager.getInstance().getString("messages", "creation.newspawn"), arena.spawnLocations.size());
        Message(decoded, sender);
    }

    private static void resetSpawnPoints(Arena arena, Player sender) {
        arena.spawnLocations = new ArrayList<Location>();
        Message("messages", "cration.resetspawn", sender);
    }

    private static void setLobby(Arena arena, Player sender) {
        arena.lobbyLoc = sender.getLocation();
        Message("messages", "cration.lobby", sender);
    }

    private static void arenaRemove(Arena arena, Player sender) {
        arena.removeArena(sender);
        Message("messages", "cration.remove", sender);
    }

    private static void arenaSave(Arena arena, Player sender) {
        arena.saveArena(sender);
    }

    public static Arena findArenaWithName(String name) {
        DodgeBow plugin = DodgeBow.getInstance();
        for (Arena arena : plugin.arenas) {
            if (arena.name.equalsIgnoreCase(name)) {
                return arena;
            }
        }
        return null;
    }

}
