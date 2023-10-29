package com.itsziroy.shrinerevive.listeners;

import com.itsziroy.shrinerevive.ItemKey;
import com.itsziroy.shrinerevive.ShrineRevive;
import com.itsziroy.shrinerevive.events.ShrineReceivedPlayerTokenEvent;
import com.itsziroy.shrinerevive.jobs.SpawnRandomFireworks;
import com.itsziroy.shrinerevive.util.ShrineReviveCallback;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;
import java.util.UUID;

public class ShrineListener implements Listener {

    private final ShrineRevive plugin;

    public ShrineListener(ShrineRevive plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInventoryPickedUp(InventoryPickupItemEvent event) {
        shrinePickedUpItem(event.getInventory(), event.getItem().getItemStack(), (boolean wasToken) -> {
            event.setCancelled(true);
            event.getItem().remove();
        });
    }

    @EventHandler
    public void onInventoryRevievedItem(InventoryClickEvent event) {
        if(event.getClickedInventory() != null) {
            if (event.getAction() == InventoryAction.PLACE_ONE
                    || event.getAction() == InventoryAction.PLACE_ALL
                    || event.getAction() == InventoryAction.PLACE_SOME) {

                shrinePickedUpItem(event.getClickedInventory(), event.getCursor(), (boolean wasToken) -> {
                    if (wasToken) {
                        event.setCursor(null);
                    } else {
                        event.setCancelled(true);
                    }
                });

            }

            if (event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY
                    && event.getClickedInventory().getType() == InventoryType.PLAYER) {

                shrinePickedUpItem(event.getInventory(), event.getCurrentItem(), (boolean wasToken) -> {
                    if (wasToken) {
                        event.setCurrentItem(null);
                    } else {
                        event.setCancelled(true);
                    }
                });
            }
        }
    }
    @EventHandler
    public void onInventoryMoved(InventoryMoveItemEvent event) {
        shrinePickedUpItem(event.getDestination(), event.getItem(), (boolean wasToken) -> event.setCancelled(true));
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

    private void shrinePickedUpItem(Inventory inventory, ItemStack itemStack, ShrineReviveCallback callback) {
        if(inventory.getType().equals(InventoryType.HOPPER)) {
            // Inventory is hopper
            BlockInventoryHolder inventoryHolder = (BlockInventoryHolder) inventory.getHolder();
            if (inventoryHolder != null) {
                PersistentDataContainer customBlockData = new CustomBlockData(inventoryHolder.getBlock(), plugin);

                // Hopper is shrine
                if (customBlockData.has(ItemKey.SHRINE.key(), ItemKey.SHRINE.dataType())) {

                    PersistentDataContainer skullData = Objects.requireNonNull(itemStack.getItemMeta()).getPersistentDataContainer();
                    String playerUid = skullData.get(ItemKey.REVIVE_TOKEN.key(), ItemKey.REVIVE_TOKEN.dataType());

                    // Dropped Item is Skull
                    if(playerUid != null) {
                        OfflinePlayer player = plugin.getServer().getOfflinePlayer(UUID.fromString(playerUid));

                        plugin.getShrineTimeManager().startRevive(player);
                        plugin.getRedis().getMessanger().send(new ShrineReceivedPlayerTokenEvent(player));


                        World world = inventoryHolder.getBlock().getWorld();
                        Location location = inventoryHolder.getBlock().getLocation().clone();

                        world.strikeLightningEffect(location);
                        location.setY(location.getBlockY() + 4);


                        // Spawn fireworks

                        for (int i = 0; i < 10; i++) {
                            BukkitRunnable runnable = new SpawnRandomFireworks(this.plugin, location);
                            runnable.runTaskLater(this.plugin, (long) (20 * i * Math.random()));
                        }
                        callback.handle(true);
                    }

                    callback.handle(false);
                }
            }
        }
    }

}
