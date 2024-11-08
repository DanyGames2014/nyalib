package net.danygames2014.nyalib.event;

import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidRegistry;
import net.mine_diver.unsafeevents.Event;
import net.mine_diver.unsafeevents.event.EventPhases;
import net.modificationstation.stationapi.api.StationAPI;

@SuppressWarnings("UnstableApiUsage")
@EventPhases(StationAPI.INTERNAL_PHASE)
public class FluidRegistryEvent extends Event {
    public void register(Fluid fluid){
        FluidRegistry.register(fluid);
    }
}
