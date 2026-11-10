package net.danygames2014.nyalib.abilities.init;

import net.danygames2014.nyalib.abilities.ability.value.BooleanAbilityValue;
import net.danygames2014.nyalib.abilities.ability.value.FloatAbilityValue;
import net.danygames2014.nyalib.abilities.ability.value.IntAbilityValue;
import net.danygames2014.nyalib.abilities.event.AbilityValueTypeRegistryEvent;
import net.mine_diver.unsafeevents.listener.EventListener;

public class AbilityValueTypeListener {
    @EventListener
    public void registerAbilityValueTypes(AbilityValueTypeRegistryEvent event) {
        event.register("integer", IntAbilityValue::new, IntAbilityValue.class, Integer.class);
        event.register("boolean", BooleanAbilityValue::new, BooleanAbilityValue.class, Boolean.class);
        event.register("float", FloatAbilityValue::new, FloatAbilityValue.class, Float.class);
    }
}
