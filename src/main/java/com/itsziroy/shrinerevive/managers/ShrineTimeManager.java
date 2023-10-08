package com.itsziroy.shrinerevive.managers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itsziroy.shrinerevive.ShrineRevive;
import com.itsziroy.shrinerevive.util.PlayerTime;
import org.bukkit.OfflinePlayer;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class ShrineTimeManager {
    private final ShrineRevive plugin;

    private Set<PlayerTime> timers = new HashSet<>();

    public ShrineTimeManager(ShrineRevive plugin) {
        this.plugin = plugin;
    }

    public void loadTimers() {
        try {
            File file = new File(this.plugin.getDataFolder(), "timers.json");
            if(file.exists()) {
                String str = Files.readString(file.toPath());
                if(!str.isEmpty()) {
                    ObjectMapper mapper = new ObjectMapper();
                    timers = mapper.readValue(str, new TypeReference<>() {
                    });
                }
            } else {
                file.createNewFile();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public PlayerTime get(OfflinePlayer player) {
        for(PlayerTime playerTime: this.timers) {
            if(playerTime.uuid().equals(player.getUniqueId().toString())) {
                return playerTime;
            }
        }
        return null;
    }
    public void startRevive(OfflinePlayer player) {
        long time = Calendar.getInstance().getTimeInMillis();
        this.timers.add(new PlayerTime(player.getUniqueId().toString(), player.getName(), time));
        this.write();
    }

    public void endRevive(OfflinePlayer player) {
        this.timers.removeIf(o -> o.uuid().equals(player.getUniqueId().toString()));
        this.write();
    }

    public void endRevive(String player) {
        this.timers.removeIf(o -> o.uuid().equals(player));
        this.write();
    }


    private void write() {
        try {
            File file = new File(this.plugin.getDataFolder(), "timers.json");

            ObjectMapper mapper = new ObjectMapper();
            String str = mapper.writeValueAsString(timers);

            Files.writeString(file.toPath(), str);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Set<PlayerTime> getTimers() {
        return timers;
    }

}
