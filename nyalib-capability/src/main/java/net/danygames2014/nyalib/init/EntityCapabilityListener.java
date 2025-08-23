package net.danygames2014.nyalib.init;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.capability.entity.energyhandler.EnergyStorageEntityCapability;
import net.danygames2014.nyalib.capability.entity.energyhandler.EnergyStorageEntityInterfaceEntityCapabilityProvider;
import net.danygames2014.nyalib.capability.entity.fluidhandler.FluidHandlerEntityCapability;
import net.danygames2014.nyalib.capability.entity.fluidhandler.FluidHandlerInterfaceEntityCapabilityProvider;
import net.danygames2014.nyalib.capability.entity.itemhandler.ItemHandlerEntityCapability;
import net.danygames2014.nyalib.capability.entity.itemhandler.ItemHandlerEntityInterfaceEntityCapabilityProvider;
import net.danygames2014.nyalib.capability.entity.itemhandler.ItemHandlerVanillaEntityCapabilityProvider;
import net.danygames2014.nyalib.event.EntityCapabilityClassRegisterEvent;
import net.danygames2014.nyalib.event.EntityCapabilityProviderRegisterEvent;
import net.mine_diver.unsafeevents.listener.EventListener;

public class EntityCapabilityListener {
    @EventListener
    public void registerEntityCapabilityClass(EntityCapabilityClassRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("item_handler"), ItemHandlerEntityCapability.class);
        event.register(NyaLib.NAMESPACE.id("fluid_handler"), FluidHandlerEntityCapability.class);
        event.register(NyaLib.NAMESPACE.id("energy_storage"), EnergyStorageEntityCapability.class);
    }

    @EventListener
    public void registerEntityCapabilityProvider(EntityCapabilityProviderRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("item_handler"), new ItemHandlerVanillaEntityCapabilityProvider());
        event.register(NyaLib.NAMESPACE.id("item_handler"), new ItemHandlerEntityInterfaceEntityCapabilityProvider());
        event.register(NyaLib.NAMESPACE.id("fluid_handler"), new FluidHandlerInterfaceEntityCapabilityProvider());
        event.register(NyaLib.NAMESPACE.id("energy_storage"), new EnergyStorageEntityInterfaceEntityCapabilityProvider());
    }
}
