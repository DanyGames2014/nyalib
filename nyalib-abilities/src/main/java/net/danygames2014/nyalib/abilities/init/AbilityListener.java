package net.danygames2014.nyalib.abilities.init;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.abilities.ability.impl.*;
import net.danygames2014.nyalib.abilities.event.AbilityRegistryEvent;
import net.mine_diver.unsafeevents.listener.EventListener;

public class AbilityListener {
    @EventListener
    public void registerDefaultAbilities(AbilityRegistryEvent event) {
        Abilities.FIRE_IMMUNITY = new FireImmunityAbility(NyaLib.NAMESPACE.id("fire_immunity"));
        event.registerAbility(Abilities.FIRE_IMMUNITY);
        
        Abilities.FLIGHT = new FlightAbility(NyaLib.NAMESPACE.id("fly"));
        event.registerAbility(Abilities.FLIGHT);
        
        Abilities.FLIGHT_SPEED = new FlightSpeedAbility(NyaLib.NAMESPACE.id("flight_speed"));
        event.registerAbility(Abilities.FLIGHT_SPEED);
        
        Abilities.INVINCIBILITY = new InvincibilityAbility(NyaLib.NAMESPACE.id("invincibility"));
        event.registerAbility(Abilities.INVINCIBILITY);
    }
}
