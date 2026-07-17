package fr.belarion.enchants;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class ArmorEffectTask extends BukkitRunnable {

    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ItemStack boots = player.getInventory().getBoots();
            CustomEnchant enchant = EnchantBookUtil.readEnchant(boots);
            if (enchant == CustomEnchant.SPEED_II) {
                player.addPotionEffect(new PotionEffect(
                        PotionEffectType.SPEED, 60, (int) enchant.getValue(), true, false), true);
            }
        }
    }
}

