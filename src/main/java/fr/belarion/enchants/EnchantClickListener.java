package fr.belarion.enchants;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class EnchantClickListener implements Listener {

    private final BelarionEnchants plugin;

    public EnchantClickListener(BelarionEnchants plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!EnchantGUI.TITLE.equals(event.getView().getTitle())) return;
        event.setCancelled(true);

        if (!(event.getWhoClicked() instanceof Player player)) return;

        CustomEnchant enchant = EnchantGUI.getEnchantFromClickedItem(plugin, event.getCurrentItem());
        if (enchant == null) return;

        EnchantGUI.tryPurchase(player, enchant, plugin);
        player.closeInventory();
    }
}
