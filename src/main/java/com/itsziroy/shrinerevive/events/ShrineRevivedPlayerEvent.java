package com.itsziroy.shrinerevive.events;

import com.itsziroy.bukkitredis.events.ExtensibleEvent;
import com.itsziroy.bukkitredis.events.player.MinecraftPlayer;
import com.itsziroy.shrinerevive.util.ReviveType;

public class ShrineRevivedPlayerEvent extends ExtensibleEvent {

    public static String NAME = "shrine_revived_player";

    private final MinecraftPlayer minecraftPlayer;
    private final ReviveType reviveType;
    public ShrineRevivedPlayerEvent(String uuid, String name, ReviveType reviveType) {
        super(NAME);
        this.minecraftPlayer = new MinecraftPlayer(uuid, name);
        this.reviveType = reviveType;

        this.put("minecraft_user", minecraftPlayer);
        this.put("revive_type", reviveType);
    }

    public MinecraftPlayer getMinecraftPlayer() {
        return minecraftPlayer;
    }

    public ReviveType getReviveType() {
        return reviveType;
    }
}
