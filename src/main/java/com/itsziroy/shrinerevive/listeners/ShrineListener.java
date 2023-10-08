package com.itsziroy.shrinerevive.listeners;

import com.itsziroy.shrinerevive.ItemKeys;
import com.itsziroy.shrinerevive.ShrineRevive;
import com.itsziroy.shrinerevive.events.ShrineReceivedPlayerTokenEvent;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;
import java.util.UUID;

public class ShrineListener implements Listener {

    private final ShrineRevive plugin;

    public ShrineListener(ShrineRevive plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryPickedUp(InventoryPickupItemEvent event) {
        // Inventory is hopper
        if(event.getInventory().getType().equals(InventoryType.HOPPER)) {
            PersistentDataContainer skullData = Objects.requireNonNull(event.getItem().getItemStack().getItemMeta()).getPersistentDataContainer();
            // Item is revive Token
            String playerUid = skullData.get(ItemKeys.REVIVE_TOKEN, PersistentDataType.STRING);
            if(playerUid != null) {
                BlockInventoryHolder inventoryHolder = (BlockInventoryHolder) event.getInventory().getHolder();
                if (inventoryHolder != null) {
                    PersistentDataContainer customBlockData = new CustomBlockData(inventoryHolder.getBlock(), plugin);

                    // Hopper is shrine
                    if(customBlockData.has(ItemKeys.SHRINE, PersistentDataType.BOOLEAN)) {
                        OfflinePlayer player = plugin.getServer().getOfflinePlayer(UUID.fromString(playerUid));

                        plugin.getShrineTimeManager().startRevive(player);
                        plugin.getRedis().getMessanger().send(new ShrineReceivedPlayerTokenEvent(player));

                        event.setCancelled(true);
                        event.getItem().remove();
                    }
                }
            }
        }

    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {
        if(event.getBlockPlaced().getType() == Material.HOPPER) {
            if(Objects.requireNonNull(event.getItemInHand().getItemMeta()).getPersistentDataContainer().has(ItemKeys.SHRINE, PersistentDataType.BOOLEAN)) {
                PersistentDataContainer customBlockData = new CustomBlockData(event.getBlockPlaced(), plugin);
                customBlockData.set(ItemKeys.SHRINE, PersistentDataType.BOOLEAN, true);
            }
        }
    }

}
