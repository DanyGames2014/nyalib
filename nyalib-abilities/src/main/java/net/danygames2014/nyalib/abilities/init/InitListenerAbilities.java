package net.danygames2014.nyalib.abilities.init;

import net.danygames2014.nyalib.abilities.event.AbilityRegistryEvent;
import net.danygames2014.nyalib.abilities.event.AbilityValueTypeRegistryEvent;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.mod.InitEvent;

public class InitListenerAbilities {
    @EventListener(phase = InitEvent.POST_INIT_PHASE)
    public void postInit(InitEvent event) {
        StationAPI.EVENT_BUS.post(new AbilityValueTypeRegistryEvent());
        StationAPI.EVENT_BUS.post(new AbilityRegistryEvent());
    }
}
