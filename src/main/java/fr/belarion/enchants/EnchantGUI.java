package fr.belarion.enchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public final class EnchantGUI {

    public static final String TITLE = ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Table d'Enchantement";
    public static final int COST_LEVELS = 60;
    public static final String SLOT_KEY = "belslot";

    private static final int[] DISPLAY_SLOTS = new int[]{11, 13, 15};

    private EnchantGUI() {}

    public static Inventory build() {
        Inventory inv = Bukkit.createInventory(null, 27, TITLE);

        CustomEnchant[] enchants = CustomEnchant.values();
        for (int i = 0; i < enchants.length && i < DISPLAY_SLOTS.length; i++) {
            inv.setItem(DISPLAY_SLOTS[i], buildDisplayItem(enchants[i]));
        }

        ItemStack info = new ItemStack(Material.EXP_BOTTLE);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(ChatColor.GREEN + "Coût : " + COST_LEVELS + " niveaux");
        List<String> infoLore = new ArrayList<String>();
        infoLore.add(ChatColor.GRAY + "Clique sur un enchant pour");
        infoLore.add(ChatColor.GRAY + "recevoir son livre custom.");
        infoMeta.setLore(infoLore);
        info.setItemMeta(infoMeta);
        inv.setItem(4, info);

        return inv;
    }

    private static ItemStack buildDisplayItem(CustomEnchant enchant) {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(enchant.getColor().toString() + ChatColor.BOLD + enchant.getDisplayName());

        List<String> lore = new ArrayList<String>();
        for (String line : enchant.getLore()) {
            lore.add(ChatColor.GRAY + line);
        }
        lore.add("");
        lore.add(ChatColor.YELLOW + "Coût : " + COST_LEVELS + " niveaux");
        lore.add(ChatColor.GREEN + "Clique pour enchanter !");
        meta.setLore(lore);
        item.setItemMeta(meta);

        HiddenTag.write(item, SLOT_KEY, enchant.getId());
        return item;
    }

    public static CustomEnchant getEnchantFromClickedItem(ItemStack item) {
        String id = HiddenTag.read(item, SLOT_KEY);
        if (id == null) return null;
        return CustomEnchant.fromId(id);
    }

    public static boolean tryPurchase(Player player, CustomEnchant enchant) {
        if (player.getLevel() < COST_LEVELS) {
            player.sendMessage(ChatColor.RED + "Il te manque des niveaux ! (" + COST_LEVELS
                    + " requis, tu en as " + player.getLevel() + ")");
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
            return false;
        }

        player.setLevel(player.getLevel() - COST_LEVELS);

        ItemStack book = EnchantBookUtil.createBook(enchant);
        java.util.Map<Integer, ItemStack> leftovers = player.getInventory().addItem(book);
        for (ItemStack leftover : leftovers.values()) {
            player.getWorld().dropItem(player.getLocation(), leftover);
        }

        player.sendMessage(ChatColor.GREEN + "Tu as reçu : " + enchant.getColor() + "Livre : " + enchant.getDisplayName());
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1.4f);
        return true;
    }
}
