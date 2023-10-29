package com.itsziroy.shrinerevive.listeners;

import com.itsziroy.shrinerevive.ItemKey;
import com.itsziroy.shrinerevive.ShrineRevive;
import com.itsziroy.shrinerevive.events.ShrineTokenPickedUpEvent;
import com.itsziroy.shrinerevive.items.ShrineReviveToken;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class TokenListener implements Listener {
    private final ShrineRevive plugin;

    public TokenListener(ShrineRevive plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onTokenPickedUp(EntityPickupItemEvent event) {
        if(event.getEntity().getType() == EntityType.PLAYER) {
            ItemMeta itemMeta = event.getItem().getItemStack().getItemMeta();
            if(itemMeta != null) {
                String playerUid = itemMeta.getPersistentDataContainer().get(ItemKey.REVIVE_TOKEN.key(), ItemKey.REVIVE_TOKEN.dataType());
                if (playerUid != null) {
                    OfflinePlayer player = plugin.getServer().getOfflinePlayer(UUID.fromString(playerUid));
                    plugin.getRedis().getMessanger().send(new ShrineTokenPickedUpEvent(player, (Player) event.getEntity()));
                }
            }
        }
    }
    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerDied(PlayerDeathEvent event) {
        ShrineReviveToken reviveToken = new ShrineReviveToken(event.getEntity());
        event.getDrops().add(reviveToken.getItemStack(1));
        event.getEntity().getInventory().clear();
    }

    @EventHandler
    public void onEntityDropped(ItemSpawnEvent event) {
        Item item = event.getEntity();
        if(ItemKey.itemHasKey(ItemKey.REVIVE_TOKEN, item)){
                // Make Item indestructible
                item.setInvulnerable(true);
                item.setGlowing(true);
                item.setGravity(true);
                item.setUnlimitedLifetime(true);
            }
    }
    @EventHandler
    public void onItemPlaced(BlockPlaceEvent event) {
        ItemStack item = event.getItemInHand();

        if(ItemKey.itemHasKey(ItemKey.REVIVE_TOKEN, item)) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("Player Tokens cannot be placed!");
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        this.plugin.getShrineTimeManager().removeAnyReviveTokenFromInventory(event.getInventory());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        this.plugin.getShrineTimeManager().removeAnyReviveTokenFromInventory(event.getPlayer().getInventory());
    }
}
