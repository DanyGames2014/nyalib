package net.danygames2014.nyalib.abilities.event;

import net.danygames2014.nyalib.abilities.value.AbilityValue;
import net.danygames2014.nyalib.abilities.value.AbilityValueFactory;
import net.danygames2014.nyalib.abilities.value.AbilityValueTypeRegistry;
import net.mine_diver.unsafeevents.Event;

public class AbilityValueTypeRegistryEvent extends Event {
    public <T> void register(String type, AbilityValueFactory<T> factory, Class<? extends AbilityValue<?>> valueClass, Class<?> valueTypeClass) {
        AbilityValueTypeRegistry.register(type, factory, valueClass, valueTypeClass);
    }
}
