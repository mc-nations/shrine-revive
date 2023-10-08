package com.itsziroy.shrinerevive.jobs;

import com.itsziroy.shrinerevive.ShrineRevive;
import com.itsziroy.shrinerevive.events.ShrineRevivedPlayerEvent;
import com.itsziroy.shrinerevive.util.PlayerTime;

import java.util.Calendar;
import java.util.Set;

public class RevivePlayerJob extends Job {

    public RevivePlayerJob(ShrineRevive plugin) {
        super(plugin, 1200);
    }
    @Override
    public void run() {
        Set<PlayerTime> playerTimeSet = this.plugin.getShrineTimeManager().getTimers();

        for (PlayerTime playerTime: playerTimeSet) {
            Calendar currentTime = Calendar.getInstance();
            if(currentTime.getTimeInMillis() - playerTime.time() > ShrineRevive.SHRINE_REVIVE_TIMEOUT) {
                this.plugin.getPlayerManager().removeDeadPlayer(playerTime.uuid());
                this.plugin.getShrineTimeManager().endRevive(playerTime.uuid());

                plugin.getRedis().getMessanger().send(new ShrineRevivedPlayerEvent(playerTime.uuid(), playerTime.name()));
            }
        }
    }
}
