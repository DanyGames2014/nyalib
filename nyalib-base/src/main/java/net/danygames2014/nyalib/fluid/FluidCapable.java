package net.danygames2014.nyalib.fluid;

import net.modificationstation.stationapi.api.util.math.Direction;

/**
 * A base fluid device, capable of interacting with other {@link FluidCapable} devices
 */
public interface FluidCapable {
    /**
     * If this block can connect with the block in the supplied direction
     * @param direction The direction to check from the block's perspective.
     *                  Example: If a machine is to the West of a pipe, that pipe should call this with East as a parameter because the pipe will be to the East of the machine
     * @return <code>true</code> if the block can connect, <code>false</code> if it cannot connect
     */
    boolean canConnectFluid(Direction direction);
}
