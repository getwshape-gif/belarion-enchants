package fr.belarion.enchants;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class EnchantTableListener implements Listener {

    private final BelarionEnchants plugin;

    public EnchantTableListener(BelarionEnchants plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        // Note : n'importe quel bloc d'émeraude sert de table pour l'instant.
        // Si vous en placez ailleurs en déco, dites-le moi, je passerai à un
        // système par emplacement précis (liste de coordonnées en config).
        if (event.getClickedBlock().getType() != Material.EMERALD_BLOCK) return;

        event.setCancelled(true);
        event.getPlayer().openInventory(EnchantGUI.build(plugin));
    }
}
