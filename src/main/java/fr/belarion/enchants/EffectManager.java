package fr.belarion.enchants;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Tache centrale unique qui gere TOUS les effets passifs des custom enchants
 * (Speed, Strength, Fire Resistance, Haste II, Anti Debuff).
 *
 * Un seul BukkitRunnable pour tout le plugin, execute une fois par seconde
 * (20 ticks) au lieu de multiplier les listeners/tasks par enchant : c'est
 * le point d'optimisation principal pour un serveur avec beaucoup de
 * joueurs. Pour chaque joueur on ne lit qu'une fois l'armure + l'item en
 * main, on en deduit l'ensemble des enchants actifs, puis on applique les
 * effets correspondants.
 */
public class EffectManager extends BukkitRunnable {

    private static final PotionEffectType[] NEGATIVE_EFFECTS = new PotionEffectType[]{
            PotionEffectType.POISON,
            PotionEffectType.SLOW,
            PotionEffectType.WEAKNESS,
            PotionEffectType.BLINDNESS,
            PotionEffectType.WITHER,
            PotionEffectType.SLOW_DIGGING,
            PotionEffectType.HUNGER,
            PotionEffectType.CONFUSION
    };

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            handlePlayer(player);
        }
    }

    private void handlePlayer(Player player) {
        PlayerInventory inv = player.getInventory();

        Set<CustomEnchant> active = EnumSet.noneOf(CustomEnchant.class);
        collect(inv.getBoots(), active);
        collect(inv.getHelmet(), active);
        collect(inv.getChestplate(), active);
        collect(inv.getLeggings(), active);

        ItemStack held = inv.getItemInHand();
        List<CustomEnchant> heldEnchants = held != null ? EnchantStorage.getEnchants(held) : null;
        boolean haste = heldEnchants != null && heldEnchants.contains(CustomEnchant.HASTE_II);

        if (active.contains(CustomEnchant.SPEED)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 60, 1, true, false), true);
        }
        if (active.contains(CustomEnchant.STRENGTH)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 60, 0, true, false), true);
        }
        if (active.contains(CustomEnchant.FIRE_RESISTANCE)) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 60, 0, true, false), true);
        }
        if (haste) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 60, 1, true, false), true);
        }
        if (active.contains(CustomEnchant.ANTI_DEBUFF)) {
            cleanseNegativeEffects(player);
        }
    }

    private void collect(ItemStack item, Set<CustomEnchant> into) {
        if (item == null || !ItemTierUtil.isEmeraldTier(item)) return;
        into.addAll(EnchantStorage.getEnchants(item));
    }

    private void cleanseNegativeEffects(Player player) {
        for (PotionEffectType type : NEGATIVE_EFFECTS) {
            if (player.hasPotionEffect(type)) {
                player.removePotionEffect(type);
            }
        }
    }
}
