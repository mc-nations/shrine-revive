package com.itsziroy.shrinerevive.events;


import com.itsziroy.shrinerevive.ShrineRevive;
import org.bukkit.OfflinePlayer;

public class ShrineReceivedPlayerTokenEvent extends ShrineOfflinePlayerBaseEvent {

    public static String NAME = "shrine_received_player_token";

    public ShrineReceivedPlayerTokenEvent(OfflinePlayer tokenPlayer) {
        super(NAME, tokenPlayer);
        this.put("revive_time", ShrineRevive.SHRINE_REVIVE_TIMEOUT);
        this.executeCallbacks();
    }

}
