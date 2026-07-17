package fr.belarion.enchants;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class EnchantBookUtil {

    public static final String TAG_KEY = "belench";

    private EnchantBookUtil() {}

    public static ItemStack createBook(CustomEnchant enchant) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName(enchant.getColor() + "Livre : " + enchant.getDisplayName());

        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "Enchant custom");
        for (String line : enchant.getLore()) {
            lore.add(ChatColor.DARK_GRAY + line);
        }
        lore.add("");
        lore.add(ChatColor.YELLOW + "Combine avec un item émeraude");
        lore.add(ChatColor.YELLOW + "dans l'Enclume en Émeraude.");
        meta.setLore(lore);
        book.setItemMeta(meta);

        HiddenTag.write(book, TAG_KEY, enchant.getId());
        return book;
    }

    /** Enchant custom présent sur un item (livre ou arme), sinon null. */
    public static CustomEnchant readEnchant(ItemStack item) {
        String id = HiddenTag.read(item, TAG_KEY);
        if (id == null) return null;
        return CustomEnchant.fromId(id);
    }

    /** Applique l'enchant custom sur un item (ligne de lore visible + tag caché). */
    public static void applyToItem(ItemStack item, CustomEnchant enchant) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? new ArrayList<String>(meta.getLore()) : new ArrayList<String>();
        String tagLine = enchant.getColor().toString() + ChatColor.BOLD + enchant.getDisplayName();
        if (!lore.contains(tagLine)) {
            lore.add(tagLine);
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        HiddenTag.write(item, TAG_KEY, enchant.getId());
    }
}
