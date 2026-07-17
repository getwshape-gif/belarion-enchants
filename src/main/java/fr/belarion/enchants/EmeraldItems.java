package fr.belarion.enchants;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Items de demonstration / admin (commande /emeralditem). Sert aussi de
 * modele pour la creation future de nouveaux items emeraude (marteaux,
 * haches, armures, arcs...) : il suffit de suivre le meme patron
 * (ItemTierUtil.setTier + tags specifiques si besoin).
 */
public final class EmeraldItems {

    public static final String TOOL_KEY = "beltool";

    private EmeraldItems() {}

    public static ItemStack createSword() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Epee en Emeraude");
        meta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Tier : Emeraude"));
        item.setItemMeta(meta);
        ItemTierUtil.setTier(item, ItemTier.EMERALD);
        return item;
    }

    public static ItemStack createHammer() {
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Hammer en Emeraude");
        meta.setLore(Arrays.asList(
                ChatColor.DARK_GRAY + "Tier : Emeraude",
                ChatColor.GRAY + "Mine en 3x3"
        ));
        item.setItemMeta(meta);
        ItemTierUtil.setTier(item, ItemTier.EMERALD);
        HiddenTag.write(item, TOOL_KEY, "hammer");
        return item;
    }

    public static ItemStack createBoots() {
        ItemStack item = new ItemStack(Material.DIAMOND_BOOTS);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Bottes en Emeraude");
        meta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Tier : Emeraude"));
        item.setItemMeta(meta);
        ItemTierUtil.setTier(item, ItemTier.EMERALD);
        return item;
    }

    public static boolean isHammer(ItemStack item) {
        return "hammer".equals(HiddenTag.read(item, TOOL_KEY));
    }
}
