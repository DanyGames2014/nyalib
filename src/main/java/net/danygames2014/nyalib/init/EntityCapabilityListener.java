package net.danygames2014.nyalib.init;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.capability.entity.itemhandler.ItemHandlerEntityCapability;
import net.danygames2014.nyalib.capability.entity.itemhandler.ItemHandlerTestEntityCapabilityProvider;
import net.danygames2014.nyalib.event.EntityCapabilityClassRegisterEvent;
import net.danygames2014.nyalib.event.EntityCapabilityProviderRegisterEvent;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;

public class EntityCapabilityListener {
    @EventListener
    public void registerBlockCapabilityClass(EntityCapabilityClassRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("item_handler"), ItemHandlerEntityCapability.class);
    }

    @EventListener(priority = ListenerPriority.LOWEST)
    public void registerInventoryBlockCapability(EntityCapabilityProviderRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("item_handler"), new ItemHandlerTestEntityCapabilityProvider());
    }
}
