package fr.belarion.enchants;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.Iterator;
import java.util.List;

/**
 * Protege la Table d'Enchantement Emeraude et l'Enclume Emeraude :
 * - insensibles aux explosions (TNT / Creeper)
 * - impossible a deplacer par un piston
 * - l'Enclume Emeraude (Sea Lantern) est incassable, meme apres des
 *   milliers d'utilisations.
 */
public class BlockProtectionListener implements Listener {

    private boolean isProtected(Material type) {
        return type == Material.EMERALD_BLOCK || type == Material.SEA_LANTERN;
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        filter(event.blockList());
    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {
        filter(event.blockList());
    }

    private void filter(List<Block> blocks) {
        Iterator<Block> it = blocks.iterator();
        while (it.hasNext()) {
            if (isProtected(it.next().getType())) {
                it.remove();
            }
        }
    }

    @EventHandler
    public void onPistonExtend(BlockPistonExtendEvent event) {
        for (Block block : event.getBlocks()) {
            if (isProtected(block.getType())) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onPistonRetract(BlockPistonRetractEvent event) {
        for (Block block : event.getBlocks()) {
            if (isProtected(block.getType())) {
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.getBlock().getType() != Material.SEA_LANTERN) return;
        event.setCancelled(true);
        Player player = event.getPlayer();
        BelarionEnchants.get().getMessagesManager().send(player, "blocks.anvil-unbreakable");
    }
}
