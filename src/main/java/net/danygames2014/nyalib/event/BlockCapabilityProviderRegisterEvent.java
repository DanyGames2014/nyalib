package net.danygames2014.nyalib.event;

import net.danygames2014.nyalib.capability.block.BlockCapabilityProvider;
import net.danygames2014.nyalib.capability.block.BlockCapabilityProviderRegistry;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.util.Identifier;

@SuppressWarnings("rawtypes")
public class BlockCapabilityProviderRegisterEvent extends Event {
    public BlockCapabilityProviderRegistry registry;
    
    public BlockCapabilityProviderRegisterEvent() {
        registry = BlockCapabilityProviderRegistry.getInstance();
    }

    public void register(Identifier identifier, BlockCapabilityProvider provider) {
        BlockCapabilityProviderRegistry.register(identifier, provider);
    }
}
