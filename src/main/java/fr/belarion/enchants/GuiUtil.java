package fr.belarion.enchants;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Helpers partages par tous les GUI du plugin pour garder une identite
 * visuelle unique : vitres vert emeraude, separateurs, boutons premium.
 */
public final class GuiUtil {

    public static final String SEPARATOR = ChatColor.DARK_GREEN + "" + ChatColor.STRIKETHROUGH
            + "                    ";

    /** Vitre teintee verte (donnee 13 = vert emeraude) utilisee comme fond premium. */
    public static final short GLASS_GREEN = 13;
    /** Vitre vert clair, utilisee pour accentuer certains bords. */
    public static final short GLASS_LIME = 5;
    public static final short GLASS_BLACK = 15;

    private GuiUtil() {}

    public static ItemStack pane(short data) {
        ItemStack pane = new ItemStack(Material.STAINED_GLASS_PANE, 1, data);
        ItemMeta meta = pane.getItemMeta();
        meta.setDisplayName(" ");
        pane.setItemMeta(meta);
        return pane;
    }

    /** Remplit tout l'inventaire de vitres vert emeraude (fond par defaut). */
    public static void fillBackground(Inventory inv) {
        ItemStack filler = pane(GLASS_GREEN);
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, filler);
        }
    }

    public static ItemStack button(Material material, ChatColor titleColor, String title, String... loreLines) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(titleColor.toString() + ChatColor.BOLD + title);
        meta.setLore(Arrays.asList(loreLines));
        item.setItemMeta(meta);
        return item;
    }

    public static void appendLore(ItemStack item, List<String> extra) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(extra);
        item.setItemMeta(meta);
    }
}
