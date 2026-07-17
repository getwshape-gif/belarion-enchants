package fr.belarion.enchants;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/** Creation / lecture des livres custom obtenus a la Table d'Enchantement Emeraude. */
public final class EnchantBookUtil {

    public static final String TAG_KEY = "belench";

    private EnchantBookUtil() {}

    public static ItemStack createBook(CustomEnchant enchant) {
        ItemStack book = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = book.getItemMeta();
        meta.setDisplayName(enchant.getColor().toString() + ChatColor.BOLD + "Livre : " + enchant.getDisplayName());

        List<String> lore = new ArrayList<String>();
        lore.add(GuiUtil.SEPARATOR);
        lore.add("");
        lore.add(enchant.getColor().toString() + ChatColor.BOLD + "✦ " + enchant.getDisplayName() + " ✦");
        lore.add("");
        for (String line : enchant.getDescription()) {
            lore.add(ChatColor.GRAY + line);
        }
        lore.add("");
        lore.add(GuiUtil.SEPARATOR);
        lore.add("");
        lore.add(ChatColor.DARK_GREEN + "Compatible");
        lore.add(ChatColor.GREEN + "✔ " + enchant.getTarget().getLabel());
        lore.add("");
        lore.add(GuiUtil.SEPARATOR);
        lore.add("");
        lore.add(ChatColor.DARK_GREEN + "Effet");
        for (String line : enchant.getEffectLines()) {
            lore.add(ChatColor.GREEN + "✔ " + line);
        }
        lore.add("");
        lore.add(GuiUtil.SEPARATOR);
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "A combiner dans l'Enclume");
        lore.add(ChatColor.DARK_GRAY + "en Emeraude.");
        lore.add("");
        lore.add(ChatColor.GREEN + "" + ChatColor.ITALIC + "Belarion");
        meta.setLore(lore);
        book.setItemMeta(meta);

        HiddenTag.write(book, TAG_KEY, enchant.getId());
        return book;
    }

    /** Custom enchant contenu dans un livre custom, sinon null. */
    public static CustomEnchant readEnchant(ItemStack item) {
        String id = HiddenTag.read(item, TAG_KEY);
        if (id == null) return null;
        return CustomEnchant.fromId(id);
    }

    public static boolean isCustomBook(ItemStack item) {
        return readEnchant(item) != null;
    }
}
