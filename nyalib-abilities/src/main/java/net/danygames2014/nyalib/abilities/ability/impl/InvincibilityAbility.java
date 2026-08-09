package net.danygames2014.nyalib.abilities.ability.impl;

import net.danygames2014.nyalib.abilities.ability.Ability;
import net.danygames2014.nyalib.abilities.ability.AbilityRule;
import net.danygames2014.nyalib.abilities.value.BooleanAbilityValue;
import net.minecraft.entity.LivingEntity;
import net.modificationstation.stationapi.api.util.Identifier;

public class InvincibilityAbility extends Ability<LivingEntity, BooleanAbilityValue> {
    public InvincibilityAbility(Identifier identifier) {
        super(identifier, BooleanAbilityValue.of(false), AbilityRule.OR);
    }
}
