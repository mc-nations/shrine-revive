package com.itsziroy.shrinerevive.listeners;

import com.itsziroy.shrinerevive.ItemKey;
import com.itsziroy.shrinerevive.ShrineRevive;
import com.itsziroy.shrinerevive.events.ShrineReceivedPlayerTokenEvent;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.BlockInventoryHolder;
import org.bukkit.inventory.meta.FireworkMeta;
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

                    world.strikeLightningEffect(location);
                    location.setY(location.getBlockY() + 4);


                    // Spawn fireworks

                    for (int i = 0; i < 10; i++) {
                        BukkitRunnable runnable = new BukkitRunnable() {

                            @Override
                            public void run() {

                                location.setX(inventoryHolder.getBlock().getLocation().getX() + Math.random() * 8 - 4);
                                location.setY(inventoryHolder.getBlock().getLocation().getY() + Math.random() * 8 - 4);


                                Firework firework = (Firework) world.spawnEntity(location, EntityType.FIREWORK);

                                FireworkMeta meta = firework.getFireworkMeta();

                                FireworkEffect.Builder builder = FireworkEffect.builder();

                                builder.withFlicker().withColor(Color.fromRGB((int) (Math.random() * 16777215 )),
                                                Color.fromRGB((int) (Math.random() * 16777215 )),
                                                Color.fromRGB((int) (Math.random() * 16777215 )),
                                                Color.fromRGB((int) (Math.random() * 16777215 ))).
                                        withFade(Color.fromRGB((int) (Math.random() * 16777215 ))).trail(true).
                                        with(FireworkEffect.Type.values()[(int) (Math.random() * 4 + 1)]);
                                meta.setPower((int) (Math.random() * 2 + 1));
                                meta.addEffect(builder.build());

                                firework.setFireworkMeta(meta);
                            }
                        };

                        runnable.runTaskLater(this.plugin, (long) (20 * i * Math.random()));


                    }


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
