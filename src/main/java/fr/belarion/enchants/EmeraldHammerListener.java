package fr.belarion.enchants;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/** Le Hammer en Emeraude mine en 3x3 autour du bloc casse (item de demonstration). */
public class EmeraldHammerListener implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        ItemStack tool = player.getItemInHand();
        if (!EmeraldItems.isHammer(tool)) return;

        Block center = event.getBlock();
        BlockFace facing = getFacing(player);

        for (Block extra : get3x3Plane(center, facing)) {
            if (extra.getType() == center.getType()) {
                extra.breakNaturally(tool);
            }
        }
    }

    /** Equivalent de player.getFacing() qui n'existe pas en 1.8. */
    private BlockFace getFacing(Player player) {
        float pitch = player.getLocation().getPitch();
        if (pitch < -45f) return BlockFace.UP;
        if (pitch > 45f) return BlockFace.DOWN;

        float yaw = player.getLocation().getYaw() % 360f;
        if (yaw < 0) yaw += 360f;
        if (yaw >= 315f || yaw < 45f) return BlockFace.SOUTH;
        if (yaw < 135f) return BlockFace.WEST;
        if (yaw < 225f) return BlockFace.NORTH;
        return BlockFace.EAST;
    }

    private List<Block> get3x3Plane(Block center, BlockFace facing) {
        List<Block> blocks = new ArrayList<Block>();

        int[] axis1;
        int[] axis2;

        switch (facing) {
            case NORTH:
            case SOUTH:
                axis1 = new int[]{1, 0, 0};
                axis2 = new int[]{0, 1, 0};
                break;
            case EAST:
            case WEST:
                axis1 = new int[]{0, 0, 1};
                axis2 = new int[]{0, 1, 0};
                break;
            default:
                axis1 = new int[]{1, 0, 0};
                axis2 = new int[]{0, 0, 1};
                break;
        }

        for (int a = -1; a <= 1; a++) {
            for (int b = -1; b <= 1; b++) {
                if (a == 0 && b == 0) continue;
                int dx = axis1[0] * a + axis2[0] * b;
                int dy = axis1[1] * a + axis2[1] * b;
                int dz = axis1[2] * a + axis2[2] * b;
                blocks.add(center.getRelative(dx, dy, dz));
            }
        }
        return blocks;
    }
}
