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
 * Disposition fixe et volontairement centree : les enchants n'occupent
 * jamais que les slots 11-17 et 20-25 (sous-ensemble des 14 slots
 * 11,12,13,14,15,16,17,20,21,22,23,24,25,26 autorises pour l'affichage),
 * le slot 26 (coin bas-droite) etant reserve en permanence a la fleche
 * de pagination "Page suivante". Cette disposition est identique sur
 * TOUTES les pages, y compris les futures : ajouter un nouvel enchant
 * dans CustomEnchant remplit simplement la page courante puis, une fois
 * pleine, cree automatiquement une page suivante qui reprend exactement
 * le meme gabarit, sans aucun changement de code necessaire ici.
 */
public final class EnchantLibraryGUI {

    public static final String TITLE = ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "Bibliotheque d'Enchants";

    public static final int SLOT_BACK = 4;
    public static final int SLOT_PREV = 18;
    public static final int SLOT_NEXT = 26;

    /**
     * Disposition fixe et definitive des enchants, identique sur toutes les
     * pages. Sous-ensemble (13 slots) des 14 slots autorises : le 14e slot
     * (26, coin bas-droite) est reserve a la fleche "Page suivante", toujours
     * visible, prete pour les futures pages.
     */
    private static final int[] DISPLAY_SLOTS = new int[]{
            11, 12, 13, 14, 15, 16, 17,
            20, 21, 22, 23, 24, 25
    };

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
        for (int i = 0; i < count; i++) {
            inv.setItem(DISPLAY_SLOTS[i], buildDisplayItem(all[start + i]));
        }

        // Fleche "Page suivante" : toujours presente en bas a droite, meme
        // s'il n'existe qu'une seule page (systeme pret pour les futurs
        // enchants). Grisee/inactive tant qu'il n'y a rien apres, verte et
        // fonctionnelle des qu'une page suivante existe.
        boolean hasNext = start + pageSize < all.length;
        inv.setItem(SLOT_NEXT, buildNextButton(hasNext));

        // Fleche "Page precedente" : n'apparait que lorsqu'il existe
        // effectivement une page anterieure.
        if (page > 0) {
            inv.setItem(SLOT_PREV, GuiUtil.button(Material.ARROW, ChatColor.GREEN, "« Page precedente",
                    GuiUtil.SEPARATOR));
        }

        return inv;
    }

    private static ItemStack buildNextButton(boolean enabled) {
        if (enabled) {
            return GuiUtil.button(Material.ARROW, ChatColor.GREEN, "Page suivante »",
                    GuiUtil.SEPARATOR);
        }
        return GuiUtil.button(Material.ARROW, ChatColor.GRAY, "Page suivante",
                GuiUtil.SEPARATOR, ChatColor.DARK_GRAY + "Aucune page suivante.", GuiUtil.SEPARATOR);
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
