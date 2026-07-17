package fr.belarion.enchants;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import java.util.Map;

public class EmeraldAnvilListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getClickedBlock() == null) return;
        if (event.getClickedBlock().getType() != Material.SEA_LANTERN) return;

        event.setCancelled(true);
        event.getPlayer().openInventory(EmeraldAnvilGUI.build());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        String title = event.getView().getTitle();

        if (event.getInventory() != null && event.getInventory().getType() == InventoryType.ANVIL
                && !EmeraldAnvilGUI.TITLE.equals(title)) {
            ItemStack current = event.getCurrentItem();
            ItemStack cursor = event.getCursor();
            if (ItemTierUtil.getTier(current) != null || ItemTierUtil.getTier(cursor) != null) {
                event.setCancelled(true);
                if (event.getWhoClicked() instanceof Player) {
                    ((Player) event.getWhoClicked()).sendMessage(
                            ChatColor.RED + "Le stuff \u00e9meraude n\u00e9cessite une Emerald Anvil.");
                }
            }
            return;
        }

        if (!EmeraldAnvilGUI.TITLE.equals(title)) return;
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();

        int raw = event.getRawSlot();
        Inventory top = event.getView().getTopInventory();

        if (raw >= top.getSize()) {
            if (event.isShiftClick()) event.setCancelled(true);
            return;
        }

        if (raw == EmeraldAnvilGUI.SLOT_ITEM || raw == EmeraldAnvilGUI.SLOT_BOOK) {
            return;
        }

        event.setCancelled(true);

        if (raw != EmeraldAnvilGUI.SLOT_CONFIRM) return;

        ItemStack base = top.getItem(EmeraldAnvilGUI.SLOT_ITEM);
        ItemStack book = top.getItem(EmeraldAnvilGUI.SLOT_BOOK);

        if (base == null || book == null || base.getType() == Material.AIR || book.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "Place un item \u00e9meraude \u00e0 gauche et un livre \u00e0 droite.");
            return;
        }

        if (ItemTierUtil.getTier(base) == null) {
            player.sendMessage(ChatColor.RED + "Les enchants ne s'appliquent que sur du stuff/outils \u00e9meraude ici.");
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
            return;
        }

        CustomEnchant custom = EnchantBookUtil.readEnchant(book);

        if (custom != null && !custom.getTarget().matches(base)) {
            player.sendMessage(ChatColor.RED + custom.getDisplayName()
                    + " s'applique uniquement sur " + custom.getTarget().getLabel() + ".");
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
            return;
        }

        if (player.getLevel() < EmeraldAnvilGUI.COST_LEVELS) {
            player.sendMessage(ChatColor.RED + "Il te manque des niveaux ! (" + EmeraldAnvilGUI.COST_LEVELS + " requis)");
            player.playSound(player.getLocation(), Sound.VILLAGER_NO, 1f, 1f);
            return;
        }

        boolean applied = false;

        if (custom != null) {
            EnchantBookUtil.applyToItem(base, custom);
            applied = true;
        } else if (book.getType() == Material.ENCHANTED_BOOK
                && book.getItemMeta() instanceof EnchantmentStorageMeta) {
            EnchantmentStorageMeta esm = (EnchantmentStorageMeta) book.getItemMeta();
            for (Map.Entry<Enchantment, Integer> entry : esm.getStoredEnchants().entrySet()) {
                base.addUnsafeEnchantment(entry.getKey(), entry.getValue());
                applied = true;
            }
        }

        if (!applied) {
            player.sendMessage(ChatColor.RED + "Le livre \u00e0 droite n'est pas un livre d'enchantement valide.");
            return;
        }

        player.setLevel(player.getLevel() - EmeraldAnvilGUI.COST_LEVELS);
        top.setItem(EmeraldAnvilGUI.SLOT_BOOK, null);
        player.sendMessage(ChatColor.GRAY + "Enchantement appliqu\u00e9.");
        player.playSound(player.getLocation(), Sound.ANVIL_USE, 1f, 1f);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!EmeraldAnvilGUI.TITLE.equals(event.getView().getTitle())) return;
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = (Player) event.getPlayer();
        Inventory top = event.getView().getTopInventory();

        int[] slots = new int[]{EmeraldAnvilGUI.SLOT_ITEM, EmeraldAnvilGUI.SLOT_BOOK};
        for (int slot : slots) {
            ItemStack item = top.getItem(slot);
            if (item != null && item.getType() != Material.AIR) {
                Map<Integer, ItemStack> leftovers = player.getInventory().addItem(item);
                for (ItemStack leftover : leftovers.values()) {
                    player.getWorld().dropItem(player.getLocation(), leftover);
                }
                top.setItem(slot, null);
            }
        }
    }
}
