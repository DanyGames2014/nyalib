package net.danygames2014.nyalib.abilities.ability.impl;

import net.danygames2014.nyalib.abilities.ability.Ability;
import net.danygames2014.nyalib.abilities.ability.AbilityRule;
import net.danygames2014.nyalib.abilities.ability.value.BooleanAbilityValue;
import net.minecraft.entity.LivingEntity;
import net.modificationstation.stationapi.api.util.Identifier;

/**
 * Makes the entity immune to fire damage and being set on fire
 */
public class FireImmunityAbility extends Ability<LivingEntity, BooleanAbilityValue> {
    public FireImmunityAbility(Identifier identifier) {
        super(identifier, BooleanAbilityValue.of(false), AbilityRule.OR);
    }
}
