package net.danygames2014.nyalib.abilities;

import net.danygames2014.nyalib.abilities.value.AbilityValue;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class Ability<T extends Entity, V extends AbilityValue<?>> {
    public final Identifier identifier;
    public final AbilityRule abilityRule;
    private V defaultValue = null;

    public Ability(Identifier identifier) {
        this(identifier, AbilityRule.OR);
    }

    public Ability(Identifier identifier, AbilityRule abilityRule) {
        this.identifier = identifier;
        this.abilityRule = abilityRule;
    }

    @Nullable
    public V getDefaultValue() {
        return defaultValue;
    }

    public Ability<T, V> setDefaultValue(V value) {
        this.defaultValue = value;
        return this;
    }
}
