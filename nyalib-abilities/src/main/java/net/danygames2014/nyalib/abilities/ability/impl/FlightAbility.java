package net.danygames2014.nyalib.abilities.ability.impl;

import net.danygames2014.nyalib.abilities.ability.Ability;
import net.danygames2014.nyalib.abilities.ability.AbilityRule;
import net.danygames2014.nyalib.abilities.ability.value.BooleanAbilityValue;
import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.util.Identifier;

/**
 * Allows the player to double jump to initiate creative-like flight
 * <p> Applicable to entities extending {@link PlayerEntity}
 * <p> Default value: <code>false</code>
 * <p> Ability rule: <code>OR</code>
 */
public class FlightAbility extends Ability<PlayerEntity, BooleanAbilityValue> {
    public FlightAbility(Identifier identifier) {
        super(identifier, BooleanAbilityValue.of(false), AbilityRule.OR);
    }
}
