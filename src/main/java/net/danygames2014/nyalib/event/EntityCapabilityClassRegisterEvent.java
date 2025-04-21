package net.danygames2014.nyalib.event;

import net.danygames2014.nyalib.capability.entity.EntityCapability;
import net.danygames2014.nyalib.capability.entity.EntityCapabilityRegistry;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.util.Identifier;

public class EntityCapabilityClassRegisterEvent extends Event {
    public EntityCapabilityRegistry registry;

    public EntityCapabilityClassRegisterEvent() {
        registry = EntityCapabilityRegistry.getInstance();
    }

    public void register(Identifier identifier, Class<? extends EntityCapability> capabilityClass) {
        EntityCapabilityRegistry.registerCapabilityClass(identifier, capabilityClass);
    }
}
