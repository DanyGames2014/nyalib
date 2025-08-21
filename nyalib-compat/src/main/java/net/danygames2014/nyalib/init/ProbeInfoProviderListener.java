package net.danygames2014.nyalib.init;

import net.danygames2014.nyalib.compat.whatsthis.*;
import net.danygames2014.whatsthis.event.BlockProbeInfoProviderRegistryEvent;
import net.danygames2014.whatsthis.event.EntityProbeInfoProviderRegistryEvent;
import net.mine_diver.unsafeevents.listener.EventListener;

public class ProbeInfoProviderListener {
    @EventListener
    public void registerBlockProbeInfoProviders(BlockProbeInfoProviderRegistryEvent event) {
        event.registerProvider(new EnergyBlockProbeInfoProvider());
        event.registerProvider(new FluidBlockProbeInfoProvider());
    }

    @EventListener
    public void registerEntityProbeInfoProviders(EntityProbeInfoProviderRegistryEvent event) {
        event.registerProvider(new EnergyEntityProbeInfoProvider());
        event.registerProvider(new FluidEntityProbeInfoProvider());
        event.registerProvider(new ItemEntityProbeInfoProvider());
    }
}
