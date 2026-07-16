package fr.belarion.enchants;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class EnchantBookUtil {

    /** Clé PDC utilisée à la fois sur le livre ET sur l'arme une fois combinée à l'enclume. */
    public static final String PDC_KEY = "belarion_custom_enchant";

    public static ItemStack createBook(BelarionEnchants plugin, CustomEnchant enchant) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();

        meta.setDisplayName(enchant.getColor() + "Livre : " + enchant.getDisplayName());

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Enchant custom");
        for (String line : enchant.getLore()) {
            lore.add(ChatColor.DARK_GRAY + line);
        }
        lore.add("");
        lore.add(ChatColor.YELLOW + "Combine avec une arme sur une enclume");
        lore.add(ChatColor.YELLOW + "pour l'appliquer.");
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(plugin, PDC_KEY);
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, enchant.getId());

        book.setItemMeta(meta);
        return book;
    }

    /** Retourne l'enchant custom présent sur un item (livre ou arme déjà enchantée), sinon null. */
    public static CustomEnchant readEnchant(BelarionEnchants plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, PDC_KEY);
        String id = meta.getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if (id == null) return null;
        return CustomEnchant.fromId(id);
    }
}
