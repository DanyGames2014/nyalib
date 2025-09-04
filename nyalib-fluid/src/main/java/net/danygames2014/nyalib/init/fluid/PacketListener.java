package net.danygames2014.nyalib.init.fluid;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.network.ClickFluidSlotC2SPacket;
import net.danygames2014.nyalib.network.FluidInventoryS2CPacket;
import net.danygames2014.nyalib.network.ScreenHandlerFluidSlotUpdateS2CPacket;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.event.network.packet.PacketRegisterEvent;
import net.modificationstation.stationapi.api.registry.PacketTypeRegistry;
import net.modificationstation.stationapi.api.registry.Registry;

public class PacketListener {
    @EventListener
    public void registerPackets(PacketRegisterEvent event) {
        Registry.register(PacketTypeRegistry.INSTANCE, NyaLib.NAMESPACE.id("click_fluid_slot"), ClickFluidSlotC2SPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NyaLib.NAMESPACE.id("screen_handler_fluid_slot_update"), ScreenHandlerFluidSlotUpdateS2CPacket.TYPE);
        Registry.register(PacketTypeRegistry.INSTANCE, NyaLib.NAMESPACE.id("fluid_inventory"), FluidInventoryS2CPacket.TYPE);
    }
}
