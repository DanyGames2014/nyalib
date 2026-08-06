package net.danygames2014.nyalib.abilities;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.danygames2014.nyalib.abilities.value.AbilityValue;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class AbilityManager {
    private static final AbilityManager INSTANCE = new AbilityManager();

    private final Reference2ObjectOpenHashMap<Entity, Reference2ObjectOpenHashMap<Ability<?, ?>, Object>> abilityValues = new Reference2ObjectOpenHashMap<>();

    private AbilityManager() {}

    public static AbilityManager getInstance() {
        return INSTANCE;
    }

    @Nullable
    public <F, H extends AbilityValue<F>, G extends Entity> F get(G entity, Ability<G, H> ability) {
        // Retrieve the entity map or compute it if it doesn't exist yet
        Reference2ObjectOpenHashMap<Ability<?, ?>, Object> valueCache = abilityValues.computeIfAbsent(entity, e -> new Reference2ObjectOpenHashMap<>());

        // Try to fetch the existing cached value first if the entity abilities are not dirty
        Object cachedValue = valueCache.get(ability);

        // If the abilities of the entity are dirty or there are no values, compute them
        if (cachedValue == null) {
            F abilityValue = compute(entity, ability);
            valueCache.put(ability, abilityValue);
            return abilityValue;
        }

        // Return computed value
        //noinspection unchecked
        return (F) cachedValue;
    }

    private <F, H extends AbilityValue<F>, G extends Entity> F compute(G entity, Ability<G, H> ability) {
        Object2ObjectOpenHashMap<Identifier, AbilityProvider> providers = entity.getAbilityProviders();

        F value = null;

        switch (ability.abilityRule) {
            case AND -> {
                for (AbilityProvider provider : providers.values()) {
                    H providerValue = provider.get(ability);
                    if (providerValue == null) continue;

                    if (value == null) {
                        value = providerValue.get();
                    } else {
                        value = providerValue.computeAnd(value);
                    }
                }
            }

            case OR -> {
                for (AbilityProvider provider : providers.values()) {
                    H providerValue = provider.get(ability);
                    if (providerValue == null) continue;

                    if (value == null) {
                        value = providerValue.get();
                    } else {
                        value = providerValue.computeOr(value);
                    }
                }
            }
        }

        return value;
    }

    public void markDirty(Entity entity) {
        Reference2ObjectOpenHashMap<Ability<?, ?>, Object> entityCache = abilityValues.get(entity);
        if (entityCache != null) {
            entityCache.clear();
        }
    }
    
    public void removeEntity(Entity entity) {
        abilityValues.remove(entity);
    }
}