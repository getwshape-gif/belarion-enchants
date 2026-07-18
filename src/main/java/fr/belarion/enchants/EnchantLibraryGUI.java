package fr.belarion.enchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

    /** Debut des deux rangees d'affichage (8 cases utiles chacune, hors boutons de nav). */
    private static final int ROW_1_START = 9;
    private static final int ROW_2_START = 18;
    private static final int ROW_WIDTH = 8;

    private EnchantLibraryGUI() {}

    public static Inventory build(int page) {
        CustomEnchant[] all = CustomEnchant.values();
        int pageSize = DISPLAY_SLOTS.length;
        int maxPage = Math.max(0, (all.length - 1) / pageSize);
        if (page < 0) page = 0;
        if (page > maxPage) page = maxPage;

        Inventory inv = Bukkit.createInventory(null, 27, TITLE);
        GuiUtil.fillPremiumBackground(inv, new int[]{0, 8, 17, 26});

        ItemStack back = GuiUtil.button(Material.ARROW, ChatColor.WHITE, "Retour",
                GuiUtil.SEPARATOR, ChatColor.GRAY + "Retour a la table.", GuiUtil.SEPARATOR);
        // La quantite de ce bouton encode discretement la page actuelle (page + 1)
        // pour permettre au listener de calculer suivant/precedent sans etat externe.
        back.setAmount(page + 1);
        inv.setItem(SLOT_BACK, back);

        int start = page * pageSize;
        int count = Math.min(pageSize, all.length - start);
        int[] slots = centeredSlots(count);
        for (int i = 0; i < count; i++) {
            inv.setItem(slots[i], buildDisplayItem(all[start + i]));
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

    /**
     * Calcule des slots centres pour "count" enchants (0 a 16), repartis sur
     * les deux rangees d'affichage (8 cases chacune). Sur une seule rangee
     * necessaire, les items sont centres horizontalement plutot que colles
     * a gauche. Sur deux rangees, chacune est centree independamment, la
     * rangee du haut recevant l'element en plus si le total est impair.
     */
    private static int[] centeredSlots(int count) {
        int[] slots = new int[count];
        if (count <= ROW_WIDTH) {
            int pad = (ROW_WIDTH - count) / 2;
            for (int i = 0; i < count; i++) {
                slots[i] = ROW_1_START + pad + i;
            }
            return slots;
        }

        int row1Count = (count + 1) / 2;
        int row2Count = count - row1Count;
        int pad1 = (ROW_WIDTH - row1Count) / 2;
        int pad2 = (ROW_WIDTH - row2Count) / 2;

        int idx = 0;
        for (int i = 0; i < row1Count; i++) {
            slots[idx++] = ROW_1_START + pad1 + i;
        }
        for (int i = 0; i < row2Count; i++) {
            slots[idx++] = ROW_2_START + pad2 + i;
        }
        return slots;
    }

    private static ItemStack buildDisplayItem(CustomEnchant enchant) {
        ItemStack item = new ItemStack(enchant.getIcon());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(enchant.getColor().toString() + ChatColor.BOLD + enchant.getDisplayName());
        meta.setLore(GuiUtil.buildEnchantLore(enchant));
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
