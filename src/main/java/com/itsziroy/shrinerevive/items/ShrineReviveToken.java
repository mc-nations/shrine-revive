package com.itsziroy.shrinerevive.items;

import com.itsziroy.shrinerevive.ItemKeys;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Objects;

public class ShrineReviveToken implements Item {

    private final Player player;

    public ShrineReviveToken(Player player) {
        this.player = player;
    }

    @Override
    public ItemStack getItemStack(int amount) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, amount);
        SkullMeta skull = (SkullMeta) Objects.requireNonNull(item.getItemMeta());

        skull.setDisplayName(player.getName() + " Revive Token");
        ArrayList<String> lore = new ArrayList<>();
        lore.add("This token can be used to revive the player");
        skull.setLore(lore);
        skull.setOwningPlayer(player);

        skull.getPersistentDataContainer().set(ItemKeys.REVIVE_TOKEN, PersistentDataType.STRING, player.getUniqueId().toString());

        item.setItemMeta(skull);
        return item;
    }
}
