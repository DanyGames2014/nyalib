package net.danygames2014.nyalib.init.fluid;

import net.danygames2014.nyalib.event.FluidRegistryEvent;
import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.Fluids;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.modificationstation.stationapi.api.util.Namespace;

public class FluidListener {
    @EventListener
    public void registerVanillaFluids(FluidRegistryEvent event) {
        event.register(
                Fluids.WATER = new Fluid(Namespace.MINECRAFT.id("water"), Block.WATER, Block.FLOWING_WATER)
                        .setColor(0xFF3460DA)
                        .disableAutomaticBucketRegistration()
                        .setFluidBucketFactory(fluid -> Item.WATER_BUCKET)
        );
        
        event.register(
                Fluids.LAVA = new Fluid(Namespace.MINECRAFT.id("lava"), Block.LAVA, Block.FLOWING_LAVA)
                        .setColor(0xFFE6913C)
                        .disableAutomaticBucketRegistration()
                        .setFluidBucketFactory(fluid -> Item.LAVA_BUCKET)
        );
        
        event.register(
                Fluids.MILK = new Fluid(Namespace.MINECRAFT.id("milk"), null, null)
                        .setColor(0xFFFFFFFF)
                        .disableAutomaticBucketRegistration()
                        .setFluidBucketFactory(fluid -> Item.MILK_BUCKET)
                        .setPlaceableInWorld(false)
        );
    }
}
