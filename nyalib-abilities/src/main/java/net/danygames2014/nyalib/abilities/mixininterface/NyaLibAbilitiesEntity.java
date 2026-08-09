package net.danygames2014.nyalib.abilities.mixininterface;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.danygames2014.nyalib.abilities.ability.AbilityProvider;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Util;

public interface NyaLibAbilitiesEntity {
    default Object2ObjectOpenHashMap<Identifier, AbilityProvider> getAbilityProviders() {
        return Util.assertImpl();
    }
    
    default AbilityProvider getAbilityProvider(Identifier identifier) {
        return Util.assertImpl();   
    }
    
    default void markAbilitiesDirty() {
        Util.assertImpl();
    }
}
