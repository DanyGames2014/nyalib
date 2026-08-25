package net.danygames2014.nyalib.abilities.ability;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.danygames2014.nyalib.abilities.ability.value.AbilityValue;
import net.danygames2014.nyalib.abilities.ability.value.AbilityValueFactory;
import net.danygames2014.nyalib.abilities.ability.value.AbilityValueTypeRegistry;
import net.danygames2014.nyalib.abilities.network.AbilitySyncS2CPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;

public class AbilityManager {
    private static final AbilityManager INSTANCE = new AbilityManager();
    private final boolean serverSide;

    private final Reference2ObjectOpenHashMap<Entity, Reference2ObjectOpenHashMap<Ability<?, ?>, Object>> abilityValues = new Reference2ObjectOpenHashMap<>();

    private AbilityManager() {
        serverSide = FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
    }

    public static AbilityManager getInstance() {
        return INSTANCE;
    }

    /**
     * Sets the value of an ability for an entity.
     * <b>DO NOT USE! This is only used for multiplayer syncing</b>
     */
    public void set(Entity entity, Ability<?, ?> ability, Object value) {
        Reference2ObjectOpenHashMap<Ability<?, ?>, Object> entityCache = abilityValues.computeIfAbsent(entity, e -> new Reference2ObjectOpenHashMap<>());
        entityCache.put(ability, value);
    }

    /**
     * Retrieves the value of an ability for an entity
     * <p> If it has not been computed yet, it will be computed
     * @param entity The entity to retrieve the ability for
     * @param ability The ability to retrieve the value of
     * @return The value of the ability for the entity or the default value if there are no providers for it
     */
    public <F, H extends AbilityValue<F>, G extends Entity> F get(G entity, Ability<G, H> ability) {
        // Retrieve the entity map or compute it if it doesn't exist yet
        Reference2ObjectOpenHashMap<Ability<?, ?>, Object> valueCache = abilityValues.computeIfAbsent(entity, e -> new Reference2ObjectOpenHashMap<>());

        // Try to fetch the existing cached value first if the entity abilities are not dirty
        Object cachedValue = valueCache.get(ability);

        // If the abilities of the entity are dirty or there are no values, compute them
        if (cachedValue == null) {
            // If the world is remote, only use cached values, if there is none, use the default value
            if (entity.world.isRemote) {
                return ability.defaultValue.get();
            }
            
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
        ObjectArrayList<H> values = new ObjectArrayList<>();
        
        F value = null;

        // Query the values of all providers
        for (AbilityProvider provider : providers.values()) {
            if (provider instanceof MultipleValueAbilityProvider multipleValueProvider) {
                List<H> multipleValues = multipleValueProvider.getMulitple(ability);
                if (multipleValues != null) {
                    values.addAll(multipleValues);
                }
            } else {
                values.add(provider.get(ability));
            }
        }
        
        switch (ability.abilityRule) {
            case AND -> {
                for (H providerValue : values) {
                    if (providerValue == null) continue;

                    if (value == null) {
                        value = providerValue.get();
                    } else {
                        value = providerValue.computeAnd(value);
                    }
                }
            }

            case OR -> {
                for (H providerValue : values) {
                    if (providerValue == null) continue;

                    if (value == null) {
                        value = providerValue.get();
                    } else {
                        value = providerValue.computeOr(value);
                    }
                }
            }
        }
        
        if (value == null) {
            value = ability.defaultValue.get();
        }

        // Handle syncing to clients on a server
        if (serverSide) {
            switch (ability.syncType) {
                case ALL -> {
                    @SuppressWarnings("unchecked")
                    AbilityValueFactory<F> factory = (AbilityValueFactory<F>) AbilityValueTypeRegistry.getFactory(value.getClass());
                    AbilityValue<F> syncedAbilityValue = factory.create();
                    syncedAbilityValue.set(value);
                    PacketHelper.sendToAllTracking(entity, new AbilitySyncS2CPacket(entity.id, ability, syncedAbilityValue));
                }

                case PLAYER_ONLY -> {
                    if (entity instanceof PlayerEntity player) {
                        @SuppressWarnings("unchecked")
                        AbilityValueFactory<F> factory = (AbilityValueFactory<F>) AbilityValueTypeRegistry.getFactory(value.getClass());
                        AbilityValue<F> syncedAbilityValue = factory.create();
                        syncedAbilityValue.set(value);
                        PacketHelper.sendTo(player, new AbilitySyncS2CPacket(-1, ability, syncedAbilityValue));
                    }
                }
            }
        }

        return value;
    }

    /**
     * Reused set used for tracking which abilities need to be synced to be instantly to the clients
     */
    ObjectOpenHashSet<Ability<?, ?>> instantSyncAbilities = new ObjectOpenHashSet<>(8, 0.8F);

    /**
     * Marks the entity abilities dirty, forcing them to be recomputed next time they are requested
     */
    public <F, H extends AbilityValue<F>, G extends Entity> void markDirty(Entity entity, Ability<?, ?> ability) {
        Reference2ObjectOpenHashMap<Ability<?, ?>, Object> entityCache = abilityValues.get(entity);
        if (entityCache == null) {
            return;
        }
        
        entityCache.remove(ability);
        
        if (ability.syncInstantly) {
            //noinspection unchecked
            get((G) entity, (Ability<G, H>) ability);
        }
    }
    
    /**
     * Marks the entity ability dirty, forcing it to be recomputed next time it's requested
     */
    public <F, H extends AbilityValue<F>, G extends Entity> void markDirty(Entity entity) {
        Reference2ObjectOpenHashMap<Ability<?, ?>, Object> entityCache = abilityValues.get(entity);
        if (entityCache == null) {
            return;
        }
        
        // Gather all the abilities that need to be instantly synced to the clients
        if (serverSide) {
            for (Ability<?, ?> ability : entityCache.keySet()) {
                if (ability.syncInstantly) {
                    instantSyncAbilities.add(ability);
                }
            }
        }

        entityCache.clear();

        // For abilities which need to be synced instantly, compute them
        if (serverSide && !instantSyncAbilities.isEmpty()) {
            for (Ability<?, ?> ability : instantSyncAbilities) {
                //noinspection unchecked
                get((G) entity, (Ability<G, H>) ability);
            }

            instantSyncAbilities.clear();
        }
    }

    /**
     * Removes the entity from the manager
     * <p> Primarily used for clearing up entities which no longer exist
     */
    public void removeEntity(Entity entity) {
        abilityValues.remove(entity);
    }
}