package net.danygames2014.nyalib.item;

import net.modificationstation.stationapi.api.util.math.Direction;

public interface ItemCapable {
    /**
     * If this block can connect with the block in the supplied direction
     * @param direction The direction to check from the block's perspective.
     *                  Example: If a machine is to the West of a pipe, that cable should call this with East as a parameter because the piúe will be to the East of the machine
     * @return <code>true</code> if the block can connect, <code>false</code> if it cannot connect
     */
    boolean canConnectItem(Direction direction);
}
