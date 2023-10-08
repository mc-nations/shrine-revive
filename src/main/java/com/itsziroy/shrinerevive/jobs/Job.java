package com.itsziroy.shrinerevive.jobs;

import com.itsziroy.shrinerevive.ShrineRevive;
import org.bukkit.scheduler.BukkitRunnable;

public abstract class Job extends BukkitRunnable {

    private int tickrate = 20;

    protected ShrineRevive plugin;

    public Job(ShrineRevive plugin, int tickrate){
        this.plugin = plugin;
        this.tickrate = tickrate;
    }
    public Job(ShrineRevive plugin){
        this.plugin = plugin;
        this.tickrate = tickrate;
    }

    public int getTickrate() {
        return tickrate;
    }

}
