package com.itsziroy.shrinerevive.managers;

import com.itsziroy.shrinerevive.ShrineRevive;
import com.itsziroy.shrinerevive.util.PlayerTime;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.*;

public class DeadPlayerManager extends JSONManager<PlayerTime> {

    public static String DEATH_KICK_MESSAGE = ChatColor.RED + "You died. \n\n" +
            ChatColor.GRAY + "A player needs to pick up your token and bring it to one of the shrines on the map to revive you. " +
            "You will be updated on the status of your token via Discord.";

    public static String DEATH_REVIVE_MESSAGE_NO_TOKEN(long time) {
        Duration duration = Duration.ofMillis(time);
        long HH = duration.toHours();
        long MM = duration.toMinutesPart();
        String timeString = HH + ":" + MM;

        return ChatColor.RED + "You died. \n\n" +
                ChatColor.GRAY + "If a player does not pick up your token, you will be revived in " + ChatColor.AQUA + timeString;
    }

    public static String DEATH_REVIVE_MESSAGE_TOKEN(long time) {
        Duration duration = Duration.ofMillis(time);
        long HH = duration.toHours();
        long MM = duration.toMinutesPart();
        String timeString = HH + ":" + MM;

        return ChatColor.RED + "You died. \n\n" +
                ChatColor.GRAY + "A player has picked up your token and you will be revived in " + ChatColor.AQUA + timeString;
    }

    public static String FILE_LOCATION = "dead_players.json";

    public DeadPlayerManager(ShrineRevive plugin) {
        super(plugin, FILE_LOCATION, PlayerTime.class);
    }


    public Set<PlayerTime> getDeadPlayers() {
        return this.getData();
    }

    public boolean isEmpty() {
        return this.data.isEmpty();
    }

    public void addDeadPlayer(Player player) {
        long time = Calendar.getInstance().getTimeInMillis();

        for(PlayerTime playerTime: this.data) {
            this.plugin.getLogger().info(String.valueOf(playerTime.hashCode()));
            this.plugin.getLogger().info(String.valueOf((new PlayerTime(player.getUniqueId().toString(), player.getName(), time).equals(playerTime))));
        }

        this.data.add(new PlayerTime(player.getUniqueId().toString(), player.getName(), time));
        this.write();

    }

    public PlayerTime get(OfflinePlayer player) {
        return this.get(player.getUniqueId().toString());
    }

    public PlayerTime get(String uuid) {
        for(PlayerTime playerTime: this.data) {
            if(playerTime.uuid().equals(uuid)) {
                return playerTime;
            }
        }
        return null;
    }

    public boolean isDead(Player player) {
        return this.get(player) != null;
    }
    public boolean isDead(OfflinePlayer player) {
        return this.get(player) != null;
    }

    public boolean isDead(String uuid) {
        return this.get(uuid) != null;
    }

    public void removeDeadPlayer(Player player) {
        this.data.removeIf(o -> o.uuid().equals(player.getUniqueId().toString()));
        this.write();
    }
    public void removeDeadPlayer(OfflinePlayer player) {
        this.data.removeIf(o -> o.uuid().equals(player.getUniqueId().toString()));
        this.write();
    }

    public void removeDeadPlayer(String player) {
        this.data.removeIf(o -> o.uuid().equals(player));
        this.write();
    }


}
