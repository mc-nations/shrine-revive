package com.itsziroy.shrinerevive.managers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itsziroy.shrinerevive.ShrineRevive;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.util.*;

public class PlayerManager {

    public static String DEATH_KICK_MESSAGE = ChatColor.RED + "You died. \n\n" +
            ChatColor.GRAY + "A player needs to pick up your token and bring it to one of the shrines on the map to revive you. " +
            "You will be updated on the status of your token via Discord.";

    public static String DEATH_REVIVE_MESSAGE(long time) {
        Duration duration = Duration.ofMillis(time);
        long HH = duration.toHours();
        long MM = duration.toMinutesPart();
        String timeInHHMMSS = String.format("%02d:%02d", HH, MM);

        return ChatColor.RED + "You died. \n\n" +
                ChatColor.GRAY + "A player has picked up your token and you will be revived in " + ChatColor.AQUA + timeInHHMMSS;
    }
    private final ShrineRevive plugin;

    private Set<String> deadPlayers = new HashSet<>();

    public PlayerManager(ShrineRevive plugin) {
        this.plugin = plugin;
    }


    public Set<String> getDeadPlayers() {
        return deadPlayers;
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
        this.deadPlayers.add(player.getUniqueId().toString());
        this.write();
    }

    private void write() {
        try {
            File file = new File(this.plugin.getDataFolder(), "dead_players.json");

            ObjectMapper mapper = new ObjectMapper();
            String str = mapper.writeValueAsString(deadPlayers);

            Files.writeString(file.toPath(), str);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isDead(Player player) {
        return this.deadPlayers.contains(player.getUniqueId().toString());
    }

    public void removeDeadPlayer(Player player) {
        this.deadPlayers.remove(player.getUniqueId().toString());
        this.write();
    }

    public void removeDeadPlayer(String player) {
        this.deadPlayers.remove(player);
        this.write();
    }


}
