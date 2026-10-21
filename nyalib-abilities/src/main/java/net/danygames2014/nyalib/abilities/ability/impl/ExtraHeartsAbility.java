package net.danygames2014.nyalib.abilities.ability.impl;

import net.danygames2014.nyalib.abilities.ability.Ability;
import net.danygames2014.nyalib.abilities.ability.AbilityRule;
import net.danygames2014.nyalib.abilities.ability.value.IntAbilityValue;
import net.minecraft.entity.LivingEntity;
import net.modificationstation.stationapi.api.util.Identifier;

public class ExtraHeartsAbility extends Ability<LivingEntity, IntAbilityValue> {
    public ExtraHeartsAbility(Identifier identifier) {
        super(identifier, IntAbilityValue.of(0), AbilityRule.AND);
    }
}
