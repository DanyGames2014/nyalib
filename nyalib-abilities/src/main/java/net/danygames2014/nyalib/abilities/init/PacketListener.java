package net.danygames2014.nyalib.abilities.init;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.abilities.network.AbilitySyncS2CPacket;
import net.danygames2014.nyalib.abilities.network.PlayerFlyingStateC2SPacket;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;

public class PacketListener {
    @EventListener
    public void registerPackets(PacketRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("player_flying_state"), PlayerFlyingStateC2SPacket.TYPE);
        event.register(NyaLib.NAMESPACE.id("ability_sync"), AbilitySyncS2CPacket.TYPE);
    }
}
