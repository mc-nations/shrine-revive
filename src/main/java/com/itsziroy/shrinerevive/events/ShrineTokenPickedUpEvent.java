package com.itsziroy.shrinerevive.events;

import com.itsziroy.bukkitredis.events.player.MinecraftPlayer;
import com.itsziroy.shrinerevive.ShrineRevive;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;


public class ShrineTokenPickedUpEvent extends ShrineOfflinePlayerBaseEvent {
    public static String NAME = "shrine_player_token_picked_up";
    public static String KEY_ACTION_USER = "action_user";

    private final Player actionPlayer;
    public ShrineTokenPickedUpEvent(OfflinePlayer tokenPlayer, Player actionPlayer) {
        super(NAME, tokenPlayer);
        this.actionPlayer = actionPlayer;

        ShrineRevive.getInstance().getLogger().info(actionPlayer.toString());

        this.put("revive_time", ShrineRevive.SHRINE_REVIVE_TIMEOUT);
        this.put(KEY_ACTION_USER, new MinecraftPlayer(actionPlayer.getUniqueId().toString(), actionPlayer.getName()));
    }

    public Player getActionPlayer() {
        return actionPlayer;
    }
}
