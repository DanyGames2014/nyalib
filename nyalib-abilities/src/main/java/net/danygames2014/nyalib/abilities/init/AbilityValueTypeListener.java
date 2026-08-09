package net.danygames2014.nyalib.abilities.init;

import net.danygames2014.nyalib.abilities.event.AbilityValueTypeRegistryEvent;
import net.danygames2014.nyalib.abilities.value.BooleanAbilityValue;
import net.danygames2014.nyalib.abilities.value.FloatAbilityValue;
import net.danygames2014.nyalib.abilities.value.IntAbilityValue;
import net.mine_diver.unsafeevents.listener.EventListener;

public class AbilityValueTypeListener {
    @EventListener
    public void registerAbilityValueTypes(AbilityValueTypeRegistryEvent event) {
        event.register("integer", IntAbilityValue::new, IntAbilityValue.class);
        event.register("boolean", BooleanAbilityValue::new, BooleanAbilityValue.class);
        event.register("float", FloatAbilityValue::new, FloatAbilityValue.class);
    }
}
