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

    private static BelarionEnchants instance;

    private ConfigManager configManager;
    private MessagesManager messagesManager;
    private EffectManager effectManager;
    private AntiDebuffGuardTask antiDebuffGuardTask;

    public static BelarionEnchants get() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        configManager = new ConfigManager(this);
        configManager.load();

        messagesManager = new MessagesManager(this);
        messagesManager.load();

        // GUI
        getServer().getPluginManager().registerEvents(new EnchantTableListener(), this);
        getServer().getPluginManager().registerEvents(new EmeraldAnvilListener(), this);

        // Protection des blocs custom
        getServer().getPluginManager().registerEvents(new BlockProtectionListener(), this);

        // Mecaniques d'enchants a evenement specifique
        getServer().getPluginManager().registerEvents(new MiningListener(), this);
        getServer().getPluginManager().registerEvents(new CombatListener(), this);
        getServer().getPluginManager().registerEvents(new FishingListener(), this);
        getServer().getPluginManager().registerEvents(new DamageListener(), this);
        getServer().getPluginManager().registerEvents(new PotionProtectionListener(), this);

        // Item de demonstration (Hammer)
        getServer().getPluginManager().registerEvents(new EmeraldHammerListener(), this);

        // Effets passifs permanents (Speed, Strength, Fire Resistance, Haste) : 1 fois/seconde
        effectManager = new EffectManager();
        effectManager.runTaskTimer(this, 20L, 20L);

        // Garde Anti Debuff : tres haute frequence (0.1s) pour une reactivite quasi instantanee
        antiDebuffGuardTask = new AntiDebuffGuardTask();
        antiDebuffGuardTask.runTaskTimer(this, 1L, 2L);

        getLogger().info("BelarionEnchants v1.0.0 (1.8.8) actif : " + CustomEnchant.values().length + " custom enchants charges.");
    }

    @Override
    public void onDisable() {
        if (effectManager != null) {
            effectManager.cancel();
        }
        if (antiDebuffGuardTask != null) {
            antiDebuffGuardTask.cancel();
        }
    }

    public ConfigManager getConfigManager() { return configManager; }
    public MessagesManager getMessagesManager() { return messagesManager; }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String name = command.getName();

        if (name.equalsIgnoreCase("belarionenchants")) {
            if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                configManager.load();
                messagesManager.load();
                if (sender instanceof Player) {
                    messagesManager.send((Player) sender, "reload");
                } else {
                    sender.sendMessage("Configuration de BelarionEnchants rechargee.");
                }
                return true;
            }
            sender.sendMessage(ChatColor.YELLOW + "Utilisation : /belarionenchants reload");
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("Commande joueur uniquement.");
            return true;
        }
        Player player = (Player) sender;

        if (name.equalsIgnoreCase("enchanttable")) {
            ItemStack block = new ItemStack(Material.EMERALD_BLOCK);
            ItemMeta meta = block.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Table d'Enchantement Emeraude");
            block.setItemMeta(meta);
            player.getInventory().addItem(block);
            messagesManager.send(player, "table-block-received");
            return true;
        }

        if (name.equalsIgnoreCase("enchantanvil")) {
            ItemStack block = new ItemStack(Material.SEA_LANTERN);
            ItemMeta meta = block.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD + "Enclume Emeraude");
            block.setItemMeta(meta);
            player.getInventory().addItem(block);
            messagesManager.send(player, "anvil-block-received");
            return true;
        }

        if (name.equalsIgnoreCase("emeralditem")) {
            if (args.length < 1) {
                player.sendMessage(ChatColor.RED + "Utilisation : /emeralditem <sword|hammer|boots>");
                return true;
            }
            ItemStack item;
            if (args[0].equalsIgnoreCase("sword")) {
                item = EmeraldItems.createSword();
            } else if (args[0].equalsIgnoreCase("hammer")) {
                item = EmeraldItems.createHammer();
            } else if (args[0].equalsIgnoreCase("boots")) {
                item = EmeraldItems.createBoots();
            } else {
                messagesManager.send(player, "unknown-item");
                return true;
            }
            player.getInventory().addItem(item);
            messagesManager.send(player, "item-received", "item", item.getItemMeta().getDisplayName());
            return true;
        }

        return false;
    }
}
