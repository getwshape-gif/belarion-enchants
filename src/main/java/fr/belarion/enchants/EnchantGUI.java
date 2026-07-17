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

    public static final String TITLE = ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "Emerald Enchanting Table";
    public static final String TITLE_LIST = ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "Enchant Library";
    public static final int COST_LEVELS = 60;

    public static final int SLOT_BOOK = 11;
    public static final int SLOT_ENCHANT = 13;
    public static final int SLOT_INFO = 15;
    public static final int SLOT_LIST = 22;
    public static final int SLOT_BACK = 22;

    private static final Random RANDOM = new Random();

    private EnchantGUI() {}

    public static Inventory build() {
        Inventory inv = Bukkit.createInventory(null, 27, TITLE);
        fill(inv);

        inv.setItem(SLOT_BOOK, null);

        ItemStack enchant = new ItemStack(Material.EMERALD);
        ItemMeta em = enchant.getItemMeta();
        em.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "Enchant");
        List<String> el = new ArrayList<String>();
        el.add(ChatColor.DARK_GRAY + "Consomme un livre vierge.");
        el.add(" ");
        el.add(ChatColor.GRAY + "Co\u00fbt  " + ChatColor.WHITE + COST_LEVELS + " niveaux");
        el.add(ChatColor.GRAY + "Gain  " + ChatColor.WHITE + "1 enchant al\u00e9atoire");
        em.setLore(el);
        enchant.setItemMeta(em);
        inv.setItem(SLOT_ENCHANT, enchant);

        ItemStack info = new ItemStack(Material.BOOK);
        ItemMeta im = info.getItemMeta();
        im.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Livre vierge");
        List<String> il = new ArrayList<String>();
        il.add(ChatColor.DARK_GRAY + "Place-le dans la case de gauche.");
        im.setLore(il);
        info.setItemMeta(im);
        inv.setItem(SLOT_INFO, info);

        ItemStack list = new ItemStack(Material.BOOK_AND_QUILL);
        ItemMeta lm = list.getItemMeta();
        lm.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Enchant Library");
        List<String> ll = new ArrayList<String>();
        ll.add(ChatColor.DARK_GRAY + "Consulter les enchants disponibles.");
        lm.setLore(ll);
        list.setItemMeta(lm);
        inv.setItem(SLOT_LIST, list);

        return inv;
    }

    public static Inventory buildList() {
        Inventory inv = Bukkit.createInventory(null, 27, TITLE_LIST);
        fill(inv);

        int slot = 10;
        for (CustomEnchant enchant : CustomEnchant.values()) {
            if (slot > 16) break;
            inv.setItem(slot, buildDisplayItem(enchant));
            slot++;
        }

        ItemStack back = new ItemStack(Material.ARROW);
        ItemMeta bm = back.getItemMeta();
        bm.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + "Retour");
        back.setItemMeta(bm);
        inv.setItem(SLOT_BACK, back);

        return inv;
    }

    private static ItemStack buildDisplayItem(CustomEnchant enchant) {
        ItemStack item = new ItemStack(Material.ENCHANTED_BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.WHITE.toString() + ChatColor.BOLD + enchant.getDisplayName());
        List<String> lore = new ArrayList<String>();
        for (String line : enchant.getLore()) {
            lore.add(ChatColor.DARK_GRAY + line);
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private static void fill(Inventory inv) {
        ItemStack filler = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15);
        ItemMeta fm = filler.getItemMeta();
        fm.setDisplayName(" ");
        filler.setItemMeta(fm);
        for (int i = 0; i < inv.getSize(); i++) {
            inv.setItem(i, filler);
        }
    }

    public static void tryEnchant(Player player, Inventory top) {
        ItemStack book = top.getItem(SLOT_BOOK);
        if (book == null || book.getType() != Material.BOOK) {
            player.sendMessage(ChatColor.RED + "Place un livre vierge dans la case de gauche.");
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

        player.sendMessage(ChatColor.GRAY + "Enchant obtenu  " + ChatColor.WHITE + ChatColor.BOLD + picked.getDisplayName());
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1.2f);
    }
}
