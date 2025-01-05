package net.danygames2014.nyalib.init;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.packet.MusicPacket;
import net.danygames2014.nyalib.packet.ParticlePacket;
import net.danygames2014.nyalib.packet.SoundPacket;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;


public class PacketListener {
    @EventListener
    public void registerPackets(PacketRegisterEvent event) {
        Registry.register(PacketTypeRegistry.INSTANCE, NyaLib.NAMESPACE.id("sound"), SoundPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NyaLib.NAMESPACE.id("music"), MusicPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NyaLib.NAMESPACE.id("particle"), ParticlePacket.TYPE);
    }
}
