package fr.belarion.enchants;

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class CombatListener implements Listener {

    private final BelarionEnchants plugin;

    public CombatListener(BelarionEnchants plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player attacker)) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;

        ItemStack weapon = attacker.getInventory().getItemInMainHand();
        CustomEnchant enchant = EnchantBookUtil.readEnchant(plugin, weapon);
        if (enchant != CustomEnchant.VAMPIRISME) return;

        double heal = event.getFinalDamage() * enchant.getValue();
        double maxHealth = attacker.getAttribute(Attribute.MAX_HEALTH).getValue();
        attacker.setHealth(Math.min(maxHealth, attacker.getHealth() + heal));
    }
}
