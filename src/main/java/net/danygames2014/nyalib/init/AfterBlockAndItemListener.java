package net.danygames2014.nyalib.init;

import net.danygames2014.nyalib.event.*;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.stat.Stat;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.registry.AfterBlockAndItemRegisterEvent;

@SuppressWarnings("unused")
public class AfterBlockAndItemListener {
    @EventListener
    public void sendNetworkTypeRegisterEvent(AfterBlockAndItemRegisterEvent event){
        StationAPI.EVENT_BUS.post(new NetworkTypeRegistryEvent());
        StationAPI.EVENT_BUS.post(new FluidRegistryEvent());
        StationAPI.EVENT_BUS.post(new BlockCapabilityClassRegisterEvent());
        StationAPI.EVENT_BUS.post(new BlockCapabilityProviderRegisterEvent());
        StationAPI.EVENT_BUS.post(new ItemCapabilityClassRegisterEvent());
        StationAPI.EVENT_BUS.post(new ItemCapabilityProviderRegisterEvent());
    }
}
