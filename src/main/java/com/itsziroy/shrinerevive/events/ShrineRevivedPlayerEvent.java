package com.itsziroy.shrinerevive.events;

import com.itsziroy.bukkitredis.events.ExtensibleEvent;
import com.itsziroy.bukkitredis.events.player.MinecraftPlayer;

public class ShrineRevivedPlayerEvent extends ExtensibleEvent {

    public static String NAME = "shrine_revived_player";

    private final MinecraftPlayer minecraftPlayer;
    public ShrineRevivedPlayerEvent(String uuid, String name) {
        super(NAME);
        this.minecraftPlayer = new MinecraftPlayer(uuid, name);

        this.put("minecraft_user", minecraftPlayer);
    }

    public MinecraftPlayer getMinecraftPlayer() {
        return minecraftPlayer;
    }
}
