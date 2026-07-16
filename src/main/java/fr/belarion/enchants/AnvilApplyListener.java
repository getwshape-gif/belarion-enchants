package fr.belarion.enchants;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Règles des enclumes :
 *
 *  - Une enclume NORMALE (vanilla) refuse toute opération dès qu'un des
 *    deux items est du tier Émeraude (réparation, renommage, combinaison,
 *    tout est bloqué : "prend l'enclume en émeraude").
 *
 *  - L'Enclume en Émeraude (bloc Lodestone custom, voir
 *    EmeraldAnvilBlockListener) laisse le fonctionnement vanilla intact
 *    pour le stuff émeraude (réparation, renommage, enchants vanilla de
 *    base), ET permet en plus d'appliquer un livre d'enchant custom
 *    dessus.
 */
public class AnvilApplyListener implements Listener {

    private final BelarionEnchants plugin;
    private final Map<UUID, Long> lastWarning = new HashMap<>();

    public AnvilApplyListener(BelarionEnchants plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory anvil = event.getInventory();
        ItemStack base = anvil.getItem(0);
        ItemStack addition = anvil.getItem(1);

        boolean isEmeraldAnvil = EmeraldAnvilBlockListener.TITLE.equals(event.getView().getTitle());

        boolean baseIsEmerald = ItemTierUtil.getTier(plugin, base) != null;
        boolean additionIsEmerald = ItemTierUtil.getTier(plugin, addition) != null;

        if (!isEmeraldAnvil) {
            // Enclume normale : blocage total dès qu'un item émeraude est impliqué
            if (baseIsEmerald || additionIsEmerald) {
                event.setResult(null);
                warnViewers(event, ChatColor.RED + "Le stuff émeraude nécessite une Enclume en Émeraude.");
            }
            return;
        }

        // On est sur l'Enclume en Émeraude.
        if (base == null || addition == null) return;

        CustomEnchant enchant = EnchantBookUtil.readEnchant(plugin, addition);
        if (enchant == null) return; // pas un livre custom : on laisse faire le vanilla (repair/rename/enchant book de base)

        if (!baseIsEmerald) {
            event.setResult(null);
            warnViewers(event, ChatColor.RED + "Les enchants custom ne s'appliquent que sur du stuff/outils émeraude.");
            return;
        }

        ItemStack result = base.clone();
        ItemMeta meta = result.getItemMeta();

        List<String> lore = meta.hasLore() ? new ArrayList<>(meta.getLore()) : new ArrayList<>();
        String tagLine = enchant.getColor() + "" + ChatColor.BOLD + enchant.getDisplayName();
        if (!lore.contains(tagLine)) {
            lore.add(tagLine);
        }
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(plugin, EnchantBookUtil.PDC_KEY);
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, enchant.getId());

        result.setItemMeta(meta);
        event.setResult(result);
    }

    /** Anti-spam : un seul message tous les 2 secondes par joueur (PrepareAnvilEvent se déclenche souvent). */
    private void warnViewers(PrepareAnvilEvent event, String message) {
        long now = System.currentTimeMillis();
        for (HumanEntity viewer : event.getViewers()) {
            if (!(viewer instanceof Player player)) continue;
            long last = lastWarning.getOrDefault(player.getUniqueId(), 0L);
            if (now - last > 2000) {
                player.sendMessage(message);
                lastWarning.put(player.getUniqueId(), now);
            }
        }
    }
}
