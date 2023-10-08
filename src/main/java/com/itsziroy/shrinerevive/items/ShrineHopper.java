package com.itsziroy.shrinerevive.items;

import com.itsziroy.shrinerevive.ItemKeys;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class ShrineHopper implements Item{
    @Override
    public ItemStack getItemStack(int amount) {
        ItemStack shrine = new ItemStack(Material.HOPPER);

        shrine.setAmount(amount);
        ItemMeta itemMeta = Objects.requireNonNull(shrine.getItemMeta());

        itemMeta.setDisplayName(ChatColor.AQUA + "Shrine");

        itemMeta.getPersistentDataContainer().set(ItemKeys.SHRINE, PersistentDataType.BOOLEAN, true);

        shrine.setItemMeta(itemMeta);

        return shrine;
    }
}
