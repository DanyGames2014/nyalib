package net.danygames2014.nyalib.init;

import net.danygames2014.nyalib.event.BlockCapabilityClassRegisterEvent;
import net.danygames2014.nyalib.event.BlockCapabilityProviderRegisterEvent;
import net.danygames2014.nyalib.event.FluidRegistryEvent;
import net.danygames2014.nyalib.event.NetworkTypeRegistryEvent;
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
    }
}
