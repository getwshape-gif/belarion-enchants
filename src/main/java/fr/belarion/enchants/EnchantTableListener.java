package fr.belarion.enchants;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * Gere tout le cycle de vie de la Table d'Enchantement Emeraude :
 * ouverture (clic droit sur un Bloc d'Emeraude), clics dans la table et
 * dans la bibliotheque d'enchants (avec pagination), fermeture (rend le
 * livre pose).
 */
public class EnchantTableListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.EMERALD_BLOCK) return;

        event.setCancelled(true);
        event.getPlayer().openInventory(EnchantTableGUI.build());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        if (EnchantLibraryGUI.TITLE.equals(title)) {
            handleLibraryClick(event, player);
            return;
        }

        if (!EnchantTableGUI.TITLE.equals(title)) return;
        handleTableClick(event, player);
    }

    private void handleTableClick(InventoryClickEvent event, Player player) {
        Inventory top = event.getView().getTopInventory();
        int raw = event.getRawSlot();

        if (raw >= top.getSize()) {
            if (event.isShiftClick()) event.setCancelled(true);
            return;
        }

        if (raw == EnchantTableGUI.SLOT_BOOK) return;

        event.setCancelled(true);

        if (raw == EnchantTableGUI.SLOT_LIBRARY) {
            player.openInventory(EnchantLibraryGUI.build(0));
            return;
        }

        if (raw == EnchantTableGUI.SLOT_ENCHANT) {
            EnchantTableGUI.tryEnchant(player, top);
        }
    }

    private void handleLibraryClick(InventoryClickEvent event, Player player) {
        event.setCancelled(true);
        int raw = event.getRawSlot();

        if (raw == EnchantLibraryGUI.SLOT_BACK) {
            player.openInventory(EnchantTableGUI.build());
            return;
        }
        if (raw == EnchantLibraryGUI.SLOT_NEXT) {
            int page = getPage(event.getView().getTopInventory()) + 1;
            player.openInventory(EnchantLibraryGUI.build(page));
            return;
        }
        if (raw == EnchantLibraryGUI.SLOT_PREV) {
            int page = getPage(event.getView().getTopInventory()) - 1;
            player.openInventory(EnchantLibraryGUI.build(page));
        }
    }

    /** La page actuelle est encodee dans la quantite du bouton Retour (page + 1). */
    private int getPage(Inventory top) {
        ItemStack back = top.getItem(EnchantLibraryGUI.SLOT_BACK);
        if (back == null) return 0;
        return Math.max(0, back.getAmount() - 1);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!EnchantTableGUI.TITLE.equals(event.getView().getTitle())) return;
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = (Player) event.getPlayer();
        Inventory top = event.getView().getTopInventory();

        ItemStack item = top.getItem(EnchantTableGUI.SLOT_BOOK);
        if (item != null && item.getType() != Material.AIR) {
            Map<Integer, ItemStack> leftovers = player.getInventory().addItem(item);
            for (ItemStack leftover : leftovers.values()) {
                player.getWorld().dropItem(player.getLocation(), leftover);
            }
            top.setItem(EnchantTableGUI.SLOT_BOOK, null);
        }
    }
}
