package net.danygames2014.nyalib.item.block;

import net.modificationstation.stationapi.api.util.math.Direction;

/**
 * An interface to be implemented on a {@link net.minecraft.block.Block} to mark as capable of
 * connecting with other {@link ItemCapable} blocks
 */
public interface ItemCapable {
    /**
     * If this block can connect with another {@link ItemCapable} block on the given side
     * @param side The side to check from the block's perspective.
     * @return <code>true</code> if the block can connect, <code>false</code> if it cannot
     */
    boolean canConnectItem(Direction side);
}
