package net.danygames2014.nyalib.init;

import net.danygames2014.nyalib.event.EntityCapabilityClassRegisterEvent;
import net.danygames2014.nyalib.event.EntityCapabilityProviderRegisterEvent;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.mod.entrypoint.EntrypointManager;

@SuppressWarnings("unused")
public class InitListener {
    @EventListener(priority = ListenerPriority.HIGHEST, phase = InitEvent.PRE_INIT_PHASE)
    public void preInit(InitEvent event) {
        FabricLoader.getInstance().getEntrypointContainers("nyalib:event_bus", Object.class).forEach(EntrypointManager::setup);
    }

    @EventListener(phase = InitEvent.POST_INIT_PHASE)
    public void postInit(InitEvent event) {
        StationAPI.EVENT_BUS.post(new EntityCapabilityClassRegisterEvent());
        StationAPI.EVENT_BUS.post(new EntityCapabilityProviderRegisterEvent());
    }
}
