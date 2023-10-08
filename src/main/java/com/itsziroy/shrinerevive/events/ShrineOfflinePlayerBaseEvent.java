package com.itsziroy.shrinerevive.events;

import com.itsziroy.bukkitredis.events.ExtensibleEvent;
import com.itsziroy.bukkitredis.events.SimplePlayer;
import org.bukkit.OfflinePlayer;

public class ShrineOfflinePlayerBaseEvent extends ExtensibleEvent {

    private final OfflinePlayer tokenPlayer;


    public static String KEY_TOKEN_USER = "token_user";

    public ShrineOfflinePlayerBaseEvent(String name, OfflinePlayer offlinePlayer) {
        super(name);
        this.tokenPlayer = offlinePlayer;

        this.put(KEY_TOKEN_USER, new SimplePlayer(offlinePlayer.getUniqueId().toString(), offlinePlayer.getName()));

        this.executeCallbacks(ShrineOfflinePlayerBaseEvent.class);
    }


    public OfflinePlayer getTokenPlayer() {
        return tokenPlayer;
    }

}
