package com.itsziroy.shrinerevive.managers;

import com.itsziroy.shrinerevive.ShrineRevive;
import com.itsziroy.shrinerevive.util.RevivedPlayer;

public class RevivedPlayerManager extends JSONManager<RevivedPlayer> {

    public static String FILE_LOCATION = "revived_players.json";

    public RevivedPlayerManager(ShrineRevive plugin) {
        super(plugin, FILE_LOCATION);
    }
}
