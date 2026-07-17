package fr.belarion.enchants;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * En 1.8 il n'y a pas de PersistentDataContainer pour stocker des données
 * cachées sur un item. Technique classique : on encode une chaîne en la
 * rendant invisible (chaque caractère précédé du caractère de couleur §),
 * et on la colle au début d'une ligne de lore. Invisible pour le joueur,
 * lisible par le plugin.
 */
public final class HiddenTag {

    private HiddenTag() {}

    public static String encode(String key, String value) {
        StringBuilder sb = new StringBuilder();
        String raw = key + ":" + value;
        for (char c : raw.toCharArray()) {
            sb.append(ChatColor.COLOR_CHAR).append(c);
        }
        return sb.toString();
    }

    /** Retourne la valeur du tag présent sur l'item pour cette clé, sinon null. */
    public static String read(ItemStack item, String key) {
        if (item == null || !item.hasItemMeta()) return null;
        ItemMeta meta = item.getItemMeta();
        if (!meta.hasLore()) return null;
        for (String line : meta.getLore()) {
            String decoded = decode(line);
            if (decoded != null && decoded.startsWith(key + ":")) {
                return decoded.substring(key.length() + 1);
            }
        }
        return null;
    }

    /** Ajoute une ligne de lore invisible portant le tag. */
    public static void write(ItemStack item, String key, String value) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.hasLore() ? new ArrayList<String>(meta.getLore()) : new ArrayList<String>();
        lore.add(encode(key, value));
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    private static String decode(String line) {
        if (line == null || line.isEmpty()) return null;
        StringBuilder sb = new StringBuilder();
        char[] chars = line.toCharArray();
        int i = 0;
        while (i < chars.length - 1 && chars[i] == ChatColor.COLOR_CHAR) {
            sb.append(chars[i + 1]);
            i += 2;
        }
        if (sb.length() == 0) return null;
        return sb.toString();
    }
}
