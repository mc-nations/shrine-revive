package com.itsziroy.shrinerevive.jobs;

import com.itsziroy.shrinerevive.ShrineRevive;
import org.bukkit.OfflinePlayer;

public class RemoveTokenFromWorldJob extends Job{
    OfflinePlayer player;
    public RemoveTokenFromWorldJob(ShrineRevive plugin, OfflinePlayer player) {
        super(plugin);
        this.player = player;
    }

    @Override
    public void run() {
        this.plugin.getShrineTimeManager().removeReviveTokenFromWorld(player.getUniqueId().toString());
        this.plugin.getShrineTimeManager().removeReviveTokenFromOnlinePlayers(player.getUniqueId().toString());

    }
}
