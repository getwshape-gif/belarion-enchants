package fr.belarion.enchants;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Quand un joueur casse un bloc avec le Hammer Émeraude, les 8 blocs
 * autour (dans le même plan que la face visée) sont cassés aussi, à
 * condition d'être le même type de bloc que celui visé (pour éviter
 * de détruire des constructions/blocs différents par accident).
 */
public class EmeraldHammerListener implements Listener {

    private final BelarionEnchants plugin;

    public EmeraldHammerListener(BelarionEnchants plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (!EmeraldItems.isHammer(plugin, tool)) return;

        Block center = event.getBlock();
        BlockFace facing = player.getFacing();

        for (Block extra : get3x3Plane(center, facing)) {
            if (extra.getType() == center.getType()) {
                extra.breakNaturally(tool);
            }
        }
    }

    /**
     * Retourne les 8 blocs qui entourent le bloc central, dans le plan
     * perpendiculaire à la direction où regarde le joueur (donc "3x3
     * vu de face" plutôt que 3x3 en profondeur).
     */
    private Iterable<Block> get3x3Plane(Block center, BlockFace facing) {
        java.util.List<Block> blocks = new java.util.ArrayList<>();

        int[] axis1;
        int[] axis2;

        switch (facing) {
            case NORTH, SOUTH -> {
                axis1 = new int[]{1, 0, 0}; // x
                axis2 = new int[]{0, 1, 0}; // y
            }
            case EAST, WEST -> {
                axis1 = new int[]{0, 0, 1}; // z
                axis2 = new int[]{0, 1, 0}; // y
            }
            default -> { // UP, DOWN
                axis1 = new int[]{1, 0, 0}; // x
                axis2 = new int[]{0, 0, 1}; // z
            }
        }

        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                if (a == 0 && b == 0) continue; // le centre est déjà cassé par l'event d'origine
                int dx = axis1[0] * a + axis2[0] * b;
                int dy = axis1[1] * a + axis2[1] * b;
                int dz = axis1[2] * a + axis2[2] * b;
                blocks.add(center.getRelative(dx, dy, dz));
            }
        }
        return blocks;
    }
}
