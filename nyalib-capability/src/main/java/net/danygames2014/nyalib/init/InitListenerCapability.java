package net.danygames2014.nyalib.init;

import net.danygames2014.nyalib.event.EntityCapabilityClassRegisterEvent;
import net.danygames2014.nyalib.event.EntityCapabilityProviderRegisterEvent;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.mod.InitEvent;

public class InitListenerCapability {
    @EventListener(phase = InitEvent.POST_INIT_PHASE)
    public void postInit(InitEvent event) {
        StationAPI.EVENT_BUS.post(new EntityCapabilityClassRegisterEvent());
        StationAPI.EVENT_BUS.post(new EntityCapabilityProviderRegisterEvent());
    }
}
