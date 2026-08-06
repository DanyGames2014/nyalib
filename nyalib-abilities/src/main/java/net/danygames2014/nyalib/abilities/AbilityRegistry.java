package net.danygames2014.nyalib.abilities;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.danygames2014.nyalib.NyaLib;
import net.modificationstation.stationapi.api.util.Identifier;

public class AbilityRegistry {
    private static final AbilityRegistry INSTANCE = new AbilityRegistry();

    private final Object2ObjectOpenHashMap<Identifier, Ability<?, ?>> abilities = new Object2ObjectOpenHashMap<>();
    private final Reference2ObjectOpenHashMap<Ability<?, ?>, Identifier> abilityToIdentifier = new Reference2ObjectOpenHashMap<>();
    private final Object2ObjectOpenHashMap<Identifier, AbilityImplementation<?>> abilityImplementations = new Object2ObjectOpenHashMap<>();

    private AbilityRegistry() {}

    public static AbilityRegistry getInstance() {
        return INSTANCE;
    }

    public static void registerAbility(Identifier identifier, Ability<?, ?> ability) {
        AbilityRegistry r = getInstance();

        Ability<?, ?> existing = r.abilities.get(identifier);
        if (existing != null) {
            NyaLib.LOGGER.warn("Attempted to register an ability {} but an ability with that identifier already exists!", identifier);
            NyaLib.LOGGER.warn("Existing ability: {}", existing.getClass().getName());
            NyaLib.LOGGER.warn("Ability being registered: {}", ability.getClass().getName());
            return;
        }

        r.abilities.put(identifier, ability);
        r.abilityToIdentifier.put(ability, identifier);
        NyaLib.LOGGER.info("Registered ability {} for {}", ability.getClass().getName(), identifier);
    }

    public static Ability<?, ?> getAbility(Identifier identifier) {
        return INSTANCE.abilities.get(identifier);
    }

    public static Identifier getIdentifier(Ability<?, ?> ability) {
        return INSTANCE.abilityToIdentifier.get(ability);
    }
}