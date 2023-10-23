package com.itsziroy.shrinerevive.listeners;

import com.itsziroy.shrinerevive.ShrineRevive;
import com.itsziroy.shrinerevive.commands.Command;
import com.itsziroy.shrinerevive.managers.DeadPlayerManager;
import com.itsziroy.shrinerevive.util.PlayerTime;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Calendar;

public class ServerListener implements Listener {
    private final ShrineRevive plugin;

    public ServerListener(ShrineRevive plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrejoin(PlayerLoginEvent event) {
        Player player = event.getPlayer();
        if (plugin.getDeadPlayerManager().isDead(player)) {
            if(!Command.hasPermission(player, "shrine.bypass_death")) {
                PlayerTime playerTime = plugin.getShrineTimeManager().get(player);
                if(playerTime != null) {
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, DeadPlayerManager.DEATH_REVIVE_MESSAGE_TOKEN(
                            ShrineRevive.SHRINE_REVIVE_TIMEOUT - (Calendar.getInstance().getTimeInMillis() - playerTime.time())
                    ));
                    return;
                }
                if(ShrineRevive.SHRINE_REVIVE_TIMEOUT_NO_TOKEN > 0) {
                    playerTime = plugin.getDeadPlayerManager().get(player);
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, DeadPlayerManager.DEATH_REVIVE_MESSAGE_TOKEN(
                            ShrineRevive.SHRINE_REVIVE_TIMEOUT - (Calendar.getInstance().getTimeInMillis() - playerTime.time())
                    ));
                    return;
                }
                event.disallow(PlayerLoginEvent.Result.KICK_OTHER, DeadPlayerManager.DEATH_KICK_MESSAGE);
            }
            else {
                plugin.getLogger().info(player.getName() + " bypassed shrine death.");
            }
        }

    }

    @EventHandler
    public void onPlayerDied(PlayerDeathEvent event) {
        plugin.getDeadPlayerManager().addDeadPlayer(event.getEntity());
        Player player = event.getEntity();
        if(!Command.hasPermission(player, "shrine.bypass_death")) {
            player.kickPlayer(DeadPlayerManager.DEATH_KICK_MESSAGE);
        } else {
            plugin.getLogger().info(player.getName() + " bypassed shrine death.");
        }
    }

}
