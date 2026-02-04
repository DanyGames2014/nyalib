package net.danygames2014.nyalib.init.multipart;

import net.danygames2014.nyalib.NyaLibMultipart;
import net.danygames2014.nyalib.packet.AttackMultipartC2SPacket;
import net.danygames2014.nyalib.packet.BreakMultipartC2SPacket;
import net.danygames2014.nyalib.packet.InteractMultipartC2SPacket;
import net.danygames2014.nyalib.packet.MultipartDataS2CPacket;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;

public class PacketListener {
    @EventListener
    public void registerPackets(PacketRegisterEvent event) {
        Registry.register(PacketTypeRegistry.INSTANCE, NyaLibMultipart.NAMESPACE.id("multipart_data"), MultipartDataS2CPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NyaLibMultipart.NAMESPACE.id("break_multipart"), BreakMultipartC2SPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NyaLibMultipart.NAMESPACE.id("attack_multipart"), AttackMultipartC2SPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NyaLibMultipart.NAMESPACE.id("interact_multipart"), InteractMultipartC2SPacket.TYPE);
    }
}
