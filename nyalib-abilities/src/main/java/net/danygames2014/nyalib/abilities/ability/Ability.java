package net.danygames2014.nyalib.abilities.ability;

import net.danygames2014.nyalib.abilities.value.AbilityValue;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.util.Identifier;

public class Ability<T extends Entity, V extends AbilityValue<?>> {
    public final Identifier identifier;
    public final AbilityRule abilityRule;
    public final V defaultValue;

    public Ability(Identifier identifier, V defaultValue) {
        this(identifier, defaultValue, AbilityRule.OR);
    }

    public Ability(Identifier identifier, V defaultValue, AbilityRule abilityRule) {
        this.identifier = identifier;
        this.abilityRule = abilityRule;
        this.defaultValue = defaultValue;
    }
}
