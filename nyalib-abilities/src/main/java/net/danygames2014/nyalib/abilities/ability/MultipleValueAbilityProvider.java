package net.danygames2014.nyalib.abilities.ability;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.abilities.ability.value.AbilityValue;
import net.danygames2014.nyalib.abilities.ability.value.AbilityValueFactory;
import net.danygames2014.nyalib.abilities.ability.value.AbilityValueTypeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * A special type of {@link AbilityProvider} that allows multiple values to be set for a single ability
 */
public class MultipleValueAbilityProvider extends AbilityProvider {
    private final Reference2ObjectOpenHashMap<Ability<?, ?>, List<AbilityValue<?>>> values;

    public MultipleValueAbilityProvider(AbilityManager manager, Identifier identifier, Entity entity) {
        super(manager, identifier, entity);
        this.values = new Reference2ObjectOpenHashMap<>();
    }

    /**
     * Sets the values of the given ability in this provider
     */
    public <G extends Entity, H extends AbilityValue<?>> void setMultiple(Ability<G, H> ability, List<AbilityValue<?>> value) {
        List<AbilityValue<?>> oldValue = values.put(ability, value);
        if (oldValue == null || (oldValue.hashCode() != value.hashCode())) {
            manager.markDirty(entity, ability);
        }
    }

    @Override
    public <G extends Entity, H extends AbilityValue<?>> void set(Ability<G, H> ability, AbilityValue<?> value) {
        throw new UnsupportedOperationException("MultipleValueAbilityProvider does not support setting a single value");
    }

    /**
     * Retrives the values of the given ability in this provider
     * <p> Note: for retrieving the actual state of the ability, use {@link AbilityManager#get(Entity, Ability)}
     *
     * @return the values of the given ability in this provider, or null if it is not set by this provider
     */
    @Nullable
    public <G extends Entity, H extends AbilityValue<?>> List<H> getMulitple(Ability<G, H> ability) {
        //noinspection unchecked
        return (List<H>) values.get(ability);
    }

    @Override
    public @Nullable <G extends Entity, H extends AbilityValue<?>> H get(Ability<G, H> ability) {
        throw new UnsupportedOperationException("MultipleValueAbilityProvider does not support getting a single value");
    }

    /**
     * Removes the values of the given ability from this provider thus making this provider no longer contribute to this ability
     */
    public <G extends Entity, H extends AbilityValue<?>> void removeMultiple(Ability<G, H> ability) {
        values.remove(ability);
        manager.markDirty(entity, ability);
    }

    @Override
    public <G extends Entity, H extends AbilityValue<?>> void remove(Ability<G, H> ability) {
        throw new UnsupportedOperationException("MultipleValueAbilityProvider does not support removing a single value");
    }

    /**
     * Clears all of the ability values in this provider
     */
    @Override
    public void clear() {
        values.clear();
        manager.markDirty(entity);
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        // If the identifier is null then something went wrong
        if (identifier == null) {
            NyaLib.LOGGER.warn("AbilityProvider identifier is null when saving!");
            return;
        }

        nbt.putString("identifier", identifier.toString());

        NbtList valuesList = new NbtList();
        ObjectIterator<Reference2ObjectMap.Entry<Ability<?, ?>, List<AbilityValue<?>>>> iterator = values.reference2ObjectEntrySet().fastIterator();

        while (iterator.hasNext()) {
            Reference2ObjectMap.Entry<Ability<?, ?>, List<AbilityValue<?>>> entry = iterator.next();
            NbtCompound valueNbt = new NbtCompound();

            // Write the ability type
            Ability<?, ?> ability = entry.getKey();
            valueNbt.putString("abilityType", ability.identifier.toString());

            // Write the value
            List<AbilityValue<?>> values = entry.getValue();
            NbtList valueList = new NbtList();
            for (AbilityValue<?> value : values) {
                valueNbt.putString("valueType", AbilityValueTypeRegistry.CLASS_TO_TYPE.get(value.getClass()));
                value.writeNbt(valueNbt);
            }

            valueNbt.put("values", valueList);

            // Add to the list of values
            valuesList.add(valueNbt);
        }

        nbt.put("values", valuesList);
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        NbtList valuesList = nbt.getList("values");
        for (int i = 0; i < valuesList.size(); i++) {
            NbtCompound valueNbt = (NbtCompound) valuesList.get(i);

            // Read the ability type
            Identifier abilityId = Identifier.of(valueNbt.getString("abilityType"));

            Ability<?, ?> ability = AbilityRegistry.getAbility(abilityId);
            if (ability == null) {
                NyaLib.LOGGER.error("Ability {} not found in registry. Has the modlist been changed? Skipping the loading of this ability.", abilityId);
                continue;
            }

            // Read the value
            AbilityValueFactory<?> valueFactory = AbilityValueTypeRegistry.TYPE_TO_FACTORY.get(valueNbt.getString("valueType"));
            if (valueFactory == null) {
                NyaLib.LOGGER.error("Value type {} not found while loading ability {}. Has the modlist been changed? Skipping the loading of this ability.", valueNbt.getString("valueType"), abilityId);
                continue;
            }

            NbtList valueList = valueNbt.getList("values");
            ObjectArrayList<AbilityValue<?>> readValues = new ObjectArrayList<>();
            for (int j = 0; j < valueList.size(); j++) {
                NbtCompound valueNbt2 = (NbtCompound) valueList.get(j);
                AbilityValue<?> value = valueFactory.create();
                value.readNbt(valueNbt2);
                readValues.add(value);
            }

            // Add to the values
            values.put(ability, readValues);
        }

        this.manager.markDirty(entity);
    }
}
