package net.danygames2014.nyalib.abilities.ability.impl;

import net.danygames2014.nyalib.abilities.ability.Ability;
import net.danygames2014.nyalib.abilities.ability.AbilityRule;
import net.danygames2014.nyalib.abilities.ability.value.FloatAbilityValue;
import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.util.Identifier;

/**
 * If the player can fly, determines the speed of the player's flight
 */
public class FlightSpeedAbility extends Ability<PlayerEntity, FloatAbilityValue> {
    public FlightSpeedAbility(Identifier identifier) {
        super(identifier, FloatAbilityValue.of(0.05F), AbilityRule.AND);
    }
}
