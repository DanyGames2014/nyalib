package net.danygames2014.nyalib.init;

import net.danygames2014.nyalib.event.BlockCapabilityClassRegisterEvent;
import net.danygames2014.nyalib.event.BlockCapabilityProviderRegisterEvent;
import net.danygames2014.nyalib.event.ItemCapabilityClassRegisterEvent;
import net.danygames2014.nyalib.event.ItemCapabilityProviderRegisterEvent;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.AfterBlockAndItemRegisterEvent;

@SuppressWarnings("unused")
public class AfterBlockAndItemListenerCapability {
    @EventListener
    public void sendNetworkTypeRegisterEvent(AfterBlockAndItemRegisterEvent event){
        StationAPI.EVENT_BUS.post(new BlockCapabilityClassRegisterEvent());
        StationAPI.EVENT_BUS.post(new BlockCapabilityProviderRegisterEvent());
        StationAPI.EVENT_BUS.post(new ItemCapabilityClassRegisterEvent());
        StationAPI.EVENT_BUS.post(new ItemCapabilityProviderRegisterEvent());
    }
}
