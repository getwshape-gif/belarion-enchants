package fr.belarion.enchants;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class EnchantClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!EnchantGUI.TITLE.equals(event.getView().getTitle())) return;
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        CustomEnchant enchant = EnchantGUI.getEnchantFromClickedItem(event.getCurrentItem());
        if (enchant == null) return;

        EnchantGUI.tryPurchase(player, enchant);
        player.closeInventory();
    }
}
