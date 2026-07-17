package fr.belarion.enchants;

import org.bukkit.configuration.file.FileConfiguration;

/** Charge et expose les valeurs de config.yml (couts, limites, etc). */
public final class ConfigManager {

    private final BelarionEnchants plugin;

    private int enchantTableCost;
    private int emeraldAnvilCost;
    private int veinMinerMaxBlocks;
    private int libraryPageSize;

    public ConfigManager(BelarionEnchants plugin) {
        this.plugin = plugin;
    }

    public void load() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        FileConfiguration cfg = plugin.getConfig();

        enchantTableCost = cfg.getInt("costs.enchant-table", 60);
        emeraldAnvilCost = cfg.getInt("costs.emerald-anvil", 30);
        veinMinerMaxBlocks = cfg.getInt("vein-miner-max-blocks", 64);
        libraryPageSize = cfg.getInt("library-page-size", 16);
    }

    public int getEnchantTableCost() { return enchantTableCost; }
    public int getEmeraldAnvilCost() { return emeraldAnvilCost; }
    public int getVeinMinerMaxBlocks() { return veinMinerMaxBlocks; }
    public int getLibraryPageSize() { return libraryPageSize; }
}
