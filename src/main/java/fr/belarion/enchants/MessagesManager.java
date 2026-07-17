package fr.belarion.enchants;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

/** Charge messages.yml et fournit des messages formates/colores, avec placeholders simples. */
public final class MessagesManager {

    private final BelarionEnchants plugin;
    private FileConfiguration messages;
    private String prefix = "";

    public MessagesManager(BelarionEnchants plugin) {
        this.plugin = plugin;
    }

    public void load() {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(file);

        // Complete avec les valeurs par defaut embarquees dans le jar si des cles manquent.
        InputStream defStream = plugin.getResource("messages.yml");
        if (defStream != null) {
            Reader reader = new InputStreamReader(defStream, StandardCharsets.UTF_8);
            YamlConfiguration defaults = YamlConfiguration.loadConfiguration(reader);
            messages.setDefaults(defaults);
        }

        prefix = color(messages.getString("prefix", ""));
    }

    public String get(String path) {
        String raw = messages.getString(path, path);
        return color(raw);
    }

    public String get(String path, String... placeholdersAndValues) {
        String raw = get(path);
        for (int i = 0; i + 1 < placeholdersAndValues.length; i += 2) {
            raw = raw.replace("%" + placeholdersAndValues[i] + "%", placeholdersAndValues[i + 1]);
        }
        return raw;
    }

    public void send(Player player, String path, String... placeholdersAndValues) {
        player.sendMessage(prefix + get(path, placeholdersAndValues));
    }

    private String color(String raw) {
        if (raw == null) return "";
        return ChatColor.translateAlternateColorCodes('&', raw);
    }
}
