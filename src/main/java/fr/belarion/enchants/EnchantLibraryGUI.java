package fr.belarion.enchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Bibliotheque d'Enchants : liste absolument tous les custom enchants
 * disponibles avec nom / description / compatibilite / effet.
 *
 * Paginee des la conception : la grille d'affichage tient 16 enchants par
 * page (DISPLAY_SLOTS), avec des boutons Page suivante / precedente. Ajouter
 * un 17e enchant dans CustomEnchant ne demande donc aucun changement ici.
 */
public final class EnchantLibraryGUI {

    public static final String TITLE = ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "Bibliotheque d'Enchants";

    public static final int SLOT_BACK = 4;
    public static final int SLOT_NEXT = 17;
    public static final int SLOT_PREV = 26;

    private static final int[] DISPLAY_SLOTS = new int[]{
            9, 10, 11, 12, 13, 14, 15, 16,
            18, 19, 20, 21, 22, 23, 24, 25
    };

    private EnchantLibraryGUI() {}

    public static Inventory build(int page) {
        CustomEnchant[] all = CustomEnchant.values();
        int pageSize = DISPLAY_SLOTS.length;
        int maxPage = Math.max(0, (all.length - 1) / pageSize);
        if (page < 0) page = 0;
        if (page > maxPage) page = maxPage;

        Inventory inv = Bukkit.createInventory(null, 27, TITLE);
        GuiUtil.fillBackground(inv);

        ItemStack back = GuiUtil.button(Material.ARROW, ChatColor.WHITE, "Retour",
                GuiUtil.SEPARATOR, ChatColor.GRAY + "Retour a la table.", GuiUtil.SEPARATOR);
        // La quantite de ce bouton encode discretement la page actuelle (page + 1)
        // pour permettre au listener de calculer suivant/precedent sans etat externe.
        back.setAmount(page + 1);
        inv.setItem(SLOT_BACK, back);

        int start = page * pageSize;
        for (int i = 0; i < pageSize; i++) {
            int index = start + i;
            if (index >= all.length) break;
            inv.setItem(DISPLAY_SLOTS[i], buildDisplayItem(all[index]));
        }

        if (start + pageSize < all.length) {
            inv.setItem(SLOT_NEXT, GuiUtil.button(Material.ARROW, ChatColor.GREEN, "Page suivante »",
                    GuiUtil.SEPARATOR));
        }
        if (page > 0) {
            inv.setItem(SLOT_PREV, GuiUtil.button(Material.ARROW, ChatColor.GREEN, "« Page precedente",
                    GuiUtil.SEPARATOR));
        }

        return inv;
    }

    private static ItemStack buildDisplayItem(CustomEnchant enchant) {
        ItemStack item = new ItemStack(enchant.getIcon());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(enchant.getColor().toString() + ChatColor.BOLD + "✦ " + enchant.getDisplayName() + " ✦");

        List<String> lore = new ArrayList<String>();
        lore.add(GuiUtil.SEPARATOR);
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
        lore.add(ChatColor.GREEN + "" + ChatColor.ITALIC + "Belarion");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public static boolean isDisplaySlot(int slot) {
        for (int s : DISPLAY_SLOTS) {
            if (s == slot) return true;
        }
        return false;
    }
}
