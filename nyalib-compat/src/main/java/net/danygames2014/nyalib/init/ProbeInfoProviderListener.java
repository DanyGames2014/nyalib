package net.danygames2014.nyalib.init;

import net.danygames2014.nyalib.compat.whatsthis.elements.ElementTankGauge;
import net.danygames2014.nyalib.compat.whatsthis.providers.*;
import net.danygames2014.whatsthis.WhatsThis;
import net.danygames2014.whatsthis.apiimpl.TheOneProbeImp;
import net.danygames2014.whatsthis.event.BlockProbeInfoProviderRegistryEvent;
import net.danygames2014.whatsthis.event.EntityProbeInfoProviderRegistryEvent;
import net.mine_diver.unsafeevents.listener.EventListener;

public class ProbeInfoProviderListener {
    public static int ELEMENT_TANK_GAUGE;
    
    @EventListener
    public void registerBlockProbeInfoProviders(BlockProbeInfoProviderRegistryEvent event) {
        ELEMENT_TANK_GAUGE = WhatsThis.theOneProbeImp.registerElementFactory(ElementTankGauge::new);
        
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
