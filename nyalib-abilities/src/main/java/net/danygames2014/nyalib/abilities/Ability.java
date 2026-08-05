package net.danygames2014.nyalib.abilities;

import net.danygames2014.nyalib.abilities.value.AbilityValue;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.util.Identifier;

public class Ability<T extends Entity, V extends AbilityValue<?>> {
    private final Identifier identifier;

    public Ability(Identifier identifier) {
        this.identifier = identifier;
    }
}
