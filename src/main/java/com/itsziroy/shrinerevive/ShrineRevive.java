package com.itsziroy.shrinerevive;

import com.itsziroy.bukkitredis.BukkitRedisPlugin;
import com.itsziroy.shrinerevive.commands.CommandCreateShrine;
import com.itsziroy.shrinerevive.listeners.ShrineListener;
import com.itsziroy.shrinerevive.listeners.TokenListener;
import com.itsziroy.shrinerevive.managers.CommandManager;
import com.jeff_media.customblockdata.CustomBlockData;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public final class ShrineRevive extends JavaPlugin {

    private final CommandManager commandManager = new CommandManager();
    private BukkitRedisPlugin bukkitRedis;

    @Override
    public void onEnable() {
        // Plugin startup logic
        commandManager.registerCommand(new CommandCreateShrine());
        getServer().getPluginManager().registerEvents(new ShrineListener(this), this);
        getServer().getPluginManager().registerEvents(new TokenListener(this), this);

        BukkitRedisPlugin bukkitRedis = (BukkitRedisPlugin) Bukkit.getPluginManager().getPlugin("BukkitRedis");
        if (bukkitRedis != null) {
            getLogger().info("BukkitRedis extenstion loaded.");
            this.bukkitRedis = bukkitRedis;
        }

        CustomBlockData.registerListener(this);
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
        if(cmd.getName().equals("shrine")) {
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

    public static ShrineRevive getInstance() {
        return getPlugin(ShrineRevive.class);
    }

    public BukkitRedisPlugin getRedis() {
        return bukkitRedis;
    }
}
