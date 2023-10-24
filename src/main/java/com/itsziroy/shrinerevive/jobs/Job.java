package com.itsziroy.shrinerevive.jobs;

import com.itsziroy.shrinerevive.ShrineRevive;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public abstract class Job extends BukkitRunnable {

    private int tickrate = 20;

    protected ShrineRevive plugin;

    public Job(ShrineRevive plugin, int tickrate){
        this.plugin = plugin;
        this.tickrate = tickrate;
    }
    public Job(ShrineRevive plugin){
        this.plugin = plugin;
    }

    public int getTickrate() {
        return tickrate;
    }

    @NotNull
    public synchronized BukkitTask runTask() throws IllegalArgumentException, IllegalStateException {
        return super.runTask(plugin);
    }

    @NotNull
    public synchronized BukkitTask runTaskAsynchronously() throws IllegalArgumentException, IllegalStateException {
        return super.runTaskAsynchronously(plugin);
    }

    @NotNull
    public synchronized BukkitTask runTaskLater(long delay) throws IllegalArgumentException, IllegalStateException {
        return super.runTaskLater(plugin, delay);
    }

    @NotNull
    public synchronized BukkitTask runTaskLaterAsynchronously(long delay) throws IllegalArgumentException, IllegalStateException {
        return super.runTaskLaterAsynchronously(plugin, delay);
    }

    @NotNull
    public synchronized BukkitTask runTaskTimer(long delay) throws IllegalArgumentException, IllegalStateException {
        return super.runTaskTimer(plugin, delay, tickrate);
    }

    @NotNull
    public synchronized BukkitTask runTaskTimerAsynchronously(long delay) throws IllegalArgumentException, IllegalStateException {
        return super.runTaskTimerAsynchronously(plugin, delay, tickrate);
    }
}
