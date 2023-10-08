package com.itsziroy.shrinerevive.listeners;

import com.itsziroy.shrinerevive.ItemKeys;
import com.itsziroy.shrinerevive.ShrineRevive;
import com.itsziroy.shrinerevive.events.ShrineTokenPickedUpEvent;
import com.itsziroy.shrinerevive.items.ShrineReviveToken;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

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
                String playerUid = event.getItem().getItemStack().getItemMeta().getPersistentDataContainer().get(ItemKeys.REVIVE_TOKEN, PersistentDataType.STRING);
                if (playerUid != null) {
                    OfflinePlayer player = plugin.getServer().getPlayer(UUID.fromString(playerUid));
                    if (player != null) {
                        plugin.getRedis().getMessanger().send(new ShrineTokenPickedUpEvent(player, (Player) event.getEntity()));
                    }
                }
            }
        }
    }
    @EventHandler
    public void onPlayerDied(PlayerDeathEvent event) {
        ShrineReviveToken reviveToken = new ShrineReviveToken(event.getEntity());
        event.getDrops().add(reviveToken.getItemStack(1));
    }

}
