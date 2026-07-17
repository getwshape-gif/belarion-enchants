package fr.belarion.enchants;

import org.bukkit.ChatColor;

public enum CustomEnchant {

    SPEED_II(
        "speed2",
        "Speed II",
        ChatColor.AQUA,
        new String[]{
            "Speed II permanent tant que les",
            "bottes sont portees."
        },
        EnchantTarget.BOOTS,
        1.0
    );

    private final String id;
    private final String displayName;
    private final ChatColor color;
    private final String[] lore;
    private final EnchantTarget target;
    private final double value;

    CustomEnchant(String id, String displayName, ChatColor color, String[] lore,
                  EnchantTarget target, double value) {
        this.id = id;
        this.displayName = displayName;
        this.color = color;
        this.lore = lore;
        this.target = target;
        this.value = value;
    }

    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public ChatColor getColor() { return color; }
    public String[] getLore() { return lore; }
    public EnchantTarget getTarget() { return target; }
    public double getValue() { return value; }

    public static CustomEnchant fromId(String id) {
        for (CustomEnchant e : values()) {
            if (e.id.equalsIgnoreCase(id)) return e;
        }
        return null;
    }
}
