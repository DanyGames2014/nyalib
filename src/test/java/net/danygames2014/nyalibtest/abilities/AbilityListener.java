package net.danygames2014.nyalibtest.abilities;

import net.danygames2014.nyalib.abilities.event.AbilityRegistryEvent;
import net.danygames2014.nyalibtest.NyaLibTest;
import net.mine_diver.unsafeevents.listener.EventListener;

public class AbilityListener {
    public static TestAbility testAbility;
    
    @EventListener
    public void registerAbilities(AbilityRegistryEvent event) {
        event.registerAbilityProvider(NyaLibTest.NAMESPACE.id("stick1"));
        event.registerAbilityProvider(NyaLibTest.NAMESPACE.id("stick2"));
        
        testAbility = new TestAbility(NyaLibTest.NAMESPACE.id("test_ability"));
        event.registerAbility(NyaLibTest.NAMESPACE.id("test_ability"), testAbility);
    }
}
