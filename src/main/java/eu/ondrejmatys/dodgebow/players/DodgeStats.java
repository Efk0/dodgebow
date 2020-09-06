package eu.ondrejmatys.dodgebow.players;

import eu.ondrejmatys.dodgebow.DodgeBow;
import eu.ondrejmatys.dodgebow.stats.PlayerStats;
import org.bukkit.entity.Player;

public class DodgeStats {

    private PlayerStats stats = DodgeBow.getInstance().statsManager;
    private Player player;

    private int kills = 0;
    private int wins = 0;
    private int games_played = 0;
    private int deaths = 0;

    public DodgeStats(Player player) {
        this.player = player;

        kills = (int) stats.getValue(player.getName(), "kills");
        wins = (int) stats.getValue(player.getName(), "wins");
        games_played = (int) stats.getValue(player.getName(), "games_played");
        deaths = (int) stats.getValue(player.getName(), "deaths");
    }

    public int getKills() {
        return kills;
    }
    public int getWins() {
        return wins;
    }
    public int getGames_played() {
        return games_played;
    }
    public int getDeaths() {
        return deaths;
    }

    public void setKills(int kills) {
        this.kills = kills;
        refreshStatsStorage();
    }
    public void setWins(int wins) {
        this.wins = wins;
        refreshStatsStorage();
    }
    public void setGames_played(int games_played) {
        this.games_played = games_played;
        refreshStatsStorage();
    }
    public void setDeaths(int deaths) {
        this.deaths = deaths;
        refreshStatsStorage();
    }

    private void refreshStatsStorage() {
        stats.setValue(player.getName(), "kills", String.valueOf(kills));
        stats.setValue(player.getName(), "wins", String.valueOf(wins));
        stats.setValue(player.getName(), "games_played", String.valueOf(games_played));
        stats.setValue(player.getName(), "deaths", String.valueOf(deaths));
    }
}
