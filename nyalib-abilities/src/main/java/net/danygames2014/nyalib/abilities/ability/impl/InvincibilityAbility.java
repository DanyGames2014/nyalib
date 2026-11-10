package net.danygames2014.nyalib.abilities.ability.impl;

import net.danygames2014.nyalib.abilities.ability.Ability;
import net.danygames2014.nyalib.abilities.ability.AbilityRule;
import net.danygames2014.nyalib.abilities.ability.value.BooleanAbilityValue;
import net.minecraft.entity.LivingEntity;
import net.modificationstation.stationapi.api.util.Identifier;

/**
 * Makes the entity invincible to any kind of damage
 * <p> Applicable to entities extending {@link LivingEntity}
 * <p> Default value: <code>false</code>
 * <p> Ability rule: <code>OR</code>
 */
public class InvincibilityAbility extends Ability<LivingEntity, BooleanAbilityValue> {
    public InvincibilityAbility(Identifier identifier) {
        super(identifier, BooleanAbilityValue.of(false), AbilityRule.OR);
    }
}
