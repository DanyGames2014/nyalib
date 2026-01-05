package net.danygames2014.nyalib.fluid;

import net.minecraft.item.Item;

public interface FluidBucket {
    Fluid getFluid();
    
    Item getEmptyBucketItem();
    
    Item getFullBucketItem(Fluid fluid);
}
