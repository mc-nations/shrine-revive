package com.itsziroy.shrinerevive.managers;

import com.itsziroy.shrinerevive.ShrineRevive;
import com.itsziroy.shrinerevive.util.RevivedPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class RevivedPlayerManager extends JSONManager<RevivedPlayer> {

    public static String FILE_LOCATION = "revived_players.json";

    public RevivedPlayerManager(ShrineRevive plugin) {
        super(plugin, FILE_LOCATION, RevivedPlayer.class);
    }

    public boolean isRevived(Player player) {
        return isRevived(player.getUniqueId().toString());
    }

    public boolean isRevived(OfflinePlayer player) {
        return isRevived(player.getUniqueId().toString());
    }
    public boolean isRevived(String uuid) {
        return this.get(uuid) != null;
    }

    public RevivedPlayer get(Player player) {
        return get(player.getUniqueId().toString());
    }
    public RevivedPlayer get(OfflinePlayer player) {
        return get(player.getUniqueId().toString());
    }
    public RevivedPlayer get(String uuid) {
        for(RevivedPlayer player: this.data) {
            if(player.uuid().equals(uuid)) {
                return player;
            }
        }
        return null;
    }

    public void add(RevivedPlayer player) {
        this.data.add(player);
        this.write();
    }

    public void remove(Player player) {
        remove(player.getUniqueId().toString());
    }

    public void remove(OfflinePlayer player) {
        remove(player.getUniqueId().toString());
    }

    public void remove(String uuid) {
        this.data.removeIf(o -> o.uuid().equals(uuid));
        this.write();
    }
}
