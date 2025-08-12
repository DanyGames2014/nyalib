package net.danygames2014.nyalib.event;

import net.danygames2014.nyalib.capability.item.ItemCapabilityProvider;
import net.danygames2014.nyalib.capability.item.ItemCapabilityRegistry;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.util.Identifier;

@SuppressWarnings("rawtypes")
public class ItemCapabilityProviderRegisterEvent extends Event {
    public ItemCapabilityRegistry registry;
    
    public ItemCapabilityProviderRegisterEvent() {
        registry = ItemCapabilityRegistry.getInstance();
    }

    public void register(Identifier identifier, ItemCapabilityProvider provider) {
        ItemCapabilityRegistry.register(identifier, provider);
    }
}
