package net.danygames2014.nyalib.energy;

import net.minecraft.world.World;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

/**
 * A device which can have the ability to receieve and send energy
 * see {@link #canInsertEnergy(Direction)} and {@link #canExtractEnergy(Direction)} for the exact capabilities of a given device
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
public interface EnergyHandler extends EnergyCapable {

    /**
     * Check if a device supports extracting energy from it, if this returns false there
     * should be no point in trying to use {@link #extractEnergy(int, Direction)}
     *
     * @param direction Direction to check
     * @return <code>true</code> if the device supports energy extraction from the given direction
     */
    boolean canExtractEnergy(@Nullable Direction direction);

    /**
     * Extract energy from the device
     *
     * @param amount    Amount of energy to be extracted
     * @param direction {@link Direction} from which the energy is extracted from
     * @return Amount of energy that was (or would have been, if simulated) extracted
     */
    int extractEnergy(int amount, @Nullable Direction direction);


    /**
     * Check if a device supports inserting energy into it, if this returns false there
     * should be no point in trying to use {@link #insertEnergy(int, Direction)}
     *
     * @param direction Direction to check
     * @return <code>true</code> if the device supports energy insertion from the given direction
     */
    boolean canInsertEnergy(@Nullable Direction direction);

    /**
     * Provide energy to the device
     *
     * @param amount    Amount of energy provided to the device
     * @param direction {@link Direction} from which the energy is received
     * @return Amount of energy that was (or would have been, if simulated) received
     */
    int insertEnergy(int amount, @Nullable Direction direction);

    /**
     * Attempts to send energy to the given side
     * @param world The world this device is in
     * @param x The x-position of this device
     * @param y The y-position of this device
     * @param z The z-position of this device
     * @param direction The direction you want to send power in
     * @return The amount of energy sent. If there is no neighbor in that direction, returns zero
     */
    default int sendEnergy(World world, int x, int y, int z, int amount, Direction direction){
        EnergyHandler neighbor = getNeighborEnergyHandler(world, x, y, z, direction);

        if(neighbor == null){
            return 0;
        }

        return neighbor.insertEnergy(amount, direction.getOpposite());
    }

    /**
     * Attempts to get a neighboring {@link EnergyHandler}
     * @param world The world this device is in
     * @param x The x-position of this device
     * @param y The y-position of this device
     * @param z The z-position of this device
     * @param direction The direction you want to look for the neighbor in
     * @return The neighbor's {@link EnergyHandler}, if there is not a neighboring {@link EnergyHandler} then returns <code>null</code>
     */
    default EnergyHandler getNeighborEnergyHandler(World world, int x, int y, int z, Direction direction) {
        if (direction == null) {
            return null;
        }

        if (world.getBlockEntity(x + direction.getOffsetX(), y + direction.getOffsetY(), z + direction.getOffsetZ()) instanceof EnergyHandler handler) {
            return handler;
        }

        return null;
    }
}
