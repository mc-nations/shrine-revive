package com.itsziroy.shrinerevive.jobs;

import com.itsziroy.shrinerevive.ShrineRevive;
import com.itsziroy.shrinerevive.events.ShrineRevivedPlayerEvent;
import com.itsziroy.shrinerevive.util.PlayerTime;
import com.itsziroy.shrinerevive.util.ReviveType;
import com.itsziroy.shrinerevive.util.RevivedPlayer;

import java.util.Calendar;
import java.util.Set;

public class RevivePlayerJob extends Job {

    public RevivePlayerJob(ShrineRevive plugin) {
        super(plugin, 100);
    }
    @Override
    public void run() {
        Set<PlayerTime> playerShrineReviveTimeSet = this.plugin.getShrineTimeManager().getData();
        Set<PlayerTime> playerDeathTimeSet = this.plugin.getDeadPlayerManager().getDeadPlayers();
        Calendar currentTime = Calendar.getInstance();
        for (PlayerTime playerTime: playerShrineReviveTimeSet) {
            if(currentTime.getTimeInMillis() - playerTime.time() > ShrineRevive.SHRINE_REVIVE_TIMEOUT) {
               this.revivePlayer(playerTime);
                plugin.getRedis().getMessanger().send(new ShrineRevivedPlayerEvent(playerTime.uuid(), playerTime.name(), ReviveType.SHRINE));
            }
        }

        // option is enabled
        if(ShrineRevive.SHRINE_REVIVE_TIMEOUT_NO_TOKEN > 0) {
            for (PlayerTime playerTime : playerDeathTimeSet) {
                if (currentTime.getTimeInMillis() - playerTime.time() > ShrineRevive.SHRINE_REVIVE_TIMEOUT_NO_TOKEN) {
                    this.revivePlayer(playerTime);
                    this.plugin.getRevivedPlayerManager().add(new RevivedPlayer(playerTime.uuid(), playerTime.name(), ReviveType.TIMER));
                    this.plugin.getShrineTimeManager().removeReviveTokenFromWorld(playerTime.uuid());
                    this.plugin.getShrineTimeManager().removeReviveTokenFromOnlinePlayers(playerTime.uuid());

                    plugin.getRedis().getMessanger().send(new ShrineRevivedPlayerEvent(playerTime.uuid(), playerTime.name(), ReviveType.TIMER));
                }
            }
        }
    }

    private void revivePlayer(PlayerTime playerTime) {
        this.plugin.getDeadPlayerManager().removeDeadPlayer(playerTime.uuid());
        this.plugin.getShrineTimeManager().endRevive(playerTime.uuid());
    }
}
