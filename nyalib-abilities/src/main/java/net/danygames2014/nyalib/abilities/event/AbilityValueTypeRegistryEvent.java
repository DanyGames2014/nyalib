package net.danygames2014.nyalib.abilities.event;

import net.danygames2014.nyalib.abilities.ability.value.AbilityValue;
import net.danygames2014.nyalib.abilities.ability.value.AbilityValueFactory;
import net.danygames2014.nyalib.abilities.ability.value.AbilityValueTypeRegistry;
import net.mine_diver.unsafeevents.Event;

/**
 * An event used for registering {@link AbilityValue} types
 */
public class AbilityValueTypeRegistryEvent extends Event {
    /**
     * @param type           id of the value type
     * @param factory        factory for creating the {@link AbilityValue} of the type
     * @param valueClass     class of the {@link AbilityValue}
     * @param valueTypeClass class of the value type held in the {@link AbilityValue}
     * @param <T>            the type held in the {@link AbilityValue}
     */
    public <T> void register(String type, AbilityValueFactory<T> factory, Class<? extends AbilityValue<?>> valueClass, Class<?> valueTypeClass) {
        AbilityValueTypeRegistry.register(type, factory, valueClass, valueTypeClass);
    }
}
