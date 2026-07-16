package fr.belarion.enchants;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class EnchantGUI {

    public static final String TITLE = ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Table d'Enchantement";
    public static final int COST_LEVELS = 60;
    private static final String SLOT_KEY = "belarion_enchant_slot_id";

    // Slots centraux du coffre 27 cases où afficher les enchants disponibles
    private static final int[] DISPLAY_SLOTS = new int[]{11, 13, 15};

    public static Inventory build(BelarionEnchants plugin) {
        Inventory inv = Bukkit.createInventory(null, 27, TITLE);

        CustomEnchant[] enchants = CustomEnchant.values();
        for (int i = 0; i < enchants.length && i < DISPLAY_SLOTS.length; i++) {
            inv.setItem(DISPLAY_SLOTS[i], buildDisplayItem(plugin, enchants[i]));
        }

        ItemStack info = new ItemStack(Material.EXPERIENCE_BOTTLE);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(ChatColor.GREEN + "Coût : " + COST_LEVELS + " niveaux");
        List<String> infoLore = new ArrayList<>();
        infoLore.add(ChatColor.GRAY + "Clique sur un enchant pour");
        infoLore.add(ChatColor.GRAY + "recevoir son livre custom.");
        infoMeta.setLore(infoLore);
        info.setItemMeta(infoMeta);
        inv.setItem(4, info);

        return inv;
    }

    private static ItemStack buildDisplayItem(BelarionEnchants plugin, CustomEnchant enchant) {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(enchant.getColor() + "" + ChatColor.BOLD + enchant.getDisplayName());

        List<String> lore = new ArrayList<>();
        for (String line : enchant.getLore()) {
            lore.add(ChatColor.GRAY + line);
        }
        lore.add("");
        lore.add(ChatColor.YELLOW + "Coût : " + COST_LEVELS + " niveaux");
        lore.add(ChatColor.GREEN + "Clique pour enchanter !");
        meta.setLore(lore);

        NamespacedKey key = new NamespacedKey(plugin, SLOT_KEY);
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, enchant.getId());
        item.setItemMeta(meta);
        return item;
    }

    public static CustomEnchant getEnchantFromClickedItem(BelarionEnchants plugin, ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;
        NamespacedKey key = new NamespacedKey(plugin, SLOT_KEY);
        String id = item.getItemMeta().getPersistentDataContainer().get(key, PersistentDataType.STRING);
        if (id == null) return null;
        return CustomEnchant.fromId(id);
    }

    public static boolean tryPurchase(Player player, CustomEnchant enchant, BelarionEnchants plugin) {
        if (player.getLevel() < COST_LEVELS) {
            player.sendMessage(ChatColor.RED + "Il te manque des niveaux ! (" + COST_LEVELS
                    + " requis, tu en as " + player.getLevel() + ")");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1f, 1f);
            return false;
        }

        player.setLevel(player.getLevel() - COST_LEVELS);

        ItemStack book = EnchantBookUtil.createBook(plugin, enchant);
        player.getInventory().addItem(book).values()
                .forEach(leftover -> player.getWorld().dropItem(player.getLocation(), leftover));

        player.sendMessage(ChatColor.GREEN + "Tu as reçu : " + enchant.getColor() + "Livre : " + enchant.getDisplayName());
        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1f, 1.4f);
        return true;
    }
}
