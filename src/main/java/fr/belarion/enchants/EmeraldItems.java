package fr.belarion.enchants;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

/**
 * Fabrique des items custom du tier Émeraude.
 *
 * Base technique : on part d'un item vanilla existant (diamant) pour
 * hériter de ses stats/mécaniques de base, on le tag avec ItemTier, et on
 * lui donne un "custom model data" que le futur resource pack pourra
 * cibler pour changer complètement l'apparence (modèle/texture émeraude).
 *
 * Custom model data utilisés (à documenter pour le pack de texture) :
 *   2001 = Épée Émeraude
 *   2002 = Hammer Émeraude (pioche, mine 3x3)
 */
public class EmeraldItems {

    public static final String TOOL_TYPE_KEY = "belarion_tool_type";

    public static ItemStack createSword(BelarionEnchants plugin) {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Épée en Émeraude");
        meta.setLore(List.of(ChatColor.DARK_GREEN + "Tier : Émeraude"));
        meta.setCustomModelData(2001);
        item.setItemMeta(meta);
        ItemTierUtil.setTier(plugin, item, ItemTier.EMERALD);
        return item;
    }

    public static ItemStack createHammer(BelarionEnchants plugin) {
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Hammer en Émeraude");
        meta.setLore(List.of(
                ChatColor.DARK_GREEN + "Tier : Émeraude",
                ChatColor.GRAY + "Mine en 3x3"
        ));
        meta.setCustomModelData(2002);
        item.setItemMeta(meta);
        ItemTierUtil.setTier(plugin, item, ItemTier.EMERALD);

        ItemStack tagged = item;
        ItemMeta tagMeta = tagged.getItemMeta();
        tagMeta.getPersistentDataContainer().set(
                new NamespacedKey(plugin, TOOL_TYPE_KEY), PersistentDataType.STRING, "hammer");
        tagged.setItemMeta(tagMeta);
        return tagged;
    }

    public static boolean isHammer(BelarionEnchants plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return false;
        String type = item.getItemMeta().getPersistentDataContainer()
                .get(new NamespacedKey(plugin, TOOL_TYPE_KEY), PersistentDataType.STRING);
        return "hammer".equals(type);
    }
}
