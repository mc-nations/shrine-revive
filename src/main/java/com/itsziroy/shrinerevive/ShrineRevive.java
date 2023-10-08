package com.itsziroy.shrinerevive;

import com.itsziroy.bukkitredis.BukkitRedisPlugin;
import com.itsziroy.shrinerevive.commands.CreateShrineCommand;
import com.itsziroy.shrinerevive.commands.RevivePlayerCommand;
import com.itsziroy.shrinerevive.jobs.RevivePlayerJob;
import com.itsziroy.shrinerevive.listeners.ServerListener;
import com.itsziroy.shrinerevive.listeners.ShrineListener;
import com.itsziroy.shrinerevive.listeners.TokenListener;
import com.itsziroy.shrinerevive.managers.CommandManager;
import com.itsziroy.shrinerevive.managers.PlayerManager;
import com.itsziroy.shrinerevive.managers.ShrineTimeManager;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public final class ShrineRevive extends JavaPlugin {

    public static long SHRINE_REVIVE_TIMEOUT = 10000L;

    private final CommandManager commandManager = new CommandManager();
    private BukkitRedisPlugin bukkitRedis;

    private File dataFolder;

    private final PlayerManager playerManager = new PlayerManager(this);


    private final ShrineTimeManager shrineTimeManager = new ShrineTimeManager(this);

    @Override
    public void onEnable() {
        // Plugin startup logic

        dataFolder = new File(getDataFolder(), "data"); // get folder "plugins/MyPlugin/data"
        dataFolder.mkdirs(); // create folder if not exists


        playerManager.loadDeadPlayers();
        shrineTimeManager.loadTimers();

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



        RevivePlayerJob checkServerUptimeJob = new RevivePlayerJob(this);

        checkServerUptimeJob.runTaskTimer(this,0, checkServerUptimeJob.getTickrate());

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
            return null;
        }
    }
    public void registerConfig(){
        File config = new File(this.getDataFolder(), "config.yml");
        if(!config.exists()){
            this.getConfig().options().copyDefaults(true);
            saveConfig();
        }
        SHRINE_REVIVE_TIMEOUT = this.getConfig().getLong("revive_time");
    }

    public static ShrineRevive getInstance() {
        return getPlugin(ShrineRevive.class);
    }

    public BukkitRedisPlugin getRedis() {
        return bukkitRedis;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public ShrineTimeManager getShrineTimeManager() {
        return shrineTimeManager;
    }
}
