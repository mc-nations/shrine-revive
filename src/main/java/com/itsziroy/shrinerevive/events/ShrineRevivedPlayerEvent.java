package com.itsziroy.shrinerevive.events;

import com.itsziroy.bukkitredis.events.ExtensibleEvent;
import com.itsziroy.bukkitredis.events.SimplePlayer;

public class ShrineRevivedPlayerEvent extends ExtensibleEvent {

    public static String NAME = "shrine_revived_player";

    private final SimplePlayer simplePlayer;
    public ShrineRevivedPlayerEvent(String uuid, String name) {
        super(NAME);
        this.simplePlayer = new SimplePlayer(uuid, name);

        this.put("minecraft_user", simplePlayer);

        this.executeCallbacks();
    }

    public SimplePlayer getSimplePlayer() {
        return simplePlayer;
    }
}
