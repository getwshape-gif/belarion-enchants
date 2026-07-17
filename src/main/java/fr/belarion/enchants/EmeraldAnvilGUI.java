package fr.belarion.enchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * GUI de l'Enclume Emeraude : 27 slots, fond premium.
 * Item emeraude : slot 11. Livre (vanilla ou custom) : slot 15. Forger : slot 13.
 * Prix fixe (config.yml costs.emerald-anvil, 30 niveaux par defaut), toujours.
 */
public final class EmeraldAnvilGUI {

    public static final String TITLE = ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "✦ Enclume Emeraude ✦";
    public static final int SLOT_ITEM = 11;
    public static final int SLOT_CONFIRM = 13;
    public static final int SLOT_BOOK = 15;

    private EmeraldAnvilGUI() {}

    public static Inventory build() {
        Inventory inv = Bukkit.createInventory(null, 27, TITLE);
        GuiUtil.fillBackground(inv);

        inv.setItem(SLOT_ITEM, null);
        inv.setItem(SLOT_BOOK, null);
        inv.setItem(SLOT_CONFIRM, buildConfirmButton());

        return inv;
    }

    public static ItemStack buildConfirmButton() {
        int cost = BelarionEnchants.get().getConfigManager().getEmeraldAnvilCost();
        return GuiUtil.button(Material.ANVIL, ChatColor.GREEN, "✦ Forger ✦",
                GuiUtil.SEPARATOR,
                ChatColor.GRAY + "Item Emeraude a gauche,",
                ChatColor.GRAY + "livre (custom ou vanilla) a droite.",
                "",
                ChatColor.YELLOW + "Cout" + ChatColor.WHITE + " " + cost + " niveaux",
                GuiUtil.SEPARATOR);
    }
}
