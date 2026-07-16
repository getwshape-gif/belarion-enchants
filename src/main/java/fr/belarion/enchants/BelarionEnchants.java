package fr.belarion.enchants;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class BelarionEnchants extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new EnchantTableListener(this), this);
        getServer().getPluginManager().registerEvents(new EnchantClickListener(this), this);
        getServer().getPluginManager().registerEvents(new AnvilApplyListener(this), this);
        getServer().getPluginManager().registerEvents(new CombatListener(this), this);
        getServer().getPluginManager().registerEvents(new EmeraldHammerListener(this), this);
        getServer().getPluginManager().registerEvents(new EmeraldAnvilBlockListener(), this);
        getLogger().info("BelarionEnchants activé.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("enchanttable")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Commande joueur uniquement.");
                return true;
            }
            ItemStack block = new ItemStack(Material.EMERALD_BLOCK);
            ItemMeta meta = block.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_GREEN + "" + ChatColor.BOLD + "Table d'Enchantement");
            block.setItemMeta(meta);
            player.getInventory().addItem(block);
            player.sendMessage(ChatColor.GREEN + "Tu as reçu une table d'enchantement. Place-la puis clique-droit dessus.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("enchantanvil")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Commande joueur uniquement.");
                return true;
            }
            ItemStack block = new ItemStack(Material.LODESTONE);
            ItemMeta meta = block.getItemMeta();
            meta.setDisplayName(EmeraldAnvilBlockListener.TITLE);
            block.setItemMeta(meta);
            player.getInventory().addItem(block);
            player.sendMessage(ChatColor.GREEN + "Tu as reçu une Enclume en Émeraude. Place-la puis clique-droit dessus.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("emeralditem")) {
            if (!(sender instanceof Player player)) {
                sender.sendMessage("Commande joueur uniquement.");
                return true;
            }
            if (args.length < 1) {
                player.sendMessage(ChatColor.RED + "Utilisation : /emeralditem <sword|hammer>");
                return true;
            }
            ItemStack item = switch (args[0].toLowerCase()) {
                case "sword" -> EmeraldItems.createSword(this);
                case "hammer" -> EmeraldItems.createHammer(this);
                default -> null;
            };
            if (item == null) {
                player.sendMessage(ChatColor.RED + "Item inconnu. Utilise : sword ou hammer");
                return true;
            }
            player.getInventory().addItem(item);
            player.sendMessage(ChatColor.GREEN + "Tu as reçu : " + item.getItemMeta().getDisplayName());
            return true;
        }

        return false;
    }
}
