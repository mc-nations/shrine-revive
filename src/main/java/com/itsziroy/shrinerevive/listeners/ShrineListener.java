package com.itsziroy.shrinerevive.listeners;

import com.itsziroy.shrinerevive.ItemKey;
import com.itsziroy.shrinerevive.ShrineRevive;
import com.itsziroy.shrinerevive.events.ShrineReceivedPlayerTokenEvent;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.Objects;
import java.util.UUID;

public class ShrineListener implements Listener {

    private final ShrineRevive plugin;

    public ShrineListener(ShrineRevive plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryPickedUp(InventoryPickupItemEvent event) {
        if(event.getInventory().getType().equals(InventoryType.HOPPER)) {
            // Inventory is hopper
            BlockInventoryHolder inventoryHolder = (BlockInventoryHolder) event.getInventory().getHolder();
            if (inventoryHolder != null) {
                PersistentDataContainer customBlockData = new CustomBlockData(inventoryHolder.getBlock(), plugin);

                // Hopper is shrine
                if (customBlockData.has(ItemKey.SHRINE.key(), ItemKey.SHRINE.dataType())) {

                    PersistentDataContainer skullData = Objects.requireNonNull(event.getItem().getItemStack().getItemMeta()).getPersistentDataContainer();
                    String playerUid = skullData.get(ItemKey.REVIVE_TOKEN.key(), ItemKey.REVIVE_TOKEN.dataType());

                    // Dropped Item is Skull
                    if(playerUid != null) {
                        OfflinePlayer player = plugin.getServer().getOfflinePlayer(UUID.fromString(playerUid));

                        plugin.getShrineTimeManager().startRevive(player);
                        plugin.getRedis().getMessanger().send(new ShrineReceivedPlayerTokenEvent(player));
                    }

                    World world =inventoryHolder.getBlock().getWorld();
                    Location location = inventoryHolder.getBlock().getLocation().clone();
                    location.setY(world.getHighestBlockYAt(location));

                    world.strikeLightningEffect(location);

                    world.spawnEntity(location, EntityType.FIREWORK);

                    event.setCancelled(true);
                    event.getItem().remove();
                }
            }
        }

    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event) {
        if(event.getBlockPlaced().getType() == Material.HOPPER) {
            if(ItemKey.itemHasKey(ItemKey.SHRINE, event.getItemInHand())) {
                PersistentDataContainer customBlockData = new CustomBlockData(event.getBlockPlaced(), plugin);
                customBlockData.set(ItemKey.SHRINE.key(), ItemKey.SHRINE.dataType(), true);
            }
        }
    }

}
