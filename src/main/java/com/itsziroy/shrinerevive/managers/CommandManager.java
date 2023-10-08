package com.itsziroy.shrinerevive.managers;

import com.itsziroy.shrinerevive.commands.Command;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    public Map<String, Command> getCommands() {
        return commands;
    }

    private final Map<String, Command> commands = new HashMap<>();

    public boolean onCommand(@NonNull CommandSender sender, String command, String[] args) {
        Command executable = commands.get(command);

        if(executable == null) {
            sender.sendMessage("Command not found!");
            return false;
        }

        if(!executable.hasPermission(sender)) {
            sender.sendMessage("No permission!");
            return false;
        }

        return executable.execute(sender, args);
    }

    public void registerCommand(Command command) {
        this.commands.put(command.getName(), command);
    }

    public void removeCommand(Command command) {
        this.commands.remove(command.getName());
    }

}
