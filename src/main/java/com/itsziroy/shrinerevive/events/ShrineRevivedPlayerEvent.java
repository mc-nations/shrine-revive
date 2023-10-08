package com.itsziroy.shrinerevive.events;

import com.itsziroy.bukkitredis.events.ExtensibleEvent;
import com.itsziroy.bukkitredis.events.SimplePlayer;

public class ShrineRevivedPlayerEvent extends ExtensibleEvent {

    public static String NAME = "shrine_revived_player";
    public ShrineRevivedPlayerEvent(String uuid, String name) {
        super(NAME);

        this.put("minecraft_user", new SimplePlayer(uuid, name));

        this.executeCallbacks();
    }
}
