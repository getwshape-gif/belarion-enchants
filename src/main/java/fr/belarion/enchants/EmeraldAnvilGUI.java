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
 * L'Enclume en Émeraude est un menu 27 cases :
 *   case 11 = item émeraude, case 15 = livre, case 13 = bouton confirmer.
 * Accepte les livres custom ET les livres d'enchant vanilla — mais
 * uniquement sur du stuff/outils émeraude.
 */
public final class EmeraldAnvilGUI {

    public static final String TITLE = ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Enclume en Émeraude";
    public static final int SLOT_ITEM = 11;
    public static final int SLOT_CONFIRM = 13;
    public static final int SLOT_BOOK = 15;
    public static final int COST_LEVELS = 10;

    private EmeraldAnvilGUI() {}

    public static Inventory build() {
        Inventory inv = Bukkit.createInventory(null, 27, TITLE);

        ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 13); // vitre verte
        ItemMeta fm = filler.getItemMeta();
        fm.setDisplayName(" ");
        filler.setItemMeta(fm);
        for (int i = 0; i < 27; i++) {
            if (i != SLOT_ITEM && i != SLOT_BOOK && i != SLOT_CONFIRM) {
                inv.setItem(i, filler);
            }
        }

        inv.setItem(SLOT_CONFIRM, buildConfirmButton());
        return inv;
    }

    public static ItemStack buildConfirmButton() {
        ItemStack confirm = new ItemStack(Material.ANVIL);
        ItemMeta meta = confirm.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Combiner");
        List<String> lore = new ArrayList<String>();
        lore.add(ChatColor.GRAY + "Item émeraude à gauche,");
        lore.add(ChatColor.GRAY + "livre (custom ou vanilla) à droite.");
        lore.add("");
        lore.add(ChatColor.YELLOW + "Coût : " + COST_LEVELS + " niveaux");
        meta.setLore(lore);
        confirm.setItemMeta(meta);
        return confirm;
    }
}
