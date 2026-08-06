package net.danygames2014.nyalibtest.abilities;

import net.danygames2014.nyalib.abilities.Ability;
import net.danygames2014.nyalib.abilities.AbilityRule;
import net.danygames2014.nyalib.abilities.value.BooleanAbilityValue;
import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.util.Identifier;

public class TestAbility extends Ability<PlayerEntity, BooleanAbilityValue> {
    public TestAbility(Identifier identifier) {
        super(identifier, AbilityRule.OR);
    }
}
