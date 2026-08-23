package net.danygames2014.nyalib.abilities.ability;

import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Reference2ObjectMap;
import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.abilities.value.AbilityValue;
import net.danygames2014.nyalib.abilities.value.AbilityValueFactory;
import net.danygames2014.nyalib.abilities.value.AbilityValueTypeRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class AbilityProvider {
    private final AbilityManager manager;
    public Identifier identifier;
    public Entity entity;
    private final Reference2ObjectOpenHashMap<Ability<?, ?>, AbilityValue<?>> values;
    
    public AbilityProvider(AbilityManager manager, Identifier identifier, Entity entity) {
        if (!AbilityRegistry.abilityProviderRegistered(identifier)) {
            throw new IllegalStateException("Ability provider " + identifier + " is not registered!");
        }
        
        this.manager = manager;
        this.identifier = identifier;
        this.entity = entity;
        this.values = new Reference2ObjectOpenHashMap<>();
    }

    public <G extends Entity, H extends AbilityValue<?>> void set(Ability<G, H> ability, AbilityValue<?> value) {
        values.put(ability, value);
        manager.markDirty(entity, ability);
    }
    
    @Nullable
    public <G extends Entity, H extends AbilityValue<?>> H get(Ability<G, H> ability) {
        //noinspection unchecked
        return (H) values.get(ability);
    }
    
    public <G extends Entity, H extends AbilityValue<?>> void remove(Ability<G, H> ability) {
        values.remove(ability);
        manager.markDirty(entity, ability);
    }
    
    public void writeNbt(NbtCompound nbt) {
        // If the identifier is null then something went wrong
        if (identifier == null) {
            NyaLib.LOGGER.warn("AbilityProvider identifier is null when saving!");
            return;
        }
        
        nbt.putString("identifier", identifier.toString());

        NbtList valuesList = new NbtList();
        ObjectIterator<Reference2ObjectMap.Entry<Ability<?, ?>, AbilityValue<?>>> iterator = values.reference2ObjectEntrySet().fastIterator();
        
        while (iterator.hasNext()) {
            Reference2ObjectMap.Entry<Ability<?, ?>, AbilityValue<?>> entry = iterator.next();
            NbtCompound valueNbt = new NbtCompound();
            
            // Write the ability type
            Ability<?, ?> ability = entry.getKey();
            valueNbt.putString("abilityType", ability.identifier.toString());
            
            // Write the value
            AbilityValue<?> value = entry.getValue();
            valueNbt.putString("valueType", AbilityValueTypeRegistry.CLASS_TO_TYPE.get(value.getClass()));
            value.writeNbt(valueNbt);
            
            // Add to the list of values
            valuesList.add(valueNbt);
        }
        
        nbt.put("values", valuesList);
    }
    
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
            AbilityValue<?> value = valueFactory.create();
            value.readNbt(valueNbt);

            // Add to the values
            values.put(ability, value);
        }
        
        this.manager.markDirty(entity);
    }
}
