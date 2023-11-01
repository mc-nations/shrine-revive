package com.itsziroy.shrinerevive.managers;

import com.itsziroy.shrinerevive.ItemKey;
import com.itsziroy.shrinerevive.ShrineRevive;
import com.itsziroy.shrinerevive.util.PlayerTime;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ShrineTimeManager extends JSONManager<PlayerTime> {
    public static String FILE_LOCATION = "timers.json";

    public ShrineTimeManager(ShrineRevive plugin) {
        super(plugin, FILE_LOCATION, PlayerTime.class);
    }

    public PlayerTime get(OfflinePlayer player) {
        for(PlayerTime playerTime: this.data) {
            if(playerTime.uuid().equals(player.getUniqueId().toString())) {
                return playerTime;
            }
        }
        return null;
    }
    public void startRevive(OfflinePlayer player) {
        long time = Calendar.getInstance().getTimeInMillis();
        this.data.add(new PlayerTime(player.getUniqueId().toString(), player.getName(), time));
        this.write();
    }


    public void endRevive(OfflinePlayer player) {
        this.data.removeIf(o -> o.uuid().equals(player.getUniqueId().toString()));
        this.write();
    }

    public void endRevive(String player) {
        this.data.removeIf(o -> o.uuid().equals(player));
        this.write();
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
