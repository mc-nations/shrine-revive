package com.itsziroy.shrinerevive.managers;

import com.itsziroy.shrinerevive.ShrineRevive;
import com.itsziroy.shrinerevive.util.PlayerTime;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.*;

public class DeadPlayerManager extends JSONManager<PlayerTime> {

    public static String DEATH_KICK_MESSAGE = ChatColor.RED + "Du bist gestorben. \n\n" +
            ChatColor.GRAY + "Ein Spieler muss dein Token aufsammeln und zu einem Shrine bringen, um den Revive Prozess " +
            "zu beschleunigen. Du wirst \u00fcber den Status deines Tokens per Discord informiert.";

    public static String DEATH_REVIVE_MESSAGE_NO_TOKEN(long time) {

        return ChatColor.RED + "Du bist gestorben. \n\n" +
                ChatColor.GRAY + "Sollte dein Token nicht von einem Spieler aufgesammelt werden wirst du in "
                + ChatColor.AQUA + formatTime(time) + " automatisch wiederbelebt.";
    }

    public static String DEATH_REVIVE_MESSAGE_TOKEN(long time) {
        return ChatColor.RED + "Du bist gestorben. \n\n" +
                ChatColor.GRAY + "Dein Token wurde aufgehoben und du wirst in " + ChatColor.AQUA + formatTime(time)+ " wiederbelebt";
    }

    private static String formatTime(long time) {
        Duration duration = Duration.ofMillis(time);
        long HH = duration.toHours();
        long MM = duration.toMinutesPart();
        String hourPart = HH == 1 ? HH + " Stunde" : HH + " Stunden";
        String minutePart = MM == 1 ? MM + " Minute" : MM + " Minuten";
        return hourPart + " und " + minutePart;
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
