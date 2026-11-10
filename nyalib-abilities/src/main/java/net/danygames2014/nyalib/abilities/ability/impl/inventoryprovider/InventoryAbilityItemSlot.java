package net.danygames2014.nyalib.abilities.ability.impl.inventoryprovider;

/**
 * Represents the type of slot an {@link InventoryAbilityItem} is in.
 */
public enum InventoryAbilityItemSlot {
    /**
     * The item is in the player's inventory (excluding the {@link #ARMOR} and {@link #HOTBAR} slots
     */
    INVENTORY,
    /**
     * The item is in the player's hotbar
     */
    HOTBAR,
    /**
     * The item is equipped as armor
     */
    ARMOR
}
