package eu.ondrejmatys.dodgebow.scoreboards;

import eu.ondrejmatys.dodgebow.arena.Arena;
import eu.ondrejmatys.dodgebow.players.DodgePlayer;

import java.util.HashMap;
import java.util.Map;

public class Placeholders {

    private static HashMap<String, String> placeholders = new HashMap<String, String>();

    public static String translate(String text) {
        for(Map.Entry<String, String> placeholder : placeholders.entrySet()) {
            text = text.replace("%" + placeholder.getKey() + "%", placeholder.getValue());
        }
        return text;
    }

    public static void loadPlaceholders(Arena arena, DodgePlayer player) {
        placeholders.put("countdown", String.valueOf(arena.countdown));
        placeholders.put("count", String.valueOf(arena.getAllPlayers().size()));
        placeholders.put("max", String.valueOf(arena.maxPlayers));
        placeholders.put("arena", arena.name);
        if (player != null) {
            placeholders.put("lives", String.valueOf(player.lives));
        }
    }
}
