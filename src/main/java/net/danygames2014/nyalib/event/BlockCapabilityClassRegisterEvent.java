package net.danygames2014.nyalib.event;

import net.danygames2014.nyalib.capability.block.BlockCapability;
import net.danygames2014.nyalib.capability.block.BlockCapabilityRegistry;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.util.Identifier;

public class BlockCapabilityClassRegisterEvent extends Event {
    public BlockCapabilityRegistry registry;

    public BlockCapabilityClassRegisterEvent() {
        registry = BlockCapabilityRegistry.getInstance();
    }

    public void register(Identifier identifier, Class<? extends BlockCapability> capabilityClass) {
        BlockCapabilityRegistry.registerCapabilityClass(identifier, capabilityClass);
    }
}
