package net.danygames2014.nyalib.energy.simple;

import net.danygames2014.nyalib.energy.EnergyCapable;
import net.danygames2014.nyalib.energy.EnergyStorage;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.jetbrains.annotations.Nullable;

/**
 * A device which can have the ability to receieve and send energy
 * see {@link #canInsertEnergy(Direction)} and {@link #canExtractEnergy(Direction)} for the exact capabilities of a given device
 */
@SuppressWarnings({"UnusedReturnValue", "unused"})
@Deprecated(forRemoval = true, since = "0.13.0")
public interface SimpleEnergyHandler extends EnergyStorage, EnergyCapable {

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
}
