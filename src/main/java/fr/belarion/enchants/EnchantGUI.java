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
import java.util.Random;

public final class EnchantGUI {

    public static final String TITLE = ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Table d'Enchantement";
    public static final String TITLE_LIST = ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Enchants Disponibles";
    public static final int COST_LEVELS = 60;

    public static final int SLOT_BOOK = 11;
    public static final int SLOT_ENCHANT = 15;
    public static final int SLOT_LIST = 22;
    public static final int SLOT_BACK = 22;

    private static final Random RANDOM = new Random();

    private EnchantGUI() {}

    public static Inventory build() {
        Inventory inv = Bukkit.createInventory(null, 27, TITLE);

        ItemStack green = pane((short) 13);
        ItemStack lime = pane((short) 5);
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, (i % 2 == 0) ? green : lime);
        }

        ItemStack info = new ItemStack(Material.ENCHANTMENT_TABLE);
        ItemMeta infoMeta = info.getItemMeta();
        infoMeta.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Table d'Enchantement en \u00c9meraude");
        List<String> infoLore = new ArrayList<String>();
        infoLore.add(ChatColor.GRAY + "Place un livre vide dans la case,");
        infoLore.add(ChatColor.GRAY + "clique sur Enchanter et re\u00e7ois un");
        infoLore.add(ChatColor.GRAY + "enchant custom al\u00e9atoire !");
        infoMeta.setLore(infoLore);
        info.setItemMeta(infoMeta);
        inv.setItem(4, info);

        inv.setItem(SLOT_BOOK, null);

        ItemStack enchant = new ItemStack(Material.EXP_BOTTLE);
        ItemMeta em = enchant.getItemMeta();
        em.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Enchanter");
        List<String> el = new ArrayList<String>();
        el.add(ChatColor.GRAY + "Co\u00fbt : " + ChatColor.YELLOW + COST_LEVELS + " niveaux");
        el.add(ChatColor.GRAY + "R\u00e9sultat : " + ChatColor.LIGHT_PURPLE + "un enchant al\u00e9atoire");
        em.setLore(el);
        enchant.setItemMeta(em);
        inv.setItem(SLOT_ENCHANT, enchant);

        ItemStack list = new ItemStack(Material.BOOK_AND_QUILL);
        ItemMeta lm = list.getItemMeta();
        lm.setDisplayName(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Enchants disponibles");
        List<String> ll = new ArrayList<String>();
        ll.add(ChatColor.GRAY + "Clique pour voir la liste.");
        lm.setLore(ll);
        list.setItemMeta(lm);
        inv.setItem(SLOT_LIST, list);

        return inv;
    }

    public static Inventory buildList() {
        Inventory inv = Bukkit.createInventory(null, 27, TITLE_LIST);

        ItemStack green = pane((short) 13);
        ItemStack lime = pane((short) 5);
        for (int i = 0; i < 27; i++) {
            inv.setItem(i, (i % 2 == 0) ? green : lime);
        }

        int slot = 10;
        for (CustomEnchant enchant : CustomEnchant.values()) {
            if (slot > 16) break;
            inv.setItem(slot, buildDisplayItem(enchant));
            slot++;
        }

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta bm = back.getItemMeta();
        bm.setDisplayName(ChatColor.RED.toString() + ChatColor.BOLD + "Retour");
        back.setItemMeta(bm);
        inv.setItem(SLOT_BACK, back);

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
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private static ItemStack pane(short color) {
        ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, color);
        ItemMeta fm = filler.getItemMeta();
        fm.setDisplayName(" ");
        filler.setItemMeta(fm);
        return filler;
    }

    public static void tryEnchant(Player player, Inventory top) {
        ItemStack book = top.getItem(SLOT_BOOK);
        if (book == null || book.getType() != Material.BOOK) {
            player.sendMessage(ChatColor.RED + "Place un livre vide dans la case \u00e0 gauche.");
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
            return;
        }

        if (player.getLevel() < COST_LEVELS) {
            player.sendMessage(ChatColor.RED + "Il te manque des niveaux ! (" + COST_LEVELS
                    + " requis, tu en as " + player.getLevel() + ")");
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
            return;
        }

        if (book.getAmount() > 1) {
            book.setAmount(book.getAmount() - 1);
            top.setItem(SLOT_BOOK, book);
        } else {
            top.setItem(SLOT_BOOK, null);
        }
        player.setLevel(player.getLevel() - COST_LEVELS);

        CustomEnchant[] all = CustomEnchant.values();
        CustomEnchant picked = all[RANDOM.nextInt(all.length)];

        ItemStack result = EnchantBookUtil.createBook(picked);
        java.util.Map<Integer, ItemStack> leftovers = player.getInventory().addItem(result);
        for (ItemStack leftover : leftovers.values()) {
            player.getWorld().dropItem(player.getLocation(), leftover);
        }

        player.sendMessage(ChatColor.GREEN + "Le livre s'illumine... Tu obtiens : "
                + picked.getColor() + ChatColor.BOLD + picked.getDisplayName());
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1.2f);
    }
}
