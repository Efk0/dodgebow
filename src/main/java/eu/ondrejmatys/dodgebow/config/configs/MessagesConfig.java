package eu.ondrejmatys.dodgebow.config.configs;

import eu.ondrejmatys.dodgebow.DodgeBow;
import eu.ondrejmatys.dodgebow.config.SimpleConfig;

public class MessagesConfig {

    public static void InitConfig() {
        SimpleConfig config = DodgeBow.getInstance().configManager.getNewConfig("messages.yml");

        config.setIfNot("prefix", "&e&lDodgeBow &7>> ");
        config.setIfNot("reload", "&aSuccessfully reloaded");

        config.setIfNot("errors", "", "Error messages");
        config.setIfNot("errors.ingame", "&cYou are already in game");
        config.setIfNot("errors.notingame", "&cYou are not in game");
        config.setIfNot("errors.nopermission", "&cYou dont have permission to use this command");
        config.setIfNot("errors.cannotingame", "&cYou cannot use this command in game");
        config.setIfNot("errors.arenaingame", "&cArena is already in game");
        config.setIfNot("errors.arenaalreadyexists", "&cArena with this name already exists");
        config.setIfNot("errors.arenanotexists", "&cThis arena does not exists");
        config.setIfNot("errors.spawnpointsfull", "&cThere cannot be more spawnpoints than players");
        config.setIfNot("errors.arenaingame2", "&cArena is in game!");
        config.setIfNot("errors.cannotedit", "&cYou cannot edit running arena. Please switch to editing mode with /dodgeadmin arena edit <arena>");
        config.setIfNot("errors.cannotjoin", "&cThis arena is currently not available");
        config.setIfNot("errors.newtestcmd", "Testičko");

        config.setIfNot("arena", "", "Arena messages");
        config.setIfNot("arena.join", "&e%s &a joined to the game &7(&e%s&f/&e%s&7)");
        config.setIfNot("arena.youjoin", "&aYou joined to the game &7(&e%s&f/&e%s&7)");
        config.setIfNot("arena.leave", "&e%s &a left the game &7(&e%s&f/&e%s&7)");
        config.setIfNot("arena.youleave", "&aYou left the game &7(&e%s&f/&e%s&7)");
        config.setIfNot("arena.countdown", "&aGame starts in &e%s &aseconds");
        config.setIfNot("arena.leaveDuringCountdown", "&aThere is not enough players. Pausing countdown!");
        config.setIfNot("arena.arenafull", "&cThis game is full :(");
        config.setIfNot("arena.arenashut", "&cThis game is turned off");
        config.setIfNot("arena.editmode", "&aThis game switched to edit mode");
        config.setIfNot("arena.start", "&aThe game is starting!");
        config.setIfNot("arena.immortalityCountdown", "&aImmortality ends in &e%s seconds");
        config.setIfNot("arena.immortalityEnd", "&aImmortality ended");
        config.setIfNot("arena.kill", "&e%s &ashot player &e%s &7(&3%s lives left&7)");
        config.setIfNot("arena.win", "&e%s Won the game!");
        config.setIfNot("arena.edit", "&aGame prepared for edit");

        config.setIfNot("creation", "", "Arena creation messages");
        config.setIfNot("creation.create", "&aGame named &e%s &asuccessfully created");
        config.setIfNot("creation.newmin", "&aMinimum players set to &e%s");
        config.setIfNot("creation.newmax", "&aMaximum players set to &e%s");
        config.setIfNot("creation.newspawn", "&aSpawnpoint &e(%s) &awas set to your location");
        config.setIfNot("creation.setlobby", "&aLobby spawnpoint was set to your location");
        config.setIfNot("creation.resetspawn", "&aSpawnpoints were successfully removed");
        config.setIfNot("creation.save", "&aThe game was saved and is ready for players");
        config.setIfNot("creation.remove", "&aThe game was successfully removed");

        config.setIfNot("scoreboard", "", new String[] {
                "These placeholders are available in scoreboards",
                "countdown",
                "count",
                "max",
                "arena",
                "lives (in game)",
        });
        config.setIfNot("scoreboard.lobby.title", "&6&lDODGEBOW");
        config.setIfNot("scoreboard.lobby.lines", new String[] {
                "&a&lCountdown: &r%countdown%",
                "",
                "&a&lPlayers: &r%count%/%max%",
                "",
                "&a&lArena: &r%arena%"

        });
        config.setIfNot("scoreboard.ingame.title", "&6&lDODGEBOW");
        config.setIfNot("scoreboard.ingame.lines", new String[] {
                "&a&lLives: &r%lives%",
                "",
                "&a&lCountdown: &r%countdown%",
                "",
                "&a&lPlayers: &r%min%/%max%",
                "",
                "&a&lArena: &r%arena%"

        });

        config.saveConfig();
    }
}
