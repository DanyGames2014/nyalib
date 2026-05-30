package net.danygames2014.nyalib.init.multipart;

import net.danygames2014.nyalib.NyaLibMultipart;
import net.danygames2014.nyalib.packet.AttackMultipartC2SPacket;
import net.danygames2014.nyalib.packet.BreakMultipartC2SPacket;
import net.danygames2014.nyalib.packet.InteractMultipartC2SPacket;
import net.danygames2014.nyalib.packet.MultipartDataS2CPacket;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;

public class PacketListener {
    @EventListener
    public void registerPackets(PacketRegisterEvent event) {
        event.register(NyaLibMultipart.NAMESPACE.id("multipart_data"), MultipartDataS2CPacket.TYPE);
        event.register(NyaLibMultipart.NAMESPACE.id("break_multipart"), BreakMultipartC2SPacket.TYPE);
        event.register(NyaLibMultipart.NAMESPACE.id("attack_multipart"), AttackMultipartC2SPacket.TYPE);
        event.register(NyaLibMultipart.NAMESPACE.id("interact_multipart"), InteractMultipartC2SPacket.TYPE);
    }
}
