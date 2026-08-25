package net.danygames2014.nyalib.abilities.ability;

import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.util.Identifier;

public interface AbilityProviderFactory {
    AbilityProvider create(AbilityManager manager, Identifier identifier, Entity entity);
}
