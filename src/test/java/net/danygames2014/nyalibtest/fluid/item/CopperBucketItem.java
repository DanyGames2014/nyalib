package net.danygames2014.nyalibtest.fluid.item;

import net.danygames2014.nyalib.fluid.Fluid;
import net.danygames2014.nyalib.fluid.FluidBucket;
import net.danygames2014.nyalib.fluid.FluidRegistry;
import net.danygames2014.nyalib.fluid.Fluids;
import net.danygames2014.nyalibtest.NyaLibTest;
import net.danygames2014.nyalibtest.mixin.BucketItemAccessor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.template.item.TemplateBucketItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class CopperBucketItem extends TemplateBucketItem implements FluidBucket {
    public CopperBucketItem(Identifier identifier, int fluidBlockId) {
        super(identifier, fluidBlockId);
    }

    @Override
    public Fluid getFluid(ItemStack stack) {
        return FluidRegistry.get(((BucketItemAccessor) this).getFluidBlockId());
    }

    @Override
    public Item getEmptyBucketItem(ItemStack cursorStack) {
        return NyaLibTest.copperBucketItem;
    }

    @Override
    public Item getFullBucketItem(Fluid fluid, ItemStack stack) {
        if (fluid == Fluids.WATER) {
            return NyaLibTest.waterCopperBucketItem;
        } else if (fluid == Fluids.MILK) {
            return NyaLibTest.milkCopperBucketItem;
        } 
        
        return null;
    }
}
