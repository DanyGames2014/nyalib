package net.danygames2014.nyalib.abilities;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.danygames2014.nyalib.NyaLib;
import net.modificationstation.stationapi.api.util.Identifier;

public class AbilityRegistry {
    private static AbilityRegistry INSTANCE;
    
    private final Object2ObjectOpenHashMap<Identifier, Ability<?, ?>> abilities = new Object2ObjectOpenHashMap<>();
    private final Object2ObjectOpenHashMap<Ability<?, ?>, Identifier> abilityClassToIdentifier = new Object2ObjectOpenHashMap<>();
    
    private final Object2ObjectOpenHashMap<Identifier, AbilityImplementation<?>> abilityImplementations = new Object2ObjectOpenHashMap<>();
    
    public static AbilityRegistry getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new AbilityRegistry();
        }
        
        return INSTANCE;
    }
    
    public static void registerAbility(Identifier identifier, Ability<?, ?> ability) {
        AbilityRegistry r = getInstance();
        
        if (r.abilities.containsKey(identifier)) {
            NyaLib.LOGGER.warn("Attempted to register an ability " + identifier + " but an ability with that identifier already exists!");
            NyaLib.LOGGER.warn("Existing ability: " + r.abilities.get(identifier).getClass().getName());
            NyaLib.LOGGER.warn("Ability being registered: " + ability.getClass().getName());
            return;
        }
        
        r.abilities.put(identifier, ability);
        r.abilityClassToIdentifier.put(ability, identifier);
        NyaLib.LOGGER.info("Registered ability " + ability.getClass().getName() + " for " + identifier);
    }
}
