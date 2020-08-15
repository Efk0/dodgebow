package eu.ondrejmatys.dodgebow.arena;

import eu.ondrejmatys.dodgebow.DodgeBow;
import eu.ondrejmatys.dodgebow.config.ConfigManager;
import eu.ondrejmatys.dodgebow.messages.Message;
import eu.ondrejmatys.dodgebow.players.DodgePlayer;
import eu.ondrejmatys.dodgebow.players.PlayerManager;
import eu.ondrejmatys.dodgebow.scoreboards.ArenaScoreboard;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Arena extends Message {

    public String name;
    public Status status = Status.UNDONE;
    public int minPlayers;
    public int maxPlayers;
    public Location lobbyLoc;
    public ArrayList<Location> spawnLocations = new ArrayList<Location>();
    public ArrayList<DodgePlayer> players = new ArrayList<DodgePlayer>();
    public ArrayList<DodgePlayer> spectors = new ArrayList<DodgePlayer>();
    public boolean immortality = false;
    public ArenaScoreboard scoreboard;
    private DodgePlayer winner = null;
    public int countdown = 30;

    private DodgeBow plugin = DodgeBow.getInstance();

    public Arena() {
        plugin.arenas.add(this);
    }

    public void initScoreboard() {
        scoreboard = new ArenaScoreboard(this);
    }

    public void changeStatus(Status status) {
        this.status = status;
    }

    public Status getStatus() {
        return this.status;
    }

    public void saveArena(Player sender) {
        File config = new File(plugin.getDataFolder() + "\\arenas", this.name + ".yml");
        File arenaDirectory = new File(plugin.getDataFolder() + "\\arenas");
        try {
            if (!arenaDirectory.exists()) {
                arenaDirectory.mkdir();
            }
            config.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        this.status = Status.IDLE;
        FileConfiguration settings = new YamlConfiguration();

        settings.set("name", this.name);
        settings.set("status", this.status.toString());
        settings.set("min", this.minPlayers);
        settings.set("max", this.maxPlayers);
        settings.set("lobby", this.lobbyLoc);
        settings.set("spawnpoints", this.spawnLocations);

        initScoreboard();

        try {
            settings.save(config);
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }

        Message("messages", "creation.save", sender);
    }

    public void removeArena(Player sender) {
        if (this.status == Status.INGAME) {
            Message("messages", "errors.arenaingame2", sender);
            return;
        }

        File config = new File(plugin.getDataFolder() + "\\arenas", this.name + ".yml");
        config.delete();

        for(DodgePlayer player : players) {
            PlayerManager.leavePlayerFromGame(player);
        }

        plugin.arenas.remove(this);
        Message("messages", "creation.remove", sender);
    }

    public void editArena() {
        this.status = Status.UNDONE;
        ArrayList<DodgePlayer> allPlayers = new ArrayList<DodgePlayer>();
        allPlayers.addAll(players);
        allPlayers.addAll(spectors);
        for(DodgePlayer player : allPlayers) {
            Message("message", "arena.arenashut", player.player);
            PlayerManager.leavePlayerFromGame(player);
        }
    }

    public void alertJoin(Player joined) {
        for(DodgePlayer player : players) {
            Player bukkitPlayer = player.player;
            if (bukkitPlayer == joined) {
                String messageDecoded = String.format(ConfigManager.getInstance().getString("messages", "arena.youjoin"), players.size(), maxPlayers);
                Message(messageDecoded, bukkitPlayer);
                continue;
            }
            String messageDecoded = String.format(ConfigManager.getInstance().getString("messages", "arena.join"), joined.getName(), players.size(), maxPlayers);
            Message(messageDecoded, bukkitPlayer);
        }
        checkPlayerCount();
    }

    public void alertLeave(Player left) {
        for(DodgePlayer player : players) {
            Player bukkitPlayer = player.player;

            String messageDecoded = String.format(ConfigManager.getInstance().getString("messages", "arena.leave"), left.getName(), players.size(), maxPlayers);
            Message(messageDecoded, bukkitPlayer);
        }
        String messageDecoded = String.format(ConfigManager.getInstance().getString("messages", "arena.youleave"), players.size(), maxPlayers);
        Message(messageDecoded, left);
        checkPlayerCount();

    }

    public void playerToSpectator(DodgePlayer player) {
        Player p = player.player;
        p.setAllowFlight(true);
        p.getInventory().clear();
        //@TODO Set spectator inventory

        p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 999999999, 1000));

        players.remove(player);
        spectors.add(player);

        refreshScoreboard();
        checkPlayerCount();
    }

    public void checkPlayerCount() {
        if (players.size() < 1) {
            if (this.status == Status.INGAME) {
                stopGame();
            }

            return;
        }

        if (players.size() < 2 && this.status == Status.INGAME) {
            winStatement(players.get(0));
            return;
        }

        refreshScoreboard();

        if (players.size() >= minPlayers) {
            if (this.status == Status.IDLE) {
                startCountDown();
            }
        } else {
            if (this.status == Status.STARTING) {
                for(DodgePlayer player : players) {
                    Message("messages", "arena.leaveDuringCountdown", player.player);
                }
                this.status = Status.IDLE;
            }
        }
    }

    private void startCountDown() {
        this.status = Status.STARTING;
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                if (getStatus() != Status.STARTING) {
                    countdown = 30;
                    this.cancel();
                    return;
                }

                if (countdown <= 0) {
                    countdown = 30;
                    startGame();
                    this.cancel();
                    return;
                }

                ArrayList<DodgePlayer> players1 = getAllPlayers();

                for(DodgePlayer player : players1) {
                    player.player.setLevel(countdown);
                }

                refreshScoreboard();

                if (countdown > 5) {
                    if (countdown % 10 == 0) {
                        String decoded = String.format(ConfigManager.getInstance().getString("messages", "arena.countdown"), countdown);
                        for(DodgePlayer player : players1) {
                            Message(decoded, player.player);
                            player.player.playSound(player.player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3F, 0.5F);
                            player.player.setLevel(countdown);
                        }
                    }
                } else {
                    String decoded = String.format(ConfigManager.getInstance().getString("messages", "arena.countdown"), countdown);
                    for(DodgePlayer player : players1) {
                        Message(decoded, player.player);
                        player.player.playSound(player.player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3F, 0.5F);
                        player.player.setLevel(countdown);
                    }
                }

                countdown--;
            }
        }.runTaskTimer(plugin, 0, 20L);
    }

    private void startGame() {
        this.status = Status.INGAME;
        refreshScoreboard();
        int spawnIndex = 0;
        for(DodgePlayer player : players) {
            player.player.teleport(spawnLocations.get(spawnIndex));
            player.player.setFallDistance(0);
            player.player.setLevel(0);

            Message("messages", "arena.start", player.player);

            player.player.setHealth(6);

            giveStartItems(player.player);

            spawnIndex++;
            if (spawnIndex >= spawnLocations.size() - 1) {
                spawnIndex = 0;
            }
        }
        immortalityCountdown();
    }

    private void immortalityCountdown() {
        immortality = true;
        BukkitTask task = new BukkitRunnable() {
            private int countdown = 5;

            @Override
            public void run() {
                if (countdown <= 0) {
                    countdown = 5;
                    immortalityEnd();
                    this.cancel();
                    return;
                }

                if (countdown > 5) {
                    if (countdown % 10 == 0) {
                        String decoded = String.format(ConfigManager.getInstance().getString("messages", "arena.immortalityCountDown"), countdown);
                        for(DodgePlayer player : players) {
                            Message(decoded, player.player);
                            player.player.playSound(player.player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3F, 0.5F);
                            player.player.sendTitle("", ChatColor.RED + String.valueOf(countdown), 1, 20, 1);
                        }
                    }
                } else {
                    String decoded = String.format(ConfigManager.getInstance().getString("messages", "arena.immortalityCountDown"), countdown);
                    for(DodgePlayer player : players) {
                        Message(decoded, player.player);
                        player.player.playSound(player.player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 3F, 0.5F);
                        player.player.sendTitle("", ChatColor.RED + String.valueOf(countdown), 1, 20, 1);
                    }
                }

                countdown--;
            }
        }.runTaskTimer(plugin, 40L, 20L);
    }

    private void immortalityEnd() {
        for(DodgePlayer player : players) {
            Message("messages", "arena.immortalityEnd", player.player);
        }
        immortality = false;
    }

    private void stopGame() {
        this.status = Status.IDLE;
        for (DodgePlayer player : getAllPlayers()) {
            PlayerManager.leavePlayerFromGame(player);
        }
        winner = null;
    }

    private void winStatement(DodgePlayer winner) {
        this.winner = winner;
        String decodeString = String.format(ConfigManager.getInstance().getString("messages", "arena.win"), winner.player.getName());
        for (DodgePlayer player : getAllPlayers()) {
            player.player.removePotionEffect(PotionEffectType.INVISIBILITY);
            player.player.getInventory().clear();

            Message(decodeString, player.player);
        }
        launchFireworks();
    }

    private void launchFireworks() {
        BukkitTask task = new BukkitRunnable() {
            private int firework = 7;

            @Override
            public void run() {
                if (firework <= 0) {
                    stopGame();
                    this.cancel();
                    return;
                }

                Firework fw = (Firework) winner.player.getWorld().spawnEntity(winner.player.getLocation(), EntityType.FIREWORK);
                FireworkMeta fwm = fw.getFireworkMeta();
                fwm.setPower(40);
                FireworkEffect fwe = FireworkEffect.builder().flicker(true).trail(true).withColor(Color.AQUA).withFade(Color.LIME).build();
                fwm.addEffect(fwe);

                fw.setFireworkMeta(fwm);
                fw.detonate();


                firework--;
            }
        }.runTaskTimer(plugin, 20L, 20L);
    }



    private void giveStartItems(Player player) {
        ItemStack bow = new ItemStack(Material.BOW);
        ItemMeta meta = bow.getItemMeta();

        meta.addEnchant(Enchantment.ARROW_INFINITE, 1, false);
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6&lPlayer Slayer"));
        meta.setUnbreakable(true);

        bow.setItemMeta(meta);
        player.getInventory().setItem(0, bow);
        player.getInventory().setItem(8, new ItemStack(Material.ARROW));
    }

    public ArrayList<DodgePlayer> getAllPlayers() {
        ArrayList<DodgePlayer> allPlayers = new ArrayList<DodgePlayer>();
        allPlayers.addAll(players);
        allPlayers.addAll(spectors);
        return allPlayers;
    }

    public void refreshScoreboard() {
        switch (this.status) {
            case IDLE:
                scoreboard.lobbyMenu();
                break;
            case STARTING:
                scoreboard.lobbyMenu();
                break;
            case INGAME:
                scoreboard.gameMenu();
        }
    }
}
