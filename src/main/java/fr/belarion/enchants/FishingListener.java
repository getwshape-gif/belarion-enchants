package fr.belarion.enchants;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Anti Rod : empeche un joueur portant l'enchant sur son armure d'etre
 * attire/deseauilibre par une canne a peche ennemie (state CAUGHT_ENTITY).
 */
public class FishingListener implements Listener {

    @EventHandler
    public void onFish(PlayerFishEvent event) {
        if (event.getState() != PlayerFishEvent.State.CAUGHT_ENTITY) return;

        Entity caught = event.getCaught();
        if (!(caught instanceof Player)) return;
        Player victim = (Player) caught;

        if (!hasAntiRod(victim)) return;
        event.setCancelled(true);
    }

    private boolean hasAntiRod(Player player) {
        return hasOn(player.getInventory().getBoots())
                || hasOn(player.getInventory().getHelmet())
                || hasOn(player.getInventory().getChestplate())
                || hasOn(player.getInventory().getLeggings());
    }

    private boolean hasOn(ItemStack item) {
        return item != null && ItemTierUtil.isEmeraldTier(item)
                && EnchantStorage.hasEnchant(item, CustomEnchant.ANTI_ROD);
    }
}
