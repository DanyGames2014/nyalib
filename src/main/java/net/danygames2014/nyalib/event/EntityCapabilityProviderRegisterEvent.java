package net.danygames2014.nyalib.event;

import net.danygames2014.nyalib.capability.entity.EntityCapabilityProvider;
import net.danygames2014.nyalib.capability.entity.EntityCapabilityRegistry;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.util.Identifier;

@SuppressWarnings("rawtypes")
public class EntityCapabilityProviderRegisterEvent extends Event {
    public EntityCapabilityRegistry registry;
    
    public EntityCapabilityProviderRegisterEvent() {
        registry = EntityCapabilityRegistry.getInstance();
    }

    public void register(Identifier identifier, EntityCapabilityProvider provider) {
        EntityCapabilityRegistry.register(identifier, provider);
    }
}
