package net.danygames2014.nyalib.init;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.capability.block.itemhandler.ItemHandlerBlockCapabilityProvider;
import net.danygames2014.nyalib.event.BlockCapabilityProviderRegisterEvent;
import net.mine_diver.unsafeevents.listener.EventListener;

public class BlockCapabilityListener {
    @EventListener
    public void registerBlockCapabilityProviders(BlockCapabilityProviderRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("item_handler"), new ItemHandlerBlockCapabilityProvider());
    }
}
