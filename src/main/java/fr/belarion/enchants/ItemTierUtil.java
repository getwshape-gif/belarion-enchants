package fr.belarion.enchants;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

public class ItemTierUtil {

    public static final String PDC_KEY = "belarion_item_tier";

    public static void setTier(BelarionEnchants plugin, ItemStack item, ItemTier tier) {
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, PDC_KEY);
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, tier.getId());
        item.setItemMeta(meta);
    }

    public static ItemTier getTier(BelarionEnchants plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        NamespacedKey key = new NamespacedKey(plugin, PDC_KEY);
        String id = item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if (id == null) return null;
        return ItemTier.fromId(id);
    }
}
