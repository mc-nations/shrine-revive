package com.itsziroy.shrinerevive.managers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.itsziroy.shrinerevive.ItemKey;
import com.itsziroy.shrinerevive.ShrineRevive;
import com.itsziroy.shrinerevive.util.PlayerTime;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

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

            if(timers.isEmpty()) {
                Files.writeString(file.toPath(),"");
                return;
            }

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


    public void removeReviveTokenFromWorld(@NotNull String uuid) {
        for (World world: this.plugin.getServer().getWorlds()) {
            for(Entity entity: world.getEntities()) {
                if(ItemFrame.class.isAssignableFrom(entity.getClass())) {
                    ItemFrame itemFrame = (ItemFrame) entity;
                    ItemStack item = itemFrame.getItem();
                    if(ItemKey.itemHasKey(ItemKey.REVIVE_TOKEN, item)) {
                        if(uuid.equals(ItemKey.getItemMeta(ItemKey.REVIVE_TOKEN, item))) {
                            itemFrame.setItem(null);
                        }
                    }
                }

                if(Item.class.isAssignableFrom(entity.getClass())) {
                    Item item = (Item) entity;
                    if(ItemKey.itemHasKey(ItemKey.REVIVE_TOKEN, item)) {
                        if(uuid.equals(ItemKey.getItemMeta(ItemKey.REVIVE_TOKEN, item))) {
                            item.remove();
                        }
                    }
                }
            }
        }
    }


    public void removeReviveTokenFromOnlinePlayers(@NotNull String uuid) {
        for (Player player: this.plugin.getServer().getOnlinePlayers()) {
            removeReviveTokenFromInventory(uuid, player.getInventory());
        }
    }

    public void removeReviveTokenFromInventory(@NotNull String uuid, @NotNull Inventory inventory) {
        ItemStack[] items = inventory.getContents();

        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                if (ItemKey.itemHasKey(ItemKey.REVIVE_TOKEN, items[i])) {
                    if (uuid.equals(ItemKey.getItemMeta(ItemKey.REVIVE_TOKEN, items[i]))) {
                        inventory.setItem(i, null);
                    }
                }
            }
        }
    }

    public void removeAnyReviveTokenFromInventory(@NotNull Inventory inventory) {
        ItemStack[] items = inventory.getContents();
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                if (ItemKey.itemHasKey(ItemKey.REVIVE_TOKEN, items[i])) {
                    if (!this.plugin.getDeadPlayerManager().isDead(ItemKey.getItemMeta(ItemKey.REVIVE_TOKEN, items[i]))) {
                        inventory.setItem(i, null);
                    }
                }
            }
        }
    }
}
