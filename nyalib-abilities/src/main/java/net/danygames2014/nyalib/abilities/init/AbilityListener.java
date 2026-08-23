package net.danygames2014.nyalib.abilities.init;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.abilities.ability.AbilitySyncType;
import net.danygames2014.nyalib.abilities.ability.impl.*;
import net.danygames2014.nyalib.abilities.event.AbilityRegistryEvent;
import net.mine_diver.unsafeevents.listener.EventListener;

public class AbilityListener {
    @EventListener
    public void registerDefaultAbilities(AbilityRegistryEvent event) {
        Abilities.FIRE_IMMUNITY = new FireImmunityAbility(NyaLib.NAMESPACE.id("fire_immunity"));
        Abilities.FIRE_IMMUNITY.setSyncType(AbilitySyncType.PLAYER_ONLY);
        event.registerAbility(Abilities.FIRE_IMMUNITY);
        
        Abilities.FLIGHT = new FlightAbility(NyaLib.NAMESPACE.id("fly"));
        Abilities.FLIGHT.setSyncType(AbilitySyncType.PLAYER_ONLY);
        Abilities.FLIGHT.setSyncInstantly(true);
        event.registerAbility(Abilities.FLIGHT);
        
        Abilities.FLIGHT_SPEED = new FlightSpeedAbility(NyaLib.NAMESPACE.id("flight_speed"));
        Abilities.FLIGHT_SPEED.setSyncType(AbilitySyncType.PLAYER_ONLY);
        Abilities.FLIGHT_SPEED.setSyncInstantly(true);
        event.registerAbility(Abilities.FLIGHT_SPEED);
        
        Abilities.INVINCIBILITY = new InvincibilityAbility(NyaLib.NAMESPACE.id("invincibility"));
        Abilities.INVINCIBILITY.setSyncType(AbilitySyncType.PLAYER_ONLY);
        event.registerAbility(Abilities.INVINCIBILITY);
    }
}
