package net.danygames2014.nyalib.init.fluid;

import net.danygames2014.nyalib.event.FluidRegistryEvent;
import net.danygames2014.nyalib.fluid.Fluids;
import net.danygames2014.nyalib.fluid.Fluid;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.util.Namespace;

public class FluidListener {
    @EventListener
    public void registerVanillaFluids(FluidRegistryEvent event) {
        event.register(
                Fluids.WATER = new Fluid(Namespace.MINECRAFT.id("water"), Block.WATER, Block.FLOWING_WATER).setColor(0xFF3460DA).disableAutomaticBucketRegistration()
        );
        
        event.register(
                Fluids.LAVA = new Fluid(Namespace.MINECRAFT.id("lava"), Block.LAVA, Block.FLOWING_LAVA).setColor(0xFFE6913C).disableAutomaticBucketRegistration()
        );
    }
}
