package net.danygames2014.nyalib.init.item;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.capability.block.itemhandler.ItemHandlerBlockCapability;
import net.danygames2014.nyalib.capability.block.itemhandler.ItemHandlerInterfaceBlockCapabilityProvider;
import net.danygames2014.nyalib.capability.block.itemhandler.ItemHandlerInventoryBlockCapabilityProvider;
import net.danygames2014.nyalib.event.BlockCapabilityClassRegisterEvent;
import net.danygames2014.nyalib.event.BlockCapabilityProviderRegisterEvent;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;

public class BlockCapabilityListener {
    @EventListener
    public void registerBlockCapabilityClass(BlockCapabilityClassRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("item_handler"), ItemHandlerBlockCapability.class);
    }

    @EventListener
    public void registerBlockCapabilityProvider(BlockCapabilityProviderRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("item_handler"), new ItemHandlerInterfaceBlockCapabilityProvider());
    }

    @EventListener(priority = ListenerPriority.LOWEST)
    public void registerBlockCapabilityProviderLowestPriority(BlockCapabilityProviderRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("item_handler"), new ItemHandlerInventoryBlockCapabilityProvider());
    }
}
