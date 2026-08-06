package net.danygames2014.nyalibtest.abilities;

import net.danygames2014.nyalib.abilities.event.AbilityRegisterEvent;
import net.danygames2014.nyalibtest.NyaLibTest;
import net.mine_diver.unsafeevents.listener.EventListener;

public class AbilityListener {
    public static TestAbility testAbility;
    
    @EventListener
    public void registerAbilities(AbilityRegisterEvent event) {
        testAbility = new TestAbility(NyaLibTest.NAMESPACE.id("test_ability"));
        event.register(NyaLibTest.NAMESPACE.id("test_ability"), testAbility);
    }
}
