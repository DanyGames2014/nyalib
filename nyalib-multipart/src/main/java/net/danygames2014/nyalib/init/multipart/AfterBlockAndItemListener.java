package net.danygames2014.nyalib.init.multipart;

import net.danygames2014.nyalib.event.MultipartComponentRegistryEvent;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.AfterBlockAndItemRegisterEvent;

public class AfterBlockAndItemListener {
    @EventListener
    public void postMultipartComponentRegistryEvent(AfterBlockAndItemRegisterEvent event) {
        StationAPI.EVENT_BUS.post(new MultipartComponentRegistryEvent());
    }
}
