package com.itsziroy.shrinerevive.listeners;

import com.itsziroy.shrinerevive.Config;
import com.itsziroy.shrinerevive.ShrineRevive;
import com.itsziroy.shrinerevive.commands.Command;
import com.itsziroy.shrinerevive.jobs.Job;
import com.itsziroy.shrinerevive.jobs.RemoveTokenFromWorldJob;
import com.itsziroy.shrinerevive.managers.DeadPlayerManager;
import com.itsziroy.shrinerevive.util.PlayerTime;
import com.itsziroy.shrinerevive.util.ReviveType;
import com.itsziroy.shrinerevive.util.RevivedPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
                    event.disallow(PlayerLoginEvent.Result.KICK_OTHER, DeadPlayerManager.DEATH_REVIVE_MESSAGE_NO_TOKEN(
                            ShrineRevive.SHRINE_REVIVE_TIMEOUT_NO_TOKEN - (Calendar.getInstance().getTimeInMillis() - playerTime.time())
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
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(!Command.hasPermission(player, "shrine.bypass_death")) {
            if (plugin.getRevivedPlayerManager().isRevived(player)) {
                RevivedPlayer revivedPlayer = plugin.getRevivedPlayerManager().get(player);
                plugin.getLogger().info("Player " + player.getName() + " is revived. Type:" + revivedPlayer.reviveType());
                if (revivedPlayer.reviveType() == ReviveType.SHRINE) {
                    plugin.getLogger().info("Teleporting " + player.getName() + " to shrine."+ revivedPlayer.getSpawnLocation());
                    Bukkit.getScheduler().runTaskLater(this.plugin, ()  -> player.teleport(revivedPlayer.getSpawnLocation()), 20);
                }
                if (revivedPlayer.reviveType() == ReviveType.TIMER) {
                    if (plugin.getConfig().getBoolean(Config.Path.NO_TOKEN_PUNISHMENT_ENABLED)) {
                        String potionType = plugin.getConfig().getString(Config.Path.NO_TOKEN_PUNISHMENT_TYPE);
                        int potionDuration = plugin.getConfig().getInt(Config.Path.NO_TOKEN_PUNISHMENT_DURATION);
                        int amplifier = plugin.getConfig().getInt(Config.Path.NO_TOKEN_PUNISHMENT_AMPLIFIER);

                        if (potionType == null) {
                            plugin.getLogger().warning("Potion type is not set in config.yml.");
                        } else {
                            PotionEffectType potionEffectType = PotionEffectType.getByName(potionType);
                            if (potionEffectType == null) {
                                plugin.getLogger().warning("Potion type " + potionType + " is not valid.");
                            } else {
                                Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
                                        plugin.getLogger().info("Setting potion effect " + potionType + " for " + player.getName() + " for " + potionDuration + " seconds.");
                                        player.addPotionEffect(new PotionEffect(potionEffectType, 20 * potionDuration, amplifier));
                                        }, 40);
                                String message = plugin.getConfig().getString(Config.Path.NO_TOKEN_PUNISHMENT_MESSAGE);
                                if (message != null) {
                                    player.sendMessage(ChatColor.RED + message);
                                }
                            }
                        }
                    }
                }
            }
        } else {
            plugin.getLogger().info(player.getName() + " bypassed shrine death.");
        }
        plugin.getRevivedPlayerManager().remove(player);
        Job removeTokensFromWorldJob = new RemoveTokenFromWorldJob(plugin, player);
        removeTokensFromWorldJob.runTaskLater(10);
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
