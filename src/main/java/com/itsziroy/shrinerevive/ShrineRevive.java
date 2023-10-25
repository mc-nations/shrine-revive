package com.itsziroy.shrinerevive;

import com.itsziroy.bukkitredis.BukkitRedisPlugin;
import com.itsziroy.shrinerevive.commands.CreateShrineCommand;
import com.itsziroy.shrinerevive.commands.RevivePlayerCommand;
import com.itsziroy.shrinerevive.jobs.Job;
import com.itsziroy.shrinerevive.jobs.RevivePlayerJob;
import com.itsziroy.shrinerevive.listeners.ServerListener;
import com.itsziroy.shrinerevive.listeners.ShrineListener;
import com.itsziroy.shrinerevive.listeners.TokenListener;
import com.itsziroy.shrinerevive.managers.CommandManager;
import com.itsziroy.shrinerevive.managers.DeadPlayerManager;
import com.itsziroy.shrinerevive.managers.ShrineTimeManager;
import com.itsziroy.shrinerevive.util.PlayerTime;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public final class ShrineRevive extends JavaPlugin {

    public static long SHRINE_REVIVE_TIMEOUT = 10000L;
    public static long SHRINE_REVIVE_TIMEOUT_NO_TOKEN = -1L;

    private final CommandManager commandManager = new CommandManager();
    private BukkitRedisPlugin bukkitRedis;

    private final DeadPlayerManager deadPlayerManager = new DeadPlayerManager(this);


    private final ShrineTimeManager shrineTimeManager = new ShrineTimeManager(this);

    @Override
    public void onEnable() {
        // Plugin startup logic


        deadPlayerManager.load();
        shrineTimeManager.load();

        commandManager.registerCommand(new CreateShrineCommand());
        commandManager.registerCommand(new RevivePlayerCommand(this));

        getServer().getPluginManager().registerEvents(new ServerListener(this), this);
        getServer().getPluginManager().registerEvents(new ShrineListener(this), this);
        getServer().getPluginManager().registerEvents(new TokenListener(this), this);

        BukkitRedisPlugin bukkitRedis = (BukkitRedisPlugin) Bukkit.getPluginManager().getPlugin("BukkitRedis");
        if (bukkitRedis != null) {
            getLogger().info("BukkitRedis extenstion loaded.");
            this.bukkitRedis = bukkitRedis;
        }

        CustomBlockData.registerListener(this);



        Job checkServerUptimeJob = new RevivePlayerJob(this);

        checkServerUptimeJob.runTaskTimer(0);

        registerConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if(args.length == 0) {
            return false;
        }
        return commandManager.onCommand(sender, args[0], Arrays.stream(args).skip(1).toArray(String[]::new));
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String alias, @NotNull String[] args) {
        if(cmd.getName().equals("shrine") && args.length < 2) {
            return new ArrayList<>() {{
                for (Map.Entry<String, com.itsziroy.shrinerevive.commands.Command> command : commandManager.getCommands().entrySet())
                    if (command.getValue().hasPermission(sender)) {
                        add(command.getKey());
                    }
            }};
        } else {
            if(Objects.equals(args[0], "revive")) {
                return new ArrayList<>() {{
                    for (PlayerTime playerTime: deadPlayerManager.getDeadPlayers()) {
                        OfflinePlayer offlinePlayer = getServer().getOfflinePlayer(UUID.fromString(playerTime.uuid()));
                        add(offlinePlayer.getName());
                    }
                }};
            }
            return null;
        }
    }
    public void registerConfig(){
        File config = new File(this.getDataFolder(), "config.yml");
        if(!config.exists()){
            this.getConfig().options().copyDefaults(true);
            saveConfig();
        }
        SHRINE_REVIVE_TIMEOUT = this.getConfig().getLong("revive_time") * 1000;
        SHRINE_REVIVE_TIMEOUT_NO_TOKEN = this.getConfig().getLong("revive_time_no_token") * 1000;

    }

    public static ShrineRevive getInstance() {
        return getPlugin(ShrineRevive.class);
    }

    public BukkitRedisPlugin getRedis() {
        return bukkitRedis;
    }

    public DeadPlayerManager getDeadPlayerManager() {
        return deadPlayerManager;
    }

    public ShrineTimeManager getShrineTimeManager() {
        return shrineTimeManager;
    }
}
