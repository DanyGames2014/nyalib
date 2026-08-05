package net.danygames2014.nyalib.abilities;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.danygames2014.nyalib.abilities.value.AbilityValue;
import net.danygames2014.nyalib.abilities.value.IntAbilityValue;
import net.minecraft.entity.Entity;
import net.modificationstation.stationapi.api.util.Identifier;

public class AbilityProvider {
    private final AbilityManager manager;
    private final Object2ObjectOpenHashMap<Ability<?, ?>, AbilityValue<?>> values;

    public AbilityProvider(AbilityManager manager) {
        this.manager = manager;
        this.values = new Object2ObjectOpenHashMap<>();
    }
    
    public <G extends Entity, H extends AbilityValue<?>> void set(Ability<G, H> ability, AbilityValue<?> value) {
        values.put(ability, value);
    }
    
    public <G extends Entity, H extends AbilityValue<?>> H get(Ability<G, H> ability) {
        return (H) values.get(ability);
    }

    public void test() {
        Ability<Entity, IntAbilityValue> test = new Ability<>(Identifier.of("test"));
        this.set(test, IntAbilityValue.of(7));
        
        IntAbilityValue b = this.get(test);
    }
}
