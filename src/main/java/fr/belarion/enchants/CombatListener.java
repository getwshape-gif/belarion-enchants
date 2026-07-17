package fr.belarion.enchants;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class CombatListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player)) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;
        Player attacker = (Player) event.getDamager();

        ItemStack weapon = attacker.getItemInHand();
        CustomEnchant enchant = EnchantBookUtil.readEnchant(weapon);
        if (enchant != CustomEnchant.VAMPIRISME) return;

        double heal = event.getFinalDamage() * enchant.getValue();
        double maxHealth = attacker.getMaxHealth();
        attacker.setHealth(Math.min(maxHealth, attacker.getHealth() + heal));
    }
}
