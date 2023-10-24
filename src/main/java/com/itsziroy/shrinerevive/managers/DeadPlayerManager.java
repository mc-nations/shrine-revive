package com.itsziroy.shrinerevive.managers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itsziroy.shrinerevive.ShrineRevive;
import com.itsziroy.shrinerevive.util.PlayerTime;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.*;

public class DeadPlayerManager {

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
    private final ShrineRevive plugin;

    private Set<PlayerTime> deadPlayers = new HashSet<>();

    public DeadPlayerManager(ShrineRevive plugin) {
        this.plugin = plugin;
    }


    public Set<PlayerTime> getDeadPlayers() {
        return deadPlayers;
    }

    public boolean isEmpty() {
        return this.deadPlayers.isEmpty();
    }
    public void loadDeadPlayers() {
        try {
            File file = new File(this.plugin.getDataFolder(), "dead_players.json");
            if(file.exists()) {
                String str = Files.readString(file.toPath());
                if(!str.isEmpty()) {
                    ObjectMapper mapper = new ObjectMapper();
                    deadPlayers = mapper.readValue(str, new TypeReference<>() {
                    });
                }
            } else {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addDeadPlayer(Player player) {
        long time = Calendar.getInstance().getTimeInMillis();
        this.deadPlayers.add(new PlayerTime(player.getUniqueId().toString(), player.getName(), time));
        this.write();
    }

    private void write() {
        try {
            File file = new File(this.plugin.getDataFolder(), "dead_players.json");

            if(deadPlayers.isEmpty()) {
                Files.writeString(file.toPath(),"");
                return;
            }
            ObjectMapper mapper = new ObjectMapper();
            String str = mapper.writeValueAsString(deadPlayers);

            Files.writeString(file.toPath(), str);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public PlayerTime get(OfflinePlayer player) {
        return this.get(player.getUniqueId().toString());
    }

    public PlayerTime get(String uuid) {
        for(PlayerTime playerTime: this.deadPlayers) {
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
        this.deadPlayers.removeIf(o -> o.uuid().equals(player.getUniqueId().toString()));
        this.write();
    }
    public void removeDeadPlayer(OfflinePlayer player) {
        this.deadPlayers.removeIf(o -> o.uuid().equals(player.getUniqueId().toString()));
        this.write();
    }

    public void removeDeadPlayer(String player) {
        this.deadPlayers.removeIf(o -> o.uuid().equals(player));
        this.write();
    }


}
