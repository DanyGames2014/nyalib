package net.danygames2014.nyalib.init.energy;

import net.danygames2014.nyalib.NyaLib;
import net.danygames2014.nyalib.capability.entity.energyhandler.EnergyStorageEntityCapability;
import net.danygames2014.nyalib.capability.entity.energyhandler.EnergyStorageEntityInterfaceEntityCapabilityProvider;
import net.danygames2014.nyalib.event.EntityCapabilityClassRegisterEvent;
import net.danygames2014.nyalib.event.EntityCapabilityProviderRegisterEvent;
import net.mine_diver.unsafeevents.listener.EventListener;

public class EntityCapabilityListener {
    @EventListener
    public void registerEntityCapabilityClass(EntityCapabilityClassRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("energy_storage"), EnergyStorageEntityCapability.class);
    }

    @EventListener
    public void registerEntityCapabilityProvider(EntityCapabilityProviderRegisterEvent event) {
        event.register(NyaLib.NAMESPACE.id("energy_storage"), new EnergyStorageEntityInterfaceEntityCapabilityProvider());
    }
}
