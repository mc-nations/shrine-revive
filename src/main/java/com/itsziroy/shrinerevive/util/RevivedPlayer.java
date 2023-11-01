package com.itsziroy.shrinerevive.util;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

public record RevivedPlayer(String uuid, String name, ReviveType reviveType, int spawnX, int spawnY, int spawnZ, String world) {

    public RevivedPlayer(OfflinePlayer player, ReviveType reviveType, int spawnX, int spawnY, int spawnZ, String world) {
        this(player.getUniqueId().toString(), player.getName(), reviveType, spawnX, spawnY, spawnZ, world);
    }
    public RevivedPlayer(OfflinePlayer player, ReviveType reviveType, Location location) {
        this(player.getUniqueId().toString(), player.getName(), reviveType, location.getBlockX(), location.getBlockY(), location.getBlockZ(),
             location.getWorld() == null ? "world" : location.getWorld().getName());
    }
    public RevivedPlayer(OfflinePlayer player, ReviveType reviveType) {
        this(player.getUniqueId().toString(), player.getName(), reviveType, 0, 0,0, "world");
    }

    public RevivedPlayer(String uuid, String name, ReviveType reviveType) {
        this(uuid, name, reviveType, 0, 0,0, "world");
    }

    @JsonIgnore
    public Location getSpawnLocation() {
        return new Location(Bukkit.getWorld(world), spawnX, spawnY, spawnZ);
    }
}
