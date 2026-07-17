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
        getServer().getPluginManager().registerEvents(new EnchantTableListener(), this);
        getServer().getPluginManager().registerEvents(new EnchantClickListener(), this);
        getServer().getPluginManager().registerEvents(new EmeraldAnvilListener(), this);
        getServer().getPluginManager().registerEvents(new CombatListener(), this);
        getServer().getPluginManager().registerEvents(new EmeraldHammerListener(), this);
        getLogger().info("BelarionEnchants (1.8.8) active.");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("enchanttable")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Commande joueur uniquement.");
                return true;
            }
            Player player = (Player) sender;
            ItemStack block = new ItemStack(Material.EMERALD_BLOCK);
            ItemMeta meta = block.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "Emerald Enchanting Table");
            block.setItemMeta(meta);
            player.getInventory().addItem(block);
            player.sendMessage(ChatColor.GRAY + "Emerald Enchanting Table re\u00e7ue. Place-la puis clique-droit dessus.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("enchantanvil")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Commande joueur uniquement.");
                return true;
            }
            Player player = (Player) sender;
            ItemStack block = new ItemStack(Material.SEA_LANTERN);
            ItemMeta meta = block.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_GRAY.toString() + ChatColor.BOLD + "Emerald Anvil");
            block.setItemMeta(meta);
            player.getInventory().addItem(block);
            player.sendMessage(ChatColor.GRAY + "Emerald Anvil re\u00e7ue. Place-la puis clique-droit dessus.");
            return true;
        }

        if (command.getName().equalsIgnoreCase("emeralditem")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("Commande joueur uniquement.");
                return true;
            }
            Player player = (Player) sender;
            if (args.length < 1) {
                player.sendMessage(ChatColor.RED + "Utilisation : /emeralditem <sword|hammer>");
                return true;
            }
            ItemStack item;
            if (args[0].equalsIgnoreCase("sword")) {
                item = EmeraldItems.createSword();
            } else if (args[0].equalsIgnoreCase("hammer")) {
                item = EmeraldItems.createHammer();
            } else {
                player.sendMessage(ChatColor.RED + "Item inconnu. Utilise : sword ou hammer");
                return true;
            }
            player.getInventory().addItem(item);
            player.sendMessage(ChatColor.GRAY + "Re\u00e7u  " + item.getItemMeta().getDisplayName());
            return true;
        }

        return false;
    }
}
