package fr.belarion.enchants;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class EnchantClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        if (EnchantGUI.TITLE_LIST.equals(title)) {
            event.setCancelled(true);
            if (event.getRawSlot() == EnchantGUI.SLOT_BACK) {
                player.openInventory(EnchantGUI.build());
            }
            return;
        }

        if (!EnchantGUI.TITLE.equals(title)) return;

        Inventory top = event.getView().getTopInventory();
        int raw = event.getRawSlot();

        if (raw >= top.getSize()) {
            if (event.isShiftClick()) event.setCancelled(true);
            return;
        }

        if (raw == EnchantGUI.SLOT_BOOK) return;

        event.setCancelled(true);

        if (raw == EnchantGUI.SLOT_LIST) {
            player.openInventory(EnchantGUI.buildList());
            return;
        }

        if (raw == EnchantGUI.SLOT_ENCHANT) {
            EnchantGUI.tryEnchant(player, top);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!EnchantGUI.TITLE.equals(event.getView().getTitle())) return;
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = (Player) event.getPlayer();
        Inventory top = event.getView().getTopInventory();

        ItemStack item = top.getItem(EnchantGUI.SLOT_BOOK);
        if (item != null && item.getType() != Material.AIR) {
            Map<Integer, ItemStack> leftovers = player.getInventory().addItem(item);
            for (ItemStack leftover : leftovers.values()) {
                player.getWorld().dropItem(player.getLocation(), leftover);
            }
            top.setItem(EnchantGUI.SLOT_BOOK, null);
        }
    }
}
