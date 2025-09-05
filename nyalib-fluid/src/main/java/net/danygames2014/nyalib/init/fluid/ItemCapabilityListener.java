package net.danygames2014.nyalib.init.fluid;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.capability.item.fluidhandler.FluidHandlerInterfaceItemCapabilityProvider;
import net.danygames2014.nyalib.capability.item.fluidhandler.FluidHandlerItemCapability;
import net.danygames2014.nyalib.event.ItemCapabilityClassRegisterEvent;
import net.danygames2014.nyalib.event.ItemCapabilityProviderRegisterEvent;
import net.mine_diver.unsafeevents.listener.EventListener;

public class ItemCapabilityListener {
    @EventListener
    public void registerItemCapabilityClass(ItemCapabilityClassRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("fluid_handler"), FluidHandlerItemCapability.class);
    }

    @EventListener
    public void registerItemCapabilityProvider(ItemCapabilityProviderRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("fluid_handler"), new FluidHandlerInterfaceItemCapabilityProvider());
    }
}
