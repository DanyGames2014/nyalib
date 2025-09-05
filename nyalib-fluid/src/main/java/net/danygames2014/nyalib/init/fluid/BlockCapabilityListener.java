package net.danygames2014.nyalib.init.fluid;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.capability.block.fluidhandler.FluidHandlerBlockCapability;
import net.danygames2014.nyalib.capability.block.fluidhandler.FluidHandlerInterfaceBlockCapabilityProvider;
import net.danygames2014.nyalib.event.BlockCapabilityClassRegisterEvent;
import net.danygames2014.nyalib.event.BlockCapabilityProviderRegisterEvent;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.mine_diver.unsafeevents.listener.ListenerPriority;

public class BlockCapabilityListener {
    @EventListener
    public void registerBlockCapabilityClass(BlockCapabilityClassRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("fluid_handler"), FluidHandlerBlockCapability.class);
    }

    @EventListener
    public void registerBlockCapabilityProvider(BlockCapabilityProviderRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("fluid_handler"), new FluidHandlerInterfaceBlockCapabilityProvider());
    }

    @EventListener(priority = ListenerPriority.LOWEST)
    public void registerBlockCapabilityProviderLowestPriority(BlockCapabilityProviderRegisterEvent event) {
    }
}
