package net.danygames2014.nyalib.event;

import net.danygames2014.nyalib.capability.block.BlockCapabilityProvider;
import net.danygames2014.nyalib.capability.block.BlockCapabilityRegistry;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.util.Identifier;

@SuppressWarnings("rawtypes")
public class BlockCapabilityProviderRegisterEvent extends Event {
    public BlockCapabilityRegistry registry;
    
    public BlockCapabilityProviderRegisterEvent() {
        registry = BlockCapabilityRegistry.getInstance();
    }

    public void register(Identifier identifier, BlockCapabilityProvider provider) {
        BlockCapabilityRegistry.register(identifier, provider);
    }
}
