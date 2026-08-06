package net.danygames2014.nyalib.abilities;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.abilities.value.AbilityValue;
import net.danygames2014.nyalib.abilities.value.AbilityValueFactory;
import net.danygames2014.nyalib.abilities.value.IntAbilityValue;
import net.danygames2014.nyalib.abilities.value.type.AbilityValueTypes;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Map;

public class AbilityProvider {
    private final AbilityManager manager;
    public Identifier identifier;
    public Entity entity;
    private final Object2ObjectOpenHashMap<Ability<?, ?>, AbilityValue<?>> values;

    public AbilityProvider(AbilityManager manager, Identifier identifier, Entity entity) {
        this.manager = manager;
        this.identifier = identifier;
        this.entity = entity;
        this.values = new Object2ObjectOpenHashMap<>();
    }
    
    public AbilityProvider(AbilityManager manager, Entity entity) {
        this(manager, null, entity);
    }

    public <G extends Entity, H extends AbilityValue<?>> void set(Ability<G, H> ability, AbilityValue<?> value) {
        values.put(ability, value);
        this.entity.markAbilitiesDirty();
    }
    
    public <G extends Entity, H extends AbilityValue<?>> H get(Ability<G, H> ability) {
        //noinspection unchecked
        return (H) values.get(ability);
    }

    public void test() {
        Ability<Entity, IntAbilityValue> test = new Ability<>(Identifier.of("test"));
        this.set(test, IntAbilityValue.of(7));
        
        IntAbilityValue b = this.get(test);
    }
    
    public void writeNbt(NbtCompound nbt) {
        nbt.putString("identifier", identifier.toString());

        NbtList valuesList = new NbtList();
        for (Map.Entry<Ability<?, ?>, AbilityValue<?>> entry : values.entrySet()) {
            NbtCompound valueNbt = new NbtCompound();
            
            // Write the ability type
            Ability<?, ?> ability = entry.getKey();
            valueNbt.putString("abilityType", ability.identifier.toString());
            
            // Write the value
            AbilityValue<?> value = entry.getValue();
            valueNbt.putString("valueType", AbilityValueTypes.CLASS_TO_TYPE.get(value.getClass()));
            value.writeNbt(valueNbt);
            
            // Add to the list of values
            valuesList.add(valueNbt);
        }
        
        nbt.put("values", valuesList);
    }
    
    public void readNbt(NbtCompound nbt) {
        identifier = Identifier.of(nbt.getString("identifier"));
        
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
            AbilityValueFactory<?> valueFactory = AbilityValueTypes.TYPE_TO_FACTORY.get(valueNbt.getString("valueType"));
            AbilityValue<?> value = valueFactory.create();
            value.readNbt(valueNbt);

            // Add to the values
            values.put(ability, value);
        }
        
        this.entity.markAbilitiesDirty();
    }
}
