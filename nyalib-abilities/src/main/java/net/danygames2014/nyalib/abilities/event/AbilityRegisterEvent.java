package net.danygames2014.nyalib.abilities.event;

import net.danygames2014.nyalib.abilities.Ability;
import net.danygames2014.nyalib.abilities.AbilityRegistry;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.util.Identifier;

public class AbilityRegisterEvent extends Event {
    public final AbilityRegistry registry;
    
    public AbilityRegisterEvent() {
        this.registry = AbilityRegistry.getInstance();
    }

    public void register(Identifier identifier, Ability<?, ?> ability) {
        AbilityRegistry.registerAbility(identifier, ability);
    }
}
