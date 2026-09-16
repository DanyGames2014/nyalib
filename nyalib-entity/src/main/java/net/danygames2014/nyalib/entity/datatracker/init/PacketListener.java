package net.danygames2014.nyalib.entity.datatracker.init;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.entity.datatracker.event.ExtendedDataTrackerDataTypeRegistryEvent;
import net.danygames2014.nyalib.entity.datatracker.network.ExtendedEntityTrackerSpawnS2CPacket;
import net.danygames2014.nyalib.entity.datatracker.network.ExtendedEntityTrackerUpdateS2CPacket;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.StationAPI;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;

public class PacketListener {
    @EventListener
    public void registerPackets(PacketRegisterEvent event) {
        StationAPI.EVENT_BUS.post(new ExtendedDataTrackerDataTypeRegistryEvent());
        
        event.register(NyaLib.NAMESPACE.id("extended_tracker_spawn"), ExtendedEntityTrackerSpawnS2CPacket.TYPE);
        event.register(NyaLib.NAMESPACE.id("extended_tracker_update"), ExtendedEntityTrackerUpdateS2CPacket.TYPE);
    }
}
