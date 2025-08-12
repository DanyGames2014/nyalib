package net.danygames2014.nyalib.init;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.sound.MusicPacket;
import net.danygames2014.nyalib.particle.ParticlePacket;
import net.danygames2014.nyalib.sound.SoundPacket;
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
