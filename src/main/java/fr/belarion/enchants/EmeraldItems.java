package fr.belarion.enchants;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public final class EmeraldItems {

    public static final String TOOL_KEY = "beltool";

    private EmeraldItems() {}

    public static ItemStack createSword() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "\u00c9p\u00e9e en \u00c9meraude");
        meta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Tier : \u00c9meraude"));
        item.setItemMeta(meta);
        ItemTierUtil.setTier(item, ItemTier.EMERALD);
        return item;
    }

    public static ItemStack createHammer() {
        ItemStack item = new ItemStack(Material.DIAMOND_PICKAXE);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Hammer en \u00c9meraude");
        meta.setLore(Arrays.asList(
                ChatColor.DARK_GRAY + "Tier : \u00c9meraude",
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
        meta.setDisplayName(ChatColor.GREEN + "Bottes en \u00c9meraude");
        meta.setLore(Arrays.asList(ChatColor.DARK_GRAY + "Tier : \u00c9meraude"));
        item.setItemMeta(meta);
        ItemTierUtil.setTier(item, ItemTier.EMERALD);
        return item;
    }

    public static boolean isHammer(ItemStack item) {
        return "hammer".equals(HiddenTag.read(item, TOOL_KEY));
    }
}
