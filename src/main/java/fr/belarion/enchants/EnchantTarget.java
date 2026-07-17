package fr.belarion.enchants;

import org.bukkit.inventory.ItemStack;

public enum EnchantTarget {

    BOOTS("les bottes"),
    HELMET("les casques"),
    CHESTPLATE("les plastrons"),
    LEGGINGS("les jambieres"),
    ARMOR("les armures"),
    WEAPON("les armes"),
    TOOL("les outils"),
    ANY("tout le stuff emeraude");

    private final String label;

    EnchantTarget(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public boolean matches(ItemStack item) {
        if (item == null) return false;
        String m = item.getType().name();
        switch (this) {
            case BOOTS:
                return m.endsWith("_BOOTS");
            case HELMET:
                return m.endsWith("_HELMET");
            case CHESTPLATE:
                return m.endsWith("_CHESTPLATE");
            case LEGGINGS:
                return m.endsWith("_LEGGINGS");
            case ARMOR:
                return m.endsWith("_BOOTS") || m.endsWith("_HELMET")
                        || m.endsWith("_CHESTPLATE") || m.endsWith("_LEGGINGS");
            case WEAPON:
                return m.endsWith("_SWORD") || m.endsWith("_AXE");
            case TOOL:
                return m.endsWith("_PICKAXE") || m.endsWith("_SPADE")
                        || m.endsWith("_HOE") || m.endsWith("_AXE");
            default:
                return true;
        }
    }
}

