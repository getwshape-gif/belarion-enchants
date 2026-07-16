package fr.belarion.enchants;

import org.bukkit.ChatColor;

/**
 * Les "tiers" de matériel custom du serveur. Les enchants custom ne
 * s'appliquent QUE sur des items qui portent un de ces tiers (voir
 * ItemTierUtil et AnvilApplyListener).
 *
 * Ajouter un nouveau tier plus tard (ex: EMERALD_RENFORCE_2) se fait
 * juste en ajoutant une ligne ici.
 */
public enum ItemTier {

    EMERALD("emerald", "Émeraude", ChatColor.GREEN),
    EMERALD_RENFORCE("emerald_renforce", "Émeraude Renforcé", ChatColor.DARK_GREEN);

    private final String id;
    private final String label;
    private final ChatColor color;

    ItemTier(String id, String label, ChatColor color) {
        this.id = id;
        this.label = label;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public ChatColor getColor() {
        return color;
    }

    public static ItemTier fromId(String id) {
        for (ItemTier t : values()) {
            if (t.id.equalsIgnoreCase(id)) return t;
        }
        return null;
    }
}
