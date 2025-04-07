package net.danygames2014.nyalib.capability.block;

import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public abstract class BlockCapabilityProvider<T extends BlockCapability> {
    /**
     * Get the capability supplied by this provider
     *
     * @param world The world the block is in
     * @param x     x-coordinate of the block
     * @param y     y-coordinate of the block
     * @param z     z-coordinate of the block
     * @return The {@link BlockCapability} this provider can provide. <code>null</code> if it cannot provide any
     */
    public abstract @Nullable T getCapability(World world, int x, int y, int z);
}
