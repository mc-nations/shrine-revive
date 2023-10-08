package com.itsziroy.shrinerevive.commands;

import com.itsziroy.shrinerevive.items.ShrineHopper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;


import java.util.Objects;

public class CommandCreateShrine extends Command {

    public CommandCreateShrine() {
        super("create");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            ShrineHopper shrineHopper = new ShrineHopper();
            ItemStack items = shrineHopper.getItemStack(1);
            player.getInventory().addItem(items);

            return true;
        }
        return true;
    }
}
