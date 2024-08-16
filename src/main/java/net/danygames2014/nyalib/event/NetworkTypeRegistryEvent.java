package net.danygames2014.nyalib.event;

import net.danygames2014.nyalib.network.NetworkType;
import net.danygames2014.nyalib.network.NetworkTypeRegistry;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;

@SuppressWarnings("UnstableApiUsage")
@EventPhases(StationAPI.INTERNAL_PHASE)
public class NetworkTypeRegistryEvent extends Event {
    public void register(NetworkType networkType){
        NetworkTypeRegistry.register(networkType.getIdentifier(), networkType);
    }
}
