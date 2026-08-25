package net.danygames2014.nyalib.abilities.event;

import net.danygames2014.nyalib.abilities.ability.Ability;
import net.danygames2014.nyalib.abilities.ability.AbilityProvider;
import net.danygames2014.nyalib.abilities.ability.AbilityProviderFactory;
import net.danygames2014.nyalib.abilities.ability.AbilityRegistry;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.util.Identifier;

/**
 * An event used for registering {@link Ability}s and {@link AbilityProvider}s
 */
public class AbilityRegistryEvent extends Event {
    public final AbilityRegistry registry;
    
    public AbilityRegistryEvent() {
        this.registry = AbilityRegistry.getInstance();
    }

    public void registerAbility(Ability<?, ?> ability) {
        registerAbility(ability.identifier, ability);
    }
    
    public void registerAbility(Identifier identifier, Ability<?, ?> ability) {
        AbilityRegistry.registerAbility(identifier, ability);
    }
    
    public void registerAbilityProvider(Identifier identifier) {
        registerAbilityProvider(identifier, AbilityProvider::new);
    }
    
    public void registerAbilityProvider(Identifier identifier, AbilityProviderFactory factory) {
        AbilityRegistry.registerAbilityProvider(identifier, factory);
    }
}
