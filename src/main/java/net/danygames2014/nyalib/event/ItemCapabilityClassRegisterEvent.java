package net.danygames2014.nyalib.event;

import net.danygames2014.nyalib.capability.item.ItemCapability;
import net.danygames2014.nyalib.capability.item.ItemCapabilityRegistry;
import net.mine_diver.unsafeevents.Event;
import net.modificationstation.stationapi.api.util.Identifier;

public class ItemCapabilityClassRegisterEvent extends Event {
    public ItemCapabilityRegistry registry;

    public ItemCapabilityClassRegisterEvent() {
        registry = ItemCapabilityRegistry.getInstance();
    }

    public void register(Identifier identifier, Class<? extends ItemCapability> capabilityClass) {
        ItemCapabilityRegistry.registerCapabilityClass(identifier, capabilityClass);
    }
}
