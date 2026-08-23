package net.danygames2014.nyalib.abilities.ability;

/**
 * Determines who receives updates about an ability changing on an entity in multiplayer
 * Ideally you want to use the lightest option possible to reduce on network traffic
 */
public enum AbilitySyncType {
    /**
     * This ability is not synced at all
     */
    NONE,
    /**
     * This ability is synced to all players tracking the entity the ability is on
     */
    ALL,
    /**
     * This ability is synced only to the player the ability is on
     */
    PLAYER_ONLY,
}
