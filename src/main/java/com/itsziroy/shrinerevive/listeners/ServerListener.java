package com.itsziroy.shrinerevive.listeners;

import com.itsziroy.shrinerevive.ShrineRevive;
import com.itsziroy.shrinerevive.commands.Command;
import com.itsziroy.shrinerevive.managers.PlayerManager;
import com.itsziroy.shrinerevive.util.PlayerTime;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;

public class ServerListener implements Listener {
    private final ShrineRevive plugin;

    public ServerListener(ShrineRevive plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onePreJoin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (plugin.getPlayerManager().isDead(player)) {
            if(!Command.hasPermission(player, "shrine.bypass_death")) {
                PlayerTime playerTime = plugin.getShrineTimeManager().get(player);
                if(playerTime != null) {
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, PlayerManager.DEATH_REVIVE_MESSAGE(playerTime.time()));
                    return;
                }
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, PlayerManager.DEATH_KICK_MESSAGE);
            }
            else {
                plugin.getLogger().info(player.getName() + " bypassed shrine death.");
            }
        }

    }

    @EventHandler
    public void onPlayerDied(PlayerDeathEvent event) {
        plugin.getPlayerManager().addDeadPlayer(event.getEntity());
        Player player = event.getEntity();
        if(!Command.hasPermission(player, "shrine.bypass_death")) {
            player.kickPlayer(PlayerManager.DEATH_KICK_MESSAGE);
        } else {
            plugin.getLogger().info(player.getName() + " bypassed shrine death.");
        }
    }

}
