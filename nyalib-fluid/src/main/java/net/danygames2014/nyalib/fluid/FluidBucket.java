package net.danygames2014.nyalib.fluid;

import net.minecraft.item.Item;

/**
 * This class is to be implemented on any item which acts like a bucket
 * Meaning that it holds one bucket of a fluid and potentially can place this fluid in world
 * It does not store fractional buckets, just one full bucket
 */
public interface FluidBucket {
    /**
     * @return The fluid contained in this "bucket", <code>null</code> if there is none
     */
    Fluid getFluid();

    /**
     * Returns the {@link Item} which represents this FluidBucket in an empty form (e.g Bucket)
     * 
     * @return The item representing this bucket in empty form
     */
    Item getEmptyBucketItem();

    /**
     * Returns the {@link Item} which represents this FluidBucket in a form filled
     * with the specified <code>fluid</code>
     * 
     * @param fluid The fluid to fill the bucket with
     * @return The item representing this bucket filled with the specified fluid
     */
    Item getFullBucketItem(Fluid fluid);
}
