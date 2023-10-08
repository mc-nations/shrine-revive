package com.itsziroy.shrinerevive.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.permissions.Permissible;

public abstract class Command {

    public static boolean hasPermission(Permissible sender, String permission) {
        return sender instanceof ConsoleCommandSender || sender.hasPermission(permission);
    }

    private final String name;

    private final String permission;

    public Command(String name, String permission) {
        this.name = name;
        this.permission = permission;
    }

    public Command(String name) {
        this.name = name;
        this.permission = "shrine.default";
    }

    public abstract boolean execute(CommandSender sender, String[] args);

    public boolean hasPermission(Permissible sender) {
        return sender instanceof ConsoleCommandSender || sender.hasPermission(permission);
    }

    public String getName() {
        return name;
    }

    public String getPermission() {
        return permission;
    }
}
