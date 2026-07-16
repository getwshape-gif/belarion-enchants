package fr.belarion.enchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryType;

/**
 * Bloc physique de l'Enclume en Émeraude. On réutilise un Lodestone comme
 * base (bloc peu utilisé par les joueurs, et distinct du bloc d'émeraude
 * déjà pris par la table d'enchantement) — à re-texturer plus tard avec
 * le pack de textures.
 *
 * Techniquement : on ouvre une VRAIE inventaire d'enclume vanilla
 * (Bukkit.createInventory(..., InventoryType.ANVIL, TITLE)), pas liée à un
 * bloc, donc toute la mécanique de réparation/combinaison/coût XP vanilla
 * fonctionne gratuitement. On la reconnaît ensuite dans AnvilApplyListener
 * grâce à son titre.
 */
public class EmeraldAnvilBlockListener implements Listener {

    public static final String TITLE = ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Enclume en Émeraude";

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        // Note : n'importe quel Lodestone sert d'enclume émeraude pour l'instant.
        // Si vous en placez ailleurs (boussole, déco), dites-le moi : je
        // passerai à un système par coordonnées précises comme pour le bloc
        // d'émeraude.
        if (event.getClickedBlock().getType() != Material.LODESTONE) return;

        event.setCancelled(true); // empêche le comportement vanilla (lier une boussole)
        Inventory anvil = Bukkit.createInventory(null, InventoryType.ANVIL, TITLE);
        event.getPlayer().openInventory(anvil);
    }
}
