package fr.belarion.enchants;

import org.bukkit.ChatColor;

public enum CustomEnchant {

    VAMPIRISME(
        "vampirisme",
        "Vampirisme",
        ChatColor.DARK_RED,
        new String[]{
            "Soigne l'attaquant d'un pourcentage",
            "des dégâts infligés au corps à corps."
        },
        0.20
    );

    private final String id;
    private final String displayName;
    private final ChatColor color;
    private final String[] lore;
    private final double value;

    CustomEnchant(String id, String displayName, ChatColor color, String[] lore, double value) {
        this.id = id;
        this.displayName = displayName;
        this.color = color;
        this.lore = lore;
        this.value = value;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public ChatColor getColor() { return color; }
    public String[] getLore() { return lore; }
    public double getValue() { return value; }

    public static CustomEnchant fromId(String id) {
        for (CustomEnchant e : values()) {
            if (e.id.equalsIgnoreCase(id)) return e;
        }
        return null;
    }
}
