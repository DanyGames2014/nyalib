package net.danygames2014.nyalib.energy;

import net.modificationstation.stationapi.api.util.math.Direction;

/**
 * A base energy block, capable of interacting with other {@link EnergyCapable} devices
 */
@SuppressWarnings("unused")
public interface EnergyCapable {
    /**
     * If this block can connect with the block in the supplied direction
     * @param direction The direction to check from the block's perspective.
     *                  Example: If a machine is to the West of a cable, that cable should call this with East as a parameter because the cable will be to the East of the machine
     * @return <code>true</code> if the block can connect, <code>false</code> if it cannot connect
     */
    boolean canConnectEnergy(Direction direction);
}
