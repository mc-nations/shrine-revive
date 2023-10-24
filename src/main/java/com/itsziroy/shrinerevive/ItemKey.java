package com.itsziroy.shrinerevive;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public record ItemKey<T, Z>(NamespacedKey key, PersistentDataType<T, Z> dataType) {

    public static <T, K> boolean itemHasKey(ItemKey<T, K> key, Item item) {
        return ItemKey.itemHasKey(key, item.getItemStack());
    }

    public static <T, K> boolean itemHasKey(ItemKey<T, K> key, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta != null) {
            return itemMeta.getPersistentDataContainer().has(key.key(), key.dataType());
        }
        return false;
    }
    public static <T, K> K getItemMeta(ItemKey<T, K> key, Item item) {
        return ItemKey.getItemMeta(key, item.getItemStack());
    }

    public static <T, K> K getItemMeta(ItemKey<T, K> key, ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        if(itemMeta != null) {
            return itemMeta.getPersistentDataContainer().get(key.key(), key.dataType());
        }
        return null;
    }

    public static ItemKey<Byte, Boolean> SHRINE = new ItemKey<>(new NamespacedKey(ShrineRevive.getInstance(), "shrine_custom_item"), PersistentDataType.BOOLEAN);
    public static ItemKey<String, String> REVIVE_TOKEN = new ItemKey<>(new NamespacedKey(ShrineRevive.getInstance(), "shrine_revive_token"), PersistentDataType.STRING);

}
