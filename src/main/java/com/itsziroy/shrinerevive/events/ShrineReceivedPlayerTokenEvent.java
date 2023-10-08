package com.itsziroy.shrinerevive.events;


import org.bukkit.OfflinePlayer;

public class ShrineReceivedPlayerTokenEvent extends ShrineOfflinePlayerBaseEvent {

    public static String NAME = "shrine_received_player_token";

    public ShrineReceivedPlayerTokenEvent(OfflinePlayer tokenPlayer) {
        super(NAME, tokenPlayer);
        this.executeCallbacks();
    }

}
