package net.danygames2014.nyalib.abilities.ability.impl;

/**
 * The default abilities provided by NyaLib
 */
public class Abilities {
    /**
     * Makes the entity immune to fire damage and being set on fire
     * <p> Applicable to entities extending {@link net.minecraft.entity.LivingEntity}
     */
    public static FireImmunityAbility FIRE_IMMUNITY;
    /**
     * Allows the player to double jump to initiate creative-like flight
     * <p> Applicable to entities extending {@link net.minecraft.entity.player.PlayerEntity}
     */
    public static FlightAbility FLIGHT;
    /**
     * If the player can fly, determines the speed of the player's flight
     * <p> Applicable to entities extending {@link net.minecraft.entity.player.PlayerEntity}
     */
    public static FlightSpeedAbility FLIGHT_SPEED;
    /**
     * Makes the entity invincible to any kind of damage
     * <p> Applicable to entities extending {@link net.minecraft.entity.LivingEntity}
     */
    public static InvincibilityAbility INVINCIBILITY;
    
    // TODO: Extra Hearts Ability
    // TODO: Sprint Ability
    // TODO: Reach Ability
    // TODO: Step Height Ability
    // TODO: Damage Boost Ability
    // TODO: Damage Resistance Ability
    // TODO: Speed Boost Ability
    // TODO: Water Breathing Ability
}
