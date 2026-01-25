package net.danygames2014.nyalib.init.item;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.packet.EnhancedPlayerInteractBlockC2SPacket;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;

public class PacketListener {
    @EventListener
    public void registerPackets(PacketRegisterEvent event) {
        Registry.register(PacketTypeRegistry.INSTANCE, NyaLib.NAMESPACE.id("enhanced_player_interact_block"), EnhancedPlayerInteractBlockC2SPacket.TYPE);
    }
}
