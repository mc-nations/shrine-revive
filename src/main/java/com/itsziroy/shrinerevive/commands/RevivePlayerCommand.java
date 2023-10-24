package com.itsziroy.shrinerevive.commands;

import com.itsziroy.shrinerevive.ShrineRevive;
import com.itsziroy.shrinerevive.events.ShrineRevivedPlayerEvent;
import com.itsziroy.shrinerevive.jobs.Job;
import com.itsziroy.shrinerevive.jobs.RemoveTokenFromWorldJob;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

public class RevivePlayerCommand extends Command{
    private final ShrineRevive plugin;

    public RevivePlayerCommand(ShrineRevive plugin) {
        super("revive");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        OfflinePlayer player = plugin.getServer().getOfflinePlayer(args[0]);
        if (player != null) {
            if(plugin.getDeadPlayerManager().isDead(player)) {
                plugin.getDeadPlayerManager().removeDeadPlayer(player);
                plugin.getShrineTimeManager().endRevive(player);

                plugin.getRedis().getMessanger().send(new ShrineRevivedPlayerEvent(player.getUniqueId().toString(), player.getName()));

                Job removeTokensFromWorldJob = new RemoveTokenFromWorldJob(plugin, player);
                removeTokensFromWorldJob.runTask();

                sender.sendMessage("Revived player " + player.getName());
            } else {
                sender.sendMessage("Player is not dead.");
            }
        } else {
            sender.sendMessage("Player " + args[0] + " not found.");
        }
        return false;
    }
}
