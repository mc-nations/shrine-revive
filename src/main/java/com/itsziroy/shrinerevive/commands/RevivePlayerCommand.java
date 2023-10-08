package com.itsziroy.shrinerevive.commands;

import com.itsziroy.shrinerevive.ShrineRevive;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RevivePlayerCommand extends Command{
    private final ShrineRevive plugin;

    public RevivePlayerCommand(ShrineRevive plugin) {
        super("revive");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        Player player = plugin.getServer().getPlayer(args[0]);
        if (player != null) {
            if(plugin.getPlayerManager().isDead(player)) {
                plugin.getPlayerManager().removeDeadPlayer(player);
                plugin.getShrineTimeManager().endRevive(player);
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
