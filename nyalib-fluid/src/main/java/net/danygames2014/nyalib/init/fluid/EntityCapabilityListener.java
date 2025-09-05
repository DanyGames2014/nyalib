package net.danygames2014.nyalib.init.fluid;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.capability.entity.fluidhandler.FluidHandlerEntityCapability;
import net.danygames2014.nyalib.capability.entity.fluidhandler.FluidHandlerInterfaceEntityCapabilityProvider;
import net.danygames2014.nyalib.event.EntityCapabilityClassRegisterEvent;
import net.danygames2014.nyalib.event.EntityCapabilityProviderRegisterEvent;
import net.mine_diver.unsafeevents.listener.EventListener;

public class EntityCapabilityListener {
    @EventListener
    public void registerEntityCapabilityClass(EntityCapabilityClassRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("fluid_handler"), FluidHandlerEntityCapability.class);
    }

    @EventListener
    public void registerEntityCapabilityProvider(EntityCapabilityProviderRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("fluid_handler"), new FluidHandlerInterfaceEntityCapabilityProvider());
    }
}
